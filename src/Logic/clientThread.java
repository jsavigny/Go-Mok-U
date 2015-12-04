package Logic;
import Logic.SocketServer;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;


class clientThread extends Thread {

    private DataInputStream is = null;
    private PrintStream os = null;
    private DataOutputStream out = null;
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
    public void displayRoom() throws IOException {
        for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] != null && threads[i] == this) {
                threads[i].os.println(roomsize);
                //threads[i].out.writeUTF("Room Size = " + roomsize);
                for(int j=0;j<roomsize;j++){
                    threads[i].os.println(SocketServer.room.get(j).get(0));
                    //threads[i].out.writeUTF("Room Tersedia = " + SocketServer.room.get(j).get(0));
                }
            }
        }
    }
    public void displayUser(String roomName) throws IOException {
        for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] != null && threads[i]==this) {
                threads[i].os.println(countPlayer(roomName));
                //out.writeUTF("Jumlah Player dalam Room " + namaroom + " = " + countPlayer(namaroom));
                for(int j=1;j<countPlayer(roomName)+1;j++){
                    threads[i].os.println(SocketServer.room.get(getIndex(roomName)).get(j));
                    System.out.println("Player: "+SocketServer.room.get(getIndex(roomName)).get(j));
                    //out.writeUTF("Nama Player = "+SocketServer.room.get(getIndex(namaroom)).get(i));
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
        for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] != null && threads[i] == this) {
                for (int j = 1; j < countPlayer(roomName) + 1; j++) {
                    if (j == 1) {
                        race = "O";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                        threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    } else if (j == 2) {
                        race = "X";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                        threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    } else if (j == 3) {
                        race = "^";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                        threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    } else if (j == 4) {
                        race = "$";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                        threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    } else if (j == 5) {
                        race = "#";
                        System.out.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                        threads[i].os.println(SocketServer.room.get(idx).get(j) + " NOW PLAYING " + race);
                    }
                }
            }
        }
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
            //out = new DataOutputStream(clientSocket.getOutputStream());
            //os.println("Just Connected To " + clientSocket.getRemoteSocketAddress());
            //out.writeUTF("Just Connected To " + clientSocket.getRemoteSocketAddress());
            //os.println("Silakan Login Terlebih Dahulu");
            //out.writeUTF("Silakan Login Terlebih Dahulu");
            String nama = is.readLine().trim();
            login(nama);
            //os.println("Welcome " + nama + " Lets Gomoku!");
            //out.writeUTF("Welcome " + nama + " Lets Gomoku!");
            System.out.println("Isi Tabel User = " + SocketServer.user);
            /*for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** A new user " + nama
                            + " entered the lobby !!! ***");
                    // threads[i].out.writeUTF("*** A new user " + nama
                    //       + " entered the chat room !!! ***");
                }
            }*/
            while (!playStatus) {
                //os.println("Silakan Masukan Pilihan \n 1. Create Room \n 2. Join Room \n 3. Chat with other Player");
                // out.writeUTF("Silakan Masukan Pilihan \n 1.Create Room \n 2. Join Room \n 3. Chat with other Player");
                String pilihan = is.readLine().trim();
                System.out.println("Pilihan Client : " + pilihan);
                if (pilihan.equals("1")||(pilihan.equalsIgnoreCase("create"))){
                    String namacreator = nama;
                    //os.println("Masukan Nama Room : ");
                    //out.writeUTF("Masukan Nama Room : ");
                    String namaroom = is.readLine().trim();
                    System.out.println("Nama Creator :" + namacreator);
                    System.out.println("Nama Room :" + namaroom);
                    createRoom(namacreator, namaroom);
                    System.out.println(SocketServer.room);
                    roomsize = SocketServer.room.size();
                    displayUser(namaroom);
                    /*while(countPlayer(namaroom)<3){
                        //Just Wait
                    }
                    os.println("Wanna Play Right Now? (Y/N)");
                    //out.writeUTF("Wanna Play Right Now? (Y/N)");
                    String play = is.readLine();
                    if((play.equals("y") || play.equals("Y")) && countPlayer(namaroom) >= 3){
                        playStatus = true;
                        Play(namaroom);
                        os.println("Play");
                        //out.writeUTF("Play");
                    }
                    else{
                        playStatus = false;
                        System.out.println("Masuk No");
                        os.println("No");
                        //out.writeUTF("No");
                    }*/
                }
                else if (pilihan.equals("2")||(pilihan.equalsIgnoreCase("join"))){
                    String namajoin = nama;
                    //os.println("Masukan Nama Room yang ingin Di Join = ");
                    String namaroom = is.readLine().trim();
                    System.out.println("Nama Joiner :" + namajoin);
                    System.out.println("Nama Room :" + namaroom);
                    joinRoom(namajoin, namaroom);
                    System.out.println(SocketServer.room);
                    displayUser(namaroom);
                    /*for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && threads[i]!=this) {
                            threads[i].os.println("Player "+nama+" has joined "+namaroom);
                        }
                    }
                    while (playStatus != true){
                        String terima =is.readLine().trim();
                        if (terima.equalsIgnoreCase("displayUser")){
                            displayUser(namaroom);
                        } else if (terima.equalsIgnoreCase("play")){

                        }
                    }
                    os.println("LETS PLAY!");
                    //out.writeUTF("LETS PLAY!");
                    Play(namaroom);*/
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
                                //threads[i].out.writeUTF("<" + nama + "> : " + line);
                            }
                        }
                    }
                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && threads[i] != this) {
                            threads[i].os.println("*** The user " + nama
                                    + " is leaving the chat room !!! ***");
                            //threads[i].out.writeUTF("*** The user " + nama
                            //      + " is leaving the chat room !!! ***");
                        }
                    }
                    os.println("*** Bye " + nama + " ***");
                    //out.writeUTF("*** Bye " + nama + " ***");
                } else if (pilihan.equalsIgnoreCase("display")){
                    displayRoom();
                } else if (pilihan.equalsIgnoreCase("displayUser")){
                    String roomName = is.readLine().trim();
                    displayUser(roomName);
                } else if (pilihan.equalsIgnoreCase("play")){
                    String roomName = is.readLine().trim();
                    int playerCount = countPlayer(roomName);
                    if (playerCount>=3){
                        Play(roomName);
                    }
                }
                /*
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    }
                }*/

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
