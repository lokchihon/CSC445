import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MultiClient {

    private boolean isDrawer = false;
    private static String host = "pi.cs.oswego.edu";
    private static int portNumber = 2715;
    private boolean isHost = false;
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<DrawData> drawPoints = new ArrayList<DrawData>();

    private String currentWord;

    private String gameName;

    private boolean sentGameName = false;

    private boolean sendClear = true;

    private boolean sentStart = false;

    private static String gameStatus = "WAITING";
    private static String username;

    private boolean canClear = false;

    protected static final String WAITING = "WAITING";
    protected static final String STARTED = "STARTED";


    private boolean sentUsername = false;

    private boolean clientRunning = true;


    int packetCounter = 0;


    private int points;

    private int currentTime = 30;

    private static GUIMainMenu m = new GUIMainMenu();

    private static GUI g;


    public void setIsHost(Boolean b) {
        this.isHost = b;
    }

    public void setSendClear(boolean sendClear) {
        this.sendClear = sendClear;
    }

    public boolean isSendClear() {
        return sendClear;
    }

    public void setSentGameName(boolean sentGameName) {
        this.sentGameName = sentGameName;
    }

    public boolean hasSentGameName() {
        return sentGameName;
    }

    public void setSentStart(boolean started) {
        sentStart = started;
    }

    public boolean getSentStart() {
        return sentStart;
    }

    public void setGameStatus(String status) {
        if (status.equals("START")) {
            gameStatus = MultiClient.STARTED;

        } else if (status.equals("END")) {
            gameStatus = MultiClient.WAITING;
        }
    }

    public String getGameStatus() {
        return gameStatus;
    }


    public boolean hasSentUsername () {
        return sentUsername;
    }

    public void setSentUsername(boolean b) {
        sentUsername = b;
    }


    public void setCurrentWord(String s) { currentWord = s; }


    public int getCurrentTime() {
        return currentTime;
    }

    public void clearCanvas() {
        this.canClear = true;
    }

    public void startGUI() {
        if (!this.isHost) {
            g.startGame();
        }
    }


    public void addMessage(String message){
        messages.add(message);
    }

    /**
     * Pulls new chats into the messages arraylist, then sends and resets messages.
     * @return
     */
    public ArrayList<String> getMessages(){

        messages.addAll(g.getChat());

        return messages;

    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getPoints(){
        return this.points;
    }




    public ArrayList<DrawData> getDrawPoints() {
        drawPoints.addAll(g.getBrushStrokes());
        System.out.println("This is the array in the client " + drawPoints);
        return drawPoints;
    }

    public boolean getClientRunning() {
        return clientRunning;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    public String getGameName() {
        return gameName;
    }

    public synchronized static void main(String[] args) {

        MultiClient client = new MultiClient();


        while(true) {
        	m.bePretty();
            if(m.hasGame()) {
                if(m.host || gameStatus.equals(MultiClient.WAITING)) {
                	username = m.uName;
                    g = m.makeGUI(m.uName, client.getCurrentWord(), m.host, client);
                    try  {
                        Socket s = new Socket(host, portNumber);
                        new ClientInput(s,client).start();
                        new ClientOutput(s,client).start();
                        g.updateTimer(client.getCurrentTime());

                        if (client.canClear) {
                            g.clearCanvas();
                            client.canClear = false;
                        }

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

                if (s.contains(g.getUsername()) && s.contains("joined")) {

                } else {

                    g.addChat(g.getUsername(), s);
                }

            }

        }



        if (!isDrawer && data.getDrawData() != null) {
            System.out.println("Read the data " + data.getDrawData());
            ArrayList<DrawData> drawing_data = data.getDrawData();
            for (DrawData d : drawing_data) {
                readDataToGui(d, g);
            }


        }

        //syncing the clocks

        if (data.getHost() != null) {

            if (this.getUsername().equalsIgnoreCase(data.getHost())) {
                setIsHost(true);
            }

        }

        //this updates the point value if it is different than the normal
        updatePoints(data.getPoints());

        this.currentTime = data.getTimeRemaining();
        g.updateTimer(data.getTimeRemaining());
        //g.setWord(currentWord);
    }

    public boolean getDrawer() { return isDrawer;    }

    public boolean isHost() {
        return isHost;
    }

    public void setDrawer(boolean b) { this.isDrawer = b;}

    public void setWord(String s) {
        g.setWord(s);
    }

}
