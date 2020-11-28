package com.example.messagingstompwebsocket.entity;

import java.util.Objects;

public class Piece {
    private final String part;
    private final Integer version;

    public Piece(String part, Integer version) {
        this.part = part;
        this.version = version;
    }

    public String getPart() {
        return part;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return Objects.equals(part, piece.part) &&
                Objects.equals(version, piece.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(part, version);
    }
}
