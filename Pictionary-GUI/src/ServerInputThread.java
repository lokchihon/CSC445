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
    private EncryptionHandler eh = new EncryptionHandler();
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
//                System.out.println("CLIENT GET DRAWER " + client.getDrawer());
                if (client.getDrawer()) {
                    try {
//                        System.out.println("Got draw data!");


                        Object read = in.readObject();

                        if(read instanceof String){
                            System.out.println("Got message");
                            String message = (String) read;
                            System.out.println(message);
                            if(message.equals("CLEAR")){
                                server.sendMessage(this.client, message);
                            }
                        }

                        if(read instanceof EDataPacket){
                            EDataPacket eread = (EDataPacket) read;
                            DataPacket readdp = eh.decode(server.getGameName(), eread.getData());
                            this.server.setDrawData(readdp.getDrawData());
                        }



                    } catch (ClassCastException ignored) {}
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    //not drawer
                    try {
                        System.out.println("Got message");
                        Object o = in.readObject();
                        System.out.println(o);
                        //wait for the client to send a string then reading that string
                        if (o instanceof String) {
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
        finally{

            try {
                server.playerDisconnected(client);
                socket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }

    }
}
