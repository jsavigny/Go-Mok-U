package Logic;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by user on 02/12/2015.
 */
public class clientThread extends Thread {

    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    public static ArrayList<String> user = new ArrayList<>();
    public static ArrayList<ArrayList<String>> room = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> selector = new ArrayList<String>();
    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }
    public void login(String name){
        user.add(name);
    }
    public void createRoom(String playerName, String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<room.size();i++){
            found = room.get(i).contains(roomName);
            if( found == true){
                idx = i;
            }
        }
        if(idx == -1) {
            selector = new ArrayList<String>();
            selector.add(roomName);
            selector.add(playerName);
            room.add(selector);
        }
    }
    public void joinRoom(String playerName, String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<room.size();i++){
            found = room.get(i).contains(roomName);
            if( found == true){
                System.out.println("Masuk contains");
                idx = i;
            }
        }
        System.out.println(idx);
        if(idx != -1) {
            System.out.println("MASUK ROOM");
            room.get(idx).add(playerName);
        }
    }
    public int countPlayer(String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<room.size();i++){
            found = room.get(i).contains(roomName);
            if( found == true){
                idx = i;
            }
        }
        int count = room.get(idx).size();
        return count-1;
    }
    public void Play(String roomName){
        int idx = -1;
        boolean found = false;
        for (int i=0;i<room.size();i++){
            found = room.get(i).contains(roomName);
            if( found == true){
                idx = i;
            }
        }
        String race;
        for (int j=1;j<countPlayer(roomName)+1;j++){
            if(j == 1) {
                race = "O";
                System.out.println(room.get(idx).get(j) + " NOW PLAYING "+race);
            }
            else if(j == 2) {
                race = "X";
                System.out.println(room.get(idx).get(j) + " NOW PLAYING "+race);
            }
            else if(j == 3) {
                race = "^";
                System.out.println(room.get(idx).get(j) + " NOW PLAYING "+race);
            }
            else if(j == 4) {
                race = "$";
                System.out.println(room.get(idx).get(j) + " NOW PLAYING "+race);
            }
            else if(j == 5) {
                race = "#";
                System.out.println(room.get(idx).get(j) + " NOW PLAYING "+race);
            }
        }
    }
    public void run() {
        while(true)
        {
            try
            {
                System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                //Terima variable dari client
                ObjectInputStream ins = new ObjectInputStream(clientSocket.getInputStream());
                //Kirim variable ke client
                ObjectOutputStream outs = new ObjectOutputStream(clientSocket.getOutputStream());
                String nama = in.readUTF();
                System.out.println("Berhasil Login " + nama);
                login(nama);
                System.out.println(user);
                out.writeUTF("Welcome " + nama + " Lets Gomoku!");
                int pilihan = in.readInt();
                out.writeInt(room.size());
                for(int i=0;i<room.size();i++){
                    out.writeUTF(room.get(i).get(0));
                }
                System.out.println("Pilihan Client : " + pilihan);
                if (pilihan == 1){
                    String namacreator = in.readUTF();
                    String namaroom = in.readUTF();
                    System.out.println("Nama Creator :"+namacreator);
                    System.out.println("Nama Room :" + namaroom);
                    createRoom(namacreator, namaroom);
                    System.out.println(room);
                    out.writeInt(countPlayer(namaroom));
                    String play = in.readUTF();
                    if((play.equals("y") || play.equals("Y")) && countPlayer(namaroom) >= 3){
                        Play(namaroom);
                        out.writeUTF("Play");
                    }
                    else{
                        System.out.println("Masuk No");
                        out.writeUTF("No");
                    }
                }
                else if (pilihan == 2){
                    String namajoin = in.readUTF();
                    String namaroom = in.readUTF();
                    System.out.println("Nama Joiner :"+namajoin);
                    System.out.println("Nama Room :" + namaroom);
                    joinRoom(namajoin, namaroom);
                    System.out.println(room);
                    out.writeInt(countPlayer(namaroom));
                    String play = in.readUTF();
                    if((play.equals("y") || play.equals("Y")) && countPlayer(namaroom) >= 3){
                        Play(namaroom);
                        out.writeUTF("Play");
                    }
                    else{
                        System.out.println("Masuk No");
                        out.writeUTF("No");
                    }
                }

                clientSocket.close();
            }catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
}