package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Lobby findByLobbyId(long lobbyId);

    Lobby findByHostId(long hostId);

    Lobby findByLobbyName(String lobbyName);

    Lobby findByPlayersContaining(Player player);

}
