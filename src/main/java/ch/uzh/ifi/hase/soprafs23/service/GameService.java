package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@Transactional
public class GameService {

    private final LobbyRepository lobbyRepository;

    @Autowired
    public GameService(
            @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
       }

    public Game newGame(Lobby lobby) {

        if (lobby.getGame() != null && lobby.isRunning()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already a Game in that Lobby!");
        }

        lobby.setRunning(true);

        Game game = new Game();
        game.setTurn(new Turn()); //instantiate game with new turn because we call delete at start of every round
        game.setLobbyId(lobby.getLobbyId());
        game.setPlayers(lobby.getPlayers());
        game.setNotPainted(lobby.getPlayers());
        game.setNrOfRoundsTotal(lobby.getNrOfRounds()); //if client can get the settings from lobby we don't need that in game
        game.setTimePerRound(lobby.getTimePerRound()); //if client can get the settings from lobby we don't need that in game

        lobby.setGame(game);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        return game;
    }

    public void integrateTurnResults(long lobbyId) {

        Game game = lobbyRepository.findByLobbyId(lobbyId).getGame();
        Turn turn = game.getTurn();

        game.updateWordsPainted(turn.getWord());
        game.updatePoints(turn);
        //check if this is the last turn
        if (game.getNotPainted().size() == 0) {
            // check if this is the last turn of the last round
            if (game.getNrOfRoundsPlayed() == game.getNrOfRoundsTotal()) {
                game.setGameOver(true);
                game.setRunning(false);
            }
            //start new round: update number of rounds played, reset list for painter logic, set next painter
            else {
                game.setNotPainted(game.getPlayers());
                Player painter = game.findPlayerById(turn.getPainterId());
                painter.setCurrentRole(PlayerRole.GUESSER);
                game.setNrOfRoundsPlayed(game.getNrOfRoundsPlayed() + 1);
                game.redistributeRoles();
            }
        }
        // this is not the last turn, just select next painter
        else {game.redistributeRoles();}

        lobbyRepository.save(getLobbyByLobbyId(lobbyId));
        lobbyRepository.flush();
    }

    public void endGame(Lobby lobby) {
        lobby.setGame(null);
        lobby.setRunning(false);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
    }

    public Lobby getLobbyByLobbyId(long lobbyId) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        if (lobby == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found!");}
        return lobby;
    }

    /*private Game getGameByLobbyId(Long lobbyId) {
        Game game = getLobbyByLobbyId(lobbyId).getGame();
        if (game == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found in Lobby!");}
        return game;
    }*/


}
