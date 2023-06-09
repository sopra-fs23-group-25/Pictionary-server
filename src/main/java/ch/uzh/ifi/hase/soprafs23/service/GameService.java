package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
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
        game.setNrOfRoundsTotal(lobby.getNrOfRounds());

        lobby.setGame(game);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        return game;
    }

    public void integrateTurnResults(Game game) {

        Turn turn = game.getTurn();

        //game.updateWordsPainted(turn.getWord());
        updatePoints(game);
        if (game.getNotPainted().isEmpty()) { //check if this is the last turn
            game.setCurrentRound(game.getCurrentRound() + 1);
            if (game.getCurrentRound() > game.getNrOfRoundsTotal()) { // check if this is the last turn of the last round
                game.setGameOver(true);
                game.setRunning(false);
            }
            else {  //start new round: update number of rounds played, reset list for painter logic, set next painter
                Player painter = game.findPlayerById(turn.getPainterId());
                painter.setCurrentRole(PlayerRole.GUESSER);
                game.setNotPainted(game.getPlayers());
                game.redistributeRoles();
            }
        }
        else {game.redistributeRoles();} // this is not the last turn, just select next painter

        lobbyRepository.save(getLobbyByLobbyId(game.getLobbyId())); // maybe not necessary then we can delete lobby id of game
        lobbyRepository.flush();
    }

    private void updatePoints(Game game){
        Turn turn = game.getTurn();

        for(Guess guess : turn.getGuesses()) {
            Player player = game.findPlayerById(guess.getUserId());
            player.setTotalScore(player.getTotalScore() + guess.getScore());
        }
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

    public Game getGameByLobbyId(Long lobbyId) {
        Game game = getLobbyByLobbyId(lobbyId).getGame();
        if (game == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There was no game found in the lobby!");}
        return game;
    }

    public void addImage(Game game, Image image) {
        game.addImage(image);
        lobbyRepository.save(getLobbyByLobbyId(game.getLobbyId()));
        lobbyRepository.flush();
    }

}
