package ch.uzh.ifi.hase.soprafs23.WordAssigner;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

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
    }

    public String getNewWord(Long lobbyId) {

        String assignedWord = "tree"; // needs to be any word which is part of the word list

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        Game game = lobby.getGame();
        List<String> passedWords = game.getWordsPainted();

        if (passedWords.size() == 0) {
            assignedWord = possibleWords.get(random.nextInt(possibleWords.size()));
        }
        else {
            while (passedWords.contains(assignedWord) & passedWords.size() < possibleWords.size()) {
                assignedWord = possibleWords.get(random.nextInt(possibleWords.size()));
            }
        }

        return assignedWord;
    }

}
