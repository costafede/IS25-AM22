package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyBackground;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuController extends FXMLController implements Initializable {

    @FXML
    private Button playButton;

    @FXML
    private GalaxyBackground galaxyBackground;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Apply CSS styles for the scene
        playButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/it/polimi/ingsw/is25am22new/styles.css").toExternalForm());
            }
        });
    }

    @FXML
    public void switchToConnectToServerScene(ActionEvent event) throws IOException {
        galaxyTruckerGUI.switchToScene("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml");
    }

    public void setGalaxyBackground(GalaxyBackground galaxyBackground) {
        this.galaxyBackground = galaxyBackground;
    }
}