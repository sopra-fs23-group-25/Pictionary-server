package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Duration;

@Entity
@Table(name = "PLAYER")
public class Player implements Serializable, Comparable<Player> {
    @Id
    private Long userId;
    @Column
    String language;
    @Column
    boolean isHost = false;
    @Column
    PlayerRole currentRole = PlayerRole.GUESSER;
    /*@Column
    Guess currentGuess;*/
    @Column
    Long totalScore;
    @Column
    Duration totalTime;

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

    public Duration getTotalTime() {return totalTime;}
    public void setTotalTime(Duration totalTime) {this.totalTime = totalTime;}
}

