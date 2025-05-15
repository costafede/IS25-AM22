package it.polimi.ingsw.is25am22new.FXMLControllers;

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

public class StartMenuController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

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
        // Stop the galaxy animation before switching scenes
        if (galaxyBackground != null) {
            galaxyBackground.stopAnimation();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setGalaxyBackground(GalaxyBackground galaxyBackground) {
        this.galaxyBackground = galaxyBackground;
    }
}