package com.chess.engine.board;

import java.util.List;

import com.chess.engine.board.Tile;

public enum BoardDirection 
{
	NORMAL
	{
		@Override
		public List<Tile> traverse(List<Tile> tiles) 
		{ return tiles; }

		@Override
		public BoardDirection opposite() 
		{ return FLIPPED; }	
	},
	
	FLIPPED
	{
		@Override
		public List<Tile> traverse(List<Tile> tiles) 
		{ return tiles.reversed(); }

		@Override
		public BoardDirection opposite() 
		{ return NORMAL; }		
	};
	
	public abstract List<Tile> traverse(List<Tile> tiles);
	public abstract BoardDirection opposite();
}
