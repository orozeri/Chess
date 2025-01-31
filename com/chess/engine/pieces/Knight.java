package com.chess.engine.pieces;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.CaptureMove;
import com.chess.engine.board.MajorMove;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece.PieceType;

public class Knight extends Piece
{
	
	private final static int[] CANDIDATE_MOVE_COORDIANTES = 
		{ -17, -15, -10, -6, 6, 10, 15, 17 };
	

	public Knight(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) 
	{ super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove); }

	@Override
	public Set<Move> calculateLegalMoves(Board board) 
	{
		int candidateDestCoordinate;
		Set<Move> legalMoves = new HashSet<Move>();
		for (int currentCandidateOffset : CANDIDATE_MOVE_COORDIANTES)
		{
			candidateDestCoordinate = piecePosition + currentCandidateOffset;
			if (BoardUtils.isValidCoordinate(candidateDestCoordinate))
			{
				if (isAFileExclusion(this.piecePosition, currentCandidateOffset) 
						|| isBFileExclusion(this.piecePosition, currentCandidateOffset)
						|| isGFileExclusion(this.piecePosition, currentCandidateOffset)
						|| isHFileExclusion(this.piecePosition, currentCandidateOffset))
					continue;
					
				final Tile candidateDestTile = board.getTile(candidateDestCoordinate);
				if (candidateDestTile.isOccupied())
				{
					if (candidateDestTile.getPiece().getAlliance() 
							!= this.pieceAlliance)
						legalMoves.add(new CaptureMove(board, this, candidateDestCoordinate, candidateDestTile.getPiece()));
				}
				else 
					legalMoves.add(new MajorMove(board, this, candidateDestCoordinate));
			}
		}
		return Collections.unmodifiableSet(legalMoves);
	}
	
	
	private static boolean isAFileExclusion(int currentCoordinate, int candidtaeOffset)
	{ return BoardUtils.A_FILE[currentCoordinate] && (candidtaeOffset == -17 
				|| candidtaeOffset == -10 || candidtaeOffset == 6 || candidtaeOffset == 15); }
	
	private static boolean isBFileExclusion(int currentCoordinate, int candidtaeOffset)
	{ return BoardUtils.B_FILE[currentCoordinate] && (candidtaeOffset == -10 
				|| candidtaeOffset == 6); }
	
	private static boolean isGFileExclusion(int currentCoordinate, int candidtaeOffset)
	{ return BoardUtils.G_FILE[currentCoordinate] && (candidtaeOffset == -6 
				|| candidtaeOffset == 10); }
	
	private static boolean isHFileExclusion(int currentCoordinate, int candidtaeOffset)
	{ return BoardUtils.H_FILE[currentCoordinate] && ( candidtaeOffset == -15 
				|| candidtaeOffset == -6 || candidtaeOffset == 10 || candidtaeOffset == 17); }
	
	@Override
	public String toString()
	{ return PieceType.KNIGHT.toString(); }
	
	@Override
	public Knight movePiece(Move move) 
	{ return new Knight(move.getDestCoordinate(), move.getMovedPiece().getAlliance(), false); }

	@Override
	public int getValue() 
	{ return 3; } 	
}
