package A3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server {
    private Client drawer;
    private int whoHasDrawn = 0;

    private HashSet<String> usernames = new HashSet<>();
    private ArrayList<Client> clients = new ArrayList<>();
    private String secretWord;
    private ArrayList<String> wordPot = new ArrayList<>();

    private int roundTime = 90;
    private int timeRemaining = roundTime;
    private boolean serverRunning = true;
    private int winners = 0;

    private ServerSocket serverSocket;
    private String host;
    private int port;

    private ArrayList<DrawData> drawData;

    private Timer timer = new Timer();

    private String gameStatus = "WAITING";

    public static void main(String[] args) throws IOException {
        Server server = new Server("lol", 1234);
        new ListenForClients(server).start();
    }

    public Server(String host, int port) throws FileNotFoundException {
        this.host = host;
        this.port = port;
        initializeUserNames();
        loadWordPot("words.txt");
    }

    public void initializeUserNames(){
        usernames.clear();
    }

    public void loadWordPot(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)));
        while(scanner.hasNext()){
            wordPot.add(scanner.next());
        }
        scanner.close();
    }

    public boolean validUsername(String username){
        boolean valid = username != null && !usernames.contains(username.toLowerCase());
        return valid;
    }

    public void newRound() {
        gameStatus = "STARTED";
        secretWord = getSecretWord();
        wordPot.remove(secretWord);
        winners = 0;

        //!!!!!choose artist
        drawer = chooseDrawer();

        String message;
        for(Client c : clients){
            if( c != drawer){
                //!!!!!send them a message saying who is the drawer
                message = drawer.getUsername() + " is the drawer";
                c.addMessage(message);
            } else {
                //!!!!!send the drawer a message telling them they are the drawer
                //!!!!!send the drawer the secret word
                message = "You are the drawer!";
                c.addMessage(message);
                message = "The secret word is: " + secretWord;
                c.addMessage(message);
            }
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Round will end in " + timeRemaining-- + " seconds.");
                if(timeRemaining <= 0){
                    try {
                        endRound();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        },0,1000);
    }

    public void endRound() throws IOException {
        //stop timer
        timer.cancel();
        timer = new Timer();

        calculatePoints();
        //!!!!! send everyone a message about the points

        //remove artist role
        for(Client c : clients){
            c.setDrawer(false);
        }

        //!!!!! enter waiting room
        gameStatus = "WAITING";
    }

    public synchronized void playerConnected(Client client){
        if(validUsername(client.username)){
            clients.add(client);
            //!!!!!send a message welcoming the new person
            for(Client c : clients){
                c.addMessage(client.username + "has joined the game!");
                //!!!!!send everyone a message that this new person has joined
            }
        }
        client.addMessage("Username was taken, try another one.");
        //!!!!!else send message saying username is taken and to choose another one

    }

    public synchronized void playerDisconnected(Client client) throws IOException {
        clients.remove(client.username);
        if(clients.size() < 2) {
            endRound();
        } else if(client.getDrawer()){
            endRound();
        }
        //!!!!!send everyone a message that this person has left the game
        for(Client c : clients){
            c.addMessage(client.username + "has left the game.");
        }

    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(this.port);
    }

    public void endServer() throws IOException {
        this.serverSocket.close();
    }

    public void sendMessage(Client sender, String message) throws IOException {
        String messageToSendOut = null;
        boolean end =  false;
        if(!sender.hasUsername()){
            sender.setName(message);
            playerConnected(sender);
        } else if (message.equals("START") && clients.get(0)==sender){
            newRound();
        } else if(message.toLowerCase().equals(secretWord.toLowerCase())){
            messageToSendOut = sender.getUsername() + "has guessed the secret word!";
            if(winners == clients.size()-1){
                end = true;
            }
        } else {
            messageToSendOut = sender.getUsername() + ": " + message;
        }

        if(messageToSendOut != null){
            for(Client client : clients){
                if(!(client == drawer)){
                    client.addMessage(message);
                }

            }
        }

        if (end){
            endRound();
        }
    }

    public void sendToAll(String message){
        if(message != null){
            for(Client client : clients){
                //!!!!!send out message
                client.addMessage(message);
            }
        }
    }

    //choose a secret word
    public String getSecretWord(){
        Random r = new Random();
        return wordPot.get(r.nextInt(wordPot.size()));
    }

    public synchronized void setDrawData(ArrayList<DrawData> d){
        this.drawData = d;
    }

    public synchronized DataPacket getData(Client client){
        return new DataPacket(drawData,timeRemaining, client.messages);
    }

    public boolean getServerRunning(){
        return this.serverRunning;
    }

    public void calculatePoints(){

    }

    public Client chooseDrawer(){
        Client newDrawer = clients.get(whoHasDrawn%clients.size());
        whoHasDrawn++;
        return newDrawer;
    }

}


