package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.websockets.WebSocketConnectListener;
import ch.uzh.ifi.hase.soprafs23.websockets.WebSocketDisconnectListener;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.ClearMessageDTO;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.DrawingMessageDTO;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.UserJoinGameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private WebSocketDisconnectListener webSocketEventListener;

    @Autowired
    private WebSocketConnectListener webSocketConnectListener;

    private final String WEBSOCKET_PREFIX = "/topic";

    @MessageMapping("/lobbies/{lobbyId}/drawing-all")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/drawing-all")
    public DrawingMessageDTO sendToAll(@Payload DrawingMessageDTO message) {
        return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/drawing-clear")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/drawing-clear")
    public ClearMessageDTO clearDrawing(@Payload ClearMessageDTO message) {
        return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/user-join")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/users")
    public UserJoinGameDTO sendUserList(@Payload UserJoinGameDTO message, @DestinationVariable  Integer lobbyId) {
        return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/user-leave")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/users")
    public UserJoinGameDTO sendUserListAfterUserLeft(@Payload UserJoinGameDTO message) {
        return message;
    }

    @MessageMapping("/lobbies/{lobbyId}/start-game")
    @SendTo(WEBSOCKET_PREFIX + "/lobbies/{lobbyId}/start-game")
    public UserJoinGameDTO startGame(@Payload UserJoinGameDTO message) {
        // set gameHasStarted

        return message;
    }
}
