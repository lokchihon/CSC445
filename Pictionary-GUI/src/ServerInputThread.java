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
    private EncryptionHandler eh;
    private String key;

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
//                        System.out.println("Got draw data!");
                        EDataPacket eread = (EDataPacket) in.readObject();
                        DataPacket read = eh.decode(key, eread.getData());

                        System.out.println("Getting draw data: " + read.getDrawData());
                        this.server.setDrawData(read.getDrawData());

                    } catch (ClassCastException ignored) {}
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    //not drawer
                    try {
//                        System.out.println("Got message");
                        Object o = in.readObject();
//                        System.out.println(o);
                        //wait for the client to send a string then reading that string
                        if (o instanceof String) {
                            System.out.println("It was a String");
                            System.out.println(o);
                            String chatMessage = (String) o;
                            if (chatMessage != null) {
                                server.sendMessage(this.client, chatMessage);
                            }
                        }

                    } catch (SocketException e) {
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (EOFException e) {
                        System.out.println("EOF Exception");
                    }
                }
                TimeUnit.MILLISECONDS.sleep(1000);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
