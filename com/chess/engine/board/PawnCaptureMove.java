package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public class PawnCaptureMove extends CaptureMove
{

	public PawnCaptureMove(Board board, Pawn movedPawn, int destCoordinate, Piece capturedPiece)
	{ super(board, movedPawn, destCoordinate, capturedPiece); }

	@Override
	public String toString()
	{ return BoardUtils.getPostionAtCoordinate(movedPiece.getPiecePosition()).substring(0, 1) + 'x' + 
			BoardUtils.getPostionAtCoordinate(capturedPiece.getPiecePosition()); }
}
