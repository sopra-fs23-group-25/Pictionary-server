package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

import javax.persistence.*;
import java.util.List;

public class GameGetDTO {

    private Long lobbyId;

    private List<Player> players;

    private List<Player> painted;

    private List<Player> notPainted;

    // private Translator translator;

    private List<String> wordsPainted;

    private String word;

    public Long getLobbyId() {return lobbyId;}
    public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {this.players = players;}

    public List<Player> getPainted() {return painted;}
    public void setPainted(List<Player> painted) {this.painted = painted;}

    public List<Player> getNotPainted() {return notPainted;}
    public void setNotPainted(List<Player> notPainted) {this.notPainted = notPainted;}

    public List<String> getWordsPainted() {return wordsPainted;}
    public void setWordsPainted(List<String> wordsPainted) {this.wordsPainted = wordsPainted;}

    public String getWord() {return word;}
    public void setWord(String word) {this.word = word;}
}
