package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Objects;


@Component
public class WebSocketConnectListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String lobbyId = Objects.requireNonNull(accessor.getNativeHeader("lobbyId")).get(0); // get the value of userId header sent from the client-side
        String userId = Objects.requireNonNull(accessor.getNativeHeader("userId")).get(0); // get the value of userId header sent from the client-side
        String sessionId = accessor.getSessionId();
        User user = userRepository.findByUserId(Long.parseLong(userId));
        user.setSessionId(sessionId);
        userRepository.save(user);
        userRepository.flush();
        System.out.println(sessionId + " has connected " + "lobbyId (Header) "+ lobbyId + " userId (Header) "+ userId);
        // perform any necessary initialization or logging here
    }
}
