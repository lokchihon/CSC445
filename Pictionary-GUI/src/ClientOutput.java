import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

public class ClientOutput extends Thread {

    //we need a client to read the info and a stream to receive the info from
    private MultiClient client;
    private ObjectOutputStream outputStream;



    // get the stream from a socket that is given to the constructor
    public ClientOutput(Socket s, MultiClient client) {

        try {
            this.outputStream = new ObjectOutputStream(s.getOutputStream());
            this.client = client;


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void run() {

        try {

            while (client.getClientRunning()) {

                try {

                    if (!client.hasSentUsername()) {
                        outputStream.writeChars(client.getUsername());
                    }



                    if (client.isHost()) {

                        outputStream.writeChars("START");
                    }

                    if (client.getDrawer()) {
                        TimeUnit.MILLISECONDS.sleep(100);
                        //Checks to make sure that the data is not empty before writing.
                        ArrayList<DrawData> draws = client.getDrawPoints();
                        if(draws.size()>0) {
                        	outputStream.writeUnshared(draws);
                            outputStream.flush();
                            outputStream.reset();
                        }
                        outputStream.writeUnshared(client.getDrawPoints());
                        outputStream.flush();
                        outputStream.reset();
                    } else {
                        //code for sending out the word packets
                        TimeUnit.MILLISECONDS.sleep(100);
                        //Checks to make sure that the data is not empty before writing.
                        ArrayList<String> chats = client.getMessages();
                        if(chats.size()>0) {
                        	outputStream.writeUnshared(chats);
                            outputStream.flush();
                            outputStream.reset();
                        }
                    }


                } catch (ConcurrentModificationException ignore) {
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e ) {
            e.printStackTrace();
        }

    }
}
