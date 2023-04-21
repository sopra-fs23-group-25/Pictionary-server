package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Guess;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@Transactional
public class TurnService {

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;

    @Autowired
    public TurnService(
            @Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
            @Qualifier("userRepository") UserRepository userRepository) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;}

    public Turn initTurn(Long lobbyId) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);

        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Turn could not be started for non-existing Lobby!");
        }

        Game game = lobby.getGame();

        Turn newTurn = new Turn();
        newTurn.setTimePerRound(game.getTimePerRound());
        newTurn.setWord("generate Word");
        newTurn.setCorrectGuesses(0);
        newTurn.setGuesses(new ArrayList<>());
        newTurn.setPainterId(game.getPainter());

        game.setTurn(newTurn);

        lobbyRepository.save(lobby);

        return newTurn;
    }

    public Turn getTurnByLobbyId(Long lobbyId) {
        Turn turn = lobbyRepository.findByLobbyId(lobbyId).getGame().getTurn();
        if (turn == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Turn found in this Lobby!");
        }
        return turn;
    }

    public void verifyGuess(Turn turn, Guess guess) {

        String translatedGuess = "generate Word"; // translate guess implement
        if (translatedGuess.equals(turn.getWord())) {
            turn.setCorrectGuesses(turn.getCorrectGuesses() + 1L); // update number pf correct guesses
            long score = 5L * (6L - turn.getCorrectGuesses()); // calculate score
            guess.setScore(score);
        }
        else {guess.setScore(0);}

        turn.addGuess(guess);// add guess to list
    }

    public Guess addUsername(Guess guess) {
        String username = userRepository.findByUserId(guess.getUserId()).getUsername();
        guess.setUsername(username);

        return guess;
    }
}
