package ch.uzh.ifi.hase.soprafs23.wordassigner;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

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
    private final List<String> possibleWords = Arrays.asList("fish", "dog", "duck", "music", "snail", "mountain",
            "moon", "tree", "rainbow", "pizza", "flower", "zoo", "king", "sun", "rocket", "lamp", "clock", "rain",
            "milk", "carrot", "spoon", "nose", "snowman", "cheese", "snowflake", "bus", "candle");

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

        testGame.setWordsPainted(new ArrayList<>());
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
        assertNotEquals(0, word.length());
    }

    @Test
    public void assignWord_allButOneTaken(){
        testGame.setWordsPainted(Arrays.asList( "dog", "duck", "music", "snail", "mountain",
                "moon", "tree", "rainbow", "pizza", "flower", "zoo", "king", "sun", "rocket", "lamp", "clock", "rain",
                "milk", "carrot", "spoon", "nose", "snowman", "cheese", "snowflake", "bus", "candle"));
        testLobby.setGame(testGame);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        String word = wordAssigner.getNewWord(1L);
        assertEquals("Fish", word);
    }

    @Test
    public void assignWord_allTaken(){
        testGame.setWordsPainted(Arrays.asList( "fish", "dog", "duck", "music", "snail", "mountain",
                "moon", "tree", "rainbow", "pizza", "flower", "zoo", "king", "sun", "rocket", "lamp", "clock", "rain",
                "milk", "carrot", "spoon", "nose", "snowman", "cheese", "snowflake", "bus", "candle"));
        testLobby.setGame(testGame);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        String word = wordAssigner.getNewWord(1L);
        assertEquals(1,testGame.getWordsPainted().size());
    }

    @Test
    public void assignWord_ensureNoDuplicates(){
        testGame.setWordsPainted(Arrays.asList( "duck", "music", "snail", "mountain",
                "moon", "tree", "rainbow", "pizza", "flower", "zoo", "king", "sun", "rocket", "lamp", "clock", "rain",
                "milk", "carrot", "spoon", "nose", "snowman", "cheese", "snowflake", "bus", "candle"));
        testLobby.setGame(testGame);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        String word1 = wordAssigner.getNewWord(1L);
        String word2 = wordAssigner.getNewWord(1L);
        assertNotEquals(word2,word1);
    }
}
