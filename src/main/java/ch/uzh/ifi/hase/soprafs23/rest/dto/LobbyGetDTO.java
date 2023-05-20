package ch.uzh.ifi.hase.soprafs23.rest.dto;


import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.util.List;

public class LobbyGetDTO {

    private Long lobbyId;

    private List<Player> players;

    private String lobbyName;

    private int maxNrOfPlayers;

    private int nrOfRounds;

    private Long timePerRound;

    private boolean isRunning;

    public Long getLobbyId() {
        return lobbyId;
    }
    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyName() {
        return lobbyName;
    }
    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public int getMaxNrOfPlayers() {return maxNrOfPlayers;}
    public void setMaxNrOfPlayers(int maxNrOfPlayers) {this.maxNrOfPlayers = maxNrOfPlayers;}

    public int getNrOfRounds() {
        return nrOfRounds;
    }
    public void setNrOfRounds(int nrOfRounds) {
        this.nrOfRounds = nrOfRounds;
    }

    public Long getTimePerRound() {
        return timePerRound;
    }
    public void setTimePerRound(Long timePerRound) {
        this.timePerRound = timePerRound;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {this.players = players;}

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
