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

public class BlackPlayer extends Player
{

	public BlackPlayer(Board board, 
			Collection<Move> blackStandardLegalMoves, 
			Collection<Move> whiteStandardLegalMoves) 
	{ super(board, blackStandardLegalMoves, whiteStandardLegalMoves); }

	@Override
	public Collection<Piece> getActivePieces() 
	{ return this.board.getBlackPieces(); }
	
	@Override
	public Alliance getAlliance()
	{ return Alliance.BLACK; }
	
	@Override
	public Player getOpponent()
	{ return board.getWhitePlayer(); }
	
	@Override
	public Collection<Move> calculateCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) 
	{
		Set<Move> kingCastles = new HashSet<>();
		if (playerKing.isFirstMove() && !isInCheck())
		{
			if (!board.getTile(5).isOccupied() && !board.getTile(6).isOccupied())
			{
				if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
						Player.calculateAttacksOnTile(5, opponentLegals).isEmpty())
				{
					Tile rookTile = board.getTile(7);
					if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove())
						kingCastles.add(new KingsideCastleMove(board, playerKing, 6, (Rook) rookTile.getPiece(), 5));;
				}
			}
			if (!board.getTile(1).isOccupied() && !board.getTile(2).isOccupied() && !board.getTile(3).isOccupied())
			{
				if (Player.calculateAttacksOnTile(1, opponentLegals).isEmpty() &&
						Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
						Player.calculateAttacksOnTile(3, opponentLegals).isEmpty())
				{
					Tile rookTile = board.getTile(0);
					if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove())
						kingCastles.add(new QueensideCastleMove(board, playerKing, 2, (Rook) rookTile.getPiece(), 3));
				}
			}
		}
		return kingCastles;
	}
}
