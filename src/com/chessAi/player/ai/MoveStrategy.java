package com.chessAi.player.ai;

import com.chessAi.board.Board;
import com.chessAi.board.Move;

public interface MoveStrategy {

    long getNumBoardsEvaluated();

    Move execute(Board board);

}
