package A3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiClient {


    private boolean isDrawer;
    //TODO go over this; I can't connect to anything but Pi and Altair from off campus
    private static String host = "pi.cs.oswego.edu";
    private static int portNumber = 2690;
    private boolean isHost;
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<DrawData> drawPoints = new ArrayList<DrawData>();
    private String gameStatus; //WAITING, STARTED

    //TODO show this
    protected static final String WAITING = "WAITING";
    protected static final String STARTED = "STARTED";


    private boolean sentUsername = false;

    private boolean clientRunning = true;

    private String username;

    int packetCounter = 0;


    private int points;

    private static GUIMainMenu m = new GUIMainMenu();

    //TODO go over this section, it creates the drawing panel, should only be called when
    //m.getGameName() points to a valid, running game in the server, or is being made.
    private static GUI g;// = m.makeGUI(m.uName, "flamingo", m.host);


    public void setGameStatus(String status) {
        if (status.equals("START")) {
            gameStatus = "STARTED";

        } else if (status.equals("END")) {
            gameStatus = "WAITING";
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


    public synchronized static void main(String[] args) {


        //really not sure about having this in the client





        MultiClient client = new MultiClient();

        try (Socket s = new Socket(host, portNumber)) {

            new ClientInput(s,client).start();
            new ClientOutput(s,client).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO I think this is where the main loop should go for the client.
        while(true) {
            //Socket and Stream logic can go here.
            if(m.hasGame()) {
                if(m.host) {
                    //tell the server you're making a new game
                    g = m.makeGUI(m.uName, "flamingo", m.host);
                }
                else {
                    //tell the server you want to get into a game, or in other words,
                    //is m.gameName a game the server knows about existing

                    //if(server.hasGame(m.gameName) { or however the server keeps track of that kind of thing, probably needs the I/O sockets for that
                    // g = m.makeGUI(m.uName, "flamingo", m.host);
                    // }
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
        //TODO show this
        for(DrawData d : g.getBrushStrokes()) {
            drawPoints.add(d);
        }
    }

    public void modifyChat() {
        //TODO show this
        for(String s : g.getChat()) {
            messages.add(s);
        }
    }


    public void readFirstPacket(DataPacket data) {

        //this reads the first packet that the server will send that says if you are the drawer or not
        ArrayList<String> messages = data.getMessages();

        if (messages.get(0) == "You are the drawer") {
            this.isDrawer = true;
        }

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

        //TODO show this, specifically the extra .getDrawer()
        this.isDrawer = data.getDrawer().getDrawer();

        if (data.getMessages() != null) {

            ArrayList<String> messages = data.getMessages();
            for (String s : messages) {
                //TODO show this
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