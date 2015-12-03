package Logic;
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
    private static final int maxClientsCount = 5;
    private static final clientThread[] threads = new clientThread[maxClientsCount];
    private static int countClient = 0;
    public static ArrayList<String> user = new ArrayList<>();
    public static ArrayList<ArrayList<String>> room = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> selector = new ArrayList<String>();
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
                        (threads[i] = new Logic.clientThread(clientSocket, threads)).start();
                        countClient++;
                        break;
                    }
                }
                //System.out.println("Jumlah client Thread Connected :"+countClient);
                for (int j=0; j<countClient; j++){
                    System.out.println("Jumlah client Thread Connected :"+countClient);
                    System.out.println("Client Connected :"+threads[j]);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
