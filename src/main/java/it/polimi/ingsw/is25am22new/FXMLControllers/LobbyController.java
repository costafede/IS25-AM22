package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LobbyController extends FXMLController implements Initializable {

    private Stage stage;
    private Scene scene;
    private boolean isHost = false;
    private int currentPlayerCount = 1;
    private int maxPlayers = 4; // Default value, can be changed based on game settings

    @FXML private ListView<String> playerListView;
    @FXML private Button startGameButton;
    @FXML private Button readyButton;
    @FXML private Button exitButton;
    @FXML private ComboBox<String> gameTypeComboBox;
    @FXML private ComboBox<String> maxPlayersComboBox;
    @FXML private TextArea statusTextArea;
    @FXML private Label errorLabel;
    @FXML private HBox settingsHBox;
    @FXML private Label gameTypeLabel;
    @FXML private Label playersLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize UI components
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
        if(readyButton.getParent() instanceof HBox parent) {
            Image readyImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/ReadyText.png")).toString());
            ImageView readyImageView = new ImageView(readyImage);
            parent.getChildren().remove(readyButton);
            parent.getChildren().remove(exitButton);
            parent.getChildren().add(0, readyImageView);
        }
        try {
            galaxyTruckerGUI.getVirtualServer().setPlayerReady(galaxyTruckerGUI.getPlayerName());
        } catch (Exception e) {
            System.out.println("Error setting player ready status: " + e.getMessage());
        }
    }

    @FXML
    public void exitLobby(ActionEvent event) throws IOException {

        galaxyTruckerGUI.getVirtualServer().quit(galaxyTruckerGUI.getPlayerName());

        // Navigate back to the start menu
        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Methods to be called from networking code
    //public void addPlayer(String playerName) {
    //    players.add(playerName);
    //    statusTextArea.appendText("\n" + playerName + " has joined the lobby.");
    //}
    //
    //public void removePlayer(String playerName) {
    //    players.remove(playerName);
    //    statusTextArea.appendText("\n" + playerName + " has left the lobby.");
    //}
    //
    //public void updatePlayerStatus(String playerName, boolean isReady) {
    //    statusTextArea.appendText("\n" + playerName + " is " + (isReady ? "ready" : "not ready") + ".");
    //}

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
        maxPlayers = Integer.parseInt(selectedMaxPlayers);
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
        this.isHost = isHost;
        if(isHost) {
            errorLabel.setText("You are the host! Setup parameters for the game!");
            setPlayersLabel();
        } else {
            errorLabel.setText("You've successfully joined the lobby!");
            setPlayersLabel();
            if (startGameButton.getParent() instanceof HBox parent) {
                parent.getChildren().remove(startGameButton);
            }
            if (settingsHBox.getParent() instanceof VBox parent) {
                parent.getChildren().remove(settingsHBox);
            }
        }
    }

    public void showError(String message) {
        errorLabel.setText(message);
    }

    public void displayLobbyUpdate(List<String> playerList, Map<String, Boolean> readyStatus, String gameType, boolean isHost){
        List<String> newList = new ArrayList<>();
        Boolean allReady = true;
        for(String player : playerList) {
            if(player.equals(GalaxyTruckerGUI.getClientModel().getPlayerName())) {
                newList.add(0, player + " (You)");
            } else {
                String readyStatusText = readyStatus.get(player) ? "READY" : "NOT READY";
                newList.add(player + " (" + readyStatusText + ")");
            }
            if(!readyStatus.get(player))
                allReady = false;
        }

        if(allReady)
            statusTextArea.appendText("ALL PLAYERS ARE READY! PREPARE TO START!\n");

        ObservableList<String> observableList = FXCollections.observableArrayList(newList);
        playerListView.setItems(observableList);
        gameTypeLabel.setText("Game Type: " + gameType.toUpperCase());
        currentPlayerCount = newList.size();
        setPlayersLabel();
    }

    private void setPlayersLabel() {
        if(isHost) {
            playersLabel.setText("Current Players: " + "(" + currentPlayerCount + "/" + maxPlayers + ")");
        } else {
            playersLabel.setText("Current Players: " + "(" + currentPlayerCount + ")");
        }
    }

    public void displayPlayerJoined(String playerName) {
        currentPlayerCount++;
        playerListView.getItems().add(playerName);
        setPlayersLabel();
        statusTextArea.appendText(playerName + " has joined the lobby!\n");
    }
}