package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class QueensideCastleMove extends CastleMove
{

	public QueensideCastleMove(Board board, Piece movedPiece, int destCoordinate, Rook rook, int rookDest)
	{ super(board, movedPiece, destCoordinate, rook, rookDest); }

	@Override
	public String toString()
	{ return "O-O-O"; }
}
