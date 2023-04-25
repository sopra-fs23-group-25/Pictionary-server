package ch.uzh.ifi.hase.soprafs23.entity;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Turn implements Serializable {

    // ensures Turn gets initialized with an empty list instead of null
    public Turn(){
        this.guesses = new ArrayList<>();
    }

    //  needed to copy a Turn object to prevent reference usage
    public Turn(Turn turn){
        this.painterId = turn.getPainterId();
        this.timePerRound = turn.getTimePerRound();
        this.correctGuesses = turn.getCorrectGuesses();
        this.guesses = turn.getGuesses();
        this.word = turn.getWord();
    }

    private long painterId;

    private long timePerRound;

    private int correctGuesses = 0;

    private List<Guess> guesses;

    private String word;

    public String getWord() {return word;}

    public void setWord(String word) {this.word = word;}

    public List<Guess> getGuesses() {
        return guesses;
    }
    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }

    public int getCorrectGuesses() {
        return correctGuesses;
    }

    public void setCorrectGuesses(int correctGuesses) {
        this.correctGuesses = correctGuesses;
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

    public void addGuess(Guess guess) {
        for (Guess initializedGuess : guesses) {
            if (initializedGuess.getUserId() == guess.getUserId()) {
                if(initializedGuess.getGuess() != null) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "There is already a guess for this player!"); // insert username
                }
                initializedGuess.setGuess(guess.getGuess());
                initializedGuess.setScore(guess.getScore());
            }
        }
    }
}
