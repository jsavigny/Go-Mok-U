package Controller;

import com.sun.xml.internal.bind.v2.TODO;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

        DataOutputStream out = LandingController.out;
        DataInputStream in = LandingController.in;

        pilJoin.setOnSelectionChanged(event -> {
            if (pilJoin.isSelected()) {
                pil = 2;
            } else {
                pil = 1;
            }
        });

        createRoomButton.setOnAction(event -> {
            roomNameVal = roomName.getText();
            try {
                out.writeInt(pil);
                out.writeUTF(name);
                out.writeUTF(roomNameVal);
                System.out.println(in.readUTF()); //Flush Welcome
                System.out.println(in.readInt()); //CountPlayer?
                try{
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
                out.writeInt(pil);
                out.writeUTF(name);
                out.writeUTF(roomNameVal);
                try{
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

        refreshRoomList.setOnAction(event -> {
            try {
                roomArrayList = new ArrayList<String>();
                System.out.println(in.readUTF()); //Flush Welcome
                int roomListSize = in.readInt();
                for (int i = 0; i < roomListSize; i++) {
                    roomArrayList.add(in.readUTF());
                    System.out.println(roomArrayList.get(i));
                }
                roomList.setItems(FXCollections.observableArrayList(roomArrayList));
                refreshRoomList.setVisible(false);
                refreshRoomList.setDisable(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        try {
            System.out.println("Lobby " + LandingController.nickName);
            name = LandingController.nickName;
            out.writeUTF(name);
            //Harus pindah baris setelah scan int
        } catch (Exception ex){
            ex.printStackTrace();
        }


    }

}
