package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class GuessDTO {

    private Long userId;
    private String guess;

    private Long score;

    public Long getUserId() {
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

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

}
