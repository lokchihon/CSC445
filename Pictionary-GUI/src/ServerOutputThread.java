package A3;

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
                    TimeUnit.MILLISECONDS.sleep(100);
                    DataPacket data = server.getData(client);
                    out.writeObject(data);
                    out.flush();
                    out.reset();
                }catch (ConcurrentModificationException ignore){} catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {e.printStackTrace();}

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
