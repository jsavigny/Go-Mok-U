package Controller;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
import Main.Main;
<<<<<<< HEAD
=======
import javafx.application.Platform;
>>>>>>> origin/master
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
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

    @FXML
    private Text congratString;

    @FXML
    private ListView<String> listPlayer;

    private int countPlayer;

    private int turn=1;

    private String fromServer=null;

    @Override
    public void initialize(URL url, ResourceBundle rb){
<<<<<<< HEAD

        try{
            ArrayList<String> userArrayList = new ArrayList<>();
            int userListSize = Integer.parseInt(Main.socketClient.getIs().readLine());
            countPlayer=userListSize;
            for (int i=0;i<userListSize;i++){
                String toAdd = Main.socketClient.getIs().readLine();
                userArrayList.add(toAdd);
            }
            ObservableList<String> oList = FXCollections.observableArrayList(userArrayList);
            listPlayer.setItems(oList);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        boolean found=false;
        int idx=0;
        while (!found){
=======
        LobbyController.state="Play";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            int userListSize;
                            ArrayList<String> userArrayList = new ArrayList<>();
                            String fromServer = Main.socketClient.getIs().readLine();
                            if (fromServer.equalsIgnoreCase("listUser")){
                                userListSize = Integer.parseInt(Main.socketClient.getIs().readLine());
                            } else {
                                userListSize = Integer.parseInt(fromServer);
                            }
                            countPlayer=userListSize;
                            for (int i=0;i<userListSize;i++){
                                String toAdd = Main.socketClient.getIs().readLine();
                                userArrayList.add(toAdd);
                            }
                            ObservableList<String> oList = FXCollections.observableArrayList(userArrayList);
                            listPlayer.setItems(oList);
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }).start();


        boolean found=false;
        int idx=0;
        while ((!found)&&!(listPlayer.getItems().isEmpty())){
>>>>>>> origin/master
            System.out.println(LobbyController.user.getName());
            if (listPlayer.getItems().get(idx).equalsIgnoreCase(LobbyController.user.getName())){
                found=true;
            }
<<<<<<< HEAD
=======
            //turnString.setText(listPlayer.getItems().get(turn)+" Turn");
            turnString.setVisible(false);
>>>>>>> origin/master
            idx++;
        }
        LobbyController.user.setIdInRoom(idx);
        roomName.setText(LobbyController.user.getRoomName());
        congratString.setVisible(false);
        turnString.setVisible(false);
        turnString.setText(listPlayer.getItems().get(turn)+" Turn");
        for (int i=0; i<20; i++){
            for (int j=0; j<20; j++){
                Pane pane = new Pane();
                pane.setId("kosong");
                pane.getStyleClass().add("game-grid-cell");
                if (i == 0) {
                    pane.getStyleClass().add("first-column");
                }
                if (j == 0) {
                    pane.getStyleClass().add("first-row");
                }
                pane.setOnMouseClicked(e->{
                    System.out.println("Baris "+board.getRowIndex(pane)+" Kolom "+board.getColumnIndex(pane)+" Berhasil di klik!");
                    if ((!pane.getId().equalsIgnoreCase("kosong"))&&(turn==LobbyController.user.getIdInRoom())){
                        Main.socketClient.setArgument("move");
                        Main.socketClient.setArgument(String.valueOf(GridPane.getRowIndex(pane)));
                        Main.socketClient.setArgument(String.valueOf(GridPane.getColumnIndex(pane)));
                        while(fromServer.equalsIgnoreCase(null)){
                            try {
                                Thread.sleep(500);
                                fromServer=Main.socketClient.getIs().readLine();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
                board.add(pane,i,j);
            }
        }
        /*while (!(turn==LobbyController.user.getIdInRoom())){
            try {
                fromServer=Main.socketClient.getIs().readLine();
                if (!fromServer.equalsIgnoreCase(null)){

                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }*/

    }
    private int nexTurn(int t){
        if (t==countPlayer){
            t=0;
        } else {
            t++;
        }
        return t;
    }
    private void drawPane(int x,int y,int whose){
        ObservableList<Node> childrens = board.getChildren();
        Node result= new Pane();
        for(Node node : childrens) {
            if(board.getRowIndex(node) == x && board.getColumnIndex(node) == y) {
                result = node;
                break;
            }
        }
        String color="brown";
        if (whose%4==0){
            color="red";
        } else if (whose%4==1){
            color="blue";
        } else if (whose%4==2){
            color="purple";
        } else if (whose%4==3){
            color="green";
        }
        result.setStyle("-fx-background-color: "+color);
    }
}
