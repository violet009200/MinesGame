package logic;

import java.util.Random;

import launcher.JMinesSweeperBoardPanel;

/**
 * Game logic of the Minesweeper game
 *
 */
public class MinesGame {
	private JMinesSweeperBoardPanel theGUI;
	private int numCols, numRows, numBombs, numSpacesLeft;
	private int ansGrid[][], gameState;//gameState = 0 still playing, 1 win, 2 lost
	boolean usrGrid[][];
	int xForBombs[], yForBombs[];
	private final int bombValue;
	private static boolean openingBombs;

	/**
	 * Create the logic for the MinesSweeper game 
	 * 
	 * @param cols Columns for the board
	 * @param rows Rows for the board
	 * @param bombs Number of bombs in the board
	 * @param daGUI GUI attached to this game
	 */
	public MinesGame(int cols, int rows, int bombs,
			JMinesSweeperBoardPanel daGUI) {
		openingBombs = true;
		theGUI = daGUI;
		numCols = cols;
		numRows = rows;
		numSpacesLeft = numRows * numCols;
		numBombs = bombs;
		ansGrid = new int[numCols][numRows];// initializes to zeros
		usrGrid = new boolean[numCols][numRows];// initializes to false
		//System.out.println(usrGrid[0][0]);
		bombValue = getBombValue();// 9 allows aligned print
		gameState = 0;
		genBoard();
		//System.out.println(xForBombs[0] + " " + yForBombs[0]);
		//printBoard();
	}

	/**
	 * (0,0) in the top left, y ascends down
	 * 
	 * @param x
	 *            The column
	 * @param y
	 *            The row
	 * @return
	 */
	public int getValue(int x, int y) {
		return ansGrid[x][y];
	}

	public static final int getBombValue() {
		return 9;
	}

	/**
	 * Returns if you lose
	 * 
	 * @param x
	 *            xPos
	 * @param y
	 *            yPos
	 * @return value at position
	 */
	public int openSpot(int x, int y) {
		if (usrGrid[x][y])
			return ansGrid[x][y];
		usrGrid[x][y] = true;
		--numSpacesLeft;
		theGUI.openSpot(x, y, false);
		if (ansGrid[x][y] == 9) {
			openBombs();
			if (gameState == 0)
				gameState = 2;
			else if (gameState == 1)
				return 9;
			else {
				//System.out.println("YOU LOSE!");
				theGUI.endGame(false);
			}
			return getBombValue();
		}
		if (ansGrid[x][y] == 0) {
			openSurrounding(x, y);
		}
		if (numSpacesLeft == numBombs){
			gameState = 1;
			openBombs();
			 if (!openingBombs){
				 //System.out.println("YOU WIN!");
				theGUI.endGame(true);
			 }
		}
		return ansGrid[x][y];
	}

	/**
	 * Opens all the bombs
	 */
	private void openBombs() {
		if(!openingBombs)
			return;
		int x = 0, y = 0;
		for (int i = 0; i < numBombs; ++i) {
			x = xForBombs[i];
			y = yForBombs[i];
			theGUI.openSpot(x, y,
					true);
		}
		openingBombs = false;
		usrGrid[x][y] = false;
		openSpot(x, y);
	}
	
	/**
	 * Used to check if bomb was already placed at this location
	 * 
	 * @param x X coordinate for the to-be-place bomb 
	 * @param y Y coordinate for the to-be-place bomb
	 * @param len Current of the length
	 * @return
	 */
	private boolean alreadySet(int x, int y, int len){
		for (int i = 0; i < len; ++i){
			if (xForBombs[i] == x && yForBombs[i] == y)
				return true;
		}
		return false;
	}

	/**
	 * Generate board for the gameplay
	 */
	private void genBoard() {
		xForBombs = new int[numBombs];
		yForBombs = new int[numBombs];
		Random temp = new Random();
		for (int i = 0; i < numBombs; ) {
			int x = temp.nextInt(numBombs);
			int y = temp.nextInt(numBombs);
			if (!alreadySet(x, y, i)){
				xForBombs[i] = x;
				yForBombs[i] = y;
			}
			else continue;//skip ++i
			++i;
		}
		for (int i = 0; i < numBombs; ++i) {

			ansGrid[xForBombs[i]][yForBombs[i]] = bombValue;// bombs are
															// negative 1
			setSurrounding(xForBombs[i], yForBombs[i]);
		}
	}

	/**
	 * Prints the answers board to the
	 * console
	 */
	private void printBoard() {
		for (int y = 0; y < numRows; ++y) {
			for (int x = 0; x < numCols; ++x) {
				System.out.print(ansGrid[x][y] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Checks for out of bounds coordinates
	 * 
	 * @param x X coordinate of the point
	 * @param y Y coordinate of the point
	 * @return True if valid, false otherwise
	 */
	private boolean isInbounds(int x, int y) {
		if (x < 0 || y < 0)
			return false;
		if (x >= numCols || y >= numRows)
			return false;
		return true;
	}
	
	/**
	 * Returns the number of flags around the point
	 * 
	 * @param x X coordinate of the point
	 * @param y Y coordinate of the point
	 * @return Number of flags
	 */
	public int getNumFlagAround(int x, int y){
		int total = 0;
		if (isInbounds(x - 1, y - 1)) {// top left
				total += theGUI.isFlagged(x -1, y - 1)? 1 : 0;
		}
		if (isInbounds(x, y - 1)) {// top
				total += theGUI.isFlagged(x, y - 1)? 1 : 0;
		}
		if (isInbounds(x + 1, y - 1)) {// top right
				total += theGUI.isFlagged(x + 1, y - 1)? 1 : 0;
		}
		if (isInbounds(x + 1, y)) {// right
				total += theGUI.isFlagged(x + 1, y)? 1 : 0;
		}
		if (isInbounds(x + 1, y + 1)) {// bot right
				total += theGUI.isFlagged(x + 1, y + 1)? 1 : 0;
		}
		if (isInbounds(x, y + 1)) {// bot
				total += theGUI.isFlagged(x, y + 1)? 1 : 0;
		}
		if (isInbounds(x - 1, y + 1)) {// bot left
				total += theGUI.isFlagged(x - 1, y + 1)? 1 : 0;
		}
		if (isInbounds(x - 1, y)) {// left
				total += theGUI.isFlagged(x - 1, y)? 1 : 0;
		}
		//System.out.println(total);
		return total;
	}

	/**
	 * Handles the opening of the point specified
	 * @param x X coordinate of the point
	 * @param y Y coordinate of the point
	 */
	public void openSurrounding(int x, int y) {
		// checks out of bounds
		if (isInbounds(x - 1, y - 1) && !usrGrid[x - 1][y - 1] && !theGUI.isFlagged(x - 1, y - 1)) {// top left
			openSpot(x - 1, y - 1);
		}
		if (isInbounds(x, y - 1) && !usrGrid[x][y - 1] && !theGUI.isFlagged(x, y - 1)) {// top
			openSpot(x, y - 1);
		}
		if (isInbounds(x + 1, y - 1) && !usrGrid[x + 1][y - 1] && !theGUI.isFlagged(x + 1, y - 1)) {// top right
			openSpot(x + 1, y - 1);
		}
		if (isInbounds(x + 1, y) && !usrGrid[x + 1][y] && !theGUI.isFlagged(x + 1, y)) {// right
			openSpot(x + 1, y);
		}
		if (isInbounds(x + 1, y + 1) && !usrGrid[x + 1][y + 1] && !theGUI.isFlagged(x + 1, y + 1)) {// bot right
			openSpot(x + 1, y + 1);
		}
		if (isInbounds(x, y + 1) && !usrGrid[x][y + 1] && !theGUI.isFlagged(x, y + 1)) {// bot
			openSpot(x, y + 1);
		}
		if (isInbounds(x - 1, y + 1) && !usrGrid[x - 1][y + 1] && !theGUI.isFlagged(x - 1, y + 1)) {// bot left
			openSpot(x - 1, y + 1);
		}
		if (isInbounds(x - 1, y) && !usrGrid[x - 1][y] && !theGUI.isFlagged(x - 1, y)) {// left
			openSpot(x - 1, y);
		}
	}

	/**
	 * Used to initialize the area around the 
	 * bomb
	 * @param x X coordinate of bomb
	 * @param y Y coordinate of bomb
	 */
	private void setSurrounding(int x, int y) {
		// checks out of bounds
		if (isInbounds(x - 1, y - 1) && !usrGrid[x][y]) {// top left
			if (ansGrid[x - 1][y - 1] != bombValue) {
				ansGrid[x - 1][y - 1] += 1;
			}
		}
		if (isInbounds(x, y - 1) && !usrGrid[x][y]) {// top
			if (ansGrid[x][y - 1] != bombValue) {
				ansGrid[x][y - 1] += 1;
			}
		}
		if (isInbounds(x + 1, y - 1) && !usrGrid[x][y]) {// top right
			if (ansGrid[x + 1][y - 1] != bombValue) {
				ansGrid[x + 1][y - 1] += 1;
			}
		}
		if (isInbounds(x + 1, y) && !usrGrid[x][y]) {// right
			if (ansGrid[x + 1][y] != bombValue) {
				ansGrid[x + 1][y] += 1;
			}
		}
		if (isInbounds(x + 1, y + 1) && !usrGrid[x][y]) {// bot right
			if (ansGrid[x + 1][y + 1] != bombValue) {
				ansGrid[x + 1][y + 1] += 1;
			}
		}
		if (isInbounds(x, y + 1) && !usrGrid[x][y]) {// bot
			if (ansGrid[x][y + 1] != bombValue) {
				ansGrid[x][y + 1] += 1;
			}
		}
		if (isInbounds(x - 1, y + 1) && !usrGrid[x][y]) {// bot left
			if (ansGrid[x - 1][y + 1] != bombValue) {
				ansGrid[x - 1][y + 1] += 1;
			}
		}
		if (isInbounds(x - 1, y) && !usrGrid[x][y]) {// left
			if (ansGrid[x - 1][y] != bombValue) {
				ansGrid[x - 1][y] += 1;
			}
		}
	}
}
