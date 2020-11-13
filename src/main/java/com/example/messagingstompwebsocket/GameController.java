package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.entity.Game;
import com.example.messagingstompwebsocket.message.InputMessage;
import com.example.messagingstompwebsocket.message.MessageType;
import com.example.messagingstompwebsocket.message.OutputMessage;
import com.example.messagingstompwebsocket.repository.GameMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;
import java.util.UUID;

@Controller
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameMapRepository gameRepository;

    public GameController(GameMapRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @MessageMapping("/game.create")
    @SendTo("/games/list")
    public OutputMessage createGame(@Payload InputMessage inputMessage, @Header("simpSessionId") String sessionId) {
        logger.info("sessionId: {}", sessionId);
        logger.info("inputMessage: {}", inputMessage);

        Game game = new Game(UUID.randomUUID().toString());
        if (inputMessage.getLocation().equals("EARTH")) {
            game.setUserOnEarth(sessionId);
        } else if (inputMessage.getLocation().equals("MOON")) {
            game.setUserOnMoon(sessionId);
        }

        String gameId = gameRepository.write(game);
        OutputMessage outputMessage = new OutputMessage(MessageType.NEW_GAME);
        outputMessage.setSender(sessionId);
        outputMessage.setContent(gameId);
        outputMessage.setLocation(inputMessage.getLocation());

        return outputMessage;
    }

    @MessageMapping("/game.join")
    @SendTo("/games/list")
    public OutputMessage joinGame(@Payload InputMessage inputMessage, @Header("simpSessionId") String sessionId) {
        logger.info("sessionId: {}", sessionId);
        logger.info("inputMessage: {}", inputMessage);

        gameRepository.read(inputMessage.getContent())
                .ifPresent(game -> {
                    if (inputMessage.getLocation().equals("EARTH") && game.getUserOnEarth() != null) {
                        game.setUserOnEarth(sessionId);
                    } else if (inputMessage.getLocation().equals("MOON") && game.getUserOnMoon() != null) {
                        game.setUserOnMoon(sessionId);
                    }
                    gameRepository.write(game);
                });

        OutputMessage outputMessage = new OutputMessage(MessageType.REMOVE_GAME);
        outputMessage.setSender(sessionId);
        outputMessage.setContent(inputMessage.getContent());
        return outputMessage;
    }

    @GetMapping("/games/list")
    public ResponseEntity<Set<Game>> countNewMessages() {
        return ResponseEntity.ok(gameRepository.readAll());
    }
}
