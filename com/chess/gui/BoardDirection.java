package com.chess.gui;

import java.util.List;

import com.chess.gui.Table.TilePanel;

public enum BoardDirection 
{
	NORMAL
	{
		@Override
		public List<TilePanel> traverse(List<TilePanel> tiles) 
		{ return tiles; }

		@Override
		public BoardDirection opposite() 
		{ return FLIPPED; }	
	},
	
	FLIPPED
	{
		@Override
		public List<TilePanel> traverse(List<TilePanel> tiles) 
		{ return tiles.reversed(); }

		@Override
		public BoardDirection opposite() 
		{ return NORMAL; }		
	};
	
	public abstract List<TilePanel> traverse(List<TilePanel> tiles);
	public abstract BoardDirection opposite();
}
