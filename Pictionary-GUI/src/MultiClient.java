import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiClient {


    private boolean isDrawer;
    private static String host = "wolf.cs.oswego.edu";
    private static int portNumber = 2690;
    private boolean isHost;
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<DrawData> drawPoints = new ArrayList<DrawData>();
    private String gameStatus; //WAITING, STARTED

    private boolean sentUsername = false;

    private boolean clientRunning = true;

    private String username;

    int packetCounter = 0;


    private int points;


    //still not sure if the gui should be here or not

    GUIMainMenu m = new GUIMainMenu();
    GUI g = m.makeGUI(m.uName, "flamingo", m.host);


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


    



    public ArrayList<String> getMessages(){
        return this.messages;
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
    }





    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }


    public void modifyPoints() {
        //this needs to have the method from the GUI that will give the full array to be appended.
        drawPoints.add(g.getBrushStrokes());
    }

    public void modifyChat() {
        //same as the method above, need to have the full list from phoenix
         messages.add(g.getChat());
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

            this.isDrawer = data.getDrawer();

            if (data.getMessages() != null) {

                ArrayList<String> messages = data.getMessages();
                for (String s : messages) {
                    //looks like I'll need the username at some point
                    g.addChat("NEED USERNAME", s);
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

    public boolean getDrawer() {
        return isDrawer;

    }

    public boolean isHost() {
        return isHost;
    }


}
