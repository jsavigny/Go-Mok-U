package Controller;

import Main.Main;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
public class LobbyController implements Initializable {

    @FXML
    private Button createRoomButton;

    @FXML
    private Button joinRoomButton;

    @FXML
    private ListView<String> roomList;

    @FXML
    private TextField roomName;

    @FXML
    private Tab pilJoin;

    @FXML
    private Tab pilCreate;

    @FXML
    private Button refreshRoomList;

    private int pil;
    public static String roomNameVal;
    private String name;
    private ArrayList<String> roomArrayList;
    @Override
    public void initialize(URL url, ResourceBundle rb){


        pilJoin.setOnSelectionChanged(event -> {
            if (pilJoin.isSelected()) {
                Main.socketClient.setArgument("display");
                try{
                    int roomListSize = Integer.parseInt(Main.socketClient.getIs().readLine());
                    roomArrayList = new ArrayList<String>();
                    for (int i=0;i<roomListSize;i++){
                        roomArrayList.add(Main.socketClient.getIs().readLine());
                    }
                    ObservableList<String> oList = FXCollections.observableArrayList(roomArrayList);
                    roomList.setItems(oList);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        createRoomButton.setOnAction(event -> {
            roomNameVal = roomName.getText();
            try {
                try{
                    Main.socketClient.setArgument("create");
                    Main.socketClient.setArgument(roomNameVal);
                    Parent root = FXMLLoader.load(getClass().getResource("../View/roomWait.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle(roomNameVal);
                    stage.setScene(scene);
                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        joinRoomButton.setOnAction(event -> {
            roomNameVal= roomList.getSelectionModel().getSelectedItem();
            try {
                try{
                    Main.socketClient.setArgument("join");
                    Main.socketClient.setArgument(roomNameVal);
                    Parent root = FXMLLoader.load(getClass().getResource("../View/roomWait.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle(roomNameVal);
                    stage.setScene(scene);
                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });


        try {
            System.out.println("Lobby " + LandingController.nickName);
            name = LandingController.nickName;
        } catch (Exception ex){
            ex.printStackTrace();
        }


    }

}
