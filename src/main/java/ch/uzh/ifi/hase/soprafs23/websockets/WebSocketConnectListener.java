package ch.uzh.ifi.hase.soprafs23.websockets;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;


@Component
public class WebSocketConnectListener implements ApplicationListener<SessionConnectEvent> {

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String lobbyId = accessor.getNativeHeader("lobbyId").get(0); // get the value of userId header sent from the client-side
        String userId = accessor.getNativeHeader("userId").get(0); // get the value of userId header sent from the client-side

        String sessionId = accessor.getSessionId();
        System.out.println(sessionId + " has connected " + "lobbyId (Header) "+ lobbyId + " userId (Header) "+ userId);
        // perform any necessary initialization or logging here
    }
}
