package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.entity.Game;
import com.example.messagingstompwebsocket.entity.Piece;
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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "${client.url}")
@Controller
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameMapRepository gameRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameController(GameMapRepository gameRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameRepository = gameRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping(value = "/games/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Game>> getAllGames() {
        return ResponseEntity.ok(gameRepository.readAll());
    }

    @GetMapping(value = "/games/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        return gameRepository.read(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/games/{gameId}/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGame(@PathVariable String gameId, @RequestBody Set<Piece> selected) {

        Set<Piece> right = gameRepository.read(gameId)
                .map(g -> {
                    if (g.getMoonPieces().containsAll(selected)
                        && selected.containsAll(g.getMoonPieces())) {
                        return g.getMoonPieces();
                    } else {
                        return g.getMoonPieces().stream()
                                .filter(selected::contains)
                                .collect(Collectors.toSet());
                    }
                }).orElse(new HashSet<>());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setLocation("EARTH");
        chatMessage.setGame(gameId);
        if (right.size() == 3) {
            chatMessage.setMessage("You Win!");
        } else {
            chatMessage.setMessage(right.size() + " right. Try Again.");
        }

        simpMessagingTemplate.convertAndSend("/games/list/"+gameId, chatMessage);

        return ResponseEntity.ok(right.size() + " right.");
    }

    @PostMapping(value = "/games/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/games/join", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(path = "/games/message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postMessage(@RequestBody ChatMessage body) {
        logger.info("/games/message, body: {}", body);

        simpMessagingTemplate.convertAndSend("/games/list/"+body.getGame(), body);

        return ResponseEntity.ok("OK");
    }
}
