package com.chessAi.pieces;

import com.chessAi.Alliance;
import com.chessAi.board.Board;
import com.chessAi.board.BoardUtils;
import com.chessAi.board.Move;
import com.chessAi.board.Move.MajorAttackMove;
import com.chessAi.board.Move.MajorMove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
//le chevale peut se  déplacer deux cases verticalement et une case horizontalement,
// ou deux cases horizontalement et une case verticalement (les deux formant un L).
// De cette façon, un chevalier peut avoir un maximum de 8 coups
//le principe du mouvement du cheval je vais le determiner par les offsets
public final class Knight extends Piece {
// determinaison des cordone du mouvement du chevale (mouvement legales dans une position actuelle peut n'importe si il est vide ou pleine 8 en totale  )

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

    public Knight(final Alliance alliance,
                  final int piecePosition) {
        super(PieceType.KNIGHT, alliance, piecePosition, true);
    }

    public Knight(final Alliance alliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.KNIGHT, alliance, piecePosition, isFirstMove);
    }
//calculer les mouvement legale du chevale attcack ou bien positionement
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            // condition sur les colonnes ou ou les offsets ne marches pas
            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }
            // get la case d"echiquier de la destination du condidat
            // c'est ici ou on applique les offset
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            // si cette case n'est pas occcupe (valide cordination)
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                if (pieceAtDestination == null) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
                //si cette case est occupé alors on l'attaque
                else {
                    // avoir la  piece dans cette destination d'alliance
                    final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();
                    // si la piece courante est different de la piece qui est dans la case alors on l'attaque
                    if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                }
            }
        }
        //retourner les mouvement legales
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.knightBonus(this.piecePosition);
    }

    @Override
    public Knight movePiece(final Move move) {
        return PieceUtils.INSTANCE.getMovedKnight(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }


    // Offset colonne exclusions
    // cette methode permet pour chaque colonne de savoir si la piece et dehors du chess board
    private static boolean isFirstColumnExclusion(final int currentPosition,
                                                  final int candidateOffset) {
        return BoardUtils.INSTANCE.FIRST_COLUMN.get(currentPosition) && ((candidateOffset == -17) ||
                (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardUtils.INSTANCE.SECOND_COLUMN.get(currentPosition) && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition,
                                                    final int candidateOffset) {
        return BoardUtils.INSTANCE.SEVENTH_COLUMN.get(currentPosition) && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(currentPosition) && ((candidateOffset == -15) || (candidateOffset == -6) ||
                (candidateOffset == 10) || (candidateOffset == 17));
    }

}

// Exception et deficulte
//si par example le chevale etait dans A4 en appliquant le offset de -10 il sera dans g6