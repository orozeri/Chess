package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public final class MajorMove extends Move 
{

	public MajorMove(Board board, Piece movedPiece, int destCoordinate) 
	{ super(board, movedPiece, destCoordinate); }

	@Override
	public int hashCode() { return super.hashCode(); }

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return this.movedPiece.toString() + BoardUtils.getPostionAtCoordinate(this.destCoordinate);
	}
}
