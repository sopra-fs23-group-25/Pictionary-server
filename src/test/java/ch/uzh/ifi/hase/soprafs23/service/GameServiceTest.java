package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @InjectMocks
    private GameService gameService;

    Lobby testLobby = new Lobby();
    Game testGame = new Game();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testLobby.setLobbyId(1L);
        testLobby.setPlayers(new ArrayList<>());
        testLobby.setHostId(1L);
        testLobby.setNrOfRounds(1);
        testLobby.setMaxNrOfPlayers(1);
        testLobby.setTimePerRound(1L);

        testGame.setLobbyId(1L);

        testLobby.setGame(testGame);
    }

    // start Game: success - already started (409)
    @Test
    public void startGame_success_changesGameAndLobby() throws Exception {

        List<Player> players = new ArrayList<>();
        Player testPlayer = new Player();
        testPlayer.setUserId(1L);
        testPlayer.setLanguage("l");
        players.add(testPlayer);
        testLobby.setPlayers(players);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        Game createdGame = gameService.newGame(testLobby);

        assertTrue(testLobby.isRunning());
        assertEquals(createdGame, testLobby.getGame());
        //assertEquals(1L, createdGame.getLobbyId());
        assertEquals(1L, testLobby.getPlayers().get(0).getUserId());
        //assertEquals("l", createdGame.getPlayers().get(0).getLanguage());

    }

    @Test
    public void startGame_alreadyAGame_throws409() {
        testLobby.setGame(new Game());
        testLobby.setRunning(true);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        assertThrows(ResponseStatusException.class, () -> gameService.newGame(testLobby));

    }

    //end Game: success
    @Test
    public void endGame_changesLobby() {
        testLobby.setRunning(true);
        testLobby.setGame(new Game());

        gameService.endGame(testLobby);

        assertFalse(testLobby.isRunning());
        assertNull(testLobby.getGame());
    }

    //get game: success - not found(404)
    @Test
    public void getGameById_returnsLobby() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        assertEquals(testGame, gameService.getGameByLobbyId(1L));
    }

    @Test
    public void getGameById_notFound() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.getGameByLobbyId(1L));
    }

    //get lobby: success - not found (404)
    @Test
    public void getLobbyById_returnsLobby() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        assertEquals(testLobby, gameService.getLobbyByLobbyId(1L));
    }

    @Test
    public void getLobbyById_notFound() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.getLobbyByLobbyId(1L));
    }

    @Test
    public void integrateResult_turnNotOver() {

        Player testPlayer = new Player();
        testPlayer.setUserId(1L);
        testPlayer.setUsername("testName");

        List<Player> playerList = new ArrayList<>();
        playerList.add(testPlayer);

        testGame.setPlayers(playerList);
        testGame.setNotPainted(playerList);

        Turn testTurn = new Turn();
        testTurn.setGuesses(testGame.initGuesses());
        testTurn.setWord("testWord");

        testGame.setTurn(testTurn);
        
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        gameService.integrateTurnResults(testGame);

        assertEquals(testGame, testLobby.getGame());
    }

    @Test
    public void integrateResult_turnOver_roundNotOver_updatesNrOfRoundsPlayed() {

        Player testPlayer = new Player();
        testPlayer.setUserId(1L);
        testPlayer.setCurrentRole(PlayerRole.PAINTER);

        List<Player> playerList = new ArrayList<>();
        playerList.add(testPlayer);

        testGame.setPlayers(playerList);
        testGame.setNotPainted(playerList);
        testGame.setNrOfRoundsTotal(1);

        Turn testTurn = new Turn();
        testTurn.setGuesses(testGame.initGuesses());
        testTurn.setWord("testWord");
        testTurn.setPainterId(testPlayer.getUserId());

        testGame.setTurn(testTurn);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        gameService.integrateTurnResults(testGame);

        assertEquals(1, testGame.getNrOfRoundsPlayed());
    }

    @Test
    public void integrateResult_turnOver_roundOver_endsGame() {

        Player testPlayer = new Player();
        testPlayer.setUserId(1L);
        testPlayer.setUsername("testName");
        testPlayer.setCurrentRole(PlayerRole.PAINTER);

        List<Player> playerList = new ArrayList<>();
        playerList.add(testPlayer);

        testGame.setPlayers(playerList);
        testGame.setNotPainted(playerList);
        testGame.setNrOfRoundsTotal(0);

        Turn testTurn = new Turn();
        testTurn.setGuesses(testGame.initGuesses());
        testTurn.setWord("testWord");
        testTurn.setPainterId(testPlayer.getUserId());

        testGame.setTurn(testTurn);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        gameService.integrateTurnResults(testGame);

        assertTrue(testGame.getGameOver());
        assertFalse(testGame.isRunning());
    }

}
