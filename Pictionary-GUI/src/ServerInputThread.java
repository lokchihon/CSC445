import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerInputThread extends Thread {
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

    public void run() {
        try {
            while (server.getServerRunning()) {
                //drawer
                if (client.getDrawer()) {
                    try {
                        ArrayList<DrawData> drawData = (ArrayList<DrawData>) in.readUnshared();
                        this.server.setDrawData(drawData);

                    } catch (ClassCastException ignored) {
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    //not drawer
                    try {
                        System.out.println("Got message");
                        //wait for the client to send a string then reading that string
                        if (in.readObject() instanceof String) {
                            System.out.println("It was a String");
                            String chatMessage = (String) in.readObject();
                            System.out.println(chatMessage);
                            if (chatMessage != null) {
                                server.sendMessage(this.client, chatMessage);
                            }
                        }

                    } catch (SocketException e) {
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (EOFException e) {
//                        System.out.println("EOF Exception");
                    }
                }
            }
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
