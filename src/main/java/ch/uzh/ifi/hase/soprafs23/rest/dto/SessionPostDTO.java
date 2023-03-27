
package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class SessionPostDTO {
    private String username;
    private String password;
    //private String language;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    //public String getLanguage() {return language;}
    //public void setLanguage(String language) {this.language = language;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}