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
    private Long lobbyId;

    @Column
    private int nrOfRoundsTotal;

    @Column
    private int currentRound = 1;

    @Column
    private boolean isRunning;

    @Column boolean gameOver;

    @OneToMany (cascade = CascadeType.PERSIST)
    private List<Player> players;

    @OneToMany (cascade = CascadeType.PERSIST)
    private List<Player> notPainted;

    @ElementCollection
    private List<String> wordsPainted = new ArrayList<>();

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

    public List<Player> getNotPainted() {
        return getCopy(notPainted);
    }
    public void setNotPainted(List<Player> notPainted) {
        List<Player> guessers = new ArrayList<>();

        for (Player player : notPainted) {
            if (player.getCurrentRole() == PlayerRole.GUESSER) {
                guessers.add(player);
            }
        }
        this.notPainted = guessers;}

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

    public int getCurrentRound() {return currentRound;}
    public void setCurrentRound(int nrOfRoundsPlayed) {this.currentRound = nrOfRoundsPlayed;}

    public Long getPainter() {
        for (Player player: players) {
            if (player.getCurrentRole() == PlayerRole.PAINTER) {
                return player.getUserId(); // why is it null
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No painter found!");
    }

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean isRunning) {this.isRunning = isRunning;}

    public boolean getGameOver () {return gameOver;}
    public void setGameOver(boolean gameOver) {this.gameOver = gameOver;}

    public Player findPlayerById(long userId) {
        for (Player player : players) {
            if (player.getUserId() == userId) {
                return player;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found!");
    }

    public void redistributeRoles() {
        for (Player player: players) {
            player.setCurrentRole(PlayerRole.GUESSER);
        }
        long nextPainterId = notPainted.remove(0).getUserId();
        Player nextPainter = findPlayerById(nextPainterId);
        nextPainter.setCurrentRole(PlayerRole.PAINTER);
    } // implement selection logic private maybe

    public void updateWordsPainted(String word) {
        wordsPainted.add(word);
    }

    public List<Guess> initGuesses() {
        List <Guess> initGuesses = new ArrayList<>();

        for(Player player : players) {
            Guess playerGuess = new Guess();
            playerGuess.setUserId(player.getUserId());
            playerGuess.setScore(0);
            playerGuess.setUsername(player.getUsername());
            playerGuess.setGuess(null);
            initGuesses.add(playerGuess);
        }
        return initGuesses;
    }
}