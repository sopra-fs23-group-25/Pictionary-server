package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class GuessPutDTO {

    private Long userId;
    private String guess;

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

}
