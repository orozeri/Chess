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

public class King extends Piece 
{
	private final static int[] CANDIDATE_MOVE_COORDIANTES = 
		{ -9, -8, -7, -1, 1, 7, 8, 9 };

	public King(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) 
	{ super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove); }

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
	{ return BoardUtils.A_FILE[currentCoordinate] && (candidtaeOffset == -9 
	|| candidtaeOffset == -1 || candidtaeOffset == 7); }
	
	private static boolean isHFileExclusion(int currentCoordinate, int candidtaeOffset)
	{ return BoardUtils.H_FILE[currentCoordinate] && ( candidtaeOffset == -7 
	|| candidtaeOffset == 1 || candidtaeOffset == 9); }
	
	@Override
	public String toString()
	{ return PieceType.KING.toString(); }
	
	@Override
	public King movePiece(Move move) 
	{ return new King(move.getDestCoordinate(), move.getMovedPiece().getAlliance(), false); } 

	@Override
	public int getValue() 
	{ return 100; } 
}
