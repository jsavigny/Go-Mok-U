package Controller;

import Logic.SocketClient;
import Main.Main;
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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
public class LandingController implements Initializable {
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
                    Main.socketClient.setArgument(nickName);
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
    }
}
