package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.entity.Game;
import com.example.messagingstompwebsocket.message.ChatMessage;
import com.example.messagingstompwebsocket.message.MessageType;
import com.example.messagingstompwebsocket.message.NewGame;
import com.example.messagingstompwebsocket.message.OutputMessage;
import com.example.messagingstompwebsocket.repository.GameMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/games/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        return gameRepository.read(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/games/create")
    public ResponseEntity<Game> createGame(@RequestBody NewGame body) {
        logger.info("/games/create, body: {}", body);

        Game game = new Game(UUID.randomUUID().toString());
        OutputMessage outputMessage = new OutputMessage(MessageType.NEW_GAME);
        outputMessage.setSender(body.getUsername());

        if (body.getPlace().equals("MOON")) {
            game.setUserOnMoon(body.getUsername());
            outputMessage.setLocation("EARTH");
        } else {
            game.setUserOnEarth(body.getUsername());
            outputMessage.setLocation("MOON");
        }
        String gameId = gameRepository.write(game);

        outputMessage.setContent(gameId);
        simpMessagingTemplate.convertAndSend("/games/list", outputMessage);

        return ResponseEntity.ok(game);
    }

    @PostMapping(path = "/games/join", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> joinGame(@RequestBody JoinGame body) {
        logger.info("/games/join, body: {}", body);

        gameRepository.read(body.getGame())
                .ifPresent(game -> {
                    logger.info("game found: {}", game);
                    if (body.getLocation().equals("EARTH") && game.getUserOnEarth() == null) {
                        logger.info("Aggiungo username alla terra");
                        game.setUserOnEarth(body.getUsername());
                    } else if (body.getLocation().equals("MOON") && game.getUserOnMoon() == null) {
                        logger.info("Aggiungo username alla luna");
                        game.setUserOnMoon(body.getUsername());
                    }
                    gameRepository.write(game);
                });

        OutputMessage outputMessage = new OutputMessage(MessageType.REMOVE_GAME);
        outputMessage.setContent(body.getGame());
        simpMessagingTemplate.convertAndSend("/games/list", outputMessage);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setGame(body.getGame());
        chatMessage.setLocation(body.getLocation());
        chatMessage.setMessage(body.getUsername() + " joined");
        simpMessagingTemplate.convertAndSend("/games/list/"+body.getGame(), chatMessage);

        return ResponseEntity.ok(gameRepository.read(body.getGame()).orElse(new Game("empty")));
    }

    @PostMapping(path = "/games/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postMessage(@RequestBody ChatMessage body) {
        logger.info("/games/message, body: {}", body);

        simpMessagingTemplate.convertAndSend("/games/list/"+body.getGame(), body);

        return ResponseEntity.ok("OK");
    }
}
