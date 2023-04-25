package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.translator.Translator;
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

    private Translator translator;

    {
        try {
            translator = Translator.getInstance();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public TurnService(
            @Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
            @Qualifier("userRepository") UserRepository userRepository) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;}

    public Turn initTurn(Long lobbyId) {

        Game game = getGameByLobbyId(lobbyId);

        Turn newTurn = new Turn();
        newTurn.setTimePerRound(game.getTimePerRound());
        newTurn.setWord("duck");
        newTurn.setCorrectGuesses(0); // can use default value
        newTurn.setGuesses(game.initGuesses());
        newTurn.setPainterId(game.getPainter());

        game.setTurn(newTurn);

        lobbyRepository.save(getLobbyByLobbyId(lobbyId));
        lobbyRepository.flush();

        return newTurn;
    }

    public void verifyGuess(Turn turn, Guess guess) throws InterruptedException {

        //implement error if user has already guessed

        //update painter score

        String translatedGuess = translateGuess(guess, true); // translate guess implement
        if (translatedGuess.equals(turn.getWord())) {
            guess.setGuess(translatedGuess);
            turn.setCorrectGuesses(turn.getCorrectGuesses() + 1); // update number pf correct guesses
            long score = 5L * (6L - turn.getCorrectGuesses()); // calculate score
            guess.setScore(score);
            givePainterPoints(turn);
        }
        else {guess.setScore(0);}

        turn.addGuess(guess);// add guess to list
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
        if (turn == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Turn found in this Lobby!");}
        return turn;
    }

    private Lobby getLobbyByLobbyId(long lobbyId) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        if (lobby == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found!");}
        return lobby;
    }

    private Game getGameByLobbyId(Long lobbyId) {
        Game game = getLobbyByLobbyId(lobbyId).getGame();
        if (game == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found in Lobby!");}
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
    private String translateGuess(Guess guess, boolean playerToSystem) throws InterruptedException {


        String language = userRepository.findByUserId(guess.getUserId()).getLanguage();
        String guessedWord = guess.getGuess();

        return translator.getSingleTranslation(guessedWord, language, playerToSystem);
    }

    // function to translate all guesses in a turn for one User
    // should ensure that neither Guesses nor Turn objects are used as references
    // instead it should copy each Turn and Guess it works with
    // currently only supports "System To User" Translation
    // return a copied and modified instance of original Turn
    protected Turn translateTurn(Turn turn, Long userId) throws InterruptedException {

        Turn newTurn = new Turn(turn);

        List<Guess> originalGuesses = new ArrayList<>(newTurn.getGuesses());
        List<Guess> translatedGuesses = new ArrayList<>();
        List<String> queries = new ArrayList<>();

        String language = userRepository.findByUserId(userId).getLanguage();
        int loopCounter = 0;

        for (Guess guess : originalGuesses) {
            translatedGuesses.add(new Guess(guess));
        }
        for (Guess guess:translatedGuesses){
            queries.add(guess.getGuess());
        }

        queries=translator.getListTranslation(queries, language, false);

        for (Guess guess:translatedGuesses){
            guess.setGuess(queries.get(loopCounter));
            loopCounter++;
        }

        return newTurn;
    }

    // used for testing
    protected void setTranslator(Translator newTranslator){translator=newTranslator;}

}
