package com.chessAi.pieces;

import com.chessAi.Alliance;
import com.chessAi.board.Board;
import com.chessAi.board.Move;

import java.util.Collection;

//la class principale ou les autres pieces vont herite d'elle
public abstract class Piece {
// pour determiner le type de la  piece
    final PieceType pieceType;
    //pour determiner l'alliance de la piece white or black
    final Alliance pieceAlliance;
    // la position de la piece
    final int piecePosition;
    // si il a le premier mouvement ou non
    private final boolean isFirstMove;

    private final int cachedHashCode;

    Piece(final PieceType type,
          final Alliance alliance,
          final int piecePosition,
          final boolean isFirstMove) {
        this.pieceType = type;
        this.piecePosition = piecePosition;
        this.pieceAlliance = alliance;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }
//cette methode permet de determiner le type de la piece si il est un pion chevale ...
    public PieceType getPieceType() {
        return this.pieceType;
    }
    //cette methode permet de determiner l'alliance de la piece black or white
    public Alliance getPieceAllegiance() {
        return this.pieceAlliance;
    }
// cette methode permet de determiner la position de la piece
    public int getPiecePosition() {
        return this.piecePosition;
    }
    // cette methode permet de determiner si il a le premier mouvement ou non
    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public abstract int locationBonus();

    public abstract Piece movePiece(Move move);
// c'est la ou ce passe peut inporte tous le traitement de chaque piece
    public abstract Collection<Move> calculateLegalMoves(final Board board);


    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return this.piecePosition == otherPiece.piecePosition && this.pieceType == otherPiece.pieceType &&
               this.pieceAlliance == otherPiece.pieceAlliance && this.isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    private int computeHashCode() {
        int result = this.pieceType.hashCode();
        result = 31 * result + this.pieceAlliance.hashCode();
        result = 31 * result + this.piecePosition;
        result = 31 * result + (this.isFirstMove ? 1 : 0);
        return result;
    }
//  Enumeration pour afficher les pieces dans le terminal avec une evaluation de chaque piece on va l'implemente dans chaque piece
    public enum PieceType {

        PAWN(100, "P"),
        KNIGHT(300, "N"),
        BISHOP(330, "B"),
        ROOK(500, "R"),
        QUEEN(900, "Q"),
        KING(10000, "K");

        private final int value;
        private final String pieceName;

        public int getPieceValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        PieceType(final int val,
                  final String pieceName) {
            this.value = val;
            this.pieceName = pieceName;
        }

    }

}