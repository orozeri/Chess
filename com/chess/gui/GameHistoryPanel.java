package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.gui.Table.MoveLog;

public class GameHistoryPanel extends JPanel 
{
	private static final Dimension DIMENSION = new Dimension(150, 400);
	
	private final DataModel model;
	private final JScrollPane scrollPane;
	
	public GameHistoryPanel() 
	{
		setLayout(new BorderLayout());
		model = new DataModel();
		final JTable table = new JTable(model);
		table.setRowHeight(15);
		scrollPane = new JScrollPane(table);
		scrollPane.setColumnHeaderView(table.getTableHeader());
		scrollPane.setPreferredSize(DIMENSION);
		add(scrollPane, BorderLayout.CENTER);
		setVisible(true);
	}
	
	void draw(Board board, MoveLog log)
	{
		int currentRow = 0;
		model.clear();
		for (Move move : log.getMoves())
		{
			final String moveText = move.toString();
			if (move.getMovedPiece().getAlliance().isWhite())
				model.setValueAt(moveText, currentRow, 0);
			else
			{
				model.setValueAt(moveText, currentRow, 1);
				currentRow++;
			}
		}
		if (log.size() > 0)
		{
			final Move lastMove = log.getMoves().get(log.size() - 1);
			final String moveText = lastMove.toString();
			if (lastMove.getMovedPiece().getAlliance().isWhite())
				model.setValueAt(moveText + calculateCheckAndCheckmateText(board), currentRow, 0);
			else
				model.setValueAt(moveText + calculateCheckAndCheckmateText(board), currentRow - 1, 1);

		}
		final JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		
		validate();
	}
	
	private String calculateCheckAndCheckmateText(Board board) 
	{
		if (board.getCurrentPlayer().isInCheckmate())
			return "#";
		else if (board.getCurrentPlayer().isInCheck())
			return "+";
		return "";
	}

	private static class DataModel extends DefaultTableModel
	{
		private final List<Row> values;
		private static final String[] NAMES = {"White", "Black" };
		
		DataModel()
		{ values = new ArrayList<>(); }
		
		public void clear()
		{ 
			values.clear();
			setRowCount(0);
		}
		
		@Override
		public int getRowCount()
		{
			if (values == null)
				return 0;
			return values.size();
		}
		
		@Override
		public int getColumnCount()
		{ return 2; }
		
		
		@Override
		public Object getValueAt(int row, int column)
		{
			final Row currentRow = values.get(row);
			if (column == 0)
				return currentRow.getWhiteMove();
			else if (column == 1)
				return currentRow.getBlackMove();
			return null;
		}
		
		@Override
		public void setValueAt(Object value, int row, int column)
		{
			final Row currentRow;
			if (values.size() <= row)
			{
				currentRow = new Row();
				values.add(currentRow);
			}
			else
				currentRow = values.get(row);
			if (column == 0)
			{
				currentRow.setWhiteMoves((String) value);
				fireTableRowsInserted(row, row);
			}
			else if (column == 1)
			{
				currentRow.setBlackMoves((String) value);
				fireTableCellUpdated(row, column);
			}
		}
		
		@Override
		public Class<?> getColumnClass(int column)
		{ return Move.class; }
		
		@Override
		public String getColumnName(int column)
		{ return NAMES[column]; }
	}
	
	private static class Row
	{
		private String whiteMove;
		private String blackMove;
		
		public String getWhiteMove()
		{ return whiteMove; }
		
		public String getBlackMove()
		{ return blackMove; }
		
		public void setWhiteMoves(String s)
		{ whiteMove = s; }
		
		public void setBlackMoves(String s)
		{ blackMove = s; }
	}
}
