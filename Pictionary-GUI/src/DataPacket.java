package A3;

import java.util.ArrayList;

public class DataPacket {
    private ArrayList<DrawData> drawData;
    private int timeRemaining;
    private ArrayList<String> messages;

    public DataPacket(ArrayList<DrawData> drawData, int timeRemaining, ArrayList<String> messages){
        this.drawData = drawData;
        this.timeRemaining = timeRemaining;
        this.messages = messages;
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
}
