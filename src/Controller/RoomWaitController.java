package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomWaitController implements Initializable {

    @FXML
    private ListView<String> playerList;

    @FXML
    private Button playButton;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            LandingController.client.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}