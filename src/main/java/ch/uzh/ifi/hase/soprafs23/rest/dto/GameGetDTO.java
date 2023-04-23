package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

import javax.persistence.*;
import java.util.List;

public class GameGetDTO {

    private Long lobbyId;

    private List<Player> players;


    private List<String> wordsPainted;

    private int nrOfRoundsPlayed;

    public Long getLobbyId() {return lobbyId;}
    public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {this.players = players;}

    public List<String> getWordsPainted() {return wordsPainted;}
    public void setWordsPainted(List<String> wordsPainted) {this.wordsPainted = wordsPainted;}

    public int getNrOfRoundsPlayed() {return nrOfRoundsPlayed;}
    public void setNrOfRoundsPlayed(int nrOfRoundsPlayed) {this.nrOfRoundsPlayed = nrOfRoundsPlayed;}

}
