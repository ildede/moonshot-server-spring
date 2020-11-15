package com.example.messagingstompwebsocket;

import java.io.Serializable;

public class JoinGame implements Serializable {

    private String game;
    private String username;
    private String location;

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "JoinGame{" +
                "game='" + game + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
