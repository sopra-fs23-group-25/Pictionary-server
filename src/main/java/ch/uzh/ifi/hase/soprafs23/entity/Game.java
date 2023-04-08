package ch.uzh.ifi.hase.soprafs23.entity;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.data.annotation.Id;

import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Game implements Serializable {

    @Id
    private Long lobbyId;

    @OneToMany
    private ArrayList<Player> players;

    @OneToMany
    private ArrayList<Long> painted;

    @OneToMany
    private ArrayList<Long> notPainted;

    // private Translator translator;

    @OneToMany
    private ArrayList <String> wordsPainted;

    private String word;

    public Long getLobbyId() {return lobbyId;}
    public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void setPlayers(ArrayList<Player> players) {this.players = players;}

    public String getWord() {return word;}
    public void setWord(String word) {this.word = word;}
}