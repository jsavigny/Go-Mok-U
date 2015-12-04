package Controller;

import Main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RoomWaitController implements Initializable {

    @FXML
    private ListView<String> playerList;
    @FXML
    private Text playerWarning;
    @FXML
    private Button playButton;
    @FXML
    private Button refreshButton;
    private int countPlayer;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        playerWarning.setVisible(false);
        try{
            ArrayList<String> userArrayList = new ArrayList<>();
            int userListSize = Integer.parseInt(Main.socketClient.getIs().readLine());
            countPlayer=userListSize;
            for (int i=0;i<userListSize;i++){
                String toAdd = Main.socketClient.getIs().readLine();
                userArrayList.add(toAdd);
            }
            ObservableList<String> oList = FXCollections.observableArrayList(userArrayList);
            playerList.setItems(oList);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        refreshButton.setOnAction(event -> {
            Main.socketClient.setArgument("displayUser");
            Main.socketClient.setArgument(LobbyController.roomNameVal);
            try{
                ArrayList<String> userArrayList = new ArrayList<>();
                int userListSize = Integer.parseInt(Main.socketClient.getIs().readLine());
                countPlayer=userListSize;
                for (int i=0;i<userListSize;i++){
                    String toAdd = Main.socketClient.getIs().readLine();
                    userArrayList.add(toAdd);
                }
                ObservableList<String> oList = FXCollections.observableArrayList(userArrayList);
                playerList.setItems(oList);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });
        playButton.setOnAction(event -> {
            Main.socketClient.setArgument("play");
            Main.socketClient.setArgument(LobbyController.roomNameVal);
            if (countPlayer>=3){

            } else {
                playerWarning.setVisible(true);
                System.out.println("TIDAK CUKUP");
            }
        });

    }

}