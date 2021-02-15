package client;

import client.controllers.AuthController;
import client.controllers.ChatController;
import client.models.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatGB extends Application {

    private Network network;
    private Stage primaryStage;

    private Stage authStage;
    private ChatController chatController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        network = new Network();
        network.connect();

        openAuthDialog();
        createChatDialog();
        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                network.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
    }

    private void openAuthDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ChatGB.class.getResource("auth-view.fxml"));

        Parent root = loader.load();
        authStage = new Stage();

        authStage.setTitle("Authentication");
        authStage.setScene(new Scene(root));
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        authStage.show();

        AuthController authController = loader.getController();
        authController.setNetwork(network);
        authController.setChatGB(this);
    }

    private void createChatDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ChatGB.class.getResource("chat-view.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("Проклятый чат");
        primaryStage.setScene(new Scene(root));

        chatController = loader.getController();
        chatController.setNetwork(network);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void openChat() {
        authStage.close();
        chatController.fieldToUsername.setText(network.getUsername());
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);
        network.waitMessage(chatController);
    }


}