package org.example.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientAppController {

    @FXML private TextArea messageArea;
    @FXML private TextArea textBoard;
    @FXML private TextArea userBoard;
    @FXML private Button connect;
    @FXML private Button leave;
    @FXML private Button send;

    private ArrayList<String> online;
    private String username;
    private Client client;
    private String ip = "127.0.0.1";

    @FXML
    void connectBttnPressed(ActionEvent event) throws IOException {
        Socket socket = new Socket(ip, 7777);
        createUsername();
        connect.setDisable(true);
        leave.setDisable(false);
        send.setDisable(false);
        client = new Client(socket,this);
        client.sendMessage(new UserData(username, username + " joined the chat", true, UserData.Message.SYSTEM, online));
        client.listenForMessages();
    }

    @FXML
    void leaveBttnPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your friends will miss you :(", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to leave?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                online.remove(username);
                client.sendMessage(new UserData(username, username + " left the chat", false, UserData.Message.SYSTEM, online));
                client.closeClient();
            }
        });
    }

    @FXML
    void sendBttnPressed(ActionEvent event) {
        String message = messageArea.getText();
        client.sendMessage(new UserData(username, username + ": " + message, true, UserData.Message.USER, online));
        messageArea.clear();
    }

    public void setChatBoardText(String message) {
        textBoard.appendText("\n" + message);
    }

    private void setFonts() {
        textBoard.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 13));
        textBoard.setStyle("-fx-text-fill: gray;");
        userBoard.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 13));
        userBoard.setStyle("-fx-text-fill: gray;");
    }

    public void initialize() {
        online = new ArrayList<>();
        setFonts();
        online.add("nobody's here");
        setUserBoardText();
        userBoard.setEditable(false);
        textBoard.setEditable(false);
        send.setDisable(true);
        leave.setDisable(true);
    }

    public void setUserBoardText() {
        userBoard.clear();
        for (String username : online) {
            userBoard.appendText(username + "\n");
        }
    }

    private void createUsername() {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Create a unique username");
        td.showAndWait();
        username = td.getEditor().getText();
    }

    public void setOnlineUsers(ArrayList<String> online) {
        this.online = online;
    }
}
