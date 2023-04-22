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

        Game game = getGameByLobbyId(lobbyId);

        Turn newTurn = new Turn();
        newTurn.setTimePerRound(game.getTimePerRound());
        newTurn.setWord("generate Word");
        newTurn.setCorrectGuesses(0);
        newTurn.setGuesses(new ArrayList<>());
        newTurn.setPainterId(game.getPainter());

        game.setTurn(newTurn);

        lobbyRepository.save(getLobbyByLobbyId(lobbyId));

        return newTurn;
    }


    public void endTurn(Long lobbyId) {
        Turn turn = getTurnByLobbyId(lobbyId);

        Game game = getGameByLobbyId(lobbyId);
        game.endTurn(turn);

        lobbyRepository.save(getLobbyByLobbyId(lobbyId)); // does this work to persist changes in lobby?
    }

    public void verifyGuess(Turn turn, Guess guess) {

        //error if user has already guesses

        String translatedGuess = "generate Word"; // translate guess implement
        if (translatedGuess.equals(turn.getWord())) {
            turn.setCorrectGuesses(turn.getCorrectGuesses() + 1); // update number pf correct guesses
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
}
