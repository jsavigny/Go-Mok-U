package ChatExample;

import Logic.SocketServer;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

/*
 * A chat server that delivers public and private messages.
 */
public class MultiThreadChatServer {

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    // This chat server can accept up to maxClientsCount clients' connections.
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

    public static void main(String args[]) {

        // The default port number.
        int portNumber = 2222;
        if (args.length < 1) {
            System.out
                    .println("Usage: java ChatExample.MultiThreadChatServer <portNumber>\n"
                            + "Now using port number=" + portNumber);
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }

    /*
     * Open a server socket on the portNumber (default 2222). Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
     */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

    /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads)).start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */
class clientThread extends Thread {

    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private static int roomsize = 0;
    private static boolean playStatus = false;
    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }
    public void login(String name){
        SocketServer.user.add(name);
    }
    public void createRoom(String playerName, String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<SocketServer.room.size();i++){
            found = SocketServer.room.get(i).contains(roomName);
            if( found == true){
                idx = i;
            }
        }
        if(idx == -1) {
            SocketServer.selector = new ArrayList<String>();
            SocketServer.selector.add(roomName);
            SocketServer.selector.add(playerName);
            SocketServer.room.add(SocketServer.selector);
        }
    }
    public void joinRoom(String playerName, String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<SocketServer.room.size();i++){
            found = SocketServer.room.get(i).contains(roomName);
            if( found == true){
                System.out.println("Masuk contains");
                idx = i;
            }
        }
        System.out.println(idx);
        if(idx != -1) {
            System.out.println("MASUK ROOM");
            SocketServer.room.get(idx).add(playerName);
        }
    }
    public int getIndex(String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<SocketServer.room.size();i++){
            found = SocketServer.room.get(i).contains(roomName);
            if( found == true){
                System.out.println("Masuk contains");
                idx = i;
            }
        }
        return idx;
    }
    public int countPlayer(String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<SocketServer.room.size();i++){
            found = SocketServer.room.get(i).contains(roomName);
            if( found == true){
                idx = i;
            }
        }
        int count = SocketServer.room.get(idx).size();
        return count-1;
    }
    public void displayRoom(){
        for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] != null && threads[i] != this) {
                threads[i].os.println("Room Size = "+roomsize);
                for(int j=0;j<roomsize;j++){
                    threads[i].os.println("Room Tersedia = "+SocketServer.room.get(j).get(0));
                }
            }
        }
    }
    public void Play(String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<SocketServer.room.size();i++){
            found = SocketServer.room.get(i).contains(roomName);
            if( found == true){
                idx = i;
            }
        }
        String race;
        //for (int i = 0; i < maxClientsCount; i++) {
            //if (threads[i] != null && threads[i] != this) {
                for (int j = 1; j < countPlayer(roomName) + 1; j++) {
                    if (j == 1) {
                        race = "O";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                      //  threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    } else if (j == 2) {
                        race = "X";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                      //  threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    } else if (j == 3) {
                        race = "^";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                      //  threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    } else if (j == 4) {
                        race = "$";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                      //  threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    } else if (j == 5) {
                        race = "#";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                       // threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    }
                }
           // }
        //}
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {
      /*
       * Create input and output streams for this client.
       */
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            os.println("Just Connected To "+clientSocket.getRemoteSocketAddress());
            os.println("Silakan Login Terlebih Dahulu");
            String nama = is.readLine().trim();
            login(nama);
            os.println("Welcome " + nama + " Lets Gomoku!");
            System.out.println("Isi Tabel User = " + SocketServer.user);
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** A new user " + nama
                            + " entered the chat room !!! ***");
                }
            }
            while (true) {
                os.println("Silakan Masukan Pilihan \n 1.Create Room \n 2. Join Room \n 3. Chat with other Player");
                displayRoom();
                String pilihan = is.readLine().trim();
                System.out.println("Pilihan Client : " + pilihan);

                if (pilihan.equals("1")){
                    String namacreator = nama;
                    os.println("Masukan Nama Room : ");
                    String namaroom = is.readLine().trim();
                    System.out.println("Nama Creator :" + namacreator);
                    System.out.println("Nama Room :" + namaroom);
                    createRoom(namacreator, namaroom);
                    System.out.println(SocketServer.room);
                    os.println("Jumlah orang dalam room "+namaroom+" = "+countPlayer(namaroom));
                    os.println("Jumlah room yang ada sekarang "+SocketServer.room.size());
                    roomsize = SocketServer.room.size();
                    displayRoom();
                    while(countPlayer(namaroom)<3){
                        //Just Wait
                    }
                    os.println("Wanna Play Right Now? (Y/N)");
                    String play = is.readLine();
                    if((play.equals("y") || play.equals("Y")) && countPlayer(namaroom) >= 3){
                        playStatus = true;
                        Play(namaroom);
                        os.println("Play");

                    }
                    else{
                        playStatus = false;
                        System.out.println("Masuk No");
                        os.println("No");
                    }
                }
                else if (pilihan.equals("2")){
                    String namajoin = nama;
                    System.out.println("Masukan Nama Room yang ingin Di Join = ");
                    String namaroom = is.readLine().trim();
                    System.out.println("Nama Joiner :"+namajoin);
                    System.out.println("Nama Room :" + namaroom);
                    joinRoom(namajoin, namaroom);
                    System.out.println(SocketServer.room);
                    os.println("Jumlah Player dalam Room "+ namaroom +" = "+countPlayer(namaroom));
                    for(int i=1;i<countPlayer(namaroom)+1;i++){
                        os.println("Nama Player = "+SocketServer.room.get(getIndex(namaroom)).get(i));
                    }
                    while (playStatus != true){

                    }
                    os.print("LETS PLAY!");
                    Play(namaroom);
                }
                else if (pilihan.equals("3")) {
                    while (true) {
                        String line = is.readLine();
                        if (line.startsWith("/quit")) {
                            break;
                        }
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null) {
                                threads[i].os.println("<" + nama + "> : " + line);
                            }
                        }
                    }
                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && threads[i] != this) {
                            threads[i].os.println("*** The user " + nama
                                    + " is leaving the chat room !!! ***");
                        }
                    }
                    os.println("*** Bye " + nama + " ***");
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    }
                }

            }


      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */

      /*
       * Close the output stream, close the input stream, close the socket.
       */

        } catch (IOException e) {
        }
    }
}
