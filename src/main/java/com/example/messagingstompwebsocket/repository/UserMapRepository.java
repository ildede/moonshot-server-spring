package com.example.messagingstompwebsocket.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserMapRepository {

    private final Map<String, String> users;

    public UserMapRepository() {
        this.users = new HashMap<>();
    }

    UserMapRepository(Map<String, String> userMap) {
        this.users = userMap;
    }

    public void add(String userId) {
        users.putIfAbsent(userId, userId);
    }

    public void remove(String userId) {
        users.remove(userId);
    }

    public int count() {
        return users.keySet().size();
    }
}
