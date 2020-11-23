package com.example.messagingstompwebsocket.entity;

import java.util.Objects;

public class Piece {
    private final String color;
    private final String shape;

    public Piece(String color, String shape) {
        this.color = color;
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return Objects.equals(color, piece.color) &&
                Objects.equals(shape, piece.shape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, shape);
    }
}
