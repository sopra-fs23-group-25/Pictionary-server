package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class SessionGetDTO {
    private String token;
    private String username;
    private Long userId;
    private String language;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setLanguage(String language) {this.language = language;}
    public String getLanguage() {return language;}
}