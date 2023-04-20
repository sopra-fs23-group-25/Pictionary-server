package ch.uzh.ifi.hase.soprafs23.rest.dto;


public class LobbyGetDTO {

    private Long lobbyId;

    private String lobbyName;

    private int maxNrOfPlayers;

    private int nrOfRounds;

    private Long timePerRound;

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

    public int getmaxNrOfPlayers() {
        return maxNrOfPlayers;
    }

    public void setmaxNrOfPlayers(int maxNrOfPlayers) {
        this.maxNrOfPlayers = maxNrOfPlayers;
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
