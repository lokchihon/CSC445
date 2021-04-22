package A3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;

public class ServerInputThread extends Thread{
    private Server server;
    private Client client;
    private Socket socket;
    private ObjectInputStream in;

    public ServerInputThread(Server server, Client client, Socket socket) throws IOException {
        this.server = server;
        this.client = client;
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void run(){
        try {
            while (server.getServerRunning()) {
                //drawer
                if (client.getDrawer()){
                    try {
                        //wait for the client to send drawing info, then reading it from that client
                        ArrayList<DrawData> drawData = (ArrayList<DrawData>) in.readObject();
                        this.server.setDrawData(drawData);
                    }catch(ClassCastException ignored){} catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    //not drawer
                    try {
                        //wait for the client to send a string then reading that string
                        String chatMessage = (String)in.readObject();
                        if(chatMessage != null){
                            server.sendMessage(this.client, chatMessage);
                        }
                    }catch (SocketException e){} catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        finally {
            try {
                server.playerDisconnected(client);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
