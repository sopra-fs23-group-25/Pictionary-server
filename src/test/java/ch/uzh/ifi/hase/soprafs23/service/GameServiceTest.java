package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private final Game testGame = new Game();


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testGame.setLobbyId(1L);
    }

    /*@Test
    public void createGame_LobbyDoesNotExist_throws404() throws Exception {
        when(lobbyRepository.findByLobbyId(testGame.getLobbyId())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(testGame));

    }*/

    @Test
    public void createGame_AlreadyAGameInLobby_throws409() throws Exception {
        //when(lobbyRepository.findByLobbyId(testGame.getLobbyId())).thenReturn(testLobby);
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testGame);
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(testGame));
    }

    @Test
    public void createGame_success() throws Exception {

        //when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby); //lobby exists
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null); //game does not yet exist
        when(gameRepository.save(Mockito.any())).thenReturn(testGame);

        Game created = gameService.createGame(testGame);

        assertEquals(1L, created.getLobbyId());
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

    /*@Test
    public void delete_success() {
        doNothing().when(gameRepository).deleteById(Mockito.anyLong());
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);

        assertNull(testGame.getPlayers());

    }*/

    @Test
    public void delete_noSuccess() {
        doNothing().when(gameRepository).deleteById(Mockito.anyLong());
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testGame);

        assertThrows(ResponseStatusException.class, () -> gameService.deleteGame(testGame.getLobbyId()));

    }


}
