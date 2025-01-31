package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Rook;

public abstract class CastleMove extends Move
{
	protected Rook rook;
	protected int rookDest;

	protected CastleMove(Board board, Piece movedPiece, int destCoordinate, Rook rook, int rookDest) 
	{ 
		super(board, movedPiece, destCoordinate);
		this.rook = rook;
		this.rookDest = rookDest;
	}
	
	@Override
	public boolean isCastle()
	{ return true; }
	
	public Rook getRook()
	{ return rook; }
	
	@Override 
	public Board execute()
	{
		Board.Builder builder = new Board.Builder();	
		for (Piece piece : this.board.getCurrentPlayer().getActivePieces())
		{
			if (!this.movedPiece.equals(piece) && !this.rook.equals(piece))
				builder.setPiece(piece);
		}
		
		for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
			builder.setPiece(piece);
		
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setPiece(new Rook(this.rookDest, this.rook.getAlliance(), false));
		builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
		return builder.build();
	}

}
