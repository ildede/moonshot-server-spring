package com.example.messagingstompwebsocket.message;

public class ChatMessage {

    private String game;
    private String location;
    private String message;

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "game='" + game + '\'' +
                ", location='" + location + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
