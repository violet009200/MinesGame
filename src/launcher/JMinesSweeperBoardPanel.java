package launcher;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JPanel;

import logic.MinesGame;

/**
 * Handles the GUI for the MinesSweeper game
 *
 */
public class JMinesSweeperBoardPanel extends JPanel{
	private final int numRows, numCols;
	private final int numBombs;
	private final int buttonSize;
	private final int pixSpacing;
	private final int pWidth, pHeight;
	private MainFrame parent;
	private MineButton board[][];
	private KeyHandler mouseListener;
	
	/**
	 * Initialize the references and the game logic 
	 * 
	 * @param p Parent MainFrame
	 */
	public JMinesSweeperBoardPanel(MainFrame p){
		super();
		parent = p;
		numRows = 10;
		numCols = 10;
		numBombs = 10;
		buttonSize = 50;
		pixSpacing = 1; // zero or greater
		pWidth = (buttonSize + pixSpacing) * numCols + pixSpacing;
		pHeight = (buttonSize + pixSpacing) * numRows + pixSpacing;
		mouseListener = new KeyHandler();
		board = new MineButton[numCols][numRows];// (x, y)
		MineButton.setGame(new MinesGame(numRows, numCols, numBombs, this));
		this.setLayout(null);
		this.setPreferredSize(new Dimension(pWidth, pHeight));
		addSquares();
	}
	
	/**
	 * Create the array of buttons
	 */
	private void addSquares(){
		MineButton aButton;
		int xLoc, yLoc;
		for(int y = 0; y < numRows; y++){
			yLoc = pixSpacing + (buttonSize + pixSpacing) * y;
			for(int x = 0; x < numCols; x++){
				aButton = new MineButton(x, y);
				aButton.addMouseListener(mouseListener);
				xLoc = pixSpacing + (buttonSize + pixSpacing) * x;
				aButton.setLocation(xLoc, yLoc); // relative location to msBoardPanel
				aButton.setSize(buttonSize, buttonSize);
				aButton.setMargin(new Insets(5, 5, 5, 5));// centers text
				board[x][y] = aButton;
				this.add(aButton);
			}
		}
	}
	
	/**
	 * Used to pipe the end game phase to
	 * the parent
	 * @param hasWon True if won, false otherwise
	 */
	public void endGame(boolean hasWon){
		parent.handleEndGame(hasWon);
	}
	
	/**
	 * Returns whether the spot is flagged right now
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isFlagged(int x, int y){
		return board[x][y].isFlaged();
	}
	
	/**
	 * Handles the opening of the of
	 * the logic board from the logic
	 * to GUI side
	 * 
	 * @param x
	 * @param y
	 * @param bombsOpening
	 */
	public void openSpot(int x, int y, boolean bombsOpening){
		if (bombsOpening)
			board[x][y].bOpen();//no else allows the end in case it didn't catch the end
		board[x][y].open();
	}
}
