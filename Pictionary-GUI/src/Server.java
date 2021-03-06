import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server {
    private Client drawer = new Client();
    private Client gameHost = new Client();
    private int whoHasDrawn = 0;
    private String gameName;

    private HashSet<String> usernames = new HashSet<>();
    private ArrayList<Client> clients = new ArrayList<>();
    private String secretWord;
    private ArrayList<String> wordPot = new ArrayList<>();

    private int roundTime = 30;
    private int timeRemaining = roundTime;
    private boolean serverRunning = true;
    private int winners = 0;

    private ServerSocket serverSocket;
    private String host;
    private int port;

    private boolean roundStarted = false;

    private ArrayList<DrawData> drawData;

    private Timer timer = new Timer();

    public static void main(String[] args) throws IOException {
        Server server = new Server("pi.cs.oswego.edu", 2715);
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
        FileReader fr = new FileReader(new File(fileName));

        BufferedReader reader = new BufferedReader(fr);
        Scanner scanner = new Scanner(reader);
        while(scanner.hasNext()){
            String word = scanner.next();
            wordPot.add(word);
        }
        scanner.close();
    }

    public boolean validUsername(String username){
        boolean valid = username != null && !usernames.contains(username.toLowerCase());
        return valid;
    }

    public void newRound() {
        roundStarted = true;
        secretWord = getSecretWord();
        wordPot.remove(secretWord);
        winners = 0;

        //!!!!!choose artist
        drawer = chooseDrawer();
        drawer.setDrawer(true);

        String message;

        for(Client c : clients){
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
            }

        }
        sendToAll("SECRET_WORD "+ secretWord);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Round will end in " + timeRemaining-- + " seconds.");
//                timeRemaining--;
                if(timeRemaining < 0){
                    try {
                        endRound();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },0,1000);
    }

    public void endRound() throws InterruptedException {
        //stop timer
        timer.cancel();
        timer = new Timer();


        //remove artist role
        for(Client c : clients){
            c.setDrawer(false);
            c.addMessage("END");

            TimeUnit.MILLISECONDS.sleep(1000);
            c.getMessages().clear();
        }
        this.drawData.clear();

        drawer = new Client();
        roundStarted = false;
        timeRemaining = 30;
        //!!!!! enter waiting room
    }

    public synchronized void playerConnected(Client client){
        if(validUsername(client.getUsername())){
            clients.add(client);
            //!!!!!send a message welcoming the new person
            sendToAll(client.getUsername() + " has joined the game!");
        }
//        client.addMessage("INVALID_USERNAME");
        //!!!!!else send message saying username is taken and to choose another one

        gameHost = clients.get(0);

    }

    public synchronized void playerDisconnected(Client client) throws IOException, InterruptedException {
        clients.remove(client);
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

    public void sendMessage(Client sender, String message) throws IOException, InterruptedException {
        String messageToSendOut = null;
        boolean end =  false;
        if(message.contains("GAME_NAME")){
            gameName = message;

        }
        else if(!sender.hasUsername()){
            System.out.println("Setting username");
            sender.setUsername(message);
            sender.setHasUsername(true);
            playerConnected(sender);
        } else if (message.equals("START") && gameHost==sender){
            System.out.println("Starting new round!");
            newRound();
        }
        else if(roundStarted) {
//            System.out.println("round started being hit");
            if (message.toLowerCase().contains(secretWord.toLowerCase())) {
//                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                messageToSendOut = sender.getUsername() + " has guessed the secret word!";
                //!!!!!!!!!!!!!need to calculate points based on countdown clock
                System.out.println("CALCULATING POINTS");
                calculatePoints(sender);
                winners++;
                if (winners == clients.size() - 1 && winners != 0) {
                    end = true;
                }
            } else if(message.contains("CLEAR")){
                drawData.clear();
//                System.out.println("CLEARED DRAW DATA");
                messageToSendOut = "CLEAR";
            }else {
                messageToSendOut = sender.getUsername() + ": " + message;
            }

        } else {
            messageToSendOut = sender.getUsername() + ": " + message;
        }

//        System.out.println("MESSAGE TO SEND OUT: "+ messageToSendOut);
        if(messageToSendOut != null){
            for(Client client : clients){
                client.addMessage(messageToSendOut);


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

        System.out.println("DRAW DATA");
        System.out.println(drawData);
//        System.out.println("TIME REMAINING");
//        System.out.println(timeRemaining);
        System.out.println("MESSAGES");
        System.out.println(client.getMessages());
        System.out.println("POINTS");
        System.out.println(client.getPoints());
        System.out.println("DRAWER");
        System.out.println(drawer.getUsername());
        System.out.println("GAME HOST");
        System.out.println(gameHost.getUsername());

        return new DataPacket(drawData,
                timeRemaining,
                client.getMessages(),
                client.getPoints(),
                drawer.getUsername(),
                gameHost.getUsername());
    }

    public boolean getServerRunning(){
        return this.serverRunning;
    }

    public void calculatePoints(Client sender){
        int points = sender.getPoints();
        sender.addPoints(points+timeRemaining);
        drawer.addPoints(5);
//        System.out.println("POINTS: "+sender.getPoints());
    }

    public Client chooseDrawer(){
        Client newDrawer = clients.get(whoHasDrawn%clients.size());
        whoHasDrawn++;
        return newDrawer;
    }

    public String getGameName(){
        return this.gameName;
    }

    public void setGameName(String gameName){
        this.gameName = gameName;
    }

}


