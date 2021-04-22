package A3;

import java.util.ArrayList;

public class DataPacket {
    private ArrayList<DrawData> drawData;
    private int timeRemaining;
    private ArrayList<String> messages;
    private int points;

    public DataPacket(ArrayList<DrawData> drawData,
                      int timeRemaining,
                      ArrayList<String> messages,
                      int points){
        this.drawData = drawData;
        this.timeRemaining = timeRemaining;
        this.messages = messages;
        this.points = points;
    }

    public void setDrawData(ArrayList<DrawData> drawData){
        this.drawData = drawData;
    }

    public ArrayList<DrawData> getDrawData(){
        return this.drawData;
    }

    public void setTimeRemaining(int timeRemaining){
        this.timeRemaining = timeRemaining;
    }

    public int getTimeRemaining(){
        return this.timeRemaining;
    }

    public void setMessages(ArrayList<String> messages){
        this.messages = messages;
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
}
