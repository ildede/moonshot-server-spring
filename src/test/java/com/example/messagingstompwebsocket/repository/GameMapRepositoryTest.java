package com.example.messagingstompwebsocket.repository;

import com.example.messagingstompwebsocket.entity.Game;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameMapRepositoryTest {

    @Test
    void IT_CAN_WRITE_NEW_ELEMENT() {
        GameMapRepository repository = new GameMapRepository();
        assertDoesNotThrow(() -> repository.write(new Game("any")));
    }

    @Test
    void IT_CAN_REMOVE_ELEMENT() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any", new Game("any"))));
        assertDoesNotThrow(() -> repository.remove("any"));
    }

    @Test
    void WRITE_ALREADY_PRESENT_ELEMENT_DOES_NOT_THROW_ERROR() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any", new Game("any"))));
        assertDoesNotThrow(() -> repository.write(new Game("any")));
    }

    @Test
    void REMOVE_MISSING_ELEMENT_DOES_NOT_THROWS_ERROR() {
        GameMapRepository repository = new GameMapRepository();
        assertDoesNotThrow(() -> repository.remove("any"));
    }

    @Test
    void IT_CAN_COUNT_ELEMENTS() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any", new Game("any"))));
        assertEquals(1, repository.count());
    }

    @Test
    void COUNT_INCREASE_WRITING_NEW_ELEMENT() {
        GameMapRepository repository = new GameMapRepository();
        repository.write(new Game("any"));

        assertEquals(1, repository.count());
    }

    @Test
    void COUNT_DECREASE_REMOVING_ELEMENT() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any", new Game("any"))));
        repository.remove("any");

        assertEquals(0, repository.count());
    }

    @Test
    void RETRIEVE_ALL_ELEMENTS() {
        Game game1 = new Game("any1");
        Game game2 = new Game("any2");
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any1", game1, "any2", game2)));
        assertEquals(Set.of(game1, game2), repository.readAll());
    }

    @Test
    void RETRIEVE_SINGLE_ELEMENT() {
        Game game1 = new Game("any1");
        Game game2 = new Game("any2");
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any1", game1, "any2", game2)));
        assertEquals(Optional.of(game1), repository.read("any1"));
    }

    @Test
    void RETRIEVE_MISSING_ELEMENT() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any1", new Game("any1"), "any2", new Game("any2"))));
        assertEquals(Optional.empty(), repository.read("any3"));
    }
}