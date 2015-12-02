import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class SocketServer extends Thread
{
    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;
    // This chat server can accept up to maxClientsCount clients' connections.
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount];
    public SocketServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
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
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
class clientThread extends Thread {

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
        selector = new ArrayList<String>();
        selector.add(roomName);
        selector.add(playerName);
        room.add(selector);
    }
    public void joinRoom(String playerName, String roomName){
        int idx = 0;
        boolean found = false;
        for (int i=0;i<room.size();i++){
            found = room.get(i).contains(roomName);
            if( found == true){
                System.out.println("Masuk contains");
                idx = i;
            }
        }
        System.out.println(idx);
        if (found == true) {
            room.get(idx).add(playerName);
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
                System.out.println("Pilihan Client : "+pilihan);
                if (pilihan == 1){
                    String namacreator = in.readUTF();
                    String namaroom = in.readUTF();
                    System.out.println("Nama Creator :"+namacreator);
                    System.out.println("Nama Room :"+namaroom);
                    createRoom(namacreator, namaroom);
                    System.out.println(room);
                }
                else if (pilihan == 2){
                    String namajoin = in.readUTF();
                    String namaroom = in.readUTF();
                    System.out.println("Nama Joiner :"+namajoin);
                    System.out.println("Nama Room :"+namaroom);
                    joinRoom(namajoin, namaroom);
                    System.out.println(room);
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
