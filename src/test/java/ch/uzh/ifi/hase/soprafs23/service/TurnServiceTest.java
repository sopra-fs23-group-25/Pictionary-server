package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.translator.Translator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


public class TurnServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Translator translator;
    @InjectMocks
    private TurnService turnService;

    Lobby testLobby = new Lobby();
    Game testGame = new Game();
    Turn testTurn = new Turn();
    User testUser = new User();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testLobby.setLobbyId(1L);
        testLobby.setPlayers(new ArrayList<>());
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

        testUser.setUserId(1L);
        testUser.setLanguage("en");
        testUser.setUsername("testUser");

        // Create a mock Translator instance
        translator = Mockito.mock(Translator.class);
        turnService.setTranslator(translator);

    }

    @Test
    public void initTurn_lobbyExists_success() {
        Player host = new Player();
        host.setCurrentRole(PlayerRole.PAINTER);
        host.setUserId(1L);
        testLobby.addPlayer(host);
        testGame.setPlayers(testLobby.getPlayers());

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
    public void verifyGuess_incorrect_0points () throws InterruptedException {
        Guess guess = new Guess();
        guess.setGuess("");
        guess.setUsername("testUser");
        guess.setScore(0);
        guess.setUserId(1L);

        // Specify the behavior of the getSingleTranslation() method
        Mockito.when(translator.getSingleTranslation(Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
                .thenReturn("mock translation");

        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        turnService.verifyGuess(testTurn, guess);

        assertEquals(0, guess.getScore());

    }

    @Test
    public void verifyGuess_incorrect_addsGuessToTurn () throws InterruptedException {
        Guess guess = new Guess();
        guess.setGuess("");
        guess.setUsername("testUser");
        guess.setScore(0);
        guess.setUserId(1L);

        // Specify the behavior of the getSingleTranslation() method
        Mockito.when(translator.getSingleTranslation(Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
                .thenReturn("mock translation");

        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        turnService.verifyGuess(testTurn, guess);

        assertEquals(guess, testTurn.getGuesses().get(0));

    }

    // missing: test for correct guess


    @Test
    public void getUsername_addsUsername() {
        User user = new User();
        user.setUsername("name");
        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);

        Guess guess = new Guess();
        guess.setUserId(1L);
        turnService.addUsername(guess);

        assertEquals(user.getUsername(),guess.getUsername());
    }

    @Test
    public void translateEntireTurn_success() throws InterruptedException {
        testUser.setLanguage("de");
        User testUser2 = new User(); testUser2.setUserId(2L); testUser2.setUsername("testUser2"); testUser2.setLanguage("en");
        Guess testGuess1 = new Guess(1L,"Vogel", 0L);
        Guess testGuess2 = new Guess(2L,"Duck", 0L);
        testTurn.addGuess(testGuess1);
        testTurn.addGuess(testGuess2);
        when(userRepository.findByUserId(1L)).thenReturn(testUser);
        when(userRepository.findByUserId(2L)).thenReturn(testUser2);
        Mockito.when(translator.getListTranslation(Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
                .thenReturn(new ArrayList<>(Arrays.asList("Vogel", "Ente")));
        turnService.translateTurn(testTurn, testUser.getUserId());
    }
}
