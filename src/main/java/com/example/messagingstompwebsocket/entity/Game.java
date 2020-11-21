package com.example.messagingstompwebsocket.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Game {
    private final String id;
    private final LocalDateTime creationTime;
    private String userOnEarth;
    private String userOnMoon;

    public Game(String gameId) {
        this.id = gameId;
        this.creationTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public String getUserOnEarth() {
        return userOnEarth;
    }

    public void setUserOnEarth(String userOnEarth) {
        this.userOnEarth = userOnEarth;
    }

    public String getUserOnMoon() {
        return userOnMoon;
    }

    public void setUserOnMoon(String userOnMoon) {
        this.userOnMoon = userOnMoon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) &&
                Objects.equals(userOnEarth, game.userOnEarth) &&
                Objects.equals(userOnMoon, game.userOnMoon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userOnEarth, userOnMoon);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", userOnEarth='" + userOnEarth + '\'' +
                ", userOnMoon='" + userOnMoon + '\'' +
                '}';
    }
}
