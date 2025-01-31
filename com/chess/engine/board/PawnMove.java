package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;

public class PawnMove extends Move 
{

	public PawnMove(Board board, Pawn movedPawn, int destCoordinate)
	{ super(board, movedPawn, destCoordinate); }

	@Override
	public String toString()
	{ return BoardUtils.getPostionAtCoordinate(destCoordinate); }
}
