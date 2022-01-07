package com.chessAi.player.ai;

import com.chessAi.board.Board;
import com.chessAi.board.BoardUtils;
import com.chessAi.board.Move;
import com.chessAi.board.MoveTransition;

import java.util.concurrent.atomic.AtomicLong;

import static com.chessAi.board.Move.*;

public final class MiniMax implements MoveStrategy {
// pour l'evaluation de l'echiquier
    private final BoardEvaluator evaluator;
    // profondeur de recherche
    private final int searchDepth;
    private long boardsEvaluated;
    private long executionTime;
    private FreqTableRow[] freqTable;
    private int freqTableIndex;

    public MiniMax(final int searchDepth) {
        this.evaluator = StandardBoardEvaluator.get();
        this.boardsEvaluated = 0;
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public long getNumBoardsEvaluated() {
        return this.boardsEvaluated;
    }
// cette fonction va retourner the best move depending on the min max algortihme
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = MoveFactory.getNullMove();
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer() + " THINKING with depth = " +this.searchDepth);
        this.freqTable = new FreqTableRow[board.currentPlayer().getLegalMoves().size()];
        this.freqTableIndex = 0;
        int moveCounter = 1;
        final int numMoves = board.currentPlayer().getLegalMoves().size();
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final FreqTableRow row = new FreqTableRow(move);
                this.freqTable[this.freqTableIndex] = row;
                // si le alliance current player est white alors son prochain mouvement va etre minnimum mouvement bcs black is the maximizer and the opposite
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                                min(moveTransition.getToBoard(), this.searchDepth - 1) :
                                max(moveTransition.getToBoard(), this.searchDepth - 1);
                System.out.println("\t" + toString() + " analyzing move (" +moveCounter + "/" +numMoves+ ") " + move +
                                   " scores " + currentValue + " " +this.freqTable[this.freqTableIndex]);
                this.freqTableIndex++;
                // si vous white et la valeur actuelle est superieure ou egale a la maximum valeur vu alors on va affecter  la maximum valeur a la valeur actuelle  et  retourner la best move
                // et le contraire pour les noires
                if (board.currentPlayer().getAlliance().isWhite() &&
                        currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.currentPlayer().getAlliance().isBlack() &&
                        currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            } else {
                System.out.println("\t" + toString() + " can't execute move (" +moveCounter+ "/" +numMoves+ ") " + move);
            }
            moveCounter++;
        }

        this.executionTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s SELECTS %s [#boards = %d time taken = %d ms, rate = %.1f\n", board.currentPlayer(),
                bestMove, this.boardsEvaluated, this.executionTime, (1000 * ((double)this.boardsEvaluated/this.executionTime)));
        long total = 0;
        for (final FreqTableRow row : this.freqTable) {
            if(row != null) {
                total += row.getCount();
            }
        }
        if(this.boardsEvaluated != total) {
            System.out.println("somethings wrong with the # of boards evaluated!");
        }
        return bestMove;
    }
    //fonction recurcive min call max and max call min


    //si vous etes dans le processe de minimisation donc on doit louper dans notre niveau +  noeud dans l'arbre et on va voir tous les mouvements possible
// implementation des deux fonction (helpers) min et max
    private int min(final Board board,
                    final int depth) {
        // au debut du gale
        if(depth == 0) {
            this.boardsEvaluated++;
            this.freqTable[this.freqTableIndex].increment();
            return this.evaluator.evaluate(board, depth);
        }
        //si c'est un check mate or stalemate
        if(isEndGameScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }
        // affecter la minimum valeur vu a un maximum qui va jamais atteindre
        int lowestSeenValue = Integer.MAX_VALUE;
        // looper sur tous les mouvements valide et repcupere la mimnimum valeur vu
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getToBoard(), depth - 1);
                //si la valeur actuelle est inferieure ou = la minimum valeur vu on la minimum valeur vu a la valeur actuelle
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        //retouner la minimum valeur vu dans depend du depth
        return lowestSeenValue;
    }

    private int max(final Board board,
                    final int depth) {
        if(depth == 0) {
            this.boardsEvaluated++;
            this.freqTable[this.freqTableIndex].increment();
            return this.evaluator.evaluate(board, depth);
        }
        if(isEndGameScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }
        // affecter la maximum valeur vu a un minimum qui va jamais atteindre
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getToBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        //retourner la maximum valeur vu
        return highestSeenValue;
    }

    private static boolean isEndGameScenario(final Board board) {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate();
    }

    private static class FreqTableRow {

        private final Move move;
        private final AtomicLong count;

        FreqTableRow(final Move move) {
            this.count = new AtomicLong();
            this.move = move;
        }

        long getCount() {
            return this.count.get();
        }

        void increment() {
            this.count.incrementAndGet();
        }

        @Override
        public String toString() {
            return BoardUtils.INSTANCE.getPositionAtCoordinate(this.move.getCurrentCoordinate()) +
                   BoardUtils.INSTANCE.getPositionAtCoordinate(this.move.getDestinationCoordinate()) + " : " +this.count;
        }
    }

}
