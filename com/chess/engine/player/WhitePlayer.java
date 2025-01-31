package com.chess.engine.player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.KingsideCastleMove;
import com.chess.engine.board.Move;
import com.chess.engine.board.QueensideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class WhitePlayer extends Player 
{

	public WhitePlayer(Board board, 
			Collection<Move> whiteStandardLegalMoves, 
			Collection<Move> blackStandardLegalMoves) 
	{ super(board, whiteStandardLegalMoves, blackStandardLegalMoves); }

	@Override
	public Collection<Piece> getActivePieces() 
	{ return board.getWhitePieces(); }

	@Override
	public Alliance getAlliance()
	{ return Alliance.WHITE; }
	
	@Override
	public Player getOpponent()
	{ return board.getBlackPlayer(); }

	@Override
	public Collection<Move> calculateCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) 
	{
		Set<Move> kingCastles = new HashSet<>();
		if (playerKing.isFirstMove() && !isInCheck())
		{
			if (!board.getTile(61).isOccupied() && !board.getTile(62).isOccupied())
			{
				if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
						Player.calculateAttacksOnTile(62, opponentLegals).isEmpty())
				{
					Tile rookTile = board.getTile(63);
					if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove())
						kingCastles.add(new KingsideCastleMove(board, playerKing, 62, (Rook) rookTile.getPiece(), 61));
				}
			}
			if (!board.getTile(57).isOccupied() && !board.getTile(58).isOccupied() && !board.getTile(59).isOccupied())
			{
				if (Player.calculateAttacksOnTile(57, opponentLegals).isEmpty() &&
						Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
						Player.calculateAttacksOnTile(59, opponentLegals).isEmpty())
				{
					Tile rookTile = board.getTile(56);
					if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove())
						kingCastles.add(new QueensideCastleMove(board, playerKing, 58, (Rook) rookTile.getPiece(), 59));
				}
			}
		}
		return kingCastles;
	}

}
