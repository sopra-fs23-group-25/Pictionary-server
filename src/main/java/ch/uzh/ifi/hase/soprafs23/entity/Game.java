package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Game")
public class Game implements Serializable {

    @Id
    private Long lobbyId; //can delete

    @Column
    private int nrOfRoundsTotal;

    @Column
    private int nrOfRoundsPlayed;

    @Column
    private long timePerRound;

    @Column
    private boolean isRunning;

    @Column boolean gameOver;

    @OneToMany (cascade = CascadeType.PERSIST)
    private List<Player> players;

    @OneToMany (cascade = CascadeType.PERSIST)
    private List<Player> painted;

    @OneToMany (cascade = CascadeType.PERSIST)
    private List<Player> notPainted;

    @ElementCollection
    @CollectionTable(name = "words_painted")
    @Column(name = "word")
    private List<String> wordsPainted;

    @Lob
    private Turn turn;

    public Long getLobbyId() {return lobbyId;}
    public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

    public List<Player> getPlayers() {
        return getCopy(players);
    }

    private List<Player> getCopy(List<Player> players) {
        List<Player> clonePlayers = new ArrayList<>();
        if(players != null) {
            clonePlayers.addAll(players);
            return clonePlayers;}
        return clonePlayers;
    }

    public void setPlayers(List<Player> players) {this.players = players;}

    public List<Player> getPainted() {
        return getCopy(painted);
    }
    public void setPainted(List<Player> painted) {this.painted = painted;}

    public List<Player> getNotPainted() {
        return getCopy(notPainted);
    }
    public void setNotPainted(List<Player> notPainted) {this.notPainted = notPainted;}

    public List<String> getWordsPainted() {return wordsPainted;}
    public void setWordsPainted(List<String> wordsPainted) {this.wordsPainted = wordsPainted;}

    public Turn getTurn() {
        return turn;
    }
    public void setTurn(Turn turn) {this.turn = turn;}

    public int getNrOfRoundsTotal() {
        return nrOfRoundsTotal;
    }
    public void setNrOfRoundsTotal(int nrOfRoundsTotal) {
        this.nrOfRoundsTotal = nrOfRoundsTotal;
    }

    public int getNrOfRoundsPlayed() {return nrOfRoundsPlayed;}
    public void setNrOfRoundsPlayed(int nrOfRoundsPlayed) {this.nrOfRoundsPlayed = nrOfRoundsPlayed;}

    public Long getTimePerRound() {
        return timePerRound;
    }
    public void setTimePerRound(Long timePerRound) {
        this.timePerRound = timePerRound;
    }

    public Long getPainter() {
        for (Player player: players) {
            if (player.getCurrentRole() == PlayerRole.PAINTER) {
                return player.getUserId(); // why is it null
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No painter found!");
    }

    private void setNextPainter() {} // implement selection logic private maybe

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean isRunning) {this.isRunning = isRunning;}

    public boolean getGameOver () {return gameOver;}
    public void setGameOver(boolean gameOver) {this.gameOver = gameOver;}

    public void endTurn(Turn turn) {

        //distribute points for painter
        Player painter = findPlayerById(turn.getPainterId());
        painter.setTotalScore(painter.getTotalScore() + (5 * turn.getCorrectGuesses()));
        // make painter guesser again
        painter.setCurrentRole(PlayerRole.GUESSER);
        //distribute points for guessers
        for(Guess guess : turn.getGuesses()) {
            Player guesser = findPlayerById(guess.getUserId());
            guesser.setTotalScore(guesser.getTotalScore() + guess.getScore());
        }

        //check if this is the last turn
        if (getNotPainted().size() == 0) {
            // check if this is the last turn of the last round
            if (getNrOfRoundsPlayed() == getNrOfRoundsTotal()) {
                setGameOver(true);
                setRunning(false);
            }
            //start new round: update number of rounds played, reset list for painter logic, set next painter
            else {
                setNrOfRoundsPlayed(getNrOfRoundsPlayed() + 1);
                setNextPainter();
            }
        }
        // this is not the last turn, just select next painter
        else {setNextPainter();}
        setTurn(null);
    }

    private Player findPlayerById(long userId) {
        for (Player player : players) {
            if (player.getUserId() == userId) {
                return player;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found!");
    }

}