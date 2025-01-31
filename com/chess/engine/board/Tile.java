package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.chess.engine.pieces.Piece;

public abstract class Tile 
{
	int tileCoordinate;
	private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();
	
	Tile(int tileCoordinate)
	{ this.tileCoordinate = tileCoordinate; }
	
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() 
	{
		final Map<Integer, EmptyTile> map = new HashMap<>();
		for (int i = 0; i < 64; i++)
			map.put(i, new EmptyTile(i));
		return map;
	}
	
	public static Tile createTile(int tileCoordinate, Piece piece)
	{ return piece != null ?  new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES.get(tileCoordinate); }

	public abstract boolean isOccupied();
	public abstract Piece getPiece();
	
	public int getCoordinate()
	{ return tileCoordinate; }
	
	public static final class EmptyTile extends Tile
	{
		public EmptyTile(int tileCoordinate) 
		{ super(tileCoordinate); }

		@Override
		public boolean isOccupied() 
		{ return false; }

		@Override
		public Piece getPiece() 
		{ return null; }
	}
	
	public static final class OccupiedTile extends Tile
	{
		private Piece pieceOnTile;
		
		OccupiedTile(int tileCoordinate, Piece piece) 
		{ 
			super(tileCoordinate); 
			pieceOnTile = piece;
		}

		@Override
		public boolean isOccupied() 
		{ return true; }

		@Override
		public Piece getPiece() 
		{ return pieceOnTile; }
		
	}
}
