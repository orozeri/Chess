package com.chess.engine.board;

import java.util.Objects;

import com.chess.engine.pieces.Piece;

public abstract class Move 
{
	final Board board;
	final Piece movedPiece;
	final int destCoordinate;
	
	protected Move(Board board, Piece movedPiece, int destCoordinate)
	{
		this.board = board; 
		this.movedPiece = movedPiece;
		this.destCoordinate = destCoordinate;
	}
	
	public int getCurrentCoordiante()
	{ return movedPiece.getPiecePosition(); }
	
	public int getDestCoordinate()
	{ return this.destCoordinate; }
	
	public Piece getMovedPiece()
	{ return this.movedPiece; }
	
	public boolean isCapture()
	{ return false; }
	
	public boolean isCastle()
	{ return false; }
	
	public Piece getCapturedPiece() 
	{ return null; }
	
	public boolean isPromotion() 
	{ return false; }

	public Board execute()
	{
		final Board.Builder builder = new Board.Builder();
		for (Piece piece : this.board.getCurrentPlayer().getActivePieces())
		{
			if (!this.movedPiece.equals(piece))
				builder.setPiece(piece);
		}
		
		for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
			builder.setPiece(piece);
		
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
		return builder.build();
	}
	
	public static class MoveFactory
	{
		private MoveFactory()
		{ throw new RuntimeException("not instantiable"); }
		
		public static Move CreateMove(Board board, int currentCoordiante, int destCoordiante)
		{
			for (Move move : board.getAllLegalMoves())
			{
				if (move.getCurrentCoordiante() == currentCoordiante &&
						move.getDestCoordinate() == destCoordiante)
					return move;
			}
			return null;
		}
	}

	@Override
	public int hashCode() 
	{ return Objects.hash(board, destCoordinate, movedPiece); }

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		return Objects.equals(board, other.board) && destCoordinate == other.destCoordinate
				&& Objects.equals(movedPiece, other.movedPiece);
	}

	public Board getBoard() 
	{ return this.board; }
}
