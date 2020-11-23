package com.example.messagingstompwebsocket.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PieceBuilder {

    private final Set<Piece> moonPieces;
    private final Set<Piece> earthPieces;

    public PieceBuilder() {
        this.earthPieces = Set.of(
                new Piece("red", "circle"),
                new Piece("blue", "circle"),
                new Piece("green", "circle"),
                new Piece("red", "square"),
                new Piece("blue", "square"),
                new Piece("green", "square"),
                new Piece("red", "triangle"),
                new Piece("blue", "triangle"),
                new Piece("green", "triangle")
        );
        Random rand = new Random();
        int numberOfElements = 3;
        ArrayList<Piece> pieces = new ArrayList<>(earthPieces);
        Set<Piece> forMoon = new HashSet<>();
        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(pieces.size());
            forMoon.add(pieces.get(randomIndex));
            pieces.remove(randomIndex);
        }
        this.moonPieces = forMoon;
    }

    public Set<Piece> forMoon() {
        return moonPieces;
    }

    public Set<Piece> forEarth() {
        return earthPieces;
    }
}
