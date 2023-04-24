package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.Entity;
import java.io.Serializable;
import java.time.Duration;

public class Guess implements Serializable, Cloneable {

    private Long userId;
    private String username;
    private String guess;
    private Long score;

    public Guess(Long userId, String guess, long score){
        this.userId = userId;
        this.guess = guess;
        this.score = score;
    }

    @Override
    public Guess clone() {
        try {
            return (Guess) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Unable to clone Guess object", e);
        }
    }

    public Guess(){}

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
