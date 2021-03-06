
import java.io.Serializable;
import java.util.ArrayList;

public class DataPacket implements Serializable {
    private ArrayList<DrawData> drawData;
    private int timeRemaining;
    private ArrayList<String> messages;
    private int points;
    private String drawer;
    private String host;

    public DataPacket(ArrayList<DrawData> drawData,
                      int timeRemaining,
                      ArrayList<String> messages,
                      int points,
                      String drawer,
                      String host){
        this.drawData = drawData;
        this.timeRemaining = timeRemaining;
        this.messages = messages;
        this.points = points;
        this.drawer = drawer;
        this.host = host;
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

    public void setDrawer(String drawer){
        this.drawer = drawer;
    }

    public String getDrawer(){
        return this.drawer;
    }

    public void setHost(String host){ this.host = host;}

    public String getHost(){ return this.host; }
}
