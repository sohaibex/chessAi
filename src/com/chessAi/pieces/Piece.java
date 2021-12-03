package com.chessAi.pieces;

import com.chessAi.board.Board;
import com.chessAi.board.Move;

import java.util.List;

public abstract class Piece {

    final int piecePosition;
    final PieceColor pieceColor;

     Piece(final int piecePosition, final PieceColor pieceColor)
     {
         this.piecePosition=piecePosition;
         this.pieceColor=pieceColor;
     }

     public PieceColor getPieceColor()
     {
         return  this.pieceColor;
     }
    public  abstract List<Move> legalMoves(final Board board);

}
