package com.chessAi.board;

import com.chessAi.pieces.Piece;

public class EmptySquare extends  Square{

    EmptySquare(int coordinate)
    {
        super(coordinate);
    }

    @Override
    public boolean isSquareOccupied() {
        return false;
    }

    @Override
    public Piece getPiece() {
        return null;
    }
}
