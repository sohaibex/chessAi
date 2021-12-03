package com.chessAi.board;

import com.chessAi.pieces.Piece;

public class OccupiedSquare extends Square{

    private Piece pieceOnSquare;
    OccupiedSquare(int coordinate, Piece pieceOnSquare)
    {
        super(coordinate);
        this.pieceOnSquare=pieceOnSquare;
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
