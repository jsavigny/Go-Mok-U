package Logic;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import Logic.SocketServer;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class SocketServer {
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int maxClientsCount = 5;
    private static final clientThread[] threads = new clientThread[maxClientsCount];
    private static int countClient = 0;
    public static ArrayList<String> user = new ArrayList<>();
    public static ArrayList<ArrayList<String>> room = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> selector = new ArrayList<String>();
    public static int playQuota;
    public static void main(String args[]) {

        // The default port number.
        int portNumber = 2222;
        if (args.length < 1) {
            System.out
                    .println("Usage: java MultiThreadChatServer <portNumber>\n"
                            + "Now using port number=" + portNumber);
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }

        try {
            serverSocket = new ServerSocket(portNumber);
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
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    out.writeUTF("Server too busy Try later.");
                    os.close();
                    out.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
