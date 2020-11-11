package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.message.InputMessage;
import com.example.messagingstompwebsocket.message.MessageType;
import com.example.messagingstompwebsocket.message.OutputMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private Map<String, String> players = new HashMap<>();

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public OutputMessage sendMessage(@Payload InputMessage inputMessage, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        logger.info("######### sendMessage #########");
        logger.info("inputMessage = " + inputMessage);
        logger.info("headerAccessor = " + headerAccessor);

        if (players.containsValue(inputMessage.getSender())) {
            OutputMessage outputMessage = new OutputMessage(MessageType.CHAT);
            outputMessage.setSender(inputMessage.getSender());
            outputMessage.setContent(inputMessage.getContent());

            return outputMessage;
        } else {
            throw new RuntimeException("BOMBA, UN GIOCATORE IN PIÙ!");
        }
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public OutputMessage addUser(@Payload InputMessage inputMessage, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("#########   addUser   #########");
        logger.info("inputMessage = " + inputMessage);
        logger.info("headerAccessor = " + headerAccessor);

        if (players.containsKey(inputMessage.getLocation())) {
            throw new RuntimeException("POSTO  OCCUPATO!");
        } else {
            players.put(inputMessage.getLocation(), inputMessage.getSender());
        }

        // Add username in web socket session
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("username", inputMessage.getSender());
        }

        OutputMessage outputMessage = new OutputMessage(MessageType.JOIN);
        outputMessage.setSender(inputMessage.getSender());
        outputMessage.setContent(inputMessage.getContent());

        return outputMessage;
    }
}
