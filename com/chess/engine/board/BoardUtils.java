package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils 
{
	public static final boolean[] A_FILE = initColumn(0);
	public static final boolean[] B_FILE = initColumn(1);
	public static final boolean[] G_FILE = initColumn(6);
	public static final boolean[] H_FILE = initColumn(7);
	
	public static final boolean[] SECOND_RANK = initRow(8);
	public static final boolean[] SEVENTH_RANK = initRow(48);
	
	public static final char[] FILE_LETTERS = initFileLetters();
	
	private static final String[] NOTATION = initNotation();
	private static final Map<String, Integer> POS_TO_COORD = initPosToCoord();
	
	public static final int NUM_TILES = 64;
	public static final int NUM_TILES_PER_ROW = 8;
	
	private BoardUtils()
	{ throw new RuntimeException("cannot be instantiated"); }

	private static boolean[] initColumn(int columnNumber) 
	{
		final boolean[] column = new boolean[NUM_TILES];
		do {
			column[columnNumber] = true;
			columnNumber += NUM_TILES_PER_ROW;
			} while (columnNumber < NUM_TILES);
				
		return column;
	}
	
	private static boolean[] initRow(int rowNumber) 
	{
		final boolean[] row = new boolean[NUM_TILES];
		do {
			row[rowNumber] = true;
			rowNumber ++;
			} while (rowNumber % NUM_TILES_PER_ROW != 0);
				
		return row;
	}
	
	private static char[] initFileLetters()
	{
		char[] fileLetters = new char[NUM_TILES_PER_ROW];
		char letter = 'a';
		for (int i = 0; i < 8; i++)
		{
			fileLetters[i] = letter;
			letter++;
		}
		return fileLetters;
	}
	
	private static String[] initNotation() 
	{
		int i = 0;
		String[] notation = new String[64];
		for (int j = NUM_TILES_PER_ROW; j >= 1; j--)
		{
			for (int k = 0; k < NUM_TILES_PER_ROW; k++)
			{
				notation[i] = new StringBuilder().append(FILE_LETTERS[i % 8]).append(j).toString();
				i++;
			}
		}
		return notation;
	}
	
	private static Map<String, Integer> initPosToCoord() 
	{
		Map<String, Integer> map = new HashMap<>();
		for (int i = 0; i < NUM_TILES; i++)
			map.put(NOTATION[i], i);
		return map;	
	}

	public static boolean isValidCoordinate(int coordinate)
	{ return coordinate >=0 && coordinate < NUM_TILES; }

	public static String getPostionAtCoordinate(int coordinate) 
	{ return NOTATION[coordinate]; }
	
	public static int getCoordianteAtPosition(String position)
	{ return POS_TO_COORD.get(position); }
}
