package A3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

class ListenForClients extends Thread{
    protected int numberOfClients;
    private boolean gameStart = false;
    private Server server;

    public ListenForClients(Server server) {
        this.server = server;
    }

    public void run( ){
        //port number
        int portNumber = 4445;
        boolean listening = true;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                Socket socket = serverSocket.accept();
                MultiClient player = new MultiClient();
                //creating input and output threads used to communicate with the client
                new ServerOutputThread(server, player, socket).start();
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {e.printStackTrace();}
                new ServerInputThread(server, player, socket).start();
                numberOfClients ++;

//                if(gameStart){
//                    listening = false;
//                }
            }
        } catch (IOException e) {
            System.err.println("Port error");
            System.exit(-1);
        }
    }
}

