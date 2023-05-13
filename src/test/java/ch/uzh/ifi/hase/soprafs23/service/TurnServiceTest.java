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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNull;


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

        testTurn.setWord("testWord");
        testTurn.setGuesses(new ArrayList<>());
        testTurn.setPainterId(1L);
        testTurn.setCorrectGuesses(0);

        testLobby.setGame(testGame);
        testGame.setTurn(testTurn);

        testUser.setUserId(1L);
        testUser.setLanguage("en");
        testUser.setUsername("testUser");

        // Create a mock Translator instance
        translator = Mockito.mock(Translator.class);
        turnService.setTranslator(translator);

    }

    // START: success - lobby does not exist
    @Test
    public void initTurn_lobbyExists_success() {
        Player host = new Player();
        host.setCurrentRole(PlayerRole.PAINTER);
        host.setUserId(1L);
        testLobby.addPlayer(host);
        testGame.setPlayers(testLobby.getPlayers());

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        Turn turn = turnService.initTurn(testLobby.getLobbyId());

        String string = "String";

        assertEquals(testTurn.getWord().getClass(), string.getClass()); // word is assigned to turn
        assertEquals(testGame.getPainter(), turn.getPainterId());
    }

    @Test
    public void initTurn_lobbyDoesNotExist_throwsNotFound() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> turnService.initTurn(testLobby.getLobbyId()));
    }

    // GET: success - not found
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
        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        assertThrows(ResponseStatusException.class, () -> turnService.getTurnByLobbyId(testLobby.getLobbyId()));
    }

    // VERIFY GUESS: incorrect - correct
    @Test
    public void verifyGuess_incorrect_0points () throws InterruptedException {
        Guess guess = new Guess();
        guess.setGuess("wrongGuess");
        guess.setUsername("testUser");
        guess.setScore(0);
        guess.setUserId(1L);

        // Specify the behavior of the getSingleTranslation() method
        Mockito.when(translator.getSingleTranslation(Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
                .thenReturn("mock translation");

        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        turnService.submitGuess(testTurn, guess);

        assertEquals(0, guess.getScore());
    }

    @Test
    public void verifyGuess_correct_25points () throws InterruptedException {
        Guess guess = new Guess();
        guess.setGuess("mock translation");
        guess.setUsername("testUser");
        guess.setScore(0);
        guess.setUserId(1L);

        testTurn.setWord("mock translation");

        // Specify the behavior of the getSingleTranslation() method
        Mockito.when(translator.getSingleTranslation(Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
                .thenReturn("mock translation");

        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        turnService.submitGuess(testTurn, guess);

        assertEquals(25, guess.getScore());
    }

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
        Turn returnedTurn = turnService.translateTurn(testTurn, testUser.getUserId());
        assertEquals(returnedTurn.getGuesses(), testTurn.getGuesses());
    }

    // DELETE turn: success
    @Test
    public void deleteTurn_success() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        turnService.deleteTurn(testLobby.getLobbyId());
        assertNull(null, testGame.getTurn());
    }

    // Validate Guess: correct, incorrect

    @Test
    public void correctGuess() throws InterruptedException {
        Guess initGuess = new Guess();
        initGuess.setGuess(null);
        initGuess.setUsername("testUser");
        initGuess.setScore(0);
        initGuess.setUserId(1L);

        Guess guess = new Guess();
        guess.setGuess("mock translation");
        guess.setUsername("testUser");
        guess.setScore(0);
        guess.setUserId(1L);

        List<Guess> guessList = new ArrayList<>();
        guessList.add(initGuess);

        testTurn.setPainterId(guess.getUserId());
        testTurn.setGuesses(guessList);

        testTurn.setWord("mock translation");

        // Specify the behavior of the getSingleTranslation() method
        Mockito.when(translator.getSingleTranslation(Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
                .thenReturn("mock translation");

        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        turnService.submitGuess(testTurn, guess);

        assertEquals(25, guess.getScore());

    }

    @Test
    public void player_guesses_twice() throws InterruptedException{
        Guess guess = new Guess();
        guess.setGuess("mock translation");
        guess.setUsername("testUser");
        guess.setScore(0);
        guess.setUserId(1L);

        List<Guess> guessList = new ArrayList<>();
        guessList.add(guess);

        testTurn.setPainterId(guess.getUserId());
        testTurn.setGuesses(guessList);

        testTurn.setWord("mock translation");

        // Specify the behavior of the getSingleTranslation() method
        Mockito.when(translator.getSingleTranslation(Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
                .thenReturn("mock translation");

        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        assertThrows(ResponseStatusException.class, () -> turnService.submitGuess(testTurn, guess));
    }
}