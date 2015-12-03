package Controller;

import Logic.SocketClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
public class LandingController implements Initializable {
    public static Socket client;
    public static DataOutputStream out;
    public static DataInputStream in;
    @FXML
    private Button submitNickname;

    @FXML
    private TextField nickNameTextField;
    public static String nickName;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        submitNickname.setOnAction(e -> {
            if (!nickNameTextField.getText().equals("")) {
                nickName = nickNameTextField.getText();
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource("../View/lobby.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle("Lobby");
                    stage.setScene(scene);
                    stage.show();
                    ((Node)(e.getSource())).getScene().getWindow().hide();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            } else {
                nickNameTextField.setText("John Doe");
            }
        });
        try
        {
            String serverName = "127.0.0.1";
            int port = 8080;
            System.out.println("Connecting to " + serverName + " on port " + port);
            client = new Socket(serverName, port);
            OutputStream outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            in = new DataInputStream(inFromServer);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
