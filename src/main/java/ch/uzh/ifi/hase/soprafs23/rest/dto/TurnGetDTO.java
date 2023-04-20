package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.entity.Guess;

import java.util.List;

public class TurnGetDTO {
    private long painterId;

    private long timePerRound;

    private List<Guess> guesses;

    private String word; // maybe

    public String getWord() {return word;}

    public void setWord(String word) {this.word = word;}

    public List<Guess> getGuesses() {
        return guesses;
    }
    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }

    public long getPainterId() {
        return painterId;
    }

    public void setPainterId(long painterId) {
        this.painterId = painterId;
    }

    public long getTimePerRound() {
        return timePerRound;
    }

    public void setTimePerRound(long timePerRound) {
        this.timePerRound = timePerRound;
    }
}
