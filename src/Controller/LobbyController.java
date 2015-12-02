package Controller;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
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
    private ListView<?> roomList;

    @FXML
    private TextField roomName;

    @FXML
    private Tab pilJoin;

    @FXML
    private Tab pilCreate;

    private int pil;
    public static String roomNameVal;
    private String name;
    @Override
    public void initialize(URL url, ResourceBundle rb){

        DataOutputStream out = LandingController.out;
        DataInputStream in = LandingController.in;

        pilJoin.setOnSelectionChanged(event -> {
            pil=2;
        });
        pilCreate.setOnSelectionChanged(event -> {
            pil=1;
        });

        createRoomButton.setOnAction(event -> {
            roomNameVal = roomName.getText();
            try {
                out.writeInt(pil);
                out.writeUTF(name);
                out.writeUTF(roomNameVal);
                LandingController.client.close();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
            }
        });

        joinRoomButton.setOnAction(event -> {
            try {
                //NOT IMPLEMENTED YET
                //TO DO : Implement join room, gimana cara dapetin list of room dari server sih?
            } catch (Exception e){
                e.printStackTrace();
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
