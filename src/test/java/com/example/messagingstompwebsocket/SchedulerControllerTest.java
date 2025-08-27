package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.entity.Game;
import com.example.messagingstompwebsocket.message.ChatMessage;
import com.example.messagingstompwebsocket.repository.GameMapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.mockito.Mockito.*;

class SchedulerControllerTest {

    private SchedulerController controller;
    private GameMapRepository gameRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameMapRepository.class);
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        controller = new SchedulerController(gameRepository, simpMessagingTemplate);
    }

    @Test
    void IT_DOES_NOT_DELETE_ANYTHING_IF_THERE_IS_NO_GAMES() {
        when(gameRepository.readAll()).thenReturn(emptySet());

        controller.deleteGamesOlderThanOneHour();

        verify(gameRepository).readAll();
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void IT_DOES_NOT_DELETE_ANYTHING_IF_ALL_GAMES_ARE_RECENT() {
        when(gameRepository.readAll()).thenReturn(Set.of(new Game("any")));

        controller.deleteGamesOlderThanOneHour();

        verify(gameRepository).readAll();
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void IT_DOES_DELETE_OLDER_GAMES() {
        Game fakeOldGame = mock(Game.class);
        when(fakeOldGame.getId()).thenReturn("fakeOldGameId");
        when(fakeOldGame.getCreationTime()).thenReturn(LocalDateTime.now().minusHours(2));
        when(gameRepository.readAll()).thenReturn(Set.of(new Game("any"), fakeOldGame));

        controller.deleteGamesOlderThanOneHour();

        verify(gameRepository, times(1)).readAll();
        verify(gameRepository, times(1)).remove("fakeOldGameId");
    }

    @Test
    void IT_SENDS_TIME_PASSED_TO_EACH_GAME() {
        Game game1 = new Game("any1");
        Game game2 = new Game("any2");
        game1.setStartTime(LocalDateTime.now().minusSeconds(10));
        game2.setStartTime(LocalDateTime.now().minusSeconds(20));

        when(gameRepository.readAll()).thenReturn(Set.of(game1, game2));

        controller.sendCurrentPlayTimeToEachGame();

        verify(gameRepository, times(1)).readAll();
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/games/list/any1"), any(ChatMessage.class));
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/games/list/any2"), any(ChatMessage.class));
    }
}