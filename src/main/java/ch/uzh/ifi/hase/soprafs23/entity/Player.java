package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;

@Entity
public class Player implements Serializable, Comparable<Player> {
    @Id
    @GeneratedValue
    private long playerId;

    @Column
    private Long userId;
    @Column
    String language;

    @Column
    String username;
    @Column
    boolean isHost = false;
    @Column
    PlayerRole currentRole = PlayerRole.GUESSER;
    @Column
    Long totalScore = 0L;

    @Override
    public int compareTo(Player player) {
        return this.totalScore.compareTo(player.totalScore);
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getLanguage() {return language;}
    public void setLanguage(String language) {this.language = language;}

    public boolean isHost() {return isHost;}
    public void makeHost() {this.isHost = true;}

    public PlayerRole getCurrentRole() {return currentRole;}
    public void setCurrentRole(PlayerRole role) {this.currentRole = role;}

    public Long getTotalScore() {return totalScore;}
    public void setTotalScore(Long totalScore) {this.totalScore = totalScore;}

    public String getUsername() {return username;}
    public void setUsername(String username) {
        this.username = username;
    }
}

