package com.chessAi.player.ai;

import com.chessAi.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);

}
