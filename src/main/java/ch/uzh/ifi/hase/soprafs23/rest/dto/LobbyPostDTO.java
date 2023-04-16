package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.sql.Time;

public class LobbyPostDTO {

    private String lobbyName;

    private Long timePerRound;

    private int nrOfRounds;




    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public Long getTimePerRound() {
        return timePerRound;
    }

    public void setTimePerRound(Long timePerRound) {
        this.timePerRound = timePerRound;
    }

    public int getNrOfRounds() {
        return nrOfRounds;
    }

    public void setNrOfRounds(int nrOfRounds) {
        this.nrOfRounds = nrOfRounds;
    }


}
