package A3;

import java.util.ArrayList;

public class Client extends Thread {
    private String username;
    private boolean drawer = false;

    private int points = 0;

    ArrayList<DrawData> drawData;
    ArrayList<String> messages;

    public boolean getDrawer() {
        return drawer;
    }
    public void setDrawer(boolean b) { this.drawer = b;}

    public boolean hasUsername() {
        return true;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){this.username = username;}

    public ArrayList<String> getMessages(){
        return this.messages;
    }

    public void addMessage(String message){
        messages.add(message);
    }

    public int getPoints(){
        return this.points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public ArrayList<DrawData> getDrawData(){
        return this.drawData;
    }

    public void setDrawData(ArrayList<DrawData> drawData){
        this.drawData = drawData;
    }
}
