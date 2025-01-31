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

public class Bishop extends Piece 
{
	private final static int[] CANDIDATE_MOVE_VECTOR_COORINATES = { -9, -7, 7, 9 };

	public Bishop(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) 
	{ super(PieceType.BISHOP ,piecePosition, pieceAlliance, isFirstMove); }

	@Override
	public Set<Move> calculateLegalMoves(Board board) 
	{
		Set<Move> legalMoves = new HashSet<Move>();
		for (int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORINATES)
		{
			int candidateDestCoordinate = this.piecePosition + currentCandidateOffset;
			while (BoardUtils.isValidCoordinate(candidateDestCoordinate))
			{
				if (isAFileExclusion(candidateDestCoordinate - currentCandidateOffset, currentCandidateOffset)
						|| isHFileExclusion(candidateDestCoordinate - currentCandidateOffset, currentCandidateOffset))
					break;
				
				final Tile candidateDestTile = board.getTile(candidateDestCoordinate);
				if (candidateDestTile.isOccupied())
				{
					if (candidateDestTile.getPiece().getAlliance() 
							!= this.pieceAlliance)
						legalMoves.add(new CaptureMove(board, this, candidateDestCoordinate, candidateDestTile.getPiece()));
					break;					
				}
				else 
					legalMoves.add(new MajorMove(board, this, candidateDestCoordinate));
				candidateDestCoordinate += currentCandidateOffset;
			}
			
		}
		return Collections.unmodifiableSet(legalMoves);
	}
	
	private static boolean isAFileExclusion(int currentCoordinate, int candidtaeOffset)
	{ return BoardUtils.A_FILE[currentCoordinate] && (candidtaeOffset == -9 || candidtaeOffset == 7); }
	
	private static boolean isHFileExclusion(int currentCoordinate, int candidtaeOffset)
	{ return BoardUtils.H_FILE[currentCoordinate] && ( candidtaeOffset == -7 || candidtaeOffset == 9); }
	
	@Override
	public String toString()
	{ return PieceType.BISHOP.toString(); }

	@Override
	public Bishop movePiece(Move move) 
	{ return new Bishop(move.getDestCoordinate(), move.getMovedPiece().getAlliance(), false); } 
	
	@Override
	public int getValue() 
	{ return 3; } 
}
