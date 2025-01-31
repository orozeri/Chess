package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public class Board 
{
	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	private final Pawn enPassantPawn;
	

	private Board(Builder builder) 
	{ 
		this.gameBoard = createGameBoard(builder); 
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
		Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
		Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);
		this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
		this.blackPlayer = new BlackPlayer(this, blackLegalMoves, whiteLegalMoves);
		whiteLegalMoves.addAll(whitePlayer.calculateCastles(whiteLegalMoves, blackLegalMoves));
		blackLegalMoves.addAll(blackPlayer.calculateCastles(blackLegalMoves, whiteLegalMoves));
		currentPlayer = builder.nextMoveMaker.choosePlayer(whitePlayer, blackPlayer);
		enPassantPawn = builder.enPassantPawn;
	}

	private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) 
	{
		final Set<Move> legalMoves = new HashSet<Move>();
		for (Piece piece : pieces)
			legalMoves.addAll(piece.calculateLegalMoves(this));
		return legalMoves;
	}

	private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance color)
	{
		final Set<Piece> activePieces = new HashSet<Piece>();
		for (Tile tile : gameBoard)
		{
			if (tile.isOccupied())
			{
				Piece piece = tile.getPiece();
				if (piece.getAlliance() == color)
					activePieces.add(piece);
			}
		}
		return Collections.unmodifiableSet(activePieces);
	}

	private static List<Tile> createGameBoard(Builder builder) 
	{
		final List<Tile> tiles = new ArrayList<Tile>();
		for (int i = 0; i < BoardUtils.NUM_TILES; i++)
			tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));
		return Collections.unmodifiableList(tiles);
	}
	
	public Collection<Piece> getBlackPieces() 
	{ return blackPieces; }
	
	public Collection<Piece> getWhitePieces() 
	{ return whitePieces; }
	
	public Player getWhitePlayer()
	{ return whitePlayer; }
	
	public Player getBlackPlayer()
	{ return blackPlayer; }
	
	public Player getCurrentPlayer()
	{ return currentPlayer; }

	public Tile getTile(int tileCoordinate) 
	{ return gameBoard.get(tileCoordinate); }
	
	public List<Tile> getTiles()
	{ return gameBoard; }
	
	public Collection<Move> getAllLegalMoves()
	{	Collection<Move> allLegalMoves = new HashSet<>(whitePlayer.getLegalMoves());
		allLegalMoves.addAll(blackPlayer.getLegalMoves());
		return Collections.unmodifiableSet(new HashSet<Move>(allLegalMoves)); 
	}
	
	public static Board createStandardBoard()
	{
		final Builder builder = new Builder();
		
		builder.setPiece(new Rook(0, Alliance.BLACK, true));
		builder.setPiece(new Knight(1, Alliance.BLACK, true));
		builder.setPiece(new Bishop(2, Alliance.BLACK, true));
		builder.setPiece(new Queen(3, Alliance.BLACK, true));
		builder.setPiece(new King(4, Alliance.BLACK, true));
		builder.setPiece(new Bishop(5, Alliance.BLACK, true));
		builder.setPiece(new Knight(6, Alliance.BLACK, true));
		builder.setPiece(new Rook(7, Alliance.BLACK, true));
		builder.setPiece(new Pawn(8, Alliance.BLACK, true));
		builder.setPiece(new Pawn(9, Alliance.BLACK, true));
		builder.setPiece(new Pawn(10, Alliance.BLACK, true));
		builder.setPiece(new Pawn(11, Alliance.BLACK, true));
		builder.setPiece(new Pawn(12, Alliance.BLACK, true));
		builder.setPiece(new Pawn(13, Alliance.BLACK, true));
		builder.setPiece(new Pawn(14, Alliance.BLACK, true));
		builder.setPiece(new Pawn(15, Alliance.BLACK, true));
		
		builder.setPiece(new Pawn(49, Alliance.WHITE, true));
		builder.setPiece(new Pawn(50, Alliance.WHITE, true));
		builder.setPiece(new Pawn(51, Alliance.WHITE, true));
		builder.setPiece(new Pawn(52, Alliance.WHITE, true));
		builder.setPiece(new Pawn(53, Alliance.WHITE, true));
		builder.setPiece(new Pawn(54, Alliance.WHITE, true));
		builder.setPiece(new Pawn(55, Alliance.WHITE, true));
		builder.setPiece(new Rook(56, Alliance.WHITE, true));
		builder.setPiece(new Knight(57, Alliance.WHITE, true));
		builder.setPiece(new Bishop(58, Alliance.WHITE, true));
		builder.setPiece(new Queen(59, Alliance.WHITE, true));
		builder.setPiece(new King(60, Alliance.WHITE, true));
		builder.setPiece(new Bishop(61, Alliance.WHITE, true));
		builder.setPiece(new Knight(62, Alliance.WHITE, true));
		builder.setPiece(new Rook(63, Alliance.WHITE, true));
		builder.setPiece(new Pawn(48, Alliance.WHITE, true));
		
		builder.setMoveMaker(Alliance.WHITE);
		
		return builder.build();
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < BoardUtils.NUM_TILES; i++)
		{
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0)
				builder.append("\n");
		}
		return builder.toString();
	}

	public static class Builder
	{
		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;
		
		public Builder() 
		{ boardConfig = new HashMap<Integer, Piece>(); }
		
		public Builder setPiece(Piece piece)
		{
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		public Builder setMoveMaker(Alliance nextMoveMaker)
		{
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}
		
		public Board build()
		{ return new Board(this); }

		public void setEnPassantPawn(Pawn enPassantPawn) 
		{ this.enPassantPawn = enPassantPawn; }
	}
	
	public Pawn getEnPassantPawn() 
	{ return enPassantPawn; }
}
