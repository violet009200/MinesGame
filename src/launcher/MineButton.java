package launcher;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import logic.MinesGame;

/**
 * Initialize the tile used in the 
 * JMinesweeperBoardPanel
 *
 */
public class MineButton extends JButton{

	private static Image allImgs[] = new Image[12];
	
	private static boolean imgsIsSet = false;
	private static ImageIcon[] numImgList = null;
	private static ImageIcon[] mineImgList = null;
	private static ImageIcon flagImg 	= null;
	private static ImageIcon qImg 	= null;
	
	private static MinesGame theGame;
	private static boolean hasStarted;
	private static boolean bombsBeingRevealed = false;
	
	private boolean hasSetIcon = false, isOpen = false;
	private int imgIndex;
	private int markIndex = 0;// 0  = blank, 1 = flag, 2 = ?
	private final int xPos, yPos;

	/**
	 * Create a button who know it's only position
	 * on the board
	 * 
	 * @param x Row coordinate
	 * @param y Column coordinate
	 */
	public MineButton(int x, int y) {
		super();
		reset();
		hasSetIcon = false;
		isOpen = false;
		imgIndex = -1;
		xPos = x;
		yPos = y;
		bombsBeingRevealed =  false;
		if (!imgsIsSet)
			setImg();
	}
	
	/**
	 * Resets the static fields
	 */
	public static void reset(){
		hasStarted = false;
		bombsBeingRevealed = false;
	}

	/**
	 * Open the bombs in end game phase
	 */
	public void bOpen(){
		if (isOpen)
			return;
		if (bombsBeingRevealed && getValue() != 9){
			return;
		}
		if (markIndex > 0){
			return;
		}
		if (!hasStarted)
			MainFrame.startTimer();
		getModel().setPressed(true);
		getModel().setEnabled(false);
		isOpen = true;
		chooseIcon(true);
		//theGame.openSpot(xPos, yPos);
	}
	
	/**
	 * Standard opening of the board
	 * at this tile
	 */
	public void open(){
		if (isOpen)
			return;
		if (bombsBeingRevealed && getValue() != 9){
			return;
		}
		if (markIndex > 0){
			return;
		}
		if (!hasStarted)
			MainFrame.startTimer();
		getModel().setPressed(true);
		getModel().setEnabled(false);
		isOpen = true;
		chooseIcon(true);
		//System.out.println("(" + xPos + ", " + yPos + ")");
		theGame.openSpot(xPos, yPos);
	}
	
	/**
	 * Checks to see if the surrounding has the same
	 * number of flags as the number at this tile
	 * @return
	 */
	private boolean canSpecialOpen(){
		int i = getValue() - theGame.getNumFlagAround(xPos, yPos);
		return i == 0;
	}
	
	public boolean isFlaged(){
		return markIndex == 1;
	}
	
	/**
	 * Handles the simultaneous left-right click
	 * event
	 */
	public void specialOpen(){
		if (!isOpen)
			return;
		if (bombsBeingRevealed && getValue() != 9){
			return;
		}
		if (markIndex > 0){
			return;
		}
		getModel().setPressed(true);
		getModel().setEnabled(false);
		isOpen = true;
		chooseIcon(true);
		if (canSpecialOpen())
			theGame.openSurrounding(xPos, yPos);
	}
	
	/**
	 * Handle the right click event
	 */
	public void toggle(){
		if (bombsBeingRevealed || isOpen)
			return;
		if (markIndex == 0)
			MainFrame.changeFlagCount(true);
		else if (markIndex == 1)
			MainFrame.changeFlagCount(false);
		++markIndex;
		markIndex %= 3;
		/*
		if (markIndex != 0)
			getModel().setEnabled(false);
		else{
			getModel().setEnabled(true);
		}
		*/
		chooseIcon(false);
	}

	/**
	 * Used to attach the button to the 
	 * game logic
	 * @param temp
	 */
	public static void setGame(MinesGame temp) {
		theGame = temp;
	}
	
	/**
	 * 
	 * @param isOpening It being called from a function 
	 * 					in order to open the button
	 */
	public void chooseIcon(boolean isOpening){// should be called on click (or pressed)
		if (markIndex == 0 && isOpening)
			imgIndex = getValue();// for now
		else if (markIndex == 0){
			imgIndex = 0;
		}else{
			imgIndex = 9 + markIndex;
		}
		if (imgIndex == 9)
			bombsBeingRevealed = true;
		//this.setPressedIcon(mineImgList[0]);//idk why dis doesn't work
		hasSetIcon = imgIndex > 0;
		this.repaint();
		//hasSetIcon = false;
	}
	
	/**
	 * Used to draw the icon on top of
	 * the button 
	 * 
	 * @see Graphics
	 */
	@Override
	public void paint(Graphics g){
		super.paint(g);
		if (!hasSetIcon || imgIndex < 1)
			return;
		int x = this.getSize().width / 2 - allImgs[imgIndex].getWidth(null) / 2;
		int y = this.getSize().height / 2 - allImgs[imgIndex].getHeight(null) / 2;
		if (imgIndex > 0)
			g.drawImage(allImgs[imgIndex], x, y, this);
	}
	
	/**
	 * Get the value form the game logic about the
	 * value under the button
	 * @return The value
	 */
	public int getValue() {
		return theGame.getValue(xPos, yPos);
	}	
	
	/**
	 * Used to load the images from resources
	 */
	private static void setImg(){
		ClassLoader temp =	MineButton.class.getClassLoader();
		numImgList = new ImageIcon[8];
		
		for(int i=1; i<8; i++){
			numImgList[i] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					temp.getResource("images/"+ i +"s.gif")));
			allImgs[i] = numImgList[i].getImage();
		}
		
		mineImgList = new ImageIcon[2];
		
		mineImgList[0] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				temp.getResource("images/mine1.gif")));
		mineImgList[1] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				temp.getResource("images/mine2.gif")));
		//System.out.println(mineImgList[0]);
		
		allImgs[9] = mineImgList[0].getImage();
		
		flagImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				temp.getResource("images/flag.gif")));
		allImgs[10] = flagImg.getImage();
		qImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				temp.getResource("images/wildcard.gif")));
		allImgs[11] = qImg.getImage();
		
		imgsIsSet = true;
	}
}
