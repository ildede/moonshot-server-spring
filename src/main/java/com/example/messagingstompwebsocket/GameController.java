package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.entity.Game;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("/games/list")
    public ResponseEntity<Set<Game>> getAllGames() {
        return ResponseEntity.ok(gameRepository.readAll());
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

        return ResponseEntity.ok(gameRepository.read(body.getGame()).orElse(new Game("empty")));
    }
}
