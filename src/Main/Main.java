package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
public class Main extends Application {

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
    }
}