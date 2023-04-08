package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private LobbyRepository lobbyRepository;


    @InjectMocks
    private GameService gameService;

    private Game testGame = new Game();
    private Lobby testLobby = new Lobby();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testGame.setLobbyId(1L);
        testLobby.setLobbyId(1L);
    }

    @Test
    public void createGameAlreadyGameWithSameLobbyId() throws Exception {
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testGame);
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(testGame));
    }

    @Test
    public void createGameLobbyDoesNotExist() throws Exception {
        when(lobbyRepository.findByLobbyId(testGame.getLobbyId())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(testGame));

    }



    @Test
    public void createGame() throws Exception {
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setLanguage("en");
        ArrayList<User> usersInLobby = new ArrayList<>();
        usersInLobby.add(testUser);
        testLobby.setUsersInLobby(usersInLobby);

        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        gameService.createGame(testGame);

        assertEquals(true, testLobby.isHasStarted());
        assertEquals(1L, testGame.getPlayers().get(0).getUserId());
    }

}
