package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public class CaptureMove extends Move
{
	final Piece capturedPiece;
	
	public CaptureMove(Board board, Piece movedPiece, int destCoordinate, Piece capturedPiece) 
	{ 
		super(board, movedPiece, destCoordinate);
		this.capturedPiece = capturedPiece;
	}
	
	@Override
	public boolean isCapture()
	{ return true; }
	
	@Override
	public Piece getCapturedPiece()
	{ return capturedPiece; }
	
	@Override
	public int hashCode()
	{ return capturedPiece.hashCode() + super.hashCode(); }
	
	@Override 
	public boolean equals(Object obj)
	{
		if (!super.equals(obj))
			return false;
		CaptureMove other = (CaptureMove) obj;
		return (capturedPiece == other.capturedPiece);
	}
	
	@Override
	public String toString()
	{ return movedPiece.toString() + 'x' + BoardUtils.getPostionAtCoordinate(destCoordinate); }
}
