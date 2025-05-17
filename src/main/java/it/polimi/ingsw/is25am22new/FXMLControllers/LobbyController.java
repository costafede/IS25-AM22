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
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends FXMLController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean isReady = false;

    @FXML private ListView<String> playerListView;
    @FXML private Button startGameButton;
    @FXML private Button readyButton;
    @FXML private Button exitButton;
    @FXML private ComboBox<String> gameTypeComboBox;
    @FXML private ComboBox<String> maxPlayersComboBox;
    @FXML private TextArea statusTextArea;
    @FXML private Label errorLabel;
    @FXML private HBox settingsHBox;

    // Sample data for testing
    private final ObservableList<String> players = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize UI components
        playerListView.setItems(players);
        gameTypeComboBox.setItems(FXCollections.observableArrayList("Tutorial", "Level2"));
        maxPlayersComboBox.setItems(FXCollections.observableArrayList("2", "3", "4"));
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

        } else {

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

    @FXML
    public void confirmSettings(ActionEvent actionEvent) {
        String selectedGameType = gameTypeComboBox.getValue();  // Gets the selected item (or null if none)
        String selectedMaxPlayers = maxPlayersComboBox.getValue();  // Gets the selected item (or null if none)

        if(selectedGameType == null || selectedMaxPlayers == null) {
            errorLabel.setText("Please select valid options for game type and max players.");
            return;
        }
        String gameType = selectedGameType.toLowerCase();
        int maxPlayers = Integer.parseInt(selectedMaxPlayers);
        try {
            galaxyTruckerGUI.getVirtualServer().setGameType(gameType);
            galaxyTruckerGUI.getVirtualServer().setNumPlayers(maxPlayers);
            if (settingsHBox.getParent() instanceof VBox parent) {
                parent.getChildren().remove(settingsHBox);
                parent.getChildren().remove(errorLabel);
            }
        } catch (Exception e) {
            System.out.println("Error setting game type or max players: " + e.getMessage());
        }
    }

    public void displayConnectionResult(boolean isHost) {
        if(isHost) {
            errorLabel.setText("You are the host! Setup parameters for the game!");
        } else {
            errorLabel.setText("You've successfully joined the lobby!");
            if (startGameButton.getParent() instanceof HBox parent) {
                parent.getChildren().remove(startGameButton);
            }
            if (settingsHBox.getParent() instanceof VBox parent) {
                parent.getChildren().remove(settingsHBox);
            }
        }
    }
}