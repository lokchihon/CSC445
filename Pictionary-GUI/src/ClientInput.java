import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


public class ClientInput extends Thread {


   //we need a client to read the info and a stream to receive the info from
    private MultiClient client;
    private ObjectInputStream inputStream;
    private EncryptionHandler eh = new EncryptionHandler();
    private String key ;

    // get the stream from a socket that is given to the constructor
    public ClientInput(Socket s, MultiClient client) {

        try {
            this.inputStream = new ObjectInputStream(s.getInputStream());
            System.out.println("Made the input thread");
            this.client = client;


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            while (client.getClientRunning()) {
                key = client.getGameName();
                try {

                    EDataPacket eread = (EDataPacket) inputStream.readObject();
                //read in a data packet, as that is the only thing the client has to read in
                   //I know this fix is really hacky, but... it works.
                    DataPacket data = eh.decode("GAME_NAME "+key, eread.getData());
                //call the read packet method that handles the packet

                client.readPacket(data);

                }catch(ClassCastException ignored){} catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
