package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class UserGetDTO {

  private Long userId;
  private String username;
  private String language;

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

}
