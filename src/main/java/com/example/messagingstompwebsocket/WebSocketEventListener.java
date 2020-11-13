package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.repository.UserMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final UserMapRepository userRepository;

    public WebSocketEventListener(UserMapRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection: {}", event.toString());
        userRepository.add(String.valueOf(event.getMessage().getHeaders().get("simpSessionId")));
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("Received a new web socket disconnection: {}", event.toString());
        userRepository.remove(String.valueOf(event.getMessage().getHeaders().get("simpSessionId")));
    }
}
