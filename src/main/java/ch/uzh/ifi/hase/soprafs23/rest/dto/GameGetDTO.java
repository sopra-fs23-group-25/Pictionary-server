package ch.uzh.ifi.hase.soprafs23.rest.dto;
import ch.uzh.ifi.hase.soprafs23.entity.Image;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import java.util.List;

public class GameGetDTO {

    private List<Player> players;

    private List<String> wordsPainted;

    private int currentRound;

    private boolean gameOver;

    private List<Image> images;

    public List<Player> getPlayers() {return players;}
    public void setPlayers(List<Player> players) {this.players = players;}

    public List<String> getWordsPainted() {return wordsPainted;}
    public void setWordsPainted(List<String> wordsPainted) {this.wordsPainted = wordsPainted;}

    public int getCurrentRound() {return currentRound;}
    public void setCurrentRound(int currentRound) {this.currentRound = currentRound;}

    public boolean isGameOver() {return gameOver;}
    public void setGameOver(boolean gameOver) {this.gameOver = gameOver;}

    public List<Image> getImages() {return images;}
    public void setImages(List<Image> images) {this.images = images;}
}
