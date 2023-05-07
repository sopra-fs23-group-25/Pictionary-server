package ch.uzh.ifi.hase.soprafs23.entity;

public class Session {
    private String username;
    private String password;
    private long userId;
    private String token;

    private String language;

    public Session(String username, String password, long userId, String token, String language) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.token = token;
        this.language = language;
    }

    // Needed for mapper
    public Session() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLanguage() {return language;}
}