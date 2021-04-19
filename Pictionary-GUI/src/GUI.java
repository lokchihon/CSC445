import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GUI implements Runnable{

    private Color color;
    private int colorCode;
    private int brushSize = 10;
    private int posX, pPosX;
    private int posY, pPosY;
    private int chatQueueIndex = 0;
    private JComponent[] buttonPanelArray = new JComponent[11];
    private JList<Object> chatList = new JList<>();
    private ArrayList<String> chatLog = new ArrayList<>();
    private ArrayList<DrawData> paintLog = new ArrayList<>();
    private JPanel canvas;
    private JFrame paintingWindow;
    private JScrollPane chatScrollPane;
    private boolean isPainter = false;
    private String theWord = "MISSING WORD";
    private JPanel wordsPanel;
    private JLabel word;
    private int secondsLeft = 300;
    private JLabel timeLabel;
    private JPanel timerPanel;
    private int points = 0;
    private JPanel pointsPanel;
    private JLabel pointsLabel;
    private long timeGuessed = Long.MAX_VALUE;
    
    protected String username;
    
    protected static final int RED = 0;
    protected static final int ORANGE = 1;
    protected static final int YELLOW = 2;
    protected static final int GREEN = 3;
    protected static final int BLUE = 4;
    protected static final int MAGENTA = 5;
    protected static final int BLACK = 6;
    protected static final int ERASE = 7;

    /**
     * This method runs the game panel. 
     */
	@Override
	public void run() {

		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(5000);

        paintingWindow = new JFrame("Not Pictionary (for legal reasons) Pictionary");
        paintingWindow.setLayout(new BorderLayout());
        //I did this because we need to be able to refer to the same points across each client. Same window = same co-ords
        paintingWindow.setResizable(false);
        color = Color.BLACK;
        
        //This method makes the canvas. In theory, it also can clear it.
		canvas = new JPanel(){

			/**
			 * Java wanted this I guess
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void paintComponents(Graphics g){
                g.setColor(color);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                DrawData d = new DrawData(new int[]{pPosX, pPosY, posX, posY}, brushSize, colorCode);
                paintLog.add(d);
                g2.drawLine(posX, posY, pPosX, pPosY);
            }
			
        };
        
        canvas.setSize(1165, 850);
        canvas.setBackground(Color.WHITE);
        
        //This is the section that listens to the mouse actions while moving. 
        canvas.addMouseMotionListener(new MouseMotionListener() {
            //This listener is for when the mouse is clicked and moving. 
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                System.out.println("DRAGGING "+mouseEvent.getX()+":"+mouseEvent.getY());
                pPosX = posX;
                pPosY = posY;
                posX = mouseEvent.getX();
                posY = mouseEvent.getY();
                if(isPainter) canvas.paintComponents(canvas.getGraphics());
            }

            //This listener is for when the mouse is moving while not clicked.
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                pPosX = posX;
                pPosY = posY;
                posX = mouseEvent.getX();
                posY = mouseEvent.getY();
            }
        });
        
        //This handles a user clicking on a point to draw something.
        canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
                pPosX = posX;
                pPosY = posY;
                if(isPainter) canvas.paintComponents(canvas.getGraphics());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				//NOT USED
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				//NOT USED
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				//NOT USED
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				//NOT USED
			}
        	
        });
        
        
        //Here we create the buttons for the player to select the color.
        int colorButtonWidth = 25;
        int colorButtonHeight = 25;
        JButton redButton = new JButton();
        redButton.setToolTipText("RED");
        try {
			redButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(
			        GUI.class.getClassLoader().getResource("red.png")))));
		} catch (IOException e) {
			System.out.println("Failed to load red.png");
			e.printStackTrace();
		}
        redButton.setPreferredSize(new Dimension(colorButtonWidth, colorButtonHeight));

        JButton orangeButton = new JButton();
        orangeButton.setToolTipText("ORANGE");
        try {
			orangeButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(
			        GUI.class.getClassLoader().getResource("orange.png")))));
		} catch (IOException e) {
			System.out.println("Failed to load orange.png");
			e.printStackTrace();
		}
        orangeButton.setPreferredSize(new Dimension(colorButtonWidth, colorButtonHeight));

        JButton yellowButton = new JButton();
        yellowButton.setToolTipText("YELLOW");
        try {
			yellowButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(
			        GUI.class.getClassLoader().getResource("yellow.png")))));
		} catch (IOException e) {
			System.out.println("Failed to load yellow.png");
			e.printStackTrace();
		}
        yellowButton.setPreferredSize(new Dimension(colorButtonWidth, colorButtonHeight));
        
        JButton greenButton = new JButton();
        greenButton.setToolTipText("GREEN");
        try {
			greenButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(
			        GUI.class.getClassLoader().getResource("green.png")))));
		} catch (IOException e) {
			System.out.println("Failed to load green.png");
			e.printStackTrace();
		}
        greenButton.setPreferredSize(new Dimension(colorButtonWidth, colorButtonHeight));
        
        JButton blueButton = new JButton();
        blueButton.setToolTipText("BLUE");
        try {
			blueButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(
			        GUI.class.getClassLoader().getResource("blue.png")))));
		} catch (IOException e) {
			System.out.println("Failed to load blue.png");
			e.printStackTrace();
		}
        blueButton.setPreferredSize(new Dimension(colorButtonWidth, colorButtonHeight));
        
        JButton magentaButton = new JButton();
        magentaButton.setToolTipText("VIOLET");
        try {
			magentaButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(
			        GUI.class.getClassLoader().getResource("magenta.png")))));
		} catch (IOException e) {
			System.out.println("Failed to load magenta.png");
			e.printStackTrace();
		}
        magentaButton.setPreferredSize(new Dimension(colorButtonWidth, colorButtonHeight));

        JButton blackButton = new JButton();
        blackButton.setToolTipText("BLACK");
        try {
			blackButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(
			        GUI.class.getClassLoader().getResource("black.png")))));
		} catch (IOException e) {
			System.out.println("Failed to load black.png");
			e.printStackTrace();
		}
        blackButton.setPreferredSize(new Dimension(colorButtonWidth, colorButtonHeight));

        JButton eraseButton = new JButton("ERASE");
        JButton clearButton = new JButton("CLEAR");
        
        //We make the buttons functional here.
        redButton.addActionListener(actionEvent -> {
        	color = Color.RED;
        	colorCode = GUI.RED;
        });
        orangeButton.addActionListener(actionEvent -> {
        	color = Color.ORANGE;
        	colorCode = GUI.ORANGE;
        });
        yellowButton.addActionListener(actionEvent -> {
        	color = Color.YELLOW;
        	colorCode = GUI.YELLOW;
        });
        greenButton.addActionListener(actionEvent -> {
        	color = Color.GREEN;
        	colorCode = GUI.GREEN;
        });
        blueButton.addActionListener(actionEvent -> {
        	color = Color.BLUE;
        	colorCode = GUI.BLUE;
        });
        magentaButton.addActionListener(actionEvent -> {
        	color = Color.MAGENTA;
        	colorCode = GUI.MAGENTA;
        });
        blackButton.addActionListener(actionEvent -> {
        	color = Color.BLACK;
        	colorCode = GUI.BLACK;
        });
        eraseButton.addActionListener(actionEvent -> {
        	color = canvas.getBackground();
        	colorCode = GUI.ERASE;
        });
        clearButton.addActionListener(actionEvent -> clearCanvas());
        
        JScrollBar slize = new JScrollBar();
        slize.setPreferredSize(new Dimension(225, 25));
        slize.setOrientation(JSlider.HORIZONTAL);
        slize.setValue(10);
        slize.setToolTipText(slize.getValue()+"");
        slize.addAdjustmentListener(actionEvent -> {
        	brushSize = slize.getValue();
            slize.setToolTipText(slize.getValue()+"");
        });
        
        //This adds the drawing controls into the button panel and buttonPanelArray.
        JPanel buttonPanel = new JPanel(new FlowLayout(30, 10, 5));
        buttonPanel.setPreferredSize(new Dimension(1165, 35));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(new JLabel("   "));
        buttonPanel.add(redButton);
        buttonPanelArray[0] = redButton;
        buttonPanel.add(orangeButton);
        buttonPanelArray[1] = orangeButton;
        buttonPanel.add(yellowButton);
        buttonPanelArray[2] = yellowButton;
        buttonPanel.add(greenButton);
        buttonPanelArray[3] = greenButton;
        buttonPanel.add(blueButton);
        buttonPanelArray[4] = blueButton;
        buttonPanel.add(magentaButton);
        buttonPanelArray[5] = magentaButton;
        buttonPanel.add(blackButton);
        buttonPanelArray[6] = blackButton;
        buttonPanel.add(new JLabel("   "));
        buttonPanel.add(eraseButton);
        buttonPanelArray[7] = eraseButton;
        buttonPanel.add(new JLabel("   "));
        JLabel sizeLabel = new JLabel("Brush Size:");
        buttonPanel.add(sizeLabel);
        buttonPanelArray[8] = sizeLabel;
        buttonPanel.add(slize);
        buttonPanelArray[9] = slize;
        buttonPanel.add(new JLabel("   "));
        buttonPanel.add(clearButton);
        buttonPanelArray[10] = clearButton;

        //This panel holds the word being drawn and the timer.
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        wordsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wordsPanel.setBackground(Color.LIGHT_GRAY);
        JLabel padding = new JLabel("    ");
        giveWord(isPainter, theWord);
        /*
        if(isPainter) word = new JLabel(theWord);
        else {
        	String blanks = "";
        	for(int q = 0; q < theWord.length(); q++) {
        		blanks += "__ ";
        	}
        	word = new JLabel(blanks);
        }
        */
        
        word.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        wordsPanel.add(padding);
        wordsPanel.add(word);
        wordsPanel.setPreferredSize(new Dimension(500, 60));
        
        timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updateTimer(300);
        timeLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        timerPanel.setBackground(Color.LIGHT_GRAY);
        timerPanel.setPreferredSize(new Dimension(100, 60));
        timerPanel.add(timeLabel);
        
        pointsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        padding = new JLabel("                                ");
        pointsLabel = new JLabel("Points: "+points);
        pointsLabel.setFont(new Font("Comis Sans MS", Font.PLAIN, 30));
        pointsPanel.setBackground(Color.LIGHT_GRAY);
        pointsPanel.setPreferredSize(new Dimension(350, 60));
        pointsPanel.add(padding);
        pointsPanel.add(pointsLabel);

        dataPanel.add(wordsPanel);
        dataPanel.add(timerPanel);
        dataPanel.add(pointsPanel);
        dataPanel.setBackground(Color.LIGHT_GRAY);
        dataPanel.setPreferredSize(new Dimension(885, 60));
        
        //This panel holds the chat information.
        JPanel chatPanel = new JPanel(new GridBagLayout());
        chatScrollPane = new JScrollPane(chatList);
        chatScrollPane.setPreferredSize(new Dimension(300, 775-100));
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;
        scrollConstraints.insets = new Insets(-10, 10, 10, 10);
        scrollConstraints.gridheight = 19;
        chatPanel.add(chatScrollPane, scrollConstraints);
        
        JTextField input = new JTextField();
        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 20;
        inputConstraints.gridheight = 1;
        input.setPreferredSize(new Dimension(300, 40));
        input.addActionListener(actionEvent -> {
        	addChat(username, input.getText());
        	input.setText("");
        });
        chatPanel.add(input, inputConstraints);
        
        chatPanel.setBackground(Color.LIGHT_GRAY);
        
        //This panel is to prevent accidental outside clicks on the outside of the window.
        JPanel westBumperPanel = new JPanel();
        westBumperPanel.setBackground(Color.LIGHT_GRAY);
        westBumperPanel.setPreferredSize(new Dimension(30, 775));
        
        //This is all the necessary adding in of everything.
        canvas.setVisible(true);
        westBumperPanel.setVisible(true);
        chatPanel.setVisible(true);
        dataPanel.setVisible(true);
        buttonPanel.setVisible(true);
        if(!isPainter) setIsDrawer(isPainter);
        paintingWindow.add(dataPanel, BorderLayout.NORTH);
        paintingWindow.add(buttonPanel, BorderLayout.SOUTH);
        paintingWindow.add(chatPanel, BorderLayout.EAST);
        paintingWindow.add(westBumperPanel, BorderLayout.WEST);
        paintingWindow.add(canvas, BorderLayout.CENTER);
        paintingWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        paintingWindow.setSize(canvas.getSize());
        paintingWindow.repaint();
        paintingWindow.setVisible(true);
		
	}
	
	/**
	 * This method tells the GUI if the user is the drawer or not.
	 * As a consequence, this also determines if the drawing controls are visible.
	 * @param canSee true if player is drawing, else false
	 */
	private void setIsDrawer(boolean canSee) {
		
		isPainter = canSee;
		for(JComponent c : buttonPanelArray) {
			c.setVisible(canSee);
		}
		
	}
	
	/**
	 * This method turns on and off the tooltips for the color buttons.
	 * @param labelOn true if player would like the colors to be labeled with tooltips, else false
	 */
	public void triggerColorLabels(boolean labelOn) {
		
		if(labelOn) {
			for(int q = 0; q < 7; q++) {
				ToolTipManager.sharedInstance().registerComponent(buttonPanelArray[q]);
			}
		}
		else {
			for(int q = 0; q < 7; q++) {
				ToolTipManager.sharedInstance().unregisterComponent(buttonPanelArray[q]);
			}
		}
		
	}
	
	/**
	 * Clears the canvas.
	 */
	public void clearCanvas() {
		Graphics g = canvas.getGraphics();
		g.setColor(canvas.getBackground());
		g.fillRect(0, 0, 1165, 850);
	}
	
	/**
	 * This method takes in a string to be displayed in the chat box. Keeps the chat log at size 512 max.
	 * @param uName the username of the player who sent the chat
	 * @param chatText the chat string to be displayed
	 * @return true if the chat contained the word being guessed.
	 */
	public boolean addChat(String uName, String chatText) {

		/*
		 * This was supposed to censor the word, but prevented any correct guesses. 
		if(chatText.toLowerCase().contains(theWord.toLowerCase())) {
			String lastHalf = chatText.substring(chatText.toLowerCase().indexOf(theWord.toLowerCase())+theWord.length());
			String firstHalf = chatText.substring(0, chatText.toLowerCase().indexOf(theWord.toLowerCase()));
			chatText = firstHalf+"****"+lastHalf;
		}
		*/
		boolean retVal = false;
		
		if(!chatText.toLowerCase().contains(theWord.toLowerCase())) chatLog.add(uName+": "+chatText);
		else {
			if(uName.equals(username)) timeGuessed = System.currentTimeMillis();
			System.out.println(uName+" guessed the word!");
			chatLog.add(uName+" guessed the word!");
			retVal =  true;
		}
		if(chatLog.size()>512) {
			chatLog.remove(0);
			if(chatQueueIndex > 0) chatQueueIndex--;
		}
		Object[] chats = chatLog.toArray();
		chatList.setListData(chats);
		chatScrollPane.validate();
    	chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum());
    	return retVal;
    	
	}
	
	/**
	 * This method will return the oldest chat.
	 * @return the oldest chat message
	 */
	public String getChat() {
		if(chatQueueIndex < chatLog.size()) return chatLog.get(chatQueueIndex++);
		else return "";
	}
	
	/**
	 * This method paints to the GUI from outside the GUI. Used to update the canvas when another player paints.
	 * @param xPrev the previous x
	 * @param yPrev
	 * @param xCurr
	 * @param yCurr
	 * @param bSize
	 * @param color
	 */
	public void drawOnMe(int xPrev, int yPrev, int xCurr, int yCurr, int bSize, int color) {
		
		posX = xCurr;
		posY = yCurr;
		pPosX = xPrev;
		pPosY = yPrev;
		brushSize = bSize;
		if(color == GUI.RED) this.color = Color.RED;
		if(color == GUI.ORANGE) this.color = Color.ORANGE;
		if(color == GUI.YELLOW) this.color = Color.YELLOW;
		if(color == GUI.GREEN) this.color = Color.GREEN;
		if(color == GUI.BLUE) this.color = Color.BLUE;
		if(color == GUI.MAGENTA) this.color = Color.MAGENTA;
		if(color == GUI.BLACK) this.color = Color.BLACK;
		if(color == GUI.ERASE) this.color = canvas.getBackground();
		canvas.paintComponents(canvas.getGraphics());
		
	}
	
	/**
	 * Returns the oldest brush stroke as DrawData objects until the log is empty, then returns null.
	 * @return the DrawData object if there is one, else null
	 */
	public DrawData getBrushStrokes() {
		if(paintLog.size() > 0) return paintLog.remove(0);
		else return null;
	}
	
	/**
	 * Checks the status of the player's ability to paint and sets the word being guessed / drawn. Has a limit of 14 characters.
	 * @param painter the status of the player
	 * @param theSuperSecretAwesomeKeyword
	 */
	public void giveWord(boolean painter, String theSuperSecretAwesomeKeyword) {
		
		setIsDrawer(painter);
		theWord = theSuperSecretAwesomeKeyword;
		if(word==null) {
			if(isPainter) word = new JLabel(theWord);
	        else {
	        	String blanks = "";
	        	for(int q = 0; q < theWord.length(); q++) {
	        		blanks += "__  ";
	        	}
	        	word = new JLabel(blanks);
	        }
		}
		else {
			if(isPainter) word.setText(theWord);
	        else {
	        	String blanks = "";
	        	for(int q = 0; q < theWord.length(); q++) {
	        		blanks += "__  ";
	        	}
	        	word.setText(blanks);
	        }
		}
		wordsPanel.validate();
		wordsPanel.repaint();
		
	}
	
	/**
	 * Tells the timer how much longer it has in the game.
	 * @param secondsRemaining
	 */
	public void updateTimer(int secondsRemaining) {
		
		secondsLeft = secondsRemaining;
		int timeSec = secondsLeft%60;
		int timeMin = secondsLeft/60;
		if(timeLabel==null) timeLabel = new JLabel(timeMin+":0"+timeSec);
		else {
			if(timeSec/10==0) timeLabel.setText(timeMin+":0"+timeSec);
			else timeLabel.setText(timeMin+":"+timeSec);
		}
		timerPanel.validate();
		
	}
	
	/**
	 * This increases the point value displayed to the player.
	 * @param increase the value by which it increased
	 */
	public void addToPoints(int increase) {
		points += increase;
		pointsLabel.setText("Points: "+points);
	}
	
	/**
	 * This method will return the time at which the player guessed the word.
	 * @return
	 */
	public long getTimeGuessed() {
		return timeGuessed;
	}
	
}
