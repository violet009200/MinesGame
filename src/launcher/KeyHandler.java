package launcher;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Routes the mouse click events to the 
 * appropriate methods
 *
 */
public class KeyHandler extends MouseAdapter {
	private boolean isLeftDwn = false;// to use for two mouse press together
	private boolean hasClicked[] = new boolean[2];// to be reset at check
	
	
	@Override
	public void mouseClicked(MouseEvent e) {//combination of press release
		MineButton temp = (MineButton)e.getSource();
		if (e.getButton() == MouseEvent.BUTTON1){
			temp.open();
			//System.out.println("Left, clicked");
		}
		if (e.getButton() == MouseEvent.BUTTON3 && !isLeftDwn){// prevent confusion
			temp.toggle();
			//System.out.println("Right, clicked");
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {//combination of press release
		MineButton temp = (MineButton)e.getSource();
		if (e.getButton() == MouseEvent.BUTTON1){
			//System.out.println("Left, pressed");
			isLeftDwn= true;
		}
		if (e.getButton() == MouseEvent.BUTTON3){
			//System.out.println("Right, pressed");
			if (isLeftDwn){
				temp.specialOpen();//open for number with flags only
				return;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {//combination of press release
		if (e.getButton() == MouseEvent.BUTTON1){
			//System.out.println("Left, released");
			isLeftDwn = false;
		}
		if (e.getButton() == MouseEvent.BUTTON3){
			//System.out.println("Right, released");
			hasClicked[0] = true;
		}
	}

}
