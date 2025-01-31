package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public final class MoveTransition {

    private final Board board;
    private final Move transitionMove;
    private final MoveStatus moveStatus;

    public MoveTransition(Board board, Move transitionMove, MoveStatus moveStatus) {
        this.board = board;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    public Board getTransitionBoard() 
    { return this.board; }

    public Move getTransitionMove() 
    { return this.transitionMove; }

    public MoveStatus getMoveStatus()
    { return this.moveStatus; }
}
