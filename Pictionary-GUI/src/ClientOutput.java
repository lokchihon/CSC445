import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

public class ClientOutput extends Thread {

    //we need a client to read the info and a stream to receive the info from
    private MultiClient client;
    private ObjectOutputStream outputStream;
    private EncryptionHandler eh = new EncryptionHandler();
    private String key;

    // get the stream from a socket that is given to the constructor
    public ClientOutput(Socket s, MultiClient client) {
        key = client.getGameName();
        try {
            this.outputStream = new ObjectOutputStream(s.getOutputStream());
            System.out.println("made the output thread");
            this.client = client;


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void run() {

        try {

            while (client.getClientRunning()) {

                try {
//                    System.out.println("Sent username?: "+ client.hasSentUsername());
                    if (!client.hasSentUsername()) {
                        System.out.println("Sent the username " + client.getUsername());
                        outputStream.writeObject(client.getUsername());
                        outputStream.flush();
                        outputStream.reset();
                        client.setSentUsername(true);
                        try {
                            TimeUnit.MILLISECONDS.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    if (!client.hasSentGameName()) {
                        outputStream.writeObject("GAME_NAME " + client.getGameName());
                        outputStream.flush();
                        outputStream.reset();
                        client.setSentGameName(true);
                    }


//                    System.out.println("IS SEND CLEAR: " + client.isSendClear());
                    if (client.isSendClear()) {
                        outputStream.writeObject("CLEAR");
                        outputStream.flush();
                        outputStream.reset();
                        client.setSendClear(false);
                        client.getDrawPoints().clear();
                    }


                    if (client.getDrawer()) {
                        //Checks to make sure that the data is not empty before writing.
                        ArrayList<DrawData> draws = client.getDrawPoints();
                        DataPacket ddata = new DataPacket(draws, 0, new ArrayList<String>(), 0, "H", "H");
                        EDataPacket data = new EDataPacket(eh.encode(key,ddata));
                        outputStream.writeObject(data);
                        outputStream.flush();
                        outputStream.reset();


                        //}

                    } else {
                        //code for sending out the word packets
                        TimeUnit.MILLISECONDS.sleep(100);
                        //Checks to make sure that the data is not empty before writing.
                        PriorityQueue<String> chats = client.getChatMessages();
                        if (chats.size() > 0) {
                            outputStream.writeObject(chats.poll());
                            outputStream.flush();
                            outputStream.reset();
                        }
                    }


                    ///System.out.println("GAME STATUS: " + client.getGameStatus());
                    if (client.getGameStatus().equals("STARTED")) {

//                       System.out.println("HOST " + client.isHost());
//                       System.out.println("GETSENTSTART " + client.getSentStart());
//                        System.out.println(client.getCurrentWord());
                        if (client.isHost() && !client.getSentStart()) {


                            //Wrap everything in a check if it is started


                            System.out.println("sent the start message");

                            outputStream.writeObject("START");
                            outputStream.flush();
                            outputStream.reset();
                            client.setSentStart(true);

                        }

                    }

                } catch (ConcurrentModificationException ignore) {
                } catch (InterruptedException e) {
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
