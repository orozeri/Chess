package com.chess.engine.board;

import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class KingsideCastleMove extends CastleMove 
{	
	public KingsideCastleMove(Board board, Piece movedPiece, int destCoordinate, Rook rook, int rookDest) 
	{ super(board, movedPiece, destCoordinate, rook, rookDest); }
	
	@Override
	public String toString()
	{ return "O-O"; }
}
