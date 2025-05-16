package it.polimi.ingsw.is25am22new.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends FXMLController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean isReady = false;

    @FXML
    private ListView<String> playerListView;

    @FXML
    private TextArea statusTextArea;

    @FXML
    private Button readyButton;

    @FXML
    private Button exitButton;

    // Sample data for testing
    private final ObservableList<String> players = FXCollections.observableArrayList(
            "Player 1 (You)",
            "Player 2",
            "Player 3"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize UI components
        playerListView.setItems(players);
        statusTextArea.setText("Waiting for all players to be ready...");

        // Apply CSS styles for the scene
        readyButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/it/polimi/ingsw/is25am22new/styles.css").toExternalForm());
            }
        });
    }

    @FXML
    public void toggleReady(ActionEvent event) {
        isReady = !isReady;
        if (isReady) {
            readyButton.setText("NOT READY");
            readyButton.getStyleClass().remove("lobby-button");
            readyButton.getStyleClass().add("ready-button");
            statusTextArea.appendText("\nYou are ready!");
        } else {
            readyButton.setText("READY");
            readyButton.getStyleClass().remove("ready-button");
            readyButton.getStyleClass().add("lobby-button");
            statusTextArea.appendText("\nYou are not ready.");
        }
    }

    @FXML
    public void exitLobby(ActionEvent event) throws IOException {
        // Navigate back to the start menu
        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Methods to be called from networking code
    public void addPlayer(String playerName) {
        players.add(playerName);
        statusTextArea.appendText("\n" + playerName + " has joined the lobby.");
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
        statusTextArea.appendText("\n" + playerName + " has left the lobby.");
    }

    public void updatePlayerStatus(String playerName, boolean isReady) {
        statusTextArea.appendText("\n" + playerName + " is " + (isReady ? "ready" : "not ready") + ".");
    }

    @FXML
    public void testBuildingShip(ActionEvent event) throws IOException {
        // Navigazione alla schermata BuildingShip per test
        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/is25am22new/BuildingShip.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/it/polimi/ingsw/is25am22new/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}