package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.entity.Game;
import com.example.messagingstompwebsocket.message.InputMessage;
import com.example.messagingstompwebsocket.message.MessageType;
import com.example.messagingstompwebsocket.message.OutputMessage;
import com.example.messagingstompwebsocket.repository.GameMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameMapRepository gameRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameController(GameMapRepository gameRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameRepository = gameRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
//
//    @MessageMapping("/game.create")
//    @SendTo("/games/list")
//    public OutputMessage createGame(@Payload InputMessage inputMessage, @Header("simpSessionId") String sessionId) {
//        logger.info("/game.create, sessionId: {}, inputMessage: {}", sessionId, inputMessage);
//
//        Game game = new Game(UUID.randomUUID().toString());
//        if (inputMessage.getLocation().equals("EARTH")) {
//            game.setUserOnEarth(sessionId);
//        } else if (inputMessage.getLocation().equals("MOON")) {
//            game.setUserOnMoon(sessionId);
//        }
//
//        String gameId = gameRepository.write(game);
//        OutputMessage outputMessage = new OutputMessage(MessageType.NEW_GAME);
//        outputMessage.setSender(sessionId);
//        outputMessage.setContent(gameId);
//        outputMessage.setLocation(inputMessage.getLocation());
//
//        return outputMessage;
//    }

//    @MessageMapping("/game.join")
//    @SendTo("/games/list")
//    public OutputMessage joinGame(@Payload InputMessage inputMessage, @Header("simpSessionId") String sessionId) {
//        logger.info("/game.join, sessionId: {}, inputMessage: {}", sessionId, inputMessage);
//
//        gameRepository.read(inputMessage.getContent())
//                .ifPresent(game -> {
//                    if (game.getUserOnEarth() != null) {
//                        game.setUserOnEarth(sessionId);
//                    } else if (game.getUserOnMoon() != null) {
//                        game.setUserOnMoon(sessionId);
//                    }
//                    gameRepository.write(game);
//                });
//
//        OutputMessage outputMessage = new OutputMessage(MessageType.REMOVE_GAME);
//        outputMessage.setSender(sessionId);
//        outputMessage.setContent(inputMessage.getContent());
//        return outputMessage;
//    }

    @GetMapping("/games/list")
    public ResponseEntity<Set<Game>> getAllGames() {
        return ResponseEntity.ok(gameRepository.readAll());
    }

    @PostMapping("/games/create")
    public ResponseEntity<Game> createGame(@RequestBody String body) {
        logger.info("/games/create, body: {}", body);

        Game game = new Game(UUID.randomUUID().toString());
        String gameId = gameRepository.write(game);

        OutputMessage outputMessage = new OutputMessage(MessageType.NEW_GAME);
        outputMessage.setContent(gameId);
        outputMessage.setLocation("EARTH");
        simpMessagingTemplate.convertAndSend("/games/list", outputMessage);

        return ResponseEntity.ok(game);
    }

    @PostMapping(path = "/games/join", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> joinGame(@RequestBody JoinGame body) {
        logger.info("/games/create, body: {}", body.toString());

        gameRepository.read(body.getGame())
                .ifPresent(game -> {
                    if (body.getLocation().equals("EARTH") && game.getUserOnEarth() != null) {
                        game.setUserOnEarth(body.getUsername());
                    } else if (body.getLocation().equals("MOON") && game.getUserOnMoon() != null) {
                        game.setUserOnMoon(body.getUsername());
                    }
                    gameRepository.write(game);
                });

        OutputMessage outputMessage = new OutputMessage(MessageType.JOIN);
        outputMessage.setContent(body.getGame());
        outputMessage.setLocation(body.getLocation());
        simpMessagingTemplate.convertAndSend("/games/list", outputMessage);

        return ResponseEntity.ok(gameRepository.read(body.getGame()).orElse(new Game("empty")));
    }
}
