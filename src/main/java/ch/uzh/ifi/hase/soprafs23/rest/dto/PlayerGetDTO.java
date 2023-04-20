package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;

import javax.persistence.Column;
import java.time.Duration;

public class PlayerGetDTO {

    private Long userId;

    private String language;


    private String username;

    boolean isHost = false;

    private PlayerRole currentRole = PlayerRole.GUESSER;

    private Long totalScore;

    public Long getUserId() { return userId; }
    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getLanguage() {return language;}
    public void setLanguage(String language) {this.language = language;}

    public boolean isHost() {return isHost;}
    public void setHost(boolean isHost) {this.isHost = true;}

    public PlayerRole getCurrentRole() {return currentRole;}
    public void setCurrentRole(PlayerRole role) {this.currentRole = role;}

    public Long getTotalScore() {return totalScore;}
    public void setTotalScore(Long totalScore) {this.totalScore = totalScore;}

    public String getUsername() {return username;}
    public void setUsername(String username) {
        this.username = username;
    }
}
