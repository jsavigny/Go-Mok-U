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

import java.io.IOException;
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
    public class User{
        private String name;

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public int getIdInRoom() {
            return idInRoom;
        }

        public void setIdInRoom(int idInRoom) {
            this.idInRoom = idInRoom;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isYourTurn() {
            return isYourTurn;
        }

        public void setYourTurn(boolean yourTurn) {
            isYourTurn = yourTurn;
        }

        private boolean isYourTurn;
        private String roomName;
        private int idInRoom;
    }
    public static String state;
    public static User user;

    private int pil;
    private ArrayList<String> roomArrayList;
    public void xRefreshRoomList(){
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
    @Override
    public void initialize(URL url, ResourceBundle rb){
        state="Lobby";
        user = new User();
        pilJoin.setOnSelectionChanged(event -> {
            if (pilJoin.isSelected()) {
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                String listenServer=null;
                while (true){
                    try {
                        Main.socketClient.setArgument("display");
                        Thread.sleep(500);
                        listenServer = Main.socketClient.getIs().readLine();
                        if (listenServer.equalsIgnoreCase("listRoom")){
                            Platform.runLater(new Runnable(){
                                @Override public void run(){
                                    xRefreshRoomList();
                                }
                            });
                        }
                        Thread.sleep(500);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!state.equalsIgnoreCase("Lobby")){
                        break;
                    }
                }
            }
        }).start();

        createRoomButton.setOnAction(event -> {
            user.setRoomName(roomName.getText());
            try {
                try{
                    Main.socketClient.setArgument("create");
                    Main.socketClient.setArgument(user.getRoomName());
                    Parent root = FXMLLoader.load(getClass().getResource("../View/roomWait.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle(user.getRoomName());
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
            user.setRoomName(roomList.getSelectionModel().getSelectedItem());
            try {
                try{
                    Main.socketClient.setArgument("join");
                    Main.socketClient.setArgument(user.getRoomName());
                    Parent root = FXMLLoader.load(getClass().getResource("../View/roomWait.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle(user.getRoomName());
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
            user.setName(LandingController.nickName);
        } catch (Exception ex){
            ex.printStackTrace();
        }


    }

}
