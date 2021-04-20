import java.awt.Dimension;

import javax.swing.JFrame;

public class GUIMainMenu implements Runnable{

	@Override
	public void run() {
		
		JFrame menuFrame = new JFrame("Not Pictionary (for legal reasons) Pictionary");
		menuFrame.setResizable(false);
		menuFrame.setSize(new Dimension(500, 500));
		menuFrame.setVisible(true);
		
	}
	
	

}
