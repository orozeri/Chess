package com.chess.engine;

import com.chess.engine.player.Player;

public enum Alliance 
{
	WHITE
	{
		@Override
		public int getDirection() 
		{ return -1; }

		@Override
		public boolean isWhite() 
		{ return true; }

		@Override
		public boolean isBlack() 
		{ return false; }
		
		@Override
		public Player choosePlayer(Player whitePlayer, Player blackPlayer)
		{ return whitePlayer; }

		@Override
		public boolean isPromotionSquare(int coordinate) 
		{ return (coordinate >= 0 && coordinate <= 7); }
	},	
	
	BLACK
	{
		@Override
		public int getDirection() 
		{ return 1; }

		@Override
		public boolean isWhite() 
		{ return false; }

		@Override
		public boolean isBlack() 
		{ return true; }
		
		@Override
		public Player choosePlayer(Player whitePlayer, Player blackPlayer)
		{ return blackPlayer; }

		@Override
		public boolean isPromotionSquare(int coordinate) 
		{ return (coordinate >= 56 && coordinate <= 63); }
	};	
	
	public abstract int getDirection();
	public abstract boolean isWhite();
	public abstract boolean isBlack();
	public abstract boolean isPromotionSquare(int coordinate);
	public abstract Player choosePlayer(Player whitePlayer, Player blackPlayer);
}
