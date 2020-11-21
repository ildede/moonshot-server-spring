package com.example.messagingstompwebsocket.message;

import java.io.Serializable;

public class NewGame implements Serializable {

    private String username;
    private String place;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "NewGame{" +
                "username='" + username + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
