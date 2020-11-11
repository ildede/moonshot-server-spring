package com.example.messagingstompwebsocket.message;

public class InputMessage {

    private String sender;
    private String content;
    private String location;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "InputMessage{" +
                "sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
