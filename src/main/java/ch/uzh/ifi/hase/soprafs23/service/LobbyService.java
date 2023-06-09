package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No lobbies were retrieved from the DB!");
        }
    }

    public Lobby createLobby(Lobby newLobby) {

        String name = newLobby.getLobbyName();

        if (name == null || name.trim().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby name must contain a character!");
        }

        User user = getSingleUser(newLobby.getHostId());
        Player host = user.convertToPlayer();
        host.makeHost();
        host.setCurrentRole(PlayerRole.PAINTER);
        newLobby.setPlayers(new ArrayList<>());
        newLobby.addPlayer(host);

        try {
            checkIfLobbyExists(newLobby);
            checkIfHostHasOtherLobby(newLobby);

            lobbyRepository.save(newLobby);
            lobbyRepository.flush();

            return newLobby;
        }

        catch (Exception e) {
            String baseErrorMessage = "The %s provided is not unique. Therefore, the Lobby could not be created!";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, e.getMessage()));
        }
    }


    public Lobby joinLobby(Lobby lobby, User user){

        if (lobby.isFull()) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is full!");}
        if (lobby.isRunning()) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is running!");}
        if (userInLobby(lobby,user.getUserId())) {throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("%s is already in Lobby!", user.getUsername()));}

        lobby.addPlayer(user.convertToPlayer());

        return lobby;
    }

    public Lobby leaveLobby(Lobby lobby, User user){
        lobby.removePlayer(user.convertToPlayer());
        return lobby;
    }

    private boolean userInLobby(Lobby lobby, Long userId) {
        for (Player player : lobby.getPlayers()) {
            if (player.getUserId().equals(userId)) {return true;}
        }
        return false;
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
        if (lobbyWithSameName != null) {throw new RuntimeException("name");}
    }

    private void checkIfHostHasOtherLobby(Lobby lobby) {
        Lobby lobbyWithSameHost = lobbyRepository.findByHostId(lobby.getHostId());
        if (lobbyWithSameHost != null) {
            throw new RuntimeException("host ID");
        }
    }

    public void deleteLobby(Lobby lobby) {
        lobbyRepository.delete(lobby);
        lobbyRepository.flush();
    }

}
