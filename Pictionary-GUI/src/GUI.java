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

    private static Color color;
    private static int brushSize = 10;
    private static int posX, pPosX;
    private static int posY, pPosY;
    private static JComponent[] buttonPanelArray = new JComponent[11];
    private static JList<Object> chatList = new JList<>();
    private static ArrayList<String> chatLog = new ArrayList<>();
    private static JPanel canvas;
    private static JFrame paintingWindow;
    private static JScrollPane chatScrollPane;
    protected static String username;

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

			@Override
            public void paintComponents(Graphics g){
                g.setColor(color);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                /*int drop = (int)(Math.random()*100+1);
                if(drop != 1)*/ g2.drawLine(posX, posY, pPosX, pPosY);
            }
        };
        
        canvas.setSize(885+280, 850);
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
                canvas.paintComponents(canvas.getGraphics());
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
                canvas.paintComponents(canvas.getGraphics());
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
        redButton.addActionListener(actionEvent -> color = Color.RED);
        orangeButton.addActionListener(actionEvent -> color = Color.ORANGE);
        yellowButton.addActionListener(actionEvent -> color = Color.YELLOW);
        greenButton.addActionListener(actionEvent -> color = Color.GREEN);
        blueButton.addActionListener(actionEvent -> color = Color.BLUE);
        magentaButton.addActionListener(actionEvent -> color = Color.MAGENTA);
        blackButton.addActionListener(actionEvent -> color = Color.BLACK);
        eraseButton.addActionListener(actionEvent -> color = canvas.getBackground());
        clearButton.addActionListener(actionEvent -> GUI.clearCanvas());
        
        JScrollBar slize = new JScrollBar();
        slize.setPreferredSize(new Dimension(125, 25));
        slize.setOrientation(JSlider.HORIZONTAL);
        slize.setValue(10);
        slize.setToolTipText(slize.getValue()+"");
        slize.addAdjustmentListener(actionEvent -> {
        	brushSize = slize.getValue();
            slize.setToolTipText(slize.getValue()+"");
        });
        
        //This adds the drawing controls into the button panel and buttonPanelArray.
        JPanel buttonPanel = new JPanel(new FlowLayout(30, 10, 5));
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
        //TODO make this hold the word and the timer, and label the chat section I guess.
        JPanel dataPanel = new JPanel();
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
        	GUI.addChat(input.getText());
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
	 * @param canSee true if player is drawing, else false.
	 */
	public static void setIsDrawer(boolean canSee) {
		for(JComponent c : buttonPanelArray) {
			c.setVisible(canSee);
		}
	}
	
	/**
	 * This method turns on and off the tooltips for the color buttons.
	 * @param labelOn true if player would like the colors to be labeled with tooltips, else false.
	 */
	public static void triggerColorLabels(boolean labelOn) {
		
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
	 * This method takes in a string to be displayed in the chat box. Keeps the chat log at size 512 max.
	 * @param chatText the chat string to be displayed.
	 */
	public static void addChat(String chatText) {
		
		chatLog.add(username+": "+chatText);
		if(chatLog.size()>512) {
			chatLog.remove(0);
		}
		Object[] chats = chatLog.toArray();
		chatList.setListData(chats);
		chatScrollPane.validate();
    	chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum());
	}
	
	/**
	 * 
	 */
	public static void clearCanvas() {
		//TODO
	}
	
	/**
	 * This method will return the oldest chat.
	 * @return
	 */
	public static String getChat() {
		//TODO
		return "";
	}
}
