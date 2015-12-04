package Main;

import Controller.RoomWaitController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
public class Main extends Application {
    public static SocketClient socketClient;
    public static void main(String[] args) {

        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent landing = FXMLLoader.load(Main.class.getResource("../View/landing.fxml"));
            Scene scene = new Scene(landing);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Room");
            primaryStage.show();

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        socketClient=new SocketClient();
        new Thread(socketClient).start();
    }

    public class SocketClient implements Runnable{
        // The client socket
        private Socket clientSocket = null;

        public PrintStream getOs() {
            return os;
        }

        public void setOs(PrintStream os) {
            this.os = os;
        }

        public DataInputStream getIs() {
            return is;
        }

        public void setIs(DataInputStream is) {
            this.is = is;
        }

        // The output stream
        private PrintStream os = null;
        // The input stream
        private DataInputStream is = null;

        private BufferedReader inputLine = null;
        private boolean closed = false;
        private ArrayList<String> argument;
        public void setArgument(String argument){
            this.argument.add(argument);
        }
        @Override
        public void run(){
            // The default port.
            int portNumber = 2222;
            // The default host.
            String host = "localhost";
            argument = new ArrayList<>();
    /*
     * Open a socket on a given host and port. Open input and output streams.
     */     String fromServer="";
            try {
                clientSocket = new Socket("localhost", 2222);
                inputLine = new BufferedReader(new InputStreamReader(System.in));
                os = new PrintStream(clientSocket.getOutputStream());
                is = new DataInputStream(clientSocket.getInputStream());
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + host);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to the host "
                        + host);
            }
            try {
                while(true){
                    Thread.sleep(500);
                    System.out.println("Argument ="+argument);
                    if (!argument.isEmpty()) {
                        for (int i=0;i<argument.size();i++) {
                            os.println(argument.get(i));
                            if (argument.get(i).equalsIgnoreCase("display")){

                            }
                            argument.remove(i);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}