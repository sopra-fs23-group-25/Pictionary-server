package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class WebSocketController {

    private final WebSocketService websocketService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final String WEBSOCKET_PREFIX = "/topic";

    public WebSocketController(WebSocketService websocketService) {
        this.websocketService = websocketService;
    }

    @MessageMapping("/lobbies/{lobbyId}/drawing-all")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/drawing-all")
    public DrawingMessageDTO sendToAll(@Payload DrawingMessageDTO message) {
        return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/drawing-clear")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/drawing-clear")
    public MessageRelayDTO clearDrawing(@Payload MessageRelayDTO message) {
    return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/users")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/users")
    public List<UserSocketGetDTO> sendUserList(@Payload UserJoinGameDTO message, @DestinationVariable  Long lobbyId) {
        return websocketService.getUsersInLobby(lobbyId);
    }


    @MessageMapping("/lobbies/{lobbyId}/start-game")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/start-game")
    public MessageRelayDTO startGame(@Payload MessageRelayDTO message) {
        return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/lobby-closed")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/lobby-closed")
    public MessageRelayDTO closeLobby(@Payload MessageRelayDTO message) {
        return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/host-disconnected")
    public MessageRelayDTO sendHostDisconnected(@Payload MessageRelayDTO message, Long lobbyId){
        String destination = WEBSOCKET_PREFIX + "/lobbies/" + lobbyId + "/host-disconnected";
        messagingTemplate.convertAndSend(destination, message);
        return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/game-state")
    public MessageRelayDTO sendGameState(@Payload MessageRelayDTO message, @DestinationVariable Long lobbyId ){
        String destination = WEBSOCKET_PREFIX + "/lobbies/" + lobbyId + "/game-state";
        messagingTemplate.convertAndSend(destination, message);
        return message;
    }
}
