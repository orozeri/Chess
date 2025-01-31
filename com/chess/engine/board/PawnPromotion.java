package com.chess.engine.board;

import java.util.Objects;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public class PawnPromotion extends Move 
{
	final Move decoratedMove;
	final Pawn promotedPawn;
	
	public PawnPromotion(Move decoratedMove)
	{ 
		super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestCoordinate());
		this.decoratedMove = decoratedMove;
		this.promotedPawn = (Pawn) this.decoratedMove.getMovedPiece();
	}
	
	@Override
	public Board execute()
	{
		final Board board = decoratedMove.execute();
		final Builder builder = new Builder();
		for (Piece piece : this.board.getCurrentPlayer().getActivePieces())
		{
			if (!promotedPawn.equals(piece))
				builder.setPiece(piece);
		}
		for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
			builder.setPiece(piece);
		builder.setPiece(this.promotedPawn.getPromotedPiece().movePiece(this));
		builder.setMoveMaker(board.getCurrentPlayer().getAlliance());
		return builder.build();
	}
	
	@Override
	public boolean isCapture()
	{ return decoratedMove.isCapture(); }
	
	@Override
	public boolean isPromotion()
	{ return true; }
	
	@Override
	public Piece getCapturedPiece()
	{ return decoratedMove.getCapturedPiece(); }
	
	@Override
	public String toString()
	{ return decoratedMove.toString() + "=Q"; }

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(decoratedMove, promotedPawn);
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PawnPromotion other = (PawnPromotion) obj;
		return Objects.equals(decoratedMove, other.decoratedMove) && Objects.equals(promotedPawn, other.promotedPawn);
	}	
}
