package com.example.messagingstompwebsocket.repository;

import com.example.messagingstompwebsocket.Game;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Arrays.asList;
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

        assertEquals(repository.count(), 1);
    }

    @Test
    void COUNT_DECREASE_REMOVING_ELEMENT() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any", new Game("any"))));
        repository.remove("any");

        assertEquals(repository.count(), 0);
    }

    @Test
    void RETRIEVE_ALL_ELEMENTS() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any1", new Game("any1"), "any2", new Game("any2"))));
        assertEquals(repository.readAll(), Set.of(new Game("any1"), new Game("any2")));
    }

    @Test
    void RETRIEVE_SINGLE_ELEMENT() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any1", new Game("any1"), "any2", new Game("any2"))));
        assertEquals(repository.read("any1"), Optional.of(new Game("any1")));
    }

    @Test
    void RETRIEVE_MISSING_ELEMENT() {
        GameMapRepository repository = new GameMapRepository(new HashMap<>(Map.of("any1", new Game("any1"), "any2", new Game("any2"))));
        assertEquals(repository.read("any3"), Optional.empty());
    }
}