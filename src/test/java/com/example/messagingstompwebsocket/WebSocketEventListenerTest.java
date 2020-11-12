package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.repository.UserMapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static org.mockito.Mockito.*;

class WebSocketEventListenerTest {

    private WebSocketEventListener listener;
    private UserMapRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(UserMapRepository.class);
        listener = new WebSocketEventListener(repository);
    }

    @Test
    void IT_SAVES_USER_ON_REPOSITORY() {
        listener.handleWebSocketConnectListener(mockSessionConnectedEvent());

        verify(repository).add("anystring");
    }

    @Test
    void IT_REMOVE_USER_FROM_REPOSITORY() {
        listener.handleWebSocketDisconnectListener(mockSessionDisconnectEvent());

        verify(repository).remove("anystring");
    }

    private SessionConnectedEvent mockSessionConnectedEvent() {
        SessionConnectedEvent event = mock(SessionConnectedEvent.class);
        Message<byte[]> message = mockMessage();
        when(event.getMessage()).thenReturn(message);
        return event;
    }
    private SessionDisconnectEvent mockSessionDisconnectEvent() {
        SessionDisconnectEvent event = mock(SessionDisconnectEvent.class);
        Message<byte[]> message = mockMessage();
        when(event.getMessage()).thenReturn(message);
        return event;
    }
    private Message<byte[]> mockMessage() {
        MessageHeaders messageHeaders = mock(MessageHeaders.class);
        when(messageHeaders.get(anyString())).thenReturn("anystring");
        Message<byte[]> message = mock(Message.class);
        when(message.getHeaders()).thenReturn(messageHeaders);
        return message;
    }
}