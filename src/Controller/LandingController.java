package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
public class LandingController implements Initializable {

    @FXML
    private Button submitNickname;

    @FXML
    private TextField nickNameTextField;
    private String nickName;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        submitNickname.setOnAction(e -> {
            if (!nickNameTextField.getText().equals("")) {
                nickName = nickNameTextField.getText();
                System.out.println(nickName);
            } else {
                nickNameTextField.setText("John Doe");
            }
        });
    }
}
