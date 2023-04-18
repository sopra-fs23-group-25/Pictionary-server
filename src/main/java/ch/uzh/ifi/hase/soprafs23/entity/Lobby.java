package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    public Lobby() {
        setPlayersInLobby(new ArrayList<>());
    }

    @Id
    @GeneratedValue
    private Long lobbyId;

    @Column(nullable = false, unique = true)
    private String lobbyName;

    @Column(nullable = false)
    private int numberOfPlayers;
    @Column(nullable = false)
    private Long timePerRound;
    @Column(nullable = false)
    private int nrOfRounds;

    @OneToMany (mappedBy = "userId")
    private List<Player> playersInLobby;

    @Column(nullable = false)
    private boolean hasStarted;


    @Column
    private Long hostId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_lobbyId")
    private Game game;

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

    public Long getTimePerRound() {
        return timePerRound;
    }
    public void setTimePerRound(Long timePerRound) {
        this.timePerRound = timePerRound;
    }

    public int getNrOfRounds() {
        return nrOfRounds;
    }
    public void setNrOfRounds(int numberOfRounds) {
        this.nrOfRounds = numberOfRounds;
    }

    public List<Player> getPlayersInLobby() {
        List<Player> clonePlayers = new ArrayList<>();
        if(playersInLobby != null) {
            clonePlayers.addAll(playersInLobby);
            return clonePlayers;}
        return clonePlayers;
    }

    public void setPlayersInLobby(List<Player> playersInLobby) {
        this.playersInLobby = playersInLobby;
    }

    public boolean isHasStarted() {return hasStarted;}
    public void setHasStarted(boolean hasStarted) {this.hasStarted = hasStarted;}

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public void setNumberOfPlayers(int numberOfPlayersInLobby) {
        this.numberOfPlayers = numberOfPlayersInLobby;
    }

    public void addPlayer(Player player){
        playersInLobby.add(player);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isFull() {
        return getNumberOfPlayers() == playersInLobby.size();
    }
    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

}
