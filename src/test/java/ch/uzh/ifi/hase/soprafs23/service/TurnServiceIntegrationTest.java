package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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
 * @see TurnService
 */
@WebAppConfiguration
@SpringBootTest
@Transactional
public class TurnServiceIntegrationTest {

    @Autowired
    private TurnService turnService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private GameService gameService;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createTurn_success() {

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

        Game game = new Game();
        lobby.setGame(game);

        Game initGame = gameService.newGame(createdLobby);

        Turn turn = turnService.initTurn(lobby.getLobbyId());

        assertNotNull(turn.getWord());
        assertEquals(initGame.getPainterId(), turn.getPainterId());
        assertEquals(initGame.getPlayers().get(0).getUserId(), turn.getGuesses().get(0).getUserId());
        assertEquals(initGame.getPlayers().size(), turn.getGuesses().size());
        assertEquals(0, turn.getCorrectGuesses());
        assertEquals(0, turn.getIncorrectGuesses());
    }
}



