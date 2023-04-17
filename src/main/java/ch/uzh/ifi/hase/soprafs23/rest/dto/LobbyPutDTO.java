package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class LobbyPutDTO {

    private Long userId;

    private Long lobbyId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

}
