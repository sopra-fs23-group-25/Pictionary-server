package ch.uzh.ifi.hase.soprafs23.wordassigner;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.*;

@Service
@Transactional
public class WordAssigner {
    private final LobbyRepository lobbyRepository;
    private final List<String> possibleWords = Arrays.asList("fish", "dog", "duck", "house", "tree", "door");
    private final Random random;

    @Autowired
    public WordAssigner(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
        random = new Random();
        Collections.sort(possibleWords);
    }

    public String getNewWord(Long lobbyId) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        Game game = lobby.getGame();
        List<String> passedWords = new ArrayList<>(game.getWordsPainted());

        // reset painted word list
        Collections.sort(passedWords);
        if(passedWords.equals(possibleWords)){
            passedWords = new ArrayList<>();
            game.setWordsPainted(passedWords);
        }
        // assign new
        String assignedWord = possibleWords.get(random.nextInt(possibleWords.size()));

        // check if word has already been used
        if (!passedWords.isEmpty()) {
            while (passedWords.contains(assignedWord)) {
                assignedWord = possibleWords.get(random.nextInt(possibleWords.size()));
            }
        }
        game.updateWordsPainted(assignedWord);

        assignedWord = assignedWord.substring(0, 1).toUpperCase() + assignedWord.substring(1);
        return assignedWord;
    }

}
