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

        if (gameRepository.findByLobbyId(game.getLobbyId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "There is already an ongoing Game in that lobby!");
        }

        Lobby lobby = lobbyRepository.findByLobbyId(game.getLobbyId());
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist!");
        }
        // the lobby exists and the game in the lobby has not yet started
        else {
            lobby.setHasStarted(true);

            game.setPlayers(createPlayers(lobby));

            game = gameRepository.save(game);
            gameRepository.flush();
            lobby = lobbyRepository.save(lobby);
            lobbyRepository.flush();

            return game;

        }
    }

    private ArrayList<Player> createPlayers(Lobby lobby) {
        List<User> usersInLobby = lobby.getUsersInLobby();
        ArrayList<Player> usersToPlayers = new ArrayList<>();
        for (User user : usersInLobby) {
            usersToPlayers.add(user.convertToPlayer());
        }
        return usersToPlayers;
    }
}
