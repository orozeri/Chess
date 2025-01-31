package com.chess.engine.player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

public abstract class Player 
{
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private final boolean isInCheck;
	
	Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves)
	{
		this.board = board;
		this.playerKing = establishKing();
		Collection<Move> allMoves = legalMoves;
		allMoves.addAll(calculateCastles(legalMoves, opponentMoves));
		this.legalMoves = allMoves;
		this.isInCheck = !Player.calculateAttacksOnTile(playerKing.getPiecePosition(), opponentMoves).isEmpty();
	}

	protected static Collection<Move> calculateAttacksOnTile(int tileCoordinate, Collection<Move> moves) 
	{
		Set<Move> attackMoves = new HashSet<Move>();
		for (Move move : moves)
		{
			if (move.getDestCoordinate() == tileCoordinate)
				attackMoves.add(move);
		}
		return Collections.unmodifiableSet(attackMoves);
	}
	
	protected boolean hasEscapeMoves() 
	{
		for (Move move : this.legalMoves)
		{
			final MoveTransition transition = makeMove(move);
			if (transition.getMoveStatus().isDone())
				return true;
		}
		return false;
	}

	private King establishKing() 
	{
		for (Piece piece : getActivePieces())
			if (piece.getPieceType().isKing())
				return (King)piece;
		throw new RuntimeException("Board does not have a king");
	}
	
	public King getPlayerKing()
	{ return this.playerKing; }
	
	public Collection<Move> getLegalMoves()
	{ return this.legalMoves; }
	
	public boolean isMoveLegal(Move move)
	{ return legalMoves.contains(move); }
	
	public boolean isInCheck()
	{ return this.isInCheck; }
	
	public boolean isInCheckmate()
	{ return this.isInCheck && !hasEscapeMoves(); }

	public boolean isInStalemate()
	{ return !this.isInCheck && !hasEscapeMoves(); }
	
	public boolean isCastled()
	{ return false; }
	
	public MoveTransition makeMove(Move move)
	{ 
		if (!isMoveLegal(move))
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		
		final Board transitionBoard = move.execute();
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile
				(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(), 
						transitionBoard.getCurrentPlayer().getLegalMoves());
		if (!kingAttacks.isEmpty())
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);

	}

	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
	public abstract Collection<Move> calculateCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);
}
