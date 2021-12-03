package com.chessAi.pieces;

import com.chessAi.board.Board;
import com.chessAi.board.Move;
import com.chessAi.board.Square;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    //-17, -15, -10, -6, 6, 10, 15, 17 are offsets of the current position
    private final static int[] Move_CORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    Knight(final int piecePosition, final PieceColor pieceColor) {
        super(piecePosition, pieceColor);

    }

    @Override
    public List<Move> legalMoves(Board board) {
        int distination;
        List<Move> legalMovesArray = new ArrayList<>();
        for (final int currentConrdinates : Move_CORDINATES) {
            distination = this.piecePosition + currentConrdinates;
                // si la piece a les bonnes cordinations
            if (true) {
                final Square distinationSqaure = board.getSquare(distination);
                // et la case n'est pas occupe ajouter dans la liste des mouvement legale sa veut dire qu'on ne peut pas l'attaquer
                if (!distinationSqaure.isSquareOccupied()) {
                    legalMovesArray.add(new Move());
                }
                // si la case est occupé
                else {
                    //on peut attacker la piece  alors on a besoin de la couleur de la piece qui est dans la case
                    final Piece pieceAtDestiniation = distinationSqaure.getPiece();
                    final PieceColor pieceColor = pieceAtDestiniation.getPieceColor();
                    //si il est de couleur different on va faire le mouvement de l attaque
                    if (this.pieceColor != pieceColor) {
                        legalMovesArray.add(new Move());
                    }
                }
            }
        }
        //retourner tous les mouvements legale
        return ImmutableList.copyOf(legalMovesArray);
    }
}
