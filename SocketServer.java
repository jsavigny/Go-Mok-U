import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class SocketServer extends Thread
{
    private ServerSocket serverSocket;
    private ArrayList<String> user = new ArrayList<>();
    private ArrayList<ArrayList<String>> room = new ArrayList<ArrayList<String>>();
    private ArrayList<String> selector = new ArrayList<String>();
    public SocketServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
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
    public void run()
    {
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                //Terima variable dari client
                ObjectInputStream ins = new ObjectInputStream(server.getInputStream());
                //Kirim variable ke client
                ObjectOutputStream outs = new ObjectOutputStream(server.getOutputStream());
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
                server.close();
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
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try
        {
            Thread t = new SocketServer(port);
            t.start();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
