package com.chess.engine.pieces;

import java.util.Objects;
import java.util.Set;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece 
{
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	protected final boolean isFirstMove;
	private final int cachedHashCode;
	
	Piece(PieceType pieceType, int piecePosition, Alliance pieceAlliance, boolean isFirstMove)
	{
		
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.isFirstMove = isFirstMove;
		this.cachedHashCode = computeHashCode();
	}

	public Alliance getAlliance()
	{ return this.pieceAlliance; }
	
	public boolean isFirstMove()
	{ return this.isFirstMove; }
	
	public abstract Set<Move> calculateLegalMoves(final Board board);
	
	@Override
	public abstract String toString();
	
	public abstract Piece movePiece(Move move);
	
	public abstract int getValue();
	
	public PieceType getPieceType()
	{ return this.pieceType; }

	public Integer getPiecePosition()
	{ return this.piecePosition; }
		
	private int computeHashCode() 
	{ return Objects.hash(isFirstMove, pieceAlliance, piecePosition, pieceType); }	
	
	@Override
	public int hashCode() 
	{ return this.cachedHashCode; }

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		return isFirstMove == other.isFirstMove && pieceAlliance == other.pieceAlliance
				&& piecePosition == other.piecePosition && pieceType == other.pieceType;
	}

	public enum PieceType
	{
		PAWN("P") {
			@Override
			public boolean isKing() 
			{ return false; }
		},
		ROOK("R") {
			@Override
			public boolean isKing() 
			{ return false; }
		},
		KNIGHT("N") {
			@Override
			public boolean isKing() 
			{ return false; }
		},
		BISHOP("B") {
			@Override
			public boolean isKing() 
			{ return false; }
		},
		QUEEN("Q") {
			@Override
			public boolean isKing()
			{ return false; }
		},
		KING("K") {
			@Override
			public boolean isKing()
			{ return true; }
		};
		
		private String pieceName;
		PieceType(String pieceName)
		{ this.pieceName = pieceName; }
		
		@Override 
		public String toString()
		{ return this.pieceName; }
		
		public abstract boolean isKing();
	}	
}
