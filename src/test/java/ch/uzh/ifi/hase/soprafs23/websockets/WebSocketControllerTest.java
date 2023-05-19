package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.controller.WebSocketController;

import ch.uzh.ifi.hase.soprafs23.websockets.dto.DrawingMessageDTO;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.MessageRelayDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebSocketControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebSocketController webSocketController;

    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private final WsTestUtil wsTestUtil = new WsTestUtil();

    private final String WEBSOCKET_PREFIX = "/topic";

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println(port);
        String wsUrl = "ws://localhost:" + port + "/ws";
        stompClient = wsTestUtil.createWebSocketClient();
        stompSession = stompClient.connect(wsUrl, new WsTestUtil.MyStompSessionHandler()).get();
    }

    @AfterEach
    public void tearDown() throws Exception {
        stompSession.disconnect();
        stompClient.stop();
    }
    @Test
    void connectsToSocket() throws Exception {
        assertThat(stompSession.isConnected()).isTrue();
    }

    @Test
    void sendPictureChanges() throws Exception{
        CompletableFuture<DrawingMessageDTO> resultKeeper = new CompletableFuture<>();
        int lobbyId = 1;
        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/"+lobbyId+"/drawing-all",
                new WsTestUtil.ImageStreamingFrameHandler(resultKeeper::complete));

        Thread.sleep(1000);

        DrawingMessageDTO drawingMessageDTO = new DrawingMessageDTO();

        drawingMessageDTO.setCurrX(1f);
        drawingMessageDTO.setCurrY(1f);
        drawingMessageDTO.setPrevX(2f);
        drawingMessageDTO.setPrevY(2f);
        drawingMessageDTO.setColor("red");
        drawingMessageDTO.setLineWidth(10);

        Thread.sleep(1000);

        stompSession.send("/app/lobbies/" + lobbyId + "/drawing-all", drawingMessageDTO);

        DrawingMessageDTO receivedMessage = resultKeeper.get(2, SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getColor()).isEqualTo(drawingMessageDTO.getColor());
    }

    @Test
    void sendGameState() throws Exception{
        CompletableFuture<MessageRelayDTO> resultKeeper = new CompletableFuture<>();
        int lobbyId = 1;
        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/" + lobbyId + "/game-state",
                new WsTestUtil.MessageRelayFrameHandler(resultKeeper::complete));

        Thread.sleep(1000);

        MessageRelayDTO messageRelayDTO = new MessageRelayDTO();
        messageRelayDTO.setTask("test");

        Thread.sleep(1000);

        stompSession.send("/app/lobbies/" + lobbyId + "/game-state", messageRelayDTO);

        MessageRelayDTO receivedMessage = resultKeeper.get(2, SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getTask()).isEqualTo(messageRelayDTO.getTask());
    }

    @Test
    void hostDisconnected() throws Exception{
        CompletableFuture<MessageRelayDTO> resultKeeper = new CompletableFuture<>();
        long lobbyId = 1;
        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/" + lobbyId + "/host-disconnected",
                new WsTestUtil.MessageRelayFrameHandler(resultKeeper::complete));

        Thread.sleep(1000);

        MessageRelayDTO messageRelayDTO = new MessageRelayDTO();
        messageRelayDTO.setTask("test");

        Thread.sleep(1000);

        webSocketController.sendHostDisconnected(messageRelayDTO, lobbyId);

        MessageRelayDTO receivedMessage = resultKeeper.get(2, SECONDS);
        assertThat(receivedMessage).isNotNull();
    }

    @Test
    void closeLobby() throws Exception{
        CompletableFuture<MessageRelayDTO> resultKeeper = new CompletableFuture<>();
        int lobbyId = 1;
        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/" + lobbyId + "/lobby-closed",
                new WsTestUtil.MessageRelayFrameHandler(resultKeeper::complete));

        Thread.sleep(1000);

        MessageRelayDTO messageRelayDTO = new MessageRelayDTO();
        messageRelayDTO.setTask("test");

        Thread.sleep(1000);

        stompSession.send("/app/lobbies/" + lobbyId + "/lobby-closed", messageRelayDTO);

        MessageRelayDTO receivedMessage = resultKeeper.get(2, SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getTask()).isEqualTo(messageRelayDTO.getTask());
    }

    @Test
    void startGameInLobby() throws Exception{
        CompletableFuture<MessageRelayDTO> resultKeeper = new CompletableFuture<>();
        int lobbyId = 1;
        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/" + lobbyId + "/start-game",
                new WsTestUtil.MessageRelayFrameHandler(resultKeeper::complete));

        Thread.sleep(1000);

        MessageRelayDTO messageRelayDTO = new MessageRelayDTO();
        messageRelayDTO.setTask("test");

        Thread.sleep(1000);

        stompSession.send("/app/lobbies/" + lobbyId + "/start-game", messageRelayDTO);

        MessageRelayDTO receivedMessage = resultKeeper.get(2, SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getTask()).isEqualTo(messageRelayDTO.getTask());
    }

    @Test
    void clearDrawing() throws Exception{
        CompletableFuture<MessageRelayDTO> resultKeeper = new CompletableFuture<>();
        int lobbyId = 1;
        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/" + lobbyId + "/drawing-clear",
                new WsTestUtil.MessageRelayFrameHandler(resultKeeper::complete));

        Thread.sleep(1000);

        MessageRelayDTO messageRelayDTO = new MessageRelayDTO();
        messageRelayDTO.setTask("test");

        Thread.sleep(1000);

        stompSession.send("/app/lobbies/" + lobbyId + "/drawing-clear", messageRelayDTO);

        MessageRelayDTO receivedMessage = resultKeeper.get(2, SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getTask()).isEqualTo(messageRelayDTO.getTask());
    }

}
