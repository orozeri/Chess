package com.chess.engine.pieces;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.EnPassant;
import com.chess.engine.board.MajorMove;
import com.chess.engine.board.Move;
import com.chess.engine.board.PawnCaptureMove;
import com.chess.engine.board.PawnJumpMove;
import com.chess.engine.board.PawnMove;
import com.chess.engine.board.PawnPromotion;
import com.chess.engine.board.Tile;

public class Pawn extends Piece
{	
	private final static int[] CANDIDATE_MOVE_COORDIANTES = { 7, 8, 9, 16 };
	
	public Pawn(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) 
	{ super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove); }

	@Override
	public Set<Move> calculateLegalMoves(Board board) 
	{
		Set<Move> legalMoves = new HashSet<Move>();
		for (int currentCandidateOffset : CANDIDATE_MOVE_COORDIANTES)
		{
			int candidateDestCoordinate = this.piecePosition + 
					currentCandidateOffset * this.pieceAlliance.getDirection();
			if (!BoardUtils.isValidCoordinate(candidateDestCoordinate))
				continue;
			
			Tile candidateDestTile = board.getTile(candidateDestCoordinate);	
			if (currentCandidateOffset == 8 && !candidateDestTile.isOccupied())
			{
				if (this.pieceAlliance.isPromotionSquare(candidateDestCoordinate))
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestCoordinate)));
				else
					legalMoves.add(new PawnMove(board, this, candidateDestCoordinate));
			}
			else if (currentCandidateOffset == 16 && this.isFirstMove && !candidateDestTile.isOccupied() 
					&& !board.getTile(candidateDestCoordinate - (this.pieceAlliance.getDirection() * 8)).isOccupied())
				legalMoves.add(new PawnJumpMove(board, this, candidateDestCoordinate));
			else if ((currentCandidateOffset == 7 || currentCandidateOffset == 9) && candidateDestTile.isOccupied()
					&& !isAFileExclusion(candidateDestCoordinate - currentCandidateOffset * this.pieceAlliance.getDirection(), currentCandidateOffset * this.pieceAlliance.getDirection())
					&& !isHFileExclusion(candidateDestCoordinate - currentCandidateOffset * this.pieceAlliance.getDirection(), currentCandidateOffset * this.pieceAlliance.getDirection())
					&& candidateDestTile.getPiece().getAlliance() != this.pieceAlliance)
			{
				if (this.pieceAlliance.isPromotionSquare(candidateDestCoordinate))
					legalMoves.add(new PawnPromotion(new PawnCaptureMove(board, this, candidateDestCoordinate, candidateDestTile.getPiece())));
				else
					legalMoves.add(new PawnCaptureMove(board, this, candidateDestCoordinate, candidateDestTile.getPiece()));
			}			
		} 
		if (board.getEnPassantPawn() != null)
		{
			Pawn enPassantPawn = board.getEnPassantPawn();
			if (enPassantPawn.getAlliance() != this.getAlliance())
			{
				if (enPassantPawn.getPiecePosition() == this.getPiecePosition() + 1 
						&& !BoardUtils.H_FILE[this.getPiecePosition()])
				{
					if (this.getAlliance().isWhite())
						legalMoves.add(new EnPassant(board, this, this.getPiecePosition() - 7, enPassantPawn)); 
					else 
						legalMoves.add(new EnPassant(board, this, this.getPiecePosition() + 9, enPassantPawn));
				}
				else if (enPassantPawn.getPiecePosition() == this.getPiecePosition() - 1 
						&& !BoardUtils.A_FILE[this.getPiecePosition()])
				{
					if (this.getAlliance().isWhite())
						legalMoves.add(new EnPassant(board, this, this.getPiecePosition() - 9, enPassantPawn));
					else 
						legalMoves.add(new EnPassant(board, this, this.getPiecePosition() + 7, enPassantPawn));
				}
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
	{ return PieceType.PAWN.toString(); }
	
	@Override
	public Pawn movePiece(Move move) 
	{ return new Pawn(move.getDestCoordinate(), move.getMovedPiece().getAlliance(), false); } 
	
	public Piece getPromotedPiece() 
	{ return new Queen(this.piecePosition, this.pieceAlliance, false); }	
	
	@Override
	public int getValue()
	{ return 1; }
	
	
}
