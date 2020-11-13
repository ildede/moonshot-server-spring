package com.example.messagingstompwebsocket.repository;

import com.example.messagingstompwebsocket.entity.Game;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GameMapRepository {

    private final Map<String, Game> games;

    public GameMapRepository() {
        this.games = new HashMap<>();
    }

    GameMapRepository(Map<String, Game> gamesMap) {
        this.games = gamesMap;
    }

    public String write(Game game) {
        games.put(game.getId(), game);
        return game.getId();
    }

    public void remove(String gameId) {
        games.remove(gameId);
    }

    public Optional<Game> read(String gameId) {
        return Optional.ofNullable(games.getOrDefault(gameId, null));
    }

    public Set<Game> readAll() {
        return new HashSet<>(games.values());
    }

    public int count() {
        return games.keySet().size();
    }
}
