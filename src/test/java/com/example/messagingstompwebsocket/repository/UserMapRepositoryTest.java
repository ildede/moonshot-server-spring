package com.example.messagingstompwebsocket.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapRepositoryTest {

    @Test
    void IT_CAN_ADD_NEW_ELEMENT() {
        UserMapRepository repository = new UserMapRepository();
        assertDoesNotThrow(() -> repository.add("any"));
    }

    @Test
    void IT_CAN_REMOVE_ELEMENT() {
        UserMapRepository repository = new UserMapRepository(new HashMap<>(Map.of("any", "any")));
        assertDoesNotThrow(() -> repository.remove("any"));
    }

    @Test
    void ADD_ALREADY_PRESENT_ELEMENT_DOES_NOT_THROW_ERROR() {
        UserMapRepository repository = new UserMapRepository(new HashMap<>(Map.of("any", "any")));
        assertDoesNotThrow(() -> repository.add("any"));
    }

    @Test
    void REMOVE_MISSING_ELEMENT_DOES_NOT_THROWS_ERROR() {
        UserMapRepository repository = new UserMapRepository();
        assertDoesNotThrow(() -> repository.remove("any"));
    }

    @ParameterizedTest
    @MethodSource
    void IT_CAN_COUNT_ELEMENTS(Map<String, String> map, int expectedCount) {
        UserMapRepository repository = new UserMapRepository(map);
        assertEquals(expectedCount, repository.count());
    }
    private static Stream<Arguments> IT_CAN_COUNT_ELEMENTS() {
        return Stream.of(
                Arguments.of(Map.of(), 0),
                Arguments.of(Map.of("a", "a"), 1),
                Arguments.of(Map.of("a", "a", "b", "b"), 2)
        );
    }

    @Test
    void COUNT_INCREASE_ADDING_ELEMENT() {
        UserMapRepository repository = new UserMapRepository();
        repository.add("any");

        assertEquals(1, repository.count());
    }

    @Test
    void COUNT_DECREASE_REMOVING_ELEMENT() {
        UserMapRepository repository = new UserMapRepository(new HashMap<>(Map.of("any", "any")));
        repository.remove("any");

        assertEquals(0, repository.count());
    }
}