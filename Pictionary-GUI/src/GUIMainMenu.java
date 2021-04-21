import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class GUIMainMenu implements Runnable{
	
	private GUI g;
	//This could also be a URL, but I think a 'game name' will be less intimidating for users
	protected String gameName;
	protected String uName;
	protected boolean validSettings = false;
	protected boolean host = false;
	protected String word = "MISSING WORD";

	@Override
	public void run() {
		
		//TODO make pretty lines draw across the screen outside the button area, or something
		
		JFrame menuFrame = new JFrame("Not Pictionary (for legal reasons) Pictionary");
		menuFrame.setResizable(false);
		menuFrame.setSize(new Dimension(420,420));
		menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		menuFrame.setVisible(true);
		
		//This is where all the action happens.
		JPanel mainMenu = new JPanel(new GridBagLayout());
		final JButton beginGame = new JButton("Host New Game");
		final JButton joinAGame = new JButton("Join Existing Game");
		final String select = "(choose a username)";
		JTextField uNameField = new JTextField(select);
		JTextField urlField = new JTextField("(enter game name)");
		final JButton startButton = new JButton("START");
		final JButton prevButton = new JButton("BACK");
		final Insets inset = new Insets(5,5,5,5);
		
		beginGame.addActionListener(actionEvent -> {
			beginGame.setVisible(false);
			joinAGame.setVisible(false);
			uNameField.setVisible(true);
			urlField.setVisible(true);
			startButton.setVisible(true);
			prevButton.setVisible(true);
			host = true;
		});
		beginGame.setPreferredSize(new Dimension(300, 45));
		GridBagConstraints beginConst = new GridBagConstraints();
		beginConst.gridx = 0;
		beginConst.gridy = 0;
		beginConst.insets = inset;
		
		joinAGame.addActionListener(actionEvent -> {
			beginGame.setVisible(false);
			joinAGame.setVisible(false);
			uNameField.setVisible(true);
			urlField.setVisible(true);
			startButton.setVisible(true);
			prevButton.setVisible(true);
			host = false;
		});
		joinAGame.setPreferredSize(new Dimension(300, 45));
		GridBagConstraints joinAConst = new GridBagConstraints();
		joinAConst.gridx = 0;
		joinAConst.gridy = 1;
		joinAConst.insets = inset;
		
		startButton.addActionListener(actionEvent -> {
			uName = uNameField.getText();
			if(uName.equals(select) || uName.equals("")) uName = "noname"+((int)(Math.random()*1000));
			if(!urlField.getText().equals("(enter game name)")) {
				gameName = urlField.getText();
				int count = 0;
				while(!host && !validSettings && count < 2500) {
					count++;
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(count < 1000) g = new GUI(uName, true, host, word);
				else {
					if(!host) JOptionPane.showMessageDialog(mainMenu, "Could not locate game.");
					else JOptionPane.showMessageDialog(mainMenu, "Failed to create game.");
				}
			}
			else {
				JOptionPane.showMessageDialog(mainMenu, "You must enter a game name to start.");
			}
		});
		startButton.setPreferredSize(new Dimension(140, 45));
		GridBagConstraints startConst = new GridBagConstraints();
		startConst.gridx = 1;
		startConst.gridy = 3;
		startConst.insets = inset;
		startButton.setVisible(false);
		
		prevButton.addActionListener(actionEvent -> {
			startButton.setVisible(false);
			uNameField.setVisible(false);
			urlField.setVisible(false);
			joinAGame.setVisible(true);
			beginGame.setVisible(true);
			prevButton.setVisible(false);
		});
		prevButton.setPreferredSize(new Dimension(140, 45));
		GridBagConstraints prevConst = new GridBagConstraints();
		prevConst.gridx = 0;
		prevConst.gridy = 3;
		prevConst.insets = inset;
		prevButton.setVisible(false);
		
		uNameField.setPreferredSize(new Dimension(300, 30));
		uNameField.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				uNameField.setText("");
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				//Do nothing
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				//Do nothing
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				//Do nothing
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				//Do nothing
			}
			
		});
		GridBagConstraints nameConst = new GridBagConstraints();
		nameConst.gridx = 0;
		nameConst.gridy = 0;
		nameConst.gridwidth = 2;
		nameConst.insets = inset;
		uNameField.setVisible(false);
		
		urlField.setPreferredSize(new Dimension(300, 30));
		urlField.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				urlField.setText("");
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				//Do nothing
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				//Do nothing
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				//Do nothing
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				//Do nothing
			}
			
		});
		GridBagConstraints urlConst = new GridBagConstraints();
		urlConst.gridx = 0;
		urlConst.gridy = 1;
		urlConst.gridwidth = 2;
		urlConst.insets = inset;
		urlField.setVisible(false);
		
		mainMenu.add(beginGame, beginConst);
		mainMenu.add(joinAGame, joinAConst);
		mainMenu.add(uNameField, nameConst);
		mainMenu.add(urlField, urlConst);
		mainMenu.add(startButton, startConst);
		mainMenu.add(prevButton, prevConst);
		
		menuFrame.add(mainMenu);
		menuFrame.validate();
		menuFrame.repaint();
		
	}
	
	/**
	 * This method tells the game if it is good to go or not, and gives the controller one last chance
	 * to overwrite the username or the game name.
	 * @param g2g the boolean value that tells the GUI if it can run
	 * @param username the username the GUI will use
	 * @param game the game name
	 * @param gameWord the word the game begins with
	 */
	public void validateSettings(boolean g2g, String username, String game, String gameWord) {
		if(g2g) {
			uName = username;
			gameName = game;
			word = gameWord;
			validSettings = true;
		}
	}
	
	/**
	 * This method returns the GUI.
	 * @return
	 */
	public GUI getGUI() {
		return g;
	}

}
