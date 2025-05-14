package it.polimi.ingsw.is25am22new.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenuController {

    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    private Button playButton;

    @FXML
    public void switchToConnectToServerScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}