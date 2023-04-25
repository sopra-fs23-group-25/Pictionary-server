package ch.uzh.ifi.hase.soprafs23.wordAssignerTest;

import ch.uzh.ifi.hase.soprafs23.WordAssigner.WordAssigner;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.translator.Translator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class WordAssignerTest {
    @Mock
    private LobbyRepository lobbyRepository;

    private WordAssigner wordAssigner;
    Lobby testLobby = new Lobby();
    Game testGame = new Game();
    private final List<String> possibleWords = Arrays.asList("fish", "dog", "duck", "house", "tree");

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.wordAssigner = new WordAssigner(lobbyRepository);
        testLobby.setLobbyId(1L);
        testLobby.setPlayers(new ArrayList<>());
        testLobby.setHostId(1L);
        testLobby.setNrOfRounds(1);
        testLobby.setMaxNrOfPlayers(1);
        testLobby.setTimePerRound(1L);

        testGame.setPlayers(new ArrayList<>());
        testGame.setLobbyId(1L);
        testGame.setTimePerRound(1L);

        testGame.setWordsPainted(Arrays.asList());
        testLobby.setGame(testGame);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
    }



    public WordAssignerTest(){

    }

    @Test
    public void assignWord_nonTaken(){
        testGame.setWordsPainted(Arrays.asList());
        testLobby.setGame(testGame);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        String word = wordAssigner.getNewWord(1L);

    }

    @Test
    public void assignWord_allButOneTaken(){
        testGame.setWordsPainted(Arrays.asList( "dog", "duck", "house", "tree"));
        testLobby.setGame(testGame);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        String word = wordAssigner.getNewWord(1L);
        assertEquals(word, "fish");

    }
}
