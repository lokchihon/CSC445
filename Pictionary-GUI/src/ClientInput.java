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
            System.out.println("Made the input thread");
            this.client = client;


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {


            while (client.getClientRunning()) {
                try {

//                System.out.println("In the while loop");
                //read in a data packet, as that is the only thing the client has to read in
                DataPacket data = (DataPacket) inputStream.readObject();
                //call the read packet method that handles the packet

                for (String s : data.getMessages()) {
                    //System.out.println("The message is " + s);

                    if (s.contains("SECRET_WORD")){
                        //System.out.println("The secret word is " + s.substring(11));
                        client.setCurrentWord(s.substring(11));
                        //For some reason putting it here works better
                        client.setWord(s.substring(11));
                    }

                    if (s.equals("INVALID_USERNAME")) {
                        client.setSentUsername(false);
                    } else if (s.equals("START")) {
                        client.setGameStatus(s);
                    }  else if (s.equals("END")) {
                    client.setGameStatus(s);
                    }  else if (s.equals("CLEAR")) {
                        client.clearCanvas();
                 } else if (s.equals("You are the drawer!")) {
                        System.out.println("You are the drawer");
                        client.setDrawer(true);


                    }

                }

                //need to receive coordinate packets and color packets and guess packets


                 if (data.getMessages() != null) {
                     client.readPacket(data);
                 }


                }catch(ClassCastException ignored){} catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }

        } catch (IOException e) {

            System.out.println("Why tf is it hitting here");
            e.printStackTrace();
        }
    }
}
