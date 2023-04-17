package ch.uzh.ifi.hase.soprafs23.rest.dto;


import ch.uzh.ifi.hase.soprafs23.entity.Game;

public class LobbyGetDTO {


    private Long lobbyId;

    private String lobbyName;

    private int numberOfPlayers;

    private int nrOfRounds;

    private Long timePerRound;

    private boolean hasStarted;

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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int nrOfPlayers) {
        this.numberOfPlayers = nrOfPlayers;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }
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
}
