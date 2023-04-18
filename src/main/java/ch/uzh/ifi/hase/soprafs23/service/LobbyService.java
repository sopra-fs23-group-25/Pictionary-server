package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.User;
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

    @Autowired
    public LobbyService(
            @Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
            @Qualifier("userRepository") UserRepository userRepository)
    {

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
        newLobby.setHasStarted(false);
        User user = getSingleUser(newLobby.getHostId());
        newLobby.setPlayersInLobby(new ArrayList<>());
        newLobby.addPlayer(user.convertToPlayer());

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


    public Lobby joinLobby(Lobby lobby, User user){

        if (lobby != null && user != null  && !lobby.isHasStarted() && !lobby.isFull()) {
            try {
                lobby.addPlayer(user.convertToPlayer());
                return lobby;
            }
            catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "There was a Problem joining the Lobby");
            }
        }
        return null;
    }

    public Game newGame(Long lobbyId) {
        Lobby lobby = getSingleLobby(lobbyId);

        if (lobby.getGame() != null && lobby.isHasStarted()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already a Game in that Lobby!");
        }

        lobby.setHasStarted(true);

        Game game = new Game();
        game.setLobbyId(lobby.getLobbyId());
        game.setPlayers(lobby.getPlayersInLobby());
        game.setNotPainted(lobby.getPlayersInLobby());

        lobby.setGame(game);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        return game;
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

    void checkIfLobbyExists(Lobby lobbyToBeCreated) {
        Lobby lobbyWithSameName = lobbyRepository.findByLobbyName(lobbyToBeCreated.getLobbyName());

        if (lobbyWithSameName != null) {
            throw new RuntimeException("name");
        }
    }

    public void endGame(Lobby lobby) {
        lobby.setGame(null);
        lobby.setHasStarted(false);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
    }

}
