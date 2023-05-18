package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.controller.WebSocketController;

import ch.uzh.ifi.hase.soprafs23.websockets.dto.DrawingMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

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
    @Test
    void connectsToSocket() throws Exception {
        assertThat(stompSession.isConnected()).isTrue();
    }

    @Test
    void sendPictureChanges() throws Exception{
        CompletableFuture<String> resultKeeper = new CompletableFuture<>();
        int lobbyId = 1;
        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/1/drawing-all",
                new WsTestUtil.ImageStreamingFrameHandlerGameSettings(resultKeeper::complete));

        Thread.sleep(1000);

        DrawingMessageDTO drawingMessageDTO = new DrawingMessageDTO();

        drawingMessageDTO.setCurrX(1f);
        drawingMessageDTO.setCurrY(1f);
        drawingMessageDTO.setPrevX(2f);
        drawingMessageDTO.setPrevY(2f);
        drawingMessageDTO.setColor("red");
        drawingMessageDTO.setLineWidth(10);

        Thread.sleep(1000);

        webSocketController.sendToAll(drawingMessageDTO);
        assertThat(resultKeeper.get(2, SECONDS)).isEqualTo(drawingMessageDTO.toString());
    }
}
