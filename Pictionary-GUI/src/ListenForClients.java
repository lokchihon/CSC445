

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

class ListenForClients extends Thread{
    protected int numberOfClients = 0;
    private Server server;

    public ListenForClients(Server server) {
        this.server = server;
    }

    public void run( ){
        //port number
        int portNumber = 2715;
        boolean listening = true;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                Socket socket = serverSocket.accept();
                System.out.println("Found a client!");
                Client player = new Client() ;
                //creating input and output threads used to communicate with the client
                new ServerOutputThread(server, player, socket).start();
                System.out.println("Made a server output thread");
//                try {
//                    TimeUnit.MILLISECONDS.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                new ServerInputThread(server, player, socket).start();
                System.out.println("Made a server input thread!");
                numberOfClients ++;

                if(numberOfClients > 8){
                    listening = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Port error");
            System.exit(-1);
        }
    }
}

