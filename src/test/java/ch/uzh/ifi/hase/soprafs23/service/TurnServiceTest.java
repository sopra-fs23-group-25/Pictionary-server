package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class TurnServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @InjectMocks
    private TurnService turnService;

    Lobby testLobby = new Lobby();
    Game testGame = new Game();
    Turn testTurn = new Turn();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testLobby.setLobbyId(1L);
        testLobby.setPlayersInLobby(new ArrayList<>());
        testLobby.setHostId(1L);
        testLobby.setNrOfRounds(1);
        testLobby.setMaxNrOfPlayers(1);
        testLobby.setTimePerRound(1L);

        testGame.setPlayers(new ArrayList<>());
        testGame.setLobbyId(1L);
        testGame.setTimePerRound(1L);


        testTurn.setWord("testWord");
        testTurn.setGuesses(new ArrayList<>());
        testTurn.setPainterId(1L);
        testTurn.setCorrectGuesses(0);
        testTurn.setTimePerRound(1L);

        testLobby.setGame(testGame);
        testGame.setTurn(testTurn);

    }

    @Test
    public void initTurn_lobbyExists_success() {
        Player host = new Player();
        host.setCurrentRole(PlayerRole.PAINTER);
        host.setUserId(1L);
        testLobby.addPlayer(host);
        testGame.setPlayers(testLobby.getPlayersInLobby());

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        Turn turn = turnService.initTurn(testLobby.getLobbyId());

        assertEquals(testGame.getPainter(), turn.getPainterId());
        assertEquals(testGame.getTimePerRound(), testTurn.getTimePerRound());
    }

    @Test
    public void initTurn_lobbyDoesNotExist_throwsNotFound() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> turnService.initTurn(testLobby.getLobbyId()));
    }

    @Test
    public void getTurn_returnsTurn() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        Turn turn = turnService.getTurnByLobbyId(testLobby.getLobbyId());

        assertEquals(testGame.getLobbyId(), turn.getPainterId());
    }

    @Test
    public void getTurn_notFound() {
        testGame.setTurn(null);
        testLobby.setGame(testGame);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        assertThrows(ResponseStatusException.class, () -> turnService.getTurnByLobbyId(testLobby.getLobbyId()));
    }

    @Test
    public void verifyGuess_incorrect_0points () {
        Guess guess = new Guess();
        guess.setUserId(1L);
        turnService.verifyGuess(testTurn, guess);

        assertEquals(0, guess.getScore());

    }

    @Test
    public void verifyGuess_incorrect_addsGuessToTurn () {
        Guess guess = new Guess();
        guess.setUserId(1L);
        turnService.verifyGuess(testTurn, guess);

        assertEquals(guess, testTurn.getGuesses().get(0));

    }

    // missing: test for correct guess








}
