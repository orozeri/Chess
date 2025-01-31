package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

public class TakenPiecesPanel extends JPanel
{
	private static final Dimension DIMENSION = new Dimension(400, 50);
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	private static final Color COLOR = Color.decode("#EEEEB2");
	
	private final JPanel westPanel;
	private final JPanel eastPanel;
	
	public TakenPiecesPanel()
	{ 
		super(new BorderLayout()); 
		setBackground(COLOR);
		setBorder(PANEL_BORDER);
		
		westPanel = new JPanel(new GridLayout(2, 8));
		eastPanel = new JPanel(new GridLayout(2, 8));
		westPanel.setBackground(COLOR);
		eastPanel.setBackground(COLOR);
		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
		
		setPreferredSize(DIMENSION);
	}
	
	public void draw(Table.MoveLog log)
	{
		westPanel.removeAll();
		eastPanel.removeAll();
		
		final List<Piece> whiteTakenPieces = new ArrayList<>();
		final List<Piece> blackTakenPieces = new ArrayList<>();
		
		for (Move move : log.getMoves())
		{
			if (move.isCapture())
			{
				Piece piece = move.getCapturedPiece();
				if (piece.getAlliance().isWhite())
					whiteTakenPieces.add(piece);
				else
					blackTakenPieces.add(piece);
			}
		}
		
		Collections.sort(whiteTakenPieces, new Comparator<Piece>()
				{
					@Override
					public int compare(Piece o1, Piece o2) 
					{ return o1.getValue() - o2.getValue(); }
				});
		
		Collections.sort(blackTakenPieces, new Comparator<Piece>()
		{
			@Override
			public int compare(Piece o1, Piece o2) 
			{ return o1.getValue() - o2.getValue(); }
		});
		
		for (Piece piece : whiteTakenPieces)
		{
			ImageIcon image = new ImageIcon("art/takenPieces/" + 
					piece.getAlliance().toString().substring(0, 1) + piece.toString() + ".png");
			JLabel label = new JLabel();
			label.setIcon(image);
			eastPanel.add(label);
		}
		for (Piece piece : blackTakenPieces)
		{
			ImageIcon image = new ImageIcon("art/takenPieces/" + 
					piece.getAlliance().toString().substring(0, 1) + piece.toString() + ".png");
			JLabel label = new JLabel();
			label.setIcon(image);
			westPanel.add(label);
		}
		
		validate();
	}
}
