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
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/styles.css")).toExternalForm());
            }
        });

        startGameButton.setDisable(true);
        readyButton.setDisable(true);
        exitButton.setDisable(true);
    }

    @FXML
    public void toggleReady(ActionEvent event) {
        if(readyButton.getParent() instanceof HBox parent) {
            Image readyImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/ReadyText.png")).toString());
            ImageView readyImageView = new ImageView(readyImage);
            parent.getChildren().remove(readyButton);
            parent.getChildren().remove(exitButton);
            parent.getChildren().addFirst(readyImageView);
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void startGame(ActionEvent event) throws IOException {
        galaxyTruckerGUI.getVirtualServer().startGameByHost(galaxyTruckerGUI.getPlayerName());
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
                errorLabel.setVisible(false);
            }
        } catch (Exception e) {
            System.out.println("Error setting game type or max players: " + e.getMessage());
        }

        readyButton.setDisable(false);
        exitButton.setDisable(false);
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
            readyButton.setDisable(false);
            exitButton.setDisable(false);
        }
    }

    public void showError(String message) {
        errorLabel.setVisible(true);
        errorLabel.setText(message);
    }

    public void displayLobbyUpdate(List<String> playerList, Map<String, Boolean> readyStatus, String gameType, boolean isHost){
        List<String> newList = new ArrayList<>();
        boolean allReady = true;
        for(String player : playerList) {
            if(player.equals(GalaxyTruckerGUI.getClientModel().getPlayerName())) {
                newList.addFirst(player + " (You)");
            } else {
                String readyStatusText = readyStatus.get(player) ? "READY" : "NOT READY";
                newList.add(player + " (" + readyStatusText + ")");
            }
            if(!readyStatus.get(player) || playerList.size() != maxPlayers) // seen only by host because players don receive maxPlayers info
                allReady = false;
        }

        if(allReady && playerList.size() != 1) {
            statusTextArea.appendText("ALL PLAYERS ARE READY! PREPARE TO START!\n");
            startGameButton.setDisable(false);
        }

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