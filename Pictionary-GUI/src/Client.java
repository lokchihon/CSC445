

import java.util.ArrayList;

public class Client extends Thread {
    private String username;
    private boolean hasUsername = false;
    private boolean drawer = false;

    private int points = 0;

    private ArrayList<String> messages = new ArrayList<>();

    public boolean getDrawer() {
        return drawer;
    }
    public void setDrawer(boolean b) { this.drawer = b;}

    public void setHasUsername(boolean b) { this.hasUsername = b;}

    public boolean hasUsername() {
        return hasUsername;
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

}
