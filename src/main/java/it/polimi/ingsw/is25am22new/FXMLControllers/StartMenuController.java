package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyBackground;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The StartMenuController class is a JavaFX controller designed to manage the behavior
 * and transition logic for the start menu scene in the Galaxy Trucker application.
 * It extends the FXMLController to inherit shared GUI and application state management functionality
 * and implements the Initializable interface for initializing scene-specific configurations.
 *
 * This controller is responsible for handling user interactions on the start menu,
 * such as initiating a transition to the "Connect to Server" scene and applying
 * specific graphical styles. It also manages the dynamic background rendering
 * through the GalaxyBackground component.
 */
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
    public void switchToConnectToServerScene(ActionEvent event) {
        try {
            galaxyTruckerGUI.switchToScene("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml");
        } catch (Exception e) {
            System.err.println("Error switching to Connect To Server scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setGalaxyBackground(GalaxyBackground galaxyBackground) {
        this.galaxyBackground = galaxyBackground;
    }
}

