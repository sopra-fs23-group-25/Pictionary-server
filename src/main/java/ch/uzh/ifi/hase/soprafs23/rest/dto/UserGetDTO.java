package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

public class UserGetDTO {

  private Long userId;
  private String username;
  private String language;
  private Long lobbyId;
  private UserStatus status;

  private String token;


    public Long getId() { return userId; }
    public void setId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguage() {return language;}
    public void setLanguage(String language) {this.language = language;}

    public Long getLobbyId() {return lobbyId;}
    public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
}
