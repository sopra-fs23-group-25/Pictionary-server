package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.controller.WebSocketController;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.DrawingMessageDTO;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.MessageRelayDTO;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.UserJoinGameDTO;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.UserSocketGetDTO;
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

    @Mock
    private LobbyRepository lobbyRepository;
    @InjectMocks
    private WebSocketService webSocketService;
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
        CompletableFuture<DrawingMessageDTO> resultKeeper = new CompletableFuture<>();
        int lobbyId = 1;
        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/"+lobbyId+"/drawing-all",
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
                new WsTestUtil.MessageRelayFrameHandlerGameSettings(resultKeeper::complete));

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
                new WsTestUtil.MessageRelayFrameHandlerGameSettings(resultKeeper::complete));

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
                new WsTestUtil.MessageRelayFrameHandlerGameSettings(resultKeeper::complete));

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
                new WsTestUtil.MessageRelayFrameHandlerGameSettings(resultKeeper::complete));

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
                new WsTestUtil.MessageRelayFrameHandlerGameSettings(resultKeeper::complete));

        Thread.sleep(1000);

        MessageRelayDTO messageRelayDTO = new MessageRelayDTO();
        messageRelayDTO.setTask("test");

        Thread.sleep(1000);

        stompSession.send("/app/lobbies/" + lobbyId + "/drawing-clear", messageRelayDTO);

        MessageRelayDTO receivedMessage = resultKeeper.get(2, SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getTask()).isEqualTo(messageRelayDTO.getTask());
    }

    /*@Test
    void getUser() throws Exception{

        long lobbyId = 1L;
        CompletableFuture<List<UserSocketGetDTO>> resultKeeper = new CompletableFuture<>();
        Lobby testLobby = new Lobby();
        Game testGame = new Game();

        Player testPlayer = new Player();
        testPlayer.setUserId(1L);
        testPlayer.setUsername("testName");

        List<Player> playerList = new ArrayList<>();
        playerList.add(testPlayer);

        testLobby.setLobbyId(1L);
        testLobby.setPlayers(playerList);
        testLobby.setHostId(1L);
        testLobby.setNrOfRounds(1);
        testLobby.setMaxNrOfPlayers(1);
        testLobby.setTimePerRound(1L);

        testGame.setLobbyId(1L);

        testLobby.setGame(testGame);

        stompSession.subscribe(WEBSOCKET_PREFIX + "/lobbies/" + lobbyId + "/users",
                new WsTestUtil.GetUserFrameHandlerGameSettings(resultKeeper::complete));

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        UserJoinGameDTO userJoinGameDTO = new UserJoinGameDTO();
        userJoinGameDTO.setTask("test");
        Thread.sleep(1000);

        stompSession.send("/app/lobbies/" + lobbyId + "/users", userJoinGameDTO);

        Thread.sleep(1000);


        List<UserSocketGetDTO> receivedMessage = resultKeeper.get(2, SECONDS);
        assertThat(receivedMessage).isNotNull();
    }*/
}
