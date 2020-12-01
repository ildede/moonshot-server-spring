package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.entity.Game;
import com.example.messagingstompwebsocket.message.ChatMessage;
import com.example.messagingstompwebsocket.repository.GameMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SchedulerController {

    private static final int TWENTY_MINUTES = 1_200_000;
    private static final int FIFTEEN_MINUTES = 900_000;
    private static final int TEN_MINUTES = 600_000;
    private static final int THIRTY_SECONDS = 30_000;
    private static final int ONE_SECOND = 1_000;

    private static final Logger logger = LoggerFactory.getLogger(SchedulerController.class);
    private final GameMapRepository gameRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SchedulerController(GameMapRepository gameRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameRepository = gameRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Scheduled(fixedRate = TEN_MINUTES)
    public void deleteGamesOlderThanOneHour() {
//        logger.info("Scheduler: deleteGamesOlderThanOneHour - START");
        Set<Game> games = gameRepository.readAll();
        Set<Game> olderGames = games.stream()
                .filter(g -> g.getCreationTime().plusMinutes(15).isBefore(LocalDateTime.now()))
                .collect(Collectors.toSet());
        olderGames.forEach(game -> gameRepository.remove(game.getId()));
//        logger.info("Scheduler: deleteGamesOlderThanOneHour - END <deleted {} games>", olderGames.size());
    }

    @Scheduled(fixedRate = ONE_SECOND)
    public void sendCurrentPlayTimeToEachGame() {
//        logger.info("Scheduler: sendCurrentPlayTimeToEachGame - START");
        gameRepository.readAll()
                .forEach(game -> {
                    if (game.getStartTime() != null) {
                        if (game.getResult() == null) {
                            LocalDateTime now = LocalDateTime.now();
                            if (now.isBefore(game.getStartTime())) {
                                Duration duration = Duration.between(now, game.getStartTime());
                                long diff = Math.abs(duration.toSeconds());
                                simpMessagingTemplate.convertAndSend(
                                        "/games/list/"+game.getId(),
                                        new ChatMessage(game.getId(), "seconds-to-start", String.valueOf(diff))
                                );
                            } else {
                                Duration duration = Duration.between(now, game.getStartTime());
                                long diff = Math.abs(duration.toSeconds());
                                if (diff == 24L) {
                                    simpMessagingTemplate.convertAndSend("/games/list/"+game.getId(), new ChatMessage(game.getId(), "GAME", "Low bandwidth, messages are now limited to 15 chars."));
                                } else if (diff == 49L) {
                                    simpMessagingTemplate.convertAndSend("/games/list/"+game.getId(), new ChatMessage(game.getId(), "GAME", "Keyboard malfunction on moon, some keys are not working."));
                                } else if (diff == 74L) {
                                    simpMessagingTemplate.convertAndSend("/games/list/"+game.getId(), new ChatMessage(game.getId(), "GAME", "Signal interference, partial transmission."));
                                }
                                simpMessagingTemplate.convertAndSend(
                                        "/games/list/"+game.getId(),
                                        new ChatMessage(game.getId(), "seconds-from-start", String.valueOf(diff))
                                );
                            }
                        }
                    }
                });
//        logger.info("Scheduler: sendCurrentPlayTimeToEachGame - END");
    }
}
