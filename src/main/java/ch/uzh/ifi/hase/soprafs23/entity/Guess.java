package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.Entity;
import java.time.Duration;

public class Guess {

    private Long userId;
    private String guess;
    private Duration time;
    private Long score;


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

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
