package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;


@Component
public class WebSocketConnectListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String lobbyId = accessor.getNativeHeader("lobbyId").get(0); // get the value of userId header sent from the client-side
        String userId = accessor.getNativeHeader("userId").get(0); // get the value of userId header sent from the client-side
        String sessionId = accessor.getSessionId();
        userRepository.findByUserId(Long.parseLong(userId)).setSessionId(sessionId);
        System.out.println(sessionId + " has connected " + "lobbyId (Header) "+ lobbyId + " userId (Header) "+ userId);
        // perform any necessary initialization or logging here
    }
}
