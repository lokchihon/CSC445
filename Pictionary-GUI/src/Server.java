package A3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server {
    private MultiClient drawer;
    private MultiClient gameHost;
    private int whoHasDrawn = 0;

    private HashSet<String> usernames = new HashSet<>();
    private ArrayList<MultiClient> clients = new ArrayList<>();
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
        secretWord = getSecretWord();
        wordPot.remove(secretWord);
        winners = 0;

        //!!!!!choose artist
        drawer = chooseDrawer();

        String message;
        for(MultiClient c : clients){
            if(c != clients.get(0)){
                c.addMessage("START");
            }
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
                    endRound();
                }
            }
        },0,1000);
    }

    public void endRound() {
        //stop timer
        timer.cancel();
        timer = new Timer();

        //remove artist role
        for(MultiClient c : clients){
            c.setDrawer(false);
        }

        //!!!!!!!!!!send message saying round has ended
        sendToAll("END");
        //!!!!! enter waiting room
    }

    public synchronized void playerConnected(MultiClient client){
        if(validUsername(client.getUsername())){
            clients.add(client);
            //!!!!!send a message welcoming the new person
            sendToAll(client.getUsername() + " has joined the game!");
        }
        client.addMessage("INVALID_USERNAME");
        //!!!!!else send message saying username is taken and to choose another one
        gameHost = clients.get(0);

    }

    public synchronized void playerDisconnected(MultiClient client) throws IOException {
        clients.remove(client.getUsername());
        if(clients.size() < 2) {
            endRound();
        } else if(client.getDrawer()){
            endRound();
        }
        //!!!!!send everyone a message that this person has left the game
        sendToAll(client.getUsername() + " has left the game.");
        gameHost = clients.get(0);

    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(this.port);
    }

    public void endServer() throws IOException {
        this.serverSocket.close();
    }

    public void sendMessage(MultiClient sender, String message) throws IOException {
        String messageToSendOut = null;
        boolean end =  false;
        if(!sender.hasSentUsername()){
            sender.setUsername(message);
            playerConnected(sender);
        } else if (message.equals("START") && gameHost==sender){
            newRound();
        } else if(message.toLowerCase().equals(secretWord.toLowerCase())){
            messageToSendOut = sender.getUsername() + " has guessed the secret word!";
            //!!!!!!!!!!!!!need to calculate points based on countdown clock
            calculatePoints(sender);
            winners++;
            if(winners == clients.size()-1){
                end = true;
            }
        } else {
            messageToSendOut = sender.getUsername() + ": " + message;
        }

        if(messageToSendOut != null){
            for(MultiClient client : clients){
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
            for(MultiClient client : clients){
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

    public synchronized DataPacket getData(MultiClient client){
        return new DataPacket(drawData,
                timeRemaining,
                client.getMessages(),
                client.getPoints(),
                this.drawer);
    }

    public boolean getServerRunning(){
        return this.serverRunning;
    }

    public void calculatePoints(MultiClient sender){
        int points = sender.getPoints();
        sender.setPoints(points+timeRemaining);
    }

    public MultiClient chooseDrawer(){
        MultiClient newDrawer = clients.get(whoHasDrawn%clients.size());
        whoHasDrawn++;
        return newDrawer;
    }

}


