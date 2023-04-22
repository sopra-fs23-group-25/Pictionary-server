package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Game")
public class Game implements Serializable {

    @Id
    private Long lobbyId; //can delete

    @Column
    private int nrOfRounds;

    @Column
    private long timePerRound;

    @Column
    private boolean isRunning;

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

    public int getNrOfRounds() {
        return nrOfRounds;
    }

    public void setNrOfRounds(int nrOfRounds) {
        this.nrOfRounds = nrOfRounds;
    }

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
        return null;
    }

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean isRunning) {this.isRunning = isRunning;}
}