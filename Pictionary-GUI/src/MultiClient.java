import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MultiClient {


    private boolean isDrawer;
    private static String host = "pi.cs.oswego.edu";
    private static int portNumber = 2690;
    private boolean isHost;
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<DrawData> drawPoints = new ArrayList<DrawData>();

    private String currentWord;
    
    private static String gameStatus;
    
    protected static final String WAITING = "WAITING";
    protected static final String STARTED = "STARTED";


    private boolean sentUsername = false;

    private boolean clientRunning = true;

    private String username;

    int packetCounter = 0;


    private int points;

    private static GUIMainMenu m = new GUIMainMenu();

    private static GUI g;

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





    public void addMessage(String message){
        messages.add(message);
    }

    public ArrayList<String> getMessages(){
        return this.messages;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getPoints(){
        return this.points;
    }


    public ArrayList<DrawData> getDrawPoints() {return this.drawPoints;}


    public boolean getClientRunning() {
        return clientRunning;
    }

    public String getCurrentWord() {
        return currentWord;
    }


    public synchronized static void main(String[] args) {

        MultiClient client = new MultiClient();

        try (Socket s = new Socket(host, portNumber)) {

            new ClientInput(s,client).start();
            new ClientOutput(s,client).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            if(m.hasGame()) {
                if(gameStatus.equals(MultiClient.STARTED)) {
                    g = m.makeGUI(m.uName, client.getCurrentWord(), m.host);
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
        this.username = name;
    }


    public void modifyPoints() {
        for(DrawData d : g.getBrushStrokes()) {
            drawPoints.add(d);
        }
    }

    public void modifyChat() {
        for(String s : g.getChat()) {
            messages.add(s);
        }
    }


    public void readFirstPacket(DataPacket data) {

        //this reads the first packet that the server will send that says if you are the drawer or not


    }


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


    public void updatePoints(int pointVal) {
        if (this.points != pointVal) {
            this.points = pointVal;
        }
    }



    public void readPacket(DataPacket data) {
        //this reads in a data packet and gets the values for the GUI

        this.isDrawer = data.getDrawer().getDrawer();

        if (data.getMessages() != null) {

            ArrayList<String> messages = data.getMessages();

            if (messages.get(0) == "You are the drawer") {
                this.isDrawer = true;

                this.currentWord = messages.get(0);
            }

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

        //this updates the point value if it is different than the normal
        updatePoints(data.getPoints());
    }

    public boolean getDrawer() { return isDrawer;    }

    public boolean isHost() {
        return isHost;
    }

    public void setDrawer(boolean b) { this.isDrawer = b;}

}
