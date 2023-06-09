package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.UserSocketGetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

    @Service
    @Transactional
public class WebSocketService {

        private final LobbyRepository lobbyRepository;


        @Autowired
        public WebSocketService(
                @Qualifier("lobbyRepository") LobbyRepository lobbyRepository)
        {
            this.lobbyRepository = lobbyRepository;
        }

        public List<UserSocketGetDTO> getUsersInLobby(Long lobbyId) {
            Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
            List<UserSocketGetDTO> listOfPlayers = new ArrayList<>();
            for (Player player:lobby.getPlayers()){
                UserSocketGetDTO currentPlayer = new UserSocketGetDTO();
                currentPlayer.setUsername(player.getUsername());
                currentPlayer.setUserId(player.getUserId());
                listOfPlayers.add(currentPlayer);
            }
            return listOfPlayers;

        }
}
