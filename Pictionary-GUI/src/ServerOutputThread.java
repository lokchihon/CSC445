import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

public class ServerOutputThread extends Thread {
    private Server server;
    private Client client;
    private Socket socket;
    private ObjectOutputStream out;
    private EncryptionHandler eh;
    private String key;

    public ServerOutputThread(Server server, Client client, Socket socket) throws IOException {
        this.server = server;
        this.client = client;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run(){
        try {
            while (server.getServerRunning()) {
                try {
                    //data packet
//                    System.out.println("Getting things to send!");
                    DataPacket data = server.getData(client);
//                    client.getMessages().clear();
//                    EDataPacket edata = new EDataPacket(eh.encode(key,data));
                    out.writeObject(data);
                    out.flush();
                    out.reset();
//                    System.out.println("Sent something");
                    TimeUnit.MILLISECONDS.sleep(1000);

                }catch (ConcurrentModificationException | IOException ignore){}
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } finally{

            try {
                server.playerDisconnected(client);
                socket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
