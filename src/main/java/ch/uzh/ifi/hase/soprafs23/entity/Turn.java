package ch.uzh.ifi.hase.soprafs23.entity;


import java.io.Serializable;
import java.util.List;


public class Turn implements Serializable {

    public Turn(){

    }

    public Turn(long painterId, long timePerRound, int correctGuesses, List<Guess> guesses, String word){
        this.painterId = painterId;
        this.timePerRound = timePerRound;
        this.correctGuesses = correctGuesses;
        this.guesses = guesses;
        this.word = word;
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
        this.guesses.add(guess);
    }
}
