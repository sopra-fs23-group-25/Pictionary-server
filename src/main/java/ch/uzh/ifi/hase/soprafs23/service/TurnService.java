package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@Transactional
public class TurnService {

    private final LobbyRepository lobbyRepository;

    @Autowired
    public TurnService(
            @Qualifier("lobbyRepository") LobbyRepository lobbyRepository)
    {this.lobbyRepository = lobbyRepository;}

    public Turn initTurn(Long lobbyId) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        Game game = lobbyRepository.findByLobbyId(lobbyId).getGame();

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
        return lobbyRepository.findByLobbyId(lobbyId).getGame().getTurn();
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
}
