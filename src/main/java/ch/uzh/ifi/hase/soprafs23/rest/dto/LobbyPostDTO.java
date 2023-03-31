package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.sql.Time;

public class LobbyPostDTO {

    private String lobbyName;

    private Time timePerRound;

    private int nrOfRounds;




    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public Time getTimePerRound() {
        return timePerRound;
    }

    public void setTimePerRound(Time timePerRound) {
        this.timePerRound = timePerRound;
    }

    public int getNrOfRounds() {
        return nrOfRounds;
    }

    public void setNrOfRounds(int nrOfRounds) {
        this.nrOfRounds = nrOfRounds;
    }


}
