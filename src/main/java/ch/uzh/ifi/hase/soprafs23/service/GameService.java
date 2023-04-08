package ch.uzh.ifi.hase.soprafs23.service;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public GameService(
            @Qualifier("gameRepository") GameRepository gameRepository,
            @Qualifier("lobbyRepository") LobbyRepository lobbyRepository)
    {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public Game createGame (Game game) {

        Lobby lobby = lobbyRepository.findByLobbyId(game.getLobbyId());
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist!");
        }
        // check if a game already exists in the lobby and throw a conflict exception if it does
        if (gameRepository.findByLobbyId(game.getLobbyId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already an ongoing game in that lobby!");
        }

        lobby.setHasStarted(true);
        game.setPlayers(createPlayers(lobby));

        game = gameRepository.save(game);
        lobby = lobbyRepository.save(lobby);

        return game;
    }

    private ArrayList<Player> createPlayers(Lobby lobby) {
        List<User> usersInLobby = lobby.getUsersInLobby();
        ArrayList<Player> usersToPlayers = new ArrayList<>();
        for (User user : usersInLobby) {
            usersToPlayers.add(user.convertToPlayer());
        }
        return usersToPlayers;
    }

    public Game gameByLobbyId(Long lobbyId) {
        Game game = gameRepository.findByLobbyId(lobbyId);
        if (game == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game does not exist!");}

        return game;
    }
}
