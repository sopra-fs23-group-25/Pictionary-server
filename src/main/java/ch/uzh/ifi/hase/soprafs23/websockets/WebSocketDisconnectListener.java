package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.controller.WebSocketController;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.MessageRelayDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LobbyService lobbyService;

    @Autowired
    LobbyRepository lobbyRepository;
    @Autowired
    WebSocketController webSocketController;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        User userLeaving = userRepository.findBySessionId(sessionId);
        Lobby lobbyLeft;
        lobbyLeft = lobbyRepository.findByHostId(userLeaving.getUserId());
        if(lobbyLeft != null){
            MessageRelayDTO newMessage = new MessageRelayDTO();
            newMessage.setTask("Host Disconnected");
            webSocketController.sendHostDisconnected(newMessage);
            lobbyService.deleteLobby(lobbyLeft);
        }

        userLeaving.setSessionId(null);
        userRepository.flush();
        System.out.println(sessionId + " has closed connection");
        // perform any necessary cleanup or logging here
    }
}