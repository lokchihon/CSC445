import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
	
	private int xAt;
	private int yAt;
	private int xWas;
	private int yWas;
	private boolean seen = false;
	private JPanel mainMenu;
	private int color = 0;
	
	public GUIMainMenu() {
		
		JFrame menuFrame = new JFrame("Not Pictionary (for legal reasons) Pictionary");
		menuFrame.setResizable(false);
		menuFrame.setSize(new Dimension(420,420));
		menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		menuFrame.setVisible(true);
		
		if(((int)(Math.random()*10))>=5) {
			xAt = (int)(Math.random()*60)+360;
			yAt = (int)(Math.random()*150)+270;
		}
		else {
			xAt = (int)(Math.random()*60);
			yAt = (int)(Math.random()*150);
		}

		//This is where all the action happens.
		mainMenu = new JPanel(new GridBagLayout()){
			//This, specifically, is the pretty line drawer.
			/**
			 * Java wanted this.
			 */
			private static final long serialVersionUID = 3792653185561200410L;

			@Override
            public void paintComponents(Graphics g){
				Color c;
				switch (color%6) {
					case 0:{
						c = Color.RED;
						break;
					}
					case 1:{
						c = Color.ORANGE;
						break;
					}
					case 2:{
						c = Color.YELLOW;
						break;
					}
					case 3:{
						c = Color.GREEN;
						break;
					}
					case 4:{
						c = Color.BLUE;
						break;
					}
					default:{
						c = Color.MAGENTA;
						break;
					}
				}
				color++;
                g.setColor(c);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                xWas = xAt;
                xAt = generateX();
                yWas = yAt;
                yAt = generateY();

                g2.drawLine(xAt, yAt, xWas, yWas);
                
            }
			
        };
        mainMenu.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				//Do nothing
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				mainMenu.paintComponents(mainMenu.getGraphics());
			}
        	
        });
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
				if(urlField.getText().equals("")) urlField.setText("(enter game name)"); 
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
		uNameField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				if(seen==true && uNameField.getText().equals("(choose a username)")) uNameField.setText("");
				seen = true;
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if(uNameField.getText().equals("")) uNameField.setText("(choose a username)");
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
				if(uNameField.getText().equals("")) uNameField.setText("(choose a username)");
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
		urlField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				if(urlField.getText().equals("(enter game name)")) urlField.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if(urlField.getText().equals("")) urlField.setText("(enter game name)");
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
		
		bePretty();
	}

	public GUI makeGUI(String username, String word, boolean hosting, MultiClient client) {
		g = null;
		client.setGameName(gameName);
		client.setIsHost(hosting);
		g = new GUI(username, true, hosting, word, client);
		gameName = "";
		return g;
	}

	public boolean hasGUI() {
		return g == null;
	}

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
	 * Used to tell the username is bad.
	 */
	public void invalidUsername() {
		JOptionPane.showMessageDialog(mainMenu, "Username invalid.");
		uName = "";
	}
	

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
	
	private int generateX() {
		
        int xAdd = (int)(Math.random()*30);
        int xPosNeg = (int)(Math.random()*2);
        if(xPosNeg>=1) xAdd*=-1;
        int retVal = xAdd+xAt;
        
        if(retVal > 0 && retVal < 420) {
        	if((120 < yAt && yAt < 270) && (60 < retVal && retVal < 360)) {
        		if(((int)(Math.random()*10))>=5) {
        			xWas = (int)(Math.random()*60)+360;
        			return xWas;
        		}
        		else {
        			xWas = (int)(Math.random()*60);
        			return xWas;
        		}
        	}
        	else return retVal;
        }
        else return generateX();
	}
	
	private int generateY() {
        
        int yAdd = (int)(Math.random()*30);
        int yPosNeg = (int)(Math.random()*2);
        if(yPosNeg>=1) yAdd*=-1;
        int retVal = yAdd+yAt;
        
        if(retVal > 0 && retVal < 420) {
        	if((60 < xAt && xAt < 360) && (120 < retVal && retVal < 270)) {
        		if(((int)(Math.random()*10))>=5) {
        			yWas = (int)(Math.random()*120)+270;
        			return yWas;
        		}
        		else {
        			yWas = (int)(Math.random()*120);
        			return yWas;
        		}
        	}
        	else return retVal;
        }
        else return generateY();
	}
	
	/**
	 * Call this every x00 miliseconds or so to paint pretty lines. 
	 */
	public void bePretty() {
		mainMenu.paintComponents(mainMenu.getGraphics());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

}
