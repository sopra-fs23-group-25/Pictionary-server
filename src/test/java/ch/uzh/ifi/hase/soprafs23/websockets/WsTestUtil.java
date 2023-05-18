package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.websockets.dto.DrawingMessageDTO;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.MessageRelayDTO;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.asm.Type.getType;

public class WsTestUtil {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    WebSocketStompClient createWebSocketClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient transport = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(transport);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

    static class MyStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            super.afterConnected(session, connectedHeaders);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            super.handleException(session, command, headers, payload, exception);
        }
    }

    public static class ImageStreamingFrameHandler implements StompFrameHandler {

        private final Consumer<DrawingMessageDTO> frameHandler;

        public ImageStreamingFrameHandler(Consumer<DrawingMessageDTO> frameHandler) {
            this.frameHandler = frameHandler;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return DrawingMessageDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            DrawingMessageDTO obj = (DrawingMessageDTO) payload;
            log.info("received message: {} with headers: {}", obj, headers);
            frameHandler.accept(obj);
        }
    }
    public static class MessageRelayFrameHandler implements StompFrameHandler {

        private final Consumer<MessageRelayDTO> frameHandler;

        public MessageRelayFrameHandler(Consumer<MessageRelayDTO> frameHandler) {
            this.frameHandler = frameHandler;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return MessageRelayDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            MessageRelayDTO obj = (MessageRelayDTO) payload;
            log.info("received message: {} with headers: {}", obj, headers);
            frameHandler.accept(obj);
        }
    }


}
