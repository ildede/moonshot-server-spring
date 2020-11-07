package com.example.messagingstompwebsocket.message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OutputMessage extends InputMessage {

    private final MessageType type;
    private final String timestamp;

    public OutputMessage(MessageType type) {
        this.type = type;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public MessageType getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
