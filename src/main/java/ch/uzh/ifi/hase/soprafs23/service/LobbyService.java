package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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


/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Autowired
    public LobbyService(
            @Qualifier("gameRepository") GameRepository gameRepository,
            @Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
            @Qualifier("userRepository") UserRepository userRepository)
    {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
    }

    public List<Lobby> getLobbies() {
        try {
            return this.lobbyRepository.findAll();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no lobbies were retrieved from the DB");
        }
    }

    public Lobby createLobby(Lobby newLobby) {
        if (newLobby.getLobbyName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no name provided");
        }
        try {
            checkIfLobbyExists(newLobby);
            newLobby = lobbyRepository.save(newLobby);
            lobbyRepository.flush();

            return newLobby;
        }
        catch (Exception e) {
            String baseErrorMessage = "The %s provided is not unique. Therefore, the Lobby could not be created!";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, e.getMessage()));

        }
    }


    public void joinLobby(Long userId, Long lobbyId){
        Lobby lobby = getSingleLobby(lobbyId);
        User user = getSingleUser(userId);

        lobby.addPlayer(user.convertToPlayer());
    }

    public void startGame(Long lobbyId) {

        Lobby lobby = getSingleLobby(lobbyId);
        lobby.setHasStarted(true);

        Game game = initGame(lobbyId);

        gameRepository.save(game);
        gameRepository.flush();
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

    }

    public Lobby getSingleLobby(long id) {
        Lobby lobbyById = lobbyRepository.findByLobbyId(id);
        if (lobbyById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist!");
        }
        return lobbyById;
    }

    public User getSingleUser(long id) {
        User userById = userRepository.findByUserId(id);
        if (userById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        return userById;
    }

    private void checkIfLobbyExists(Lobby lobbyToBeCreated) {
        Lobby lobbyWithSameName = lobbyRepository.findByLobbyName(lobbyToBeCreated.getLobbyName());

        if (lobbyWithSameName != null) {
            throw new RuntimeException("name");
        }
    }

    private Game initGame(Long lobbyId) {

        Game game = gameRepository.findByLobbyId(lobbyId);
        if (game == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game was not found!");}

        List<Player> players = getSingleLobby(lobbyId).getPlayersInLobby();

        game.setPlayers(players);
        game.setNotPainted(players);
        game.setPainted(new ArrayList<Player>());
        game.setWord(null);
        game.setWordsPainted(new ArrayList<String>());

        return game;
    }

}
