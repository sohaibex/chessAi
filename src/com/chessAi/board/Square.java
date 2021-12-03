package com.chessAi.board;

import com.chessAi.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Square {


    protected final int caseCordinate;
    private static final Map<Integer, EmptySquare> EMPTY_SQUARE_CACHE = createAllPossibleEmptyTiles();



    Square(int caseCordinate) {
        this.caseCordinate = caseCordinate;
    }


    private static Map<Integer, EmptySquare> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptySquare> emptySquareMap = new HashMap<>();

        for (int i = 0; i < 64; i++) {
            emptySquareMap.put(i, new EmptySquare(i));
        }
        return ImmutableMap.copyOf(emptySquareMap);
    }

    public static Square createSquare(final int squareCoordinate, final Piece piece) {
        return piece != null ? new OccupiedSquare(squareCoordinate, piece) : EMPTY_SQUARE_CACHE.get(squareCoordinate);
    }



    public abstract boolean isSquareOccupied();

    public abstract Piece getPiece();

}
