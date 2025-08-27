package com.example.messagingstompwebsocket.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class Game {
    private final String id;
    private final LocalDateTime creationTime;
    private final Set<Piece> moonPieces;
    private final Set<Piece> earthPieces;
    private LocalDateTime startTime;
    private String userOnEarth;
    private String userOnMoon;
    private String result;

    public Game(String gameId) {
        this.id = gameId;
        this.creationTime = LocalDateTime.now();
        PieceBuilder builder = new PieceBuilder();
        this.moonPieces = builder.forMoon();
        this.earthPieces = builder.forEarth();
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) &&
                Objects.equals(moonPieces, game.moonPieces) &&
                Objects.equals(earthPieces, game.earthPieces) &&
                Objects.equals(startTime, game.startTime) &&
                Objects.equals(userOnEarth, game.userOnEarth) &&
                Objects.equals(userOnMoon, game.userOnMoon) &&
                Objects.equals(result, game.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moonPieces, earthPieces, startTime, userOnEarth, userOnMoon, result);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", creationTime=" + creationTime +
                ", startTime=" + startTime +
                ", userOnEarth='" + userOnEarth + '\'' +
                ", userOnMoon='" + userOnMoon + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
