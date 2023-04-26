package ch.uzh.ifi.hase.soprafs23.websockets.dto;

public class UserSocketGetDTO {
    private String username;
    private long userId;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId (long userId) {this.userId = userId;}
    public long getUserId() {return userId;}



}
