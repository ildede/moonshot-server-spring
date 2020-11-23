package com.example.messagingstompwebsocket.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
    private final String id;
    private final LocalDateTime creationTime;
    private final Set<Piece> moonPieces;
    private final Set<Piece> earthPieces;
    private String userOnEarth;
    private String userOnMoon;

    public Game(String gameId) {
        this.id = gameId;
        this.creationTime = LocalDateTime.now();
        this.moonPieces = Set.of(
                new Piece("red", "circle"),
                new Piece("blue", "square"),
                new Piece("green", "triangle")
        );
        this.earthPieces = Stream.concat(
                moonPieces.stream(),
                Set.of(
                        new Piece("green", "circle"),
                        new Piece("red", "square"),
                        new Piece("blue", "triangle")
                ).stream())
                .collect(Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Set<Piece> getMoonPieces() {
        return moonPieces;
    }

    public Set<Piece> getEarthPieces() {
        return earthPieces;
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
                Objects.equals(creationTime, game.creationTime) &&
                Objects.equals(moonPieces, game.moonPieces) &&
                Objects.equals(earthPieces, game.earthPieces) &&
                Objects.equals(userOnEarth, game.userOnEarth) &&
                Objects.equals(userOnMoon, game.userOnMoon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationTime, moonPieces, earthPieces, userOnEarth, userOnMoon);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", creationTime=" + creationTime +
                ", moonPieces=" + moonPieces +
                ", earthPieces=" + earthPieces +
                ", userOnEarth='" + userOnEarth + '\'' +
                ", userOnMoon='" + userOnMoon + '\'' +
                '}';
    }
}
