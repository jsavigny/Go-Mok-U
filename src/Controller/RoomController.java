package Controller;

/**
 * Created by Julio Savigny on 12/2/2015.
 */
import Main.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
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
        LobbyController.state="Play";
        listPlayer = RoomWaitController.pL;


        boolean found=false;
        int idx=0;
        while ((!found)&&!(listPlayer.getItems().isEmpty())){
            System.out.println(LobbyController.user.getName());
            if (listPlayer.getItems().get(idx).equalsIgnoreCase(LobbyController.user.getName())){
                found=true;
            }
            //turnString.setText(listPlayer.getItems().get(turn)+" Turn");
            turnString.setVisible(false);
            idx++;
        }
        turnString.setText(list);
        LobbyController.user.setIdInRoom(idx);
        System.out.println("User id in room = "+LobbyController.user.getIdInRoom());
        roomName.setText(LobbyController.user.getRoomName());
        congratString.setVisible(false);
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
                    int x=GridPane.getRowIndex(pane);
                    int y=GridPane.getColumnIndex(pane);
                    System.out.println("TURN = "+turn);
                    if ((pane.getId().equalsIgnoreCase("kosong"))&&(turn==LobbyController.user.getIdInRoom())){
                        Main.socketClient.setArgument("move");
                        Main.socketClient.setArgument(String.valueOf(x));
                        Main.socketClient.setArgument(String.valueOf(y));
                        Main.socketClient.setArgument(String.valueOf(turn));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String listenServer=null;
                                final boolean[] stopListen = new boolean[1];
                                while (true){
                                    System.out.println(stopListen[0]);
                                    stopListen[0] =false;
                                    try {
                                        listenServer = Main.socketClient.getIs().readLine();
                                        if (listenServer.equalsIgnoreCase("ok")){
                                            Platform.runLater(new Runnable(){
                                                @Override public void run(){
                                                    drawPane(x,y,turn);
                                                    turn=nexTurn(turn);
                                                    stopListen[0] =true;
                                                    pane.setId("tidak kosong");
                                                }
                                            });
                                        } else {
                                            System.out.println(listenServer);
                                        }
                                        Thread.sleep(250);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (stopListen[0]){
                                        break;
                                    }

                                }
                            }
                        }).start();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                String listenServer=null;
                while (true) {
                    if (!(turn == LobbyController.user.getIdInRoom())) {
                        try {
                            listenServer = Main.socketClient.getIs().readLine();
                            System.out.println(listenServer);
                            if (listenServer.equalsIgnoreCase("draw")) {
                                int id = Integer.parseInt(Main.socketClient.getIs().readLine());
                                int x = Integer.parseInt(Main.socketClient.getIs().readLine());
                                int y = Integer.parseInt(Main.socketClient.getIs().readLine());
                                System.out.println(id + " " + x + " " + y);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        drawPane(x, y, id);
                                        turn = nexTurn(turn);
                                    }
                                });
                            } else {
                                System.out.println("else " + listenServer);
                            }
                            Thread.sleep(1000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        System.out.println("Giliranku!");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
    private int nexTurn(int t){
        if (t==3){
            t=1;
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
            color="yellow";
        } else if (whose%4==1){
            color="red";
        } else if (whose%4==2){
            color="green";
        } else if (whose%4==3){
            color="blue";
        }
        result.setStyle("-fx-background-color: "+color);
    }
}
