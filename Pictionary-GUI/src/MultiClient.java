import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MultiClient {

    private boolean isDrawer = true;
    private static String host = "localhost";
    private static int portNumber = 8080;
    private boolean isHost = true;
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<DrawData> drawPoints = new ArrayList<DrawData>();

    private String currentWord;

    private static String gameStatus;
    private static String username;

    protected static final String WAITING = "WAITING";
    protected static final String STARTED = "STARTED";


    private boolean sentUsername = false;

    private boolean clientRunning = true;


    int packetCounter = 0;


    private int points;

    private static GUIMainMenu m = new GUIMainMenu();

    private static GUI g;


    public void setIsHost(Boolean b) {
        this.isHost = b;
    }


    public void setGameStatus(String status) {
        if (status.equals("START")) {
            gameStatus = MultiClient.STARTED;

        } else if (status.equals("END")) {
            gameStatus = MultiClient.WAITING;
        }
    }


    public boolean hasSentUsername () {
        return sentUsername;
    }

    public void setSentUsername(boolean b) {
        sentUsername = b;
    }


    public void setCurrentWord(String s) { currentWord = s; }





    public void addMessage(String message){
        messages.add(message);
    }

    /**
     * Pulls new chats into the messages arraylist, then sends and resets messages.
     * @return
     */
    public ArrayList<String> getMessages(){
    	ArrayList<String> retVal;
    	modifyChat();
    	retVal = this.messages;
    	this.messages = new ArrayList<>();
        return retVal;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getPoints(){
        return this.points;
    }


    public ArrayList<DrawData> getDrawPoints() {
    	ArrayList<DrawData> retVal;
    	this.modifyPoints();
    	retVal = this.drawPoints;
    	this.drawPoints = new ArrayList<>();
    	return retVal;
    }

    public boolean getClientRunning() {
        return clientRunning;
    }

    public String getCurrentWord() {
        return currentWord;
    }


    public synchronized static void main(String[] args) {

        MultiClient client = new MultiClient();


        while(true) {
        	m.bePretty();
            if(m.hasGame()) {
                if(m.host || gameStatus.equals(MultiClient.STARTED)) {
                	username = m.uName;
                    g = m.makeGUI(m.uName, client.getCurrentWord(), m.host);
                    try  {
                        Socket s = new Socket(host, portNumber);
                        new ClientInput(s,client).start();
                        new ClientOutput(s,client).start();

                    } catch (IOException e) {
                        System.out.println("Hitting the IOException in the MC main method");
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }





    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        username = name;
    }




    //This will add the points from the gui to the array of points to be sent to the server

    public void modifyPoints() {
        for(DrawData d : g.getBrushStrokes()) {
            drawPoints.add(d);
        }
    }


    // This will add the points from the gui to the local messages
    public void modifyChat() {
        for(String s : g.getChat()) {
            messages.add(s);
        }
    }



    //This gets the points from a single draw data and draws it on a GUI


    public void readDataToGui(DrawData drawData, GUI g) {
        int[] points = drawData.getPoints();
        int xPrev = points[0];
        int yPrev = points[1];
        int xCurr = points[2];
        int yCurr = points[3];
        int bSize = drawData.getBrushSize();
        int color = drawData.getColor();
        g.drawOnMe(xPrev,yPrev,xCurr,yCurr,bSize,color);
    }



    //A method for updating the points based on what comes from the server

    public void updatePoints(int pointVal) {
        if (this.points != pointVal) {
            this.points = pointVal;
        }
    }



    //The main method for reading from a packet and converting the data into useful variables

    public void readPacket(DataPacket data) {
        //this reads in a data packet and gets the values for the GUI
        if(username.equalsIgnoreCase(data.getDrawer())){
            this.isDrawer = true;
        }

        if (data.getMessages() != null) {

            ArrayList<String> messages = data.getMessages();



            for (String s : messages) {
                g.addChat(g.getUsername(), s);
            }

        }



        if (!this.isDrawer && data.getDrawData() != null) {
            ArrayList<DrawData> drawing_data = data.getDrawData();
            for (DrawData d : drawing_data) {
                readDataToGui(d, g);
            }


        }

        if (data.getHost() != null) {

            if (this.getUsername().equalsIgnoreCase(data.getHost())) {
                setIsHost(true);
            }

        }

        //this updates the point value if it is different than the normal
        updatePoints(data.getPoints());
    }

    public boolean getDrawer() { return isDrawer;    }

    public boolean isHost() {
        return isHost;
    }

    public void setDrawer(boolean b) { this.isDrawer = b;}

}
