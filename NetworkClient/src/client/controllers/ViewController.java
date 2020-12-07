package client.controllers;

import client.NetworkClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import client.models.*;

import java.io.IOException;

public class ViewController {

    @FXML
    public ListView<String> usersList;

    @FXML
    private Button sendButton;
    @FXML
    private TextArea chatHistory;
    @FXML
    private TextField textField;

    private Network network;

    @FXML
    public void initialize() {
        usersList.setItems(FXCollections.observableArrayList(NetworkClient.USERS_TEST_DATA));
        sendButton.setOnAction(event -> ViewController.this.sendMessage());
        textField.setOnAction(event -> ViewController.this.sendMessage());
    }
//network.getUsername() +
    private void sendMessage() {
        String message = textField.getText();
        textField.clear();
        if(message.isBlank()){
            return;
        }

        appendMessage( "Я:  " + message);



        try {
            network.getDataOutputStream().writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Ошибка при отправке сообщения";
            NetworkClient.showErrorMessage(e.getMessage(), errorMessage);
        }
    }

//    private void sendOwnMessage() {
//        String message = textField.getText();
//        appendMessage(network.getUsername() + ": " + message);
//        textField.clear();
//
//        try {
//            network.getDataOutputStream().writeUTF(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//            String errorMessage = "Ошибка при отправке сообщения";
//            NetworkClient.showErrorMessage(e.getMessage(), errorMessage);
//        }
//    }


    public void setNetwork(Network network) {
        this.network = network;
    }

    public void appendMessage(String message) {
        chatHistory.appendText(message);
        chatHistory.appendText(System.lineSeparator());
    }

}