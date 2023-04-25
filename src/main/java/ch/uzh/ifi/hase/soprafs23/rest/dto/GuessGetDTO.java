package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class GuessGetDTO {

    private String username;
    private String guess;
    private Long score;

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

    public String getUsername() {return this.username;}
    public void setUsername(String username) {this.username = username;}

}
