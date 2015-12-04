package Controller;

import Main.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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
    public static ListView<String> pL;
    public void xRefreshUserList(){
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
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        LobbyController.state="RoomWait";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String listenServer=null;
                while (true){
                    try {
                        Main.socketClient.setArgument("displayUser");
                        Main.socketClient.setArgument(LobbyController.user.getRoomName());
                        Thread.sleep(500);
                        listenServer = Main.socketClient.getIs().readLine();
                        if (listenServer.equalsIgnoreCase("listUser")){
                            Platform.runLater(new Runnable(){
                                @Override public void run(){
                                    xRefreshUserList();
                                }
                            });
                        } else {
                            System.out.println("ListenServer "+listenServer);
                        }
                        Thread.sleep(2000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (countPlayer>=3){
                        pL=playerList;
                        break;
                    }
                }
            }
        }).start();
        playerWarning.setVisible(false);

        playButton.setOnAction(event -> {
            Main.socketClient.setArgument("play");
            Main.socketClient.setArgument(LobbyController.user.getRoomName());
            if (countPlayer>=3){
                try{
                    LobbyController.state="Transition";
                    Thread.sleep(500);
                    String listen=Main.socketClient.getIs().readLine();
                    System.out.println(listen);
                    if (listen.equalsIgnoreCase("okPlay")) {
                        Parent root = FXMLLoader.load(getClass().getResource("../View/room.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setTitle("Play!");
                        stage.setScene(scene);
                        stage.show();
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } else {
                playerWarning.setVisible(true);
                System.out.println("TIDAK CUKUP PEMAIN");
            }
        });

    }

}