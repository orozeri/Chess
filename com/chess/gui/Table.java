package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

public class Table
{
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private final TakenPiecesPanel takenPiecesPanel;
	private final GameHistoryPanel historyPanel;
	private final JPanel checkmate;

	private final List<Board> boards;
	private Board chessBoard;
	private Tile sourceTile;
	private Piece selectedPiece;
	private BoardDirection direction;
	private boolean showLegalMoves;
	private MoveLog moveLog;
	private int currentBoard;
	
	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(725, 675);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(475, 350);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	private static final Dimension BUTTON_DIMENSION = new Dimension(20, 20);
	
	private final Color lightTileColor = Color.decode("#BD6736");
	private final Color darkTileColor = Color.decode("#E1DBC4");


	
	public Table()
	{
		gameFrame = new JFrame("Chess");
		gameFrame.setLayout(new BorderLayout());
		
		JMenuBar tableMenuBar = createMenuBar();
		gameFrame.setJMenuBar(tableMenuBar);
		gameFrame.setSize(OUTER_FRAME_DIMENSION);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon image = new ImageIcon("art/misc/blackKnight.png");
		gameFrame.setIconImage(image.getImage());
		
		chessBoard = Board.createStandardBoard();
		direction = BoardDirection.NORMAL;
		showLegalMoves = true;
		moveLog = new MoveLog();
		boards = new ArrayList<>();
		boards.add(chessBoard);
		
		boardPanel = new BoardPanel();
		boardPanel.setVisible(false);
		
		takenPiecesPanel = new TakenPiecesPanel();
		takenPiecesPanel.setVisible(false);
		
		historyPanel = new GameHistoryPanel();
		historyPanel.setVisible(false);
		
		checkmate = new JPanel();
		JLabel label = new JLabel("Game Over");
		checkmate.add(label);
		checkmate.setVisible(false);

		gameFrame.add(boardPanel, BorderLayout.CENTER);
		gameFrame.add(takenPiecesPanel, BorderLayout.SOUTH);
		gameFrame.add(historyPanel, BorderLayout.EAST);
		gameFrame.add(checkmate, BorderLayout.NORTH);
		gameFrame.setResizable(false);
		
		gameFrame.setVisible(true);
	}

	private JMenuBar createMenuBar()
	{ 
		JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(creatFileMenu());
		tableMenuBar.add(createPrefrencesMenu()); 
		tableMenuBar.add(createButtonPanel());
		return tableMenuBar;
	}

	private JMenu creatFileMenu() 
	{
		JMenu fileMenu = new JMenu("File");
		JMenuItem start = new JMenuItem("Start Game");
		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{ 
				boardPanel.setVisible(true);
				takenPiecesPanel.setVisible(true);
				historyPanel.setVisible(true);
			}			
		});
		fileMenu.add(start);
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{ System.exit(0); }			
			});
		fileMenu.add(exit);
		
		return fileMenu;
	}
	
	private JMenu createPrefrencesMenu()
	{
		JMenu prefrencesMenu = new JMenu("Prefrences");
		JMenuItem flip = new JMenuItem("Flip Board");
		flip.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{ 
				direction = direction.opposite();
				boardPanel.drawBoard(chessBoard);
			}			
		});
		
		JCheckBoxMenuItem highlight = new JCheckBoxMenuItem("Show Legal Moves", true);
		highlight.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{ showLegalMoves = highlight.isSelected(); }			
		});
		
		prefrencesMenu.add(flip);
		prefrencesMenu.addSeparator();
		prefrencesMenu.add(highlight);
		return prefrencesMenu;
	}
	
	private JPanel createButtonPanel()
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		panel.add(createSkipPrevButton());
		panel.add(createPrevButton());
		panel.add(createNextButton());
		panel.add(createSkipNextButton());
		return panel;
	}
	
	private JButton createPrevButton()
	{
		JButton button = new JButton(new ImageIcon("art/misc/prevButton.png"));
		initButton(button);
		button.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{ 
				chessBoard = boards.get(currentBoard);
				if (currentBoard > 0)
				{
					currentBoard--;
					boardPanel.getTile(moveLog.get(currentBoard).getCurrentCoordiante()).setBackgroundMark(false);
					boardPanel.getTile(moveLog.get(currentBoard).getDestCoordinate()).setBackgroundMark(false);
					if (currentBoard >= 1)
					{
						boardPanel.getTile(moveLog.get(currentBoard - 1).getCurrentCoordiante()).setBackgroundMark(true);
						boardPanel.getTile(moveLog.get(currentBoard - 1).getDestCoordinate()).setBackgroundMark(true);
					}
				}
				boardPanel.drawBoard(chessBoard);
				chessBoard = boards.get(currentBoard);
				boardPanel.drawBoard(chessBoard);
			}
		});
		return button;
	}
	
	private JButton createNextButton()
	{
		JButton button = new JButton(new ImageIcon("art/misc/nextButton.png"));
		initButton(button);
		button.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{ 
				if (currentBoard < boards.size() - 1)
				{
					currentBoard++;
					if (currentBoard > 1)
					{
						boardPanel.getTile(moveLog.get(currentBoard - 2).getCurrentCoordiante()).setBackgroundMark(false);
						boardPanel.getTile(moveLog.get(currentBoard - 2).getDestCoordinate()).setBackgroundMark(false);
					}
					boardPanel.getTile(moveLog.get(currentBoard - 1).getCurrentCoordiante()).setBackgroundMark(true);
					boardPanel.getTile(moveLog.get(currentBoard - 1).getDestCoordinate()).setBackgroundMark(true);
				}
				boardPanel.drawBoard(chessBoard);
				chessBoard = boards.get(currentBoard);
				boardPanel.drawBoard(chessBoard);
			}
		});
		return button;
	}
	
	private JButton createSkipPrevButton()
	{
		JButton button = new JButton(new ImageIcon("art/misc/skipPrev.png"));
		initButton(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{ 
				if (currentBoard == 0)
					return;
				boardPanel.getTile(moveLog.get(currentBoard- 1).getCurrentCoordiante()).setBackgroundMark(false);
				boardPanel.getTile(moveLog.get(currentBoard - 1).getDestCoordinate()).setBackgroundMark(false);
				currentBoard = 0;
				chessBoard = boards.get(currentBoard);
				boardPanel.drawBoard(chessBoard); 
			}
		});
		return button;
	}
	
	private JButton createSkipNextButton()
	{
		JButton button = new JButton(new ImageIcon("art/misc/skipNext.png"));
		initButton(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{ 
				if (boards.size() == 0)
					return;
				currentBoard = boards.size() - 1;		
				boardPanel.getTile(moveLog.get(currentBoard - 1).getCurrentCoordiante()).setBackgroundMark(true);
				boardPanel.getTile(moveLog.get(currentBoard - 1).getDestCoordinate()).setBackgroundMark(true);
				if (currentBoard >= 2)
				{
					for (int i = currentBoard - 2; i >= 0; i--)
						boardPanel.getTile(i).setBackgroundMark(false);
				}
				chessBoard = boards.get(currentBoard);
				boardPanel.drawBoard(chessBoard); 
			}
		});
		return button;
	}
	
	private void initButton(JButton button)
	{
		button.setContentAreaFilled(false);
		button.setFocusPainted( false );
		button.setPreferredSize(BUTTON_DIMENSION);
		button.setBorderPainted(false);
	}
	
	public class BoardPanel extends JPanel
	{
		final private List<TilePanel> tiles;
		
		BoardPanel()
		{
			super(new GridLayout(8, 8));
			this.tiles = new ArrayList<>();
			for (int i = 0; i <BoardUtils.NUM_TILES; i++)
			{
				TilePanel tile = new TilePanel(this, i);
				tiles.add(tile);
				add(tile);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		public void drawBoard(Board board) 
		{
			removeAll();
			for (TilePanel tilePanel : direction.traverse(tiles))
			{
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
		}		
		
		private void highlightLegalMoves(Piece piece)
		{
			if (showLegalMoves)
			{
				for (Move move : piece.calculateLegalMoves(chessBoard))
				{
					final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
					if (transition.getMoveStatus().isDone())
					{
						for (TilePanel tilePanel : tiles)
						{
							if (tilePanel.getID() == move.getDestCoordinate())
							{
								tilePanel.setHighlightMark(true);
								tilePanel.drawTile(chessBoard);
							}
						}
					}
				}				
				if (selectedPiece.getPieceType().isKing())
				{
					Collection<Move> castleMoves = 
							chessBoard.getCurrentPlayer().calculateCastles(chessBoard.getCurrentPlayer().getLegalMoves(), chessBoard.getCurrentPlayer().getOpponent().getLegalMoves());
					for (Move move : castleMoves)
					{
						for (TilePanel tilePanel : tiles)
						{
							if (tilePanel.getID() == move.getDestCoordinate())
							{
								tilePanel.setHighlightMark(true);
								tilePanel.drawTile(chessBoard);
							}
						}
					}
				}				
			}
		}

		public void removeHighlight(Piece piece) 
		{
			for (Move move : piece.calculateLegalMoves(chessBoard))
			{	
				for (TilePanel tilePanel : tiles)
				{
					if (tilePanel.getID() == move.getDestCoordinate())
					{
						tilePanel.drawTile(chessBoard);
					}
				}
			}
		}
		
		public void clearBackground()
		{
			for (TilePanel tilePanel : tiles)
				tilePanel.setBackgroundMark(false);
		}
		
		public TilePanel getTile(int index)
		{ return tiles.get(index); }
	}
	
	public static class MoveLog
	{
		private final List<Move> moves;
		
		public MoveLog()
		{ moves = new ArrayList<>(); }
		
		public List<Move> getMoves()
		{ return moves; }
		
		public Move get(int index)
		{ return moves.get(index); }
		
		public Move getLast()
		{ return moves.get(moves.size() - 1); }
		
		public void add(Move move)
		{ moves.add(move); }
		
		public int size()
		{ return moves.size(); }
		
		public void clear()
		{ moves.clear(); }
		
		public Move remove(int index)
		{ return moves.remove(index); }
		
		public boolean remove(Move move)
		{ return moves.remove(move); }
	}
	
	public class TilePanel extends JPanel
	{
		private final int tileID;
		private boolean highlightMark;
		private boolean backgroundMark;
		
		TilePanel(final BoardPanel boardPanel, int tileID) 
		{
			super(new GridBagLayout());
			this.tileID = tileID;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);
			addMouseListener(new MouseListener() 
					{

						@Override
						public void mouseClicked(MouseEvent e) 
						{
							if (SwingUtilities.isLeftMouseButton(e))
							{
								if (chessBoard.getTile(tileID).isOccupied() && 
										chessBoard.getTile(tileID).getPiece().getAlliance() == chessBoard.getCurrentPlayer().getAlliance())
								{
									if (selectedPiece != null)
									{
										boardPanel.removeHighlight(selectedPiece);
										boardPanel.getTile(selectedPiece.getPiecePosition()).assignTileColor();
									}
									sourceTile = chessBoard.getTile(tileID);
									selectedPiece = sourceTile.getPiece();
									boardPanel.highlightLegalMoves(selectedPiece);
									boardPanel.clearBackground();
									TilePanel tilePanel = boardPanel.getTile(tileID);
									tilePanel.setBackgroundMark(true);
									tilePanel.change();
								}
								else if (sourceTile != null)
								{
									final Move move = MoveFactory.CreateMove(chessBoard, sourceTile.getCoordinate(), tileID);
									final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);						
									if (transition.getMoveStatus().isDone())
									{
										chessBoard = transition.getTransitionBoard();										
										boardPanel.drawBoard(chessBoard);
										currentBoard++;
										if (currentBoard < boards.size() - 1)
										{
											checkmate.setVisible(false);
											int size = boards.size();
											for (int i = 0; i < size; i++)
											{
												if (i >= currentBoard)
												{
													boards.remove(currentBoard);
													moveLog.remove(currentBoard - 1);
												}
											}
										}
										TilePanel tilePanel = boardPanel.getTile(tileID);
										tilePanel.setBackgroundMark(true);
										tilePanel.change();
										boards.add(chessBoard);
										moveLog.add(move);
										historyPanel.draw(chessBoard, moveLog);
										takenPiecesPanel.draw(moveLog);
										sourceTile = null;
										selectedPiece = null;
										if (chessBoard.getCurrentPlayer().isInCheckmate() || chessBoard.getCurrentPlayer().isInStalemate())
											checkmate.setVisible(true);
									}
								}
							}
						}

						@Override
						public void mousePressed(MouseEvent e) {}

						@Override
						public void mouseReleased(MouseEvent e) {}

						@Override
						public void mouseEntered(MouseEvent e) {}

						@Override
						public void mouseExited(MouseEvent e) {}
					});
			validate();
		}
		
		public int getID()
		{ return tileID; }
		
		public void setHighlightMark(boolean mark)
		{ highlightMark = mark; }
		
		public void setBackgroundMark(boolean mark)
		{ backgroundMark = mark; }

		public void drawTile(Board board) 
		{
			assignTileColor();
			assignTilePieceIcon(board);
			if (highlightMark)
				highlight();
			if (backgroundMark)
				change();
			validate();
			repaint();
			highlightMark = false;
		}

		private void assignTileColor() 
		{
            boolean isLight = ((tileID + tileID / 8) % 2 == 0);
            setBackground(isLight ? lightTileColor : darkTileColor);
		}
		
		private void assignTilePieceIcon(Board board)
		{
			removeAll();
			if (board.getTile(tileID).isOccupied())
			{
				ImageIcon image = new ImageIcon("art/pieces/" + 
						board.getTile(tileID).getPiece().getAlliance().toString().substring(0, 1) +
						board.getTile(tileID).getPiece().toString() + ".png");
				JLabel label = new JLabel();
				label.setIcon(image);
				this.add(label);
			}
		}
		
		private void highlight()
		{			
			String path;
			if (!chessBoard.getTile(tileID).isOccupied())
			{ path = "art/misc/grayDot.png"; }
			else
			{ path = "art/attackedPieces/" + chessBoard.getTile(tileID).getPiece().getAlliance().toString().substring(0, 1) +
					chessBoard.getTile(tileID).getPiece().toString() + "-overlay.png"; }
			
			ImageIcon image = new ImageIcon(path);
			JLabel label = new JLabel();
			label.setIcon(image);
			this.removeAll();
			this.add(label);
		}
		
		public void change()
		{ this.setBackground(Color.yellow); }
	}
}





