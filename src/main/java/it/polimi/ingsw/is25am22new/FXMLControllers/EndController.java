package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.EndPhaseCommands.QuitCommand;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyBackground;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EndController extends FXMLController {

    @FXML
    private TableView<Map.Entry<String, Integer>> leaderboardTable;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> playerColumn;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Number> scoreColumn;

    @FXML
    private Button quitButton;

    @FXML
    private StackPane rootPane;

    private GalaxyBackground galaxyBackground;

    /**
     * Initializes the controller and sets up the leaderboard table.
     * This method is automatically called after the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        playerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        scoreColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()));

        // Add galaxy background programmatically
        Platform.runLater(this::setupGalaxyBackground);

        setup(null, GalaxyTruckerGUI.getClientModel(), GalaxyTruckerGUI.getPrimaryStage(), GalaxyTruckerGUI.getVirtualServer());

        // Leaderboard table setup
        leaderboardTable.setPlaceholder(new javafx.scene.control.Label("No scores available"));

        if(model.getLeaderboard() != null)
            setClientAndScores(model.getLeaderboard());
    }

    /**
     * Sets up the galaxy background with animated stars.
     * This is called after the FXML has been fully loaded.
     */
    private void setupGalaxyBackground() {
        if (rootPane != null) {
            // Create the galaxy background with the proper size
            galaxyBackground = new GalaxyBackground(1280, 720);

            // Insert it as the first child to be behind everything else
            rootPane.getChildren().add(0, galaxyBackground);

            // Make sure it resizes with the pane
            galaxyBackground.widthProperty().bind(rootPane.widthProperty());
            galaxyBackground.heightProperty().bind(rootPane.heightProperty());
        }
    }


    /**
     * Sets the client and scores for the leaderboard.
     * This method retrieves the leaderboard data from the model and populates the table.
     */

    public void setClientAndScores(Map<String, Integer> leaderboard) {

        Map<String, Integer> scores = model.getLeaderboard();

        if(scores == null)
            scores = leaderboard;

        // Sort scores in descending order and create observable list for table
        ObservableList<Map.Entry<String, Integer>> leaderboardData = FXCollections.observableArrayList(
            scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList())
        );

        leaderboardTable.setItems(leaderboardData);
    }

    /**
     * Handles the quit button action.
     * Closes the game client and exits the application.
     */
    @FXML
    private void handleQuitButton() {
        // Stop the galaxy background animation before closing
        if (galaxyBackground != null) {
            galaxyBackground.stopAnimation();
        }

        Command cmd = new QuitCommand(virtualServer, null);

        if (cmd.isApplicable(model)) {
            try {
                new Thread (() -> {
                    try {
                        cmd.execute(model);
                    } catch (Exception e){
                        ///  TODO
                    }
                }).start();
            } catch (Exception e) {
                System.err.println("Error quitting game: " + e.getMessage());
            } finally {
                Platform.exit();
            }
        } else {
            // If client is null, just exit the application
            System.out.println("Error: galaxy background is null");
            Platform.exit();
        }
    }
}
