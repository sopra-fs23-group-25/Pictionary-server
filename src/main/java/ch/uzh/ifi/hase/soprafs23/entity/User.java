package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key
 * - userId: Long
 * - username: String
 * - password: String
 * - language: String
 * - lobbyId: Long = null
 * - status: UserStatus
 * - token: String
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UserStatus status;


    @Column()
    private String sessionId;

    public String getSessionId() {return sessionId;}

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public Long getUserId() { return userId; }
    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {return password;}
    public void setPassword(String password) { this.password = password;}

    public String getLanguage() {return language;}
    public void setLanguage(String language) {this.language = language;}

    public String getToken() {return token;}
    public void setToken(String token) {
        this.token = token;
    }

    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Player convertToPlayer() {
        Player player = new Player();
        player.setUserId(this.getUserId());
        player.setLanguage(this.language);
        player.setUsername(this.getUsername());
        return player;
    }
}
