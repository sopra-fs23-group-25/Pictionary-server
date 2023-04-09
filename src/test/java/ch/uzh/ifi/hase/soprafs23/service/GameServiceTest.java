package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private LobbyRepository lobbyRepository;


    @InjectMocks
    private GameService gameService;

    private final Game testGame = new Game();
    private final Lobby testLobby = new Lobby();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testGame.setLobbyId(1L);
        testLobby.setLobbyId(1L);
    }

    @Test
    public void createGame_LobbyDoesNotExist_throws404() throws Exception {
        when(lobbyRepository.findByLobbyId(testGame.getLobbyId())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(testGame));

    }

    @Test
    public void createGame_AlreadyAGameInLobby_throws409() throws Exception {
        when(lobbyRepository.findByLobbyId(testGame.getLobbyId())).thenReturn(testLobby);
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testGame);
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(testGame));
    }

    @Test
    public void createGame_success() throws Exception {
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setLanguage("en");
        ArrayList<User> usersInLobby = new ArrayList<>();
        usersInLobby.add(testUser);
        testLobby.setUsersInLobby(usersInLobby);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby); //lobby exists
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null); //game does not yet exist

        gameService.createGame(testGame);

        assertTrue(testLobby.isHasStarted());
        assertEquals(1L, testGame.getPlayers().get(0).getUserId());
    }

    @Test
    public void getGame_exists() throws Exception {
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testGame);
        Game game = gameService.gameByLobbyId(testGame.getLobbyId());

        assertEquals(1L, game.getLobbyId());
    }

    @Test
    public void getGame_NotExist_throws404() throws Exception {
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.gameByLobbyId(1L));
    }

}
