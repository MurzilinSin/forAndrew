package client.controllers;

import client.models.Network;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import server.chat.MyServer;
import server.chat.handler.ClientHandler;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatController {

    @FXML
    private TextField textField;
    @FXML
    private TextField fieldToChangeNick;
    @FXML
    private Label textChangeUsername;
    @FXML
    public Label fieldToUsername;
    @FXML
    private TextArea windowChat;

    @FXML
    private ListView<String> usersList;

    private Network network;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd|HH:mm:ss");


    public void setNetwork(Network network) {
        this.network = network;
    }

    @FXML
    void enterMessage() {
        String message = textField.getText().trim();
        if(message.isEmpty()) {
            return;
        }
        appendMessage("Я: " + message);
        try {
            network.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при отправке сообщения");
        }
        textField.clear();
    }

    public void appendMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        windowChat.appendText(dtf.format(now));
        windowChat.appendText(System.lineSeparator());
        windowChat.appendText(message);
        windowChat.appendText(System.lineSeparator());
        windowChat.appendText(System.lineSeparator());
    }
    @FXML
    public void showTextfield(){
        fieldToChangeNick.setVisible(true);
        textChangeUsername.setVisible(false);
        fieldToUsername.setVisible(false);
    }

    @FXML
    public void changeNick() throws SQLException, ClassNotFoundException {
        String newUsername = fieldToChangeNick.getText().trim();
        if(newUsername.isEmpty()){
            Alert error = new Alert(Alert.AlertType.INFORMATION);
            error.setTitle("Ошибка");
            error.setHeaderText(null);
            error.setContentText("Ник пустым быть не может!");
            error.showAndWait();
            return;
        }
        try {
            network.sendMessage("/change "+ newUsername);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при смене ника");
        }
        fieldToChangeNick.clear();
        fieldToChangeNick.setVisible(false);
        textChangeUsername.setVisible(true);
        fieldToUsername.setVisible(true);
        fieldToUsername.setText(newUsername);
    }

}
