package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Lobby() {
        setPlayers(new ArrayList<>());
    }

    @Id
    @GeneratedValue
    private Long lobbyId;

    @Column(nullable = false, unique = true)
    private String lobbyName;

    @Column(nullable = false)
    private int maxNrOfPlayers;

    @Column(nullable = false)
    private Long timePerRound;
    @Column(nullable = false)
    private int nrOfRounds;

    @OneToMany (cascade = CascadeType.PERSIST)
    private List<Player> players;

    @Column(nullable = false)
    private boolean isRunning = false;

    @Column(unique = true)
    private Long hostId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    public List<Player> getPlayers() {
        List<Player> clonePlayers = new ArrayList<>();
        if(players != null) {
            clonePlayers.addAll(players);
            return clonePlayers;}
        return clonePlayers;
    }

    public void setPlayers(List<Player> playersInLobby) {
        this.players = playersInLobby;
    }

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean isRunning) {this.isRunning = isRunning;}

    public int getMaxNrOfPlayers() {
        return maxNrOfPlayers;
    }
    public void setMaxNrOfPlayers(int numberOfPlayersInLobby) {
        this.maxNrOfPlayers = numberOfPlayersInLobby;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isFull() {
        return maxNrOfPlayers == players.size();
    }
    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }
}
