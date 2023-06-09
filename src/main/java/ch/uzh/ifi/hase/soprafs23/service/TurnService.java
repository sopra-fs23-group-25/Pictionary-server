package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.wordassigner.WordAssigner;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.translator.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TurnService {

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;

    private WordAssigner wordAssigner;
    private Translator translator;

    private final Logger log = LoggerFactory.getLogger(TurnService.class);

    @Autowired
    public TurnService(
            @Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
            @Qualifier("userRepository") UserRepository userRepository) {
        this.lobbyRepository = lobbyRepository;
        this.wordAssigner = new WordAssigner(lobbyRepository);
        this.userRepository = userRepository;

        try {
            this.translator = Translator.getInstance();
        }
        catch (IOException e) {throw new RuntimeException(e);}
    }

    public Turn initTurn(Long lobbyId) {

        Game game = getGameByLobbyId(lobbyId);

        Turn newTurn = new Turn();
        newTurn.setWord(wordAssigner.getNewWord(lobbyId));
        newTurn.setCorrectGuesses(0); // can use default value
        newTurn.setGuesses(game.initGuesses());
        newTurn.setPainterId(game.getPainterId());

        game.setTurn(newTurn);

        lobbyRepository.save(getLobbyByLobbyId(lobbyId));
        lobbyRepository.flush();

        return newTurn;
    }

    public void submitGuess(Turn turn, Guess guess) {

        String translatedGuess = translateGuess(guess, true); // translate guess implement
        guess.setGuess(translatedGuess);
        if (translatedGuess.equals(turn.getWord())) {
            turn.setCorrectGuesses(turn.getCorrectGuesses() + 1); // update number pf correct guesses
            long score = 5L * (5L - turn.getCorrectGuesses()); // calculate score
            guess.setScore(score);
            givePainterPoints(turn);
        }
        else {
            turn.setIncorrectGuesses(turn.getIncorrectGuesses() + 1);
            guess.setScore(0);
        }

        turn.addGuess(guess);// add guess to list
    }

    public boolean everyPlayerGuessed(Turn turn) {
        return (turn.getIncorrectGuesses() + turn.getCorrectGuesses() == turn.getGuesses().size() - 1);
    }

    public void deleteTurn(long lobbyId) {
        Game game = getGameByLobbyId(lobbyId);
        game.setTurn(null);
    }

    public Guess addUsername(Guess guess) {
        String username = userRepository.findByUserId(guess.getUserId()).getUsername();
        guess.setUsername(username);

        return guess;
    }

    public Turn getTurnByLobbyId(Long lobbyId) {
        Turn turn = getGameByLobbyId(lobbyId).getTurn();
        if (turn == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Turn found in this Lobby!");
        }

        return turn;
    }

    private Lobby getLobbyByLobbyId(long lobbyId) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found!");
        }
        return lobby;
    }

    private Game getGameByLobbyId(Long lobbyId) {
        Game game = getLobbyByLobbyId(lobbyId).getGame();
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There was no game found in the lobby!");
        }
        return game;
    }

    private void givePainterPoints(Turn turn) {
        for (Guess guess : turn.getGuesses()) {
            if (guess.getUserId() == turn.getPainterId()) {
                guess.setScore(guess.getScore() + 5);
            }
        }
    }

    //used to prepare a guess for translation
    // translator need to know which way to translate, therefore a flag (playerToSystem) is set accordingly
    private String translateGuess(Guess guess, boolean playerToSystem) {

        String language = userRepository.findByUserId(guess.getUserId()).getLanguage();
        String guessedWord = guess.getGuess().toLowerCase();
        guessedWord = guessedWord.substring(0, 1).toUpperCase() + guessedWord.substring(1);

         try {
             if(guessedWord.length()>0){
                guessedWord = translator.getSingleTranslation(guessedWord, language, playerToSystem);
             }
         }
         catch(Exception e){
             log.error(e.getMessage());
         }
         return guessedWord;
    }

    // function to translate all guesses in a turn for one User
    // should ensure that neither Guesses nor Turn objects are used as references
    // instead it should copy each Turn and Guess it works with
    // currently only supports "System To User" Translation
    // return a copied and modified instance of original Turn
    public Turn translateTurn(Turn turn, Long userId) {

        Turn newTurn = new Turn(turn);

        List<Guess> originalGuesses = new ArrayList<>(newTurn.getGuesses());
        List<Guess> translatedGuesses = new ArrayList<>(0);
        List<String> queries = new ArrayList<>();

        User user = userRepository.findByUserId(userId);
        String language = user.getLanguage();
        String word = translator.getSingleTranslation(turn.getWord(), language, false);
        newTurn.setWord(word);

        int loopCounter = 0;

        for (Guess guess : originalGuesses) {
            if (guess.getGuess() != null) {
                translatedGuesses.add(new Guess(guess));
            }
            else {
                Guess emptyGuess = new Guess(guess);
                emptyGuess.setGuess(" ");
                translatedGuesses.add(emptyGuess);
            }

        }
        for (Guess guess : translatedGuesses) {
            queries.add(guess.getGuess());
        }

        queries = translator.getListTranslation(queries, language, false);

        for (Guess guess : translatedGuesses) {
            String guessedWord = queries.get(loopCounter);
            if (guessedWord.length()>0) {
                guessedWord = guessedWord.substring(0, 1).toUpperCase() + guessedWord.substring(1);
            }
            guess.setGuess(guessedWord);
            loopCounter++;
        }
        newTurn.setGuesses(translatedGuesses);

        return newTurn;
    }

    // used for testing
    protected void setTranslator(Translator newTranslator) {
        translator = newTranslator;
    }

}
