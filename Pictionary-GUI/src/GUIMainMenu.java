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

public class GUIMainMenu extends JFrame {
	
	/**
	 * Java wanted this.
	 */
	private static final long serialVersionUID = -6150947612286163108L;
	private GUI g;
	//This could also be a URL, but I think a 'game name' will be less intimidating for users
	protected String gameName = "";
	protected String uName = "";
	protected String word = "MISSING WORD";
	protected boolean host = false;
	
	private JPanel mainMenu;
	
	public GUIMainMenu() {
		//TODO make pretty lines draw across the screen outside the button area, or something
		
		JFrame menuFrame = new JFrame("Not Pictionary (for legal reasons) Pictionary");
		menuFrame.setResizable(false);
		menuFrame.setSize(new Dimension(420,420));
		menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		menuFrame.setVisible(true);
		
		//This is where all the action happens.
		mainMenu = new JPanel(new GridBagLayout());
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
			gameName = urlField.getText();
			if(uName.equals(select) || uName.equals("")) uName = "noname"+((int)(Math.random()*1000));
			if(!urlField.getText().equals("(enter game name)") && !urlField.getText().equals("")) {
				gameName = urlField.getText();
			}
			else {
				JOptionPane.showMessageDialog(mainMenu, "You must enter a game name to start.");
			}
		});
		startButton.setPreferredSize(new Dimension(145, 45));
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
		prevButton.setPreferredSize(new Dimension(145, 45));
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
	 * 
	 * @return
	 */
	public GUI makeGUI(String username, String word, boolean hosting) {
		g = null;
		g = new GUI(username, true, hosting, word);
		gameName = "";
		return g;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasGUI() {
		return g == null;
	}
	
	/**
	 * 
	 * @return
	 */
	public GUI getGUI() {
		return g;
	}
	
	/**
	 * Used to alert the player that the game name was not valid.
	 */
	public void couldNotFindGame() {
		JOptionPane.showMessageDialog(mainMenu, "Could not find game.");
		gameName = "";
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasGame() {
		return !gameName.equals("") && !gameName.equals("(enter game name)");
	}
	
	/**
	 * Removes the game.
	 */
	public void removeGame() {
		gameName = "";
		g = null;
	}

}
