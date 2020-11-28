package com.example.messagingstompwebsocket.entity;

import java.util.*;

public class PieceBuilder {

    private final Set<Piece> moonPieces;
    private final Set<Piece> earthPieces;

    public PieceBuilder() {
        Random rand = new Random();
        int numberOfElements = 3;
        List<String> parts = Arrays.asList("top", "scuttle", "door", "body", "bottom");

        Set<Piece> forMoon = new HashSet<>();
        Set<Piece> forEarth = new HashSet<>();
        for (String part : parts) {
            for (int i = 0; i < numberOfElements; i++) {
                Piece piece = new Piece(part, rand.nextInt(19));
                forEarth.add(piece);
                if (i == 0) {
                    forMoon.add(piece);
                }
            }
        }

        moonPieces = forMoon;
        earthPieces = forEarth;
    }

    public Set<Piece> forMoon() {
        return moonPieces;
    }

    public Set<Piece> forEarth() {
        return earthPieces;
    }
}
