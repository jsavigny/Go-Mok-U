package Controller;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomController implements Initializable {

    @FXML
    private Text turnString;

    @FXML
    private GridPane board;

    @FXML
    private Text roomName;

    @FXML
    private Pane infoPane;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        for (int i=0; i<20; i++){
            for (int j=0; j<20; j++){
                Pane pane = new Pane();
                pane.setOnMouseReleased(e->{
                    System.out.println(GridPane.getRowIndex(pane)+"-"+GridPane.getColumnIndex(pane));
                });

                board.add(pane,i,j);
            }
        }

    }

}
