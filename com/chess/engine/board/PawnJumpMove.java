package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public class PawnJumpMove extends PawnMove 
{

	public PawnJumpMove(Board board, Pawn movedPawn, int destCoordinate)
	{ super(board, movedPawn, destCoordinate); }

	@Override
	public Board execute()
	{
		final Board.Builder builder = new Board.Builder();
		for (Piece piece : board.getCurrentPlayer().getActivePieces())
		{
			if (!movedPiece.equals(piece))
				builder.setPiece(piece);
		}
		for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces())
			builder.setPiece(piece);
		Pawn pawn = (Pawn) movedPiece.movePiece(this);
		builder.setPiece(pawn);
		builder.setEnPassantPawn(pawn);
		builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
		return builder.build();
 
	}
}
