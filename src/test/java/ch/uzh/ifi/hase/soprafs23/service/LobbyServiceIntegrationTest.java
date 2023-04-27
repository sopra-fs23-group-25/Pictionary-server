package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see LobbyService
 */
@WebAppConfiguration
@SpringBootTest
@Transactional
public class LobbyServiceIntegrationTest {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createLobby_createsNewLobby() {

        Lobby lobby = new Lobby();
        lobby.setLobbyName("testLobby");
        lobby.setTimePerRound(1L);
        lobby.setNrOfRounds(1);
        lobby.setHostId(1L);

        User host = new User();
        host.setUsername("testName");
        host.setLanguage("testLanguage");
        host.setPassword("testPassword");
        host.setStatus(UserStatus.OFFLINE);
        host.setToken("testToken");
        userRepository.save(host);

        lobby.setHostId(host.getUserId());

        Lobby createdLobby = lobbyService.createLobby(lobby);

        assertNotNull(createdLobby.getLobbyId());
        assertEquals("testLobby", createdLobby.getLobbyName());
        assertEquals(1, createdLobby.getPlayers().size());
        assertEquals(PlayerRole.PAINTER,
                createdLobby.getPlayers().get(0).getCurrentRole());
    }

    @Test
    public void getSingleLobby_returnsExistingLobby() {

        Lobby lobby = new Lobby();
        lobby.setLobbyName("testLobby");
        lobby.setTimePerRound(1L);
        lobby.setNrOfRounds(1);
        lobby.setHostId(1L);
        lobbyRepository.save(lobby);

        Lobby retrievedLobby = lobbyService.getSingleLobby(lobby.getLobbyId());

        assertNotNull(retrievedLobby);
        assertEquals(lobby.getLobbyId(), retrievedLobby.getLobbyId());
    }

}