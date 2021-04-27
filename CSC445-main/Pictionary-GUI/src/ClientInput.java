import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class ClientInput extends Thread {


   //we need a client to read the info and a stream to receive the info from
    private MultiClient client;
    private ObjectInputStream inputStream;



    // get the stream from a socket that is given to the constructor
    public ClientInput(Socket s, MultiClient client) {

        try {
            this.inputStream = new ObjectInputStream(s.getInputStream());
            this.client = client;


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {


            while (client.getClientRunning()) {

                //read in a server packet, as that is the only thing the client has to read in
                DataPacket data = (DataPacket) inputStream.readUnshared();
                //call the read packet method that handles the packet

                client.setSentUsername(true);
                for (String s : data.getMessages()) {
                    if (s.equals("INVALID_USERNAME")) {
                        client.setSentUsername(false);
                    } else if (s.equals("START")) {
                        client.setGameStatus(s);
                    }  else if (s.equals("END")) {
                    client.setGameStatus(s);
                 }
                }

                //need to receive coordinate packets and color packets and guess packets
                client.readPacket(data);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
