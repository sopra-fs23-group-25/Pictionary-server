package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "GAME")
public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long lobbyId;

    @OneToMany (mappedBy = "userId")
    private List<Player> players;

    @OneToMany (mappedBy = "userId")
    private List<Player> painted;

    @OneToMany (mappedBy = "userId")
    private List<Player> notPainted;

    // private Translator translator;

    @ElementCollection
    @CollectionTable(name = "words_painted")
    @Column(name = "word")
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