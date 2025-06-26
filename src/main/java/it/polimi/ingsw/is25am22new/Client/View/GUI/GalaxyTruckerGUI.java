package it.polimi.ingsw.is25am22new.Client.View.GUI;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ClientModelObserver;
import it.polimi.ingsw.is25am22new.FXMLControllers.*;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.EnhancedClientView;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The GalaxyTruckerGUI class serves as the main graphical user interface for the Galaxy Trucker game.
 * It extends the JavaFX Application class and implements the ClientModelObserver and EnhancedClientView interfaces
 * to handle updates and interactions with the game model and view components.
 *
 * Responsibilities:
 * - Initializes the game client, controllers, and GUI components.
 * - Manages the transition between game scenes and stages.
 * - Observes the model to update the GUI in response to game state changes.
 * - Handles client-server communication feedback for multiplayer capabilities.
 */
public class GalaxyTruckerGUI extends Application implements ClientModelObserver, EnhancedClientView {

    private StartMenuController startMenuController;
    private ConnectToServerController connectToServerController;
    private LobbyController lobbyController;
    private BuildingShipController buildingShipController;
    private CardPhaseController cardPhaseController;
    private CorrectingShipController correctingShipController;
    private PlaceCrewMemberController placeCrewMemberController;
    private EndController endController;

    private static ClientModel clientModel;
    private static Stage primaryStage;
    private static VirtualServer virtualServer;

    /**
     * Entry point of the JavaFX application.
     * Initializes the start menu, sets up the stage, and loads the FXML layout.
     *
     * @param primaryStage the primary window of the application
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        clientModel.addListener(this);
        setPrimaryStage(primaryStage);
        // Create a modified version of the FXML loader that doesn't try to use custom components
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml"));
        Parent root = loader.load();

        // If the FXML loading was successful
        if (root instanceof StackPane) {
            StackPane stackPane = (StackPane) root;

            // Create and add the galaxy background programmatically as the first child
            GalaxyBackground galaxyBackground = new GalaxyBackground(1280, 720);
            stackPane.getChildren().add(0, galaxyBackground);

            try {
                it.polimi.ingsw.is25am22new.FXMLControllers.StartMenuController controller =
                        loader.getController();
                controller.setGalaxyBackground(galaxyBackground);
                controller.setup(this, clientModel, primaryStage, virtualServer);
            } catch (Exception e) {
                System.out.println("Warning: Could not set galaxy background in controller");
            }
        }

        primaryStage.setOnCloseRequest(e -> handle(e));

        Scene scene = new Scene(root, 1280, 720);
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/Icon.png")).toString());
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main method, launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Handles the window close event. If the virtual server is connected, a disconnection request is sent.
     * Otherwise, the application exits immediately.
     *
     * @param we the window event triggered by closing the application
     */
    public void handle(WindowEvent we) {
        if (virtualServer != null) {
            try {
                virtualServer.disconnect();
                System.out.println("Richiesta di disconnessione inviata al server");
            } catch (IOException e) {
                System.err.println("Errore durante la disconnessione: " + e.getMessage());
            }
            we.consume();
        } else {
            System.exit(0);
        }
    }

    /**
     * Called when the full model has been received from the server and the game is ready to start.
     * Transitions the UI to the ship building scene.
     *
     * @param model the updated {@link ClientModel}
     */
    @Override
    public void updateGame(ClientModel model) {
        Platform.runLater(() -> {
            switchToScene("/it/polimi/ingsw/is25am22new/BuildingShip.fxml");
        });
    }

    /**
     * Called when the hourglass has been stopped.
     * Updates the building ship controller if the game is in the BUILDING phase.
     */
    @Override
    public void updateStopHourglass() {
        if(clientModel.getGamePhase().getPhaseType().equals(PhaseType.BUILDING)){
            Platform.runLater(() -> buildingShipController.updateStopHourglass());
        }
    }

    /**
     * Called when the hourglass has been started.
     *
     * @param hourglassSpot the initial spot of the hourglass
     */
    @Override
    public void updateStartHourglass(int hourglassSpot) {
        Platform.runLater(() -> buildingShipController.updateStartHourglass(hourglassSpot));
    }

    /**
     * Called when the game phase changes. Switches the scene based on the new phase type.
     *
     * @param gamePhase the updated {@link GamePhase}
     */
    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        Platform.runLater(() -> {
            switch (clientModel.getGamePhase().getPhaseType()) {
                case CORRECTINGSHIP -> {
                    switchToScene("/it/polimi/ingsw/is25am22new/CorrectingShipPhase.fxml");
                }
                case PLACECREWMEMBERS -> {
                    switchToScene("/it/polimi/ingsw/is25am22new/PlaceCrewMembersPhase.fxml");
                }
                case CARD -> {
                    switchToScene("/it/polimi/ingsw/is25am22new/CardPhase.fxml");
                }
                case END -> {
                    switchToScene("/it/polimi/ingsw/is25am22new/End.fxml");
                }
                default -> System.out.println("Client - Tipo di fase non gestito: " + clientModel.getGamePhase().getPhaseType());
            }
        });
    }


    @Override
    public void updateBank(Bank bank) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            /// TODO case CARD -> Platform.runLater(() -> cardPhaseController.drawBankInCardPhase(bank));
        }
    }
    /**
     * Called when the list of covered component tiles is updated.
     *
     * @param coveredComponentTiles the updated list of covered tiles
     */
    @Override
    public void updateCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {

    }

    /**
     * Called when the list of uncovered component tiles is updated.
     * Delegates the update to the BuildingShipController.
     *
     * @param uncoveredComponentTiles the updated list of uncovered tiles
     */
    @Override
    public void updateUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        Platform.runLater(() -> buildingShipController.updateUncoveredComponentTiles(uncoveredComponentTiles));
    }

    /**
     * Called when all shipboards are updated.
     * Delegates the update of each individual shipboard.
     *
     * @param shipboards a map of player names to their shipboards
     */
    @Override
    public void updateShipboards(Map<String, Shipboard> shipboards) {
        for(Shipboard shipboard : shipboards.values()){
            updateShipboard(shipboard);
        }
    }

    /**
     * Called when the flightboard is updated.
     * Delegates rendering to the appropriate controller based on the current phase.
     *
     * @param flightboard the updated {@link Flightboard}
     */
    @Override
    public void updateFlightboard(Flightboard flightboard) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case BUILDING -> Platform.runLater(() -> buildingShipController.updateFlightBoard(flightboard));
            case CORRECTINGSHIP -> Platform.runLater(() -> correctingShipController.updateFlightBoard(flightboard));
            case PLACECREWMEMBERS -> Platform.runLater(() -> placeCrewMemberController.updateFlightBoard(flightboard));
            case CARD ->  Platform.runLater(() -> cardPhaseController.drawFlightboardInCardPhase(flightboard));
        }
    }

    /**
     * Called when a single shipboard is updated.
     * Delegates rendering to the appropriate controller based on the current phase.
     *
     * @param shipboard the updated {@link Shipboard}
     */
    @Override
    public void updateShipboard(Shipboard shipboard) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case BUILDING -> Platform.runLater(() -> buildingShipController.drawShipInBuildingPhase(shipboard));
            case PLACECREWMEMBERS ->  Platform.runLater(() -> placeCrewMemberController.drawShipInPlaceMembersPhase(shipboard));
            case CORRECTINGSHIP ->  Platform.runLater(() -> correctingShipController.drawShipInCorrectingShipPhase(shipboard));
            case CARD ->  Platform.runLater(() -> cardPhaseController.drawShips());
        }
    }

    /**
     * Called when the deck of adventure cards is updated.
     * Currently not handled in the GUI.
     *
     * @param deck the updated list of {@link AdventureCard}
     */
    @Override
    public void updateDeck(List<AdventureCard> deck) {

    }

    /**
     * Called when the current player changes.
     * Updates the current player info during the card phase.
     *
     * @param player the name of the current player
     */
    @Override
    public void updateCurrPlayer(String player) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case CARD -> Platform.runLater(() -> cardPhaseController.updateCurrPlayerInfo());
        }
    }

    /**
     * Called when the current adventure card changes.
     * Updates the card display during the card phase.
     *
     * @param currCard the current {@link AdventureCard}
     */
    @Override
    public void updateCurrCard(AdventureCard currCard) {
       switch (clientModel.getGamePhase().getPhaseType()) {
           case CARD -> Platform.runLater(() -> cardPhaseController.drawCard());
       }
    }

    /**
     * Called when the dice roll changes.
     * Updates the dice display during the card phase.
     *
     * @param dices the new {@link Dices} values
     */
    @Override
    public void updateDices(Dices dices) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case CARD -> Platform.runLater(() -> cardPhaseController.drawDices());
        }
    }

    /**
     * Called when the initial message signaling the start of the game is received.
     * Currently not handled in the GUI.
     *
     * @param gameStartMessageReceived true if the game start message has been received
     */
    @Override
    public void updateGameStartMessageReceived(boolean gameStartMessageReceived) {

    }

    /**
     * Called when the leaderboard is updated at the end of the game.
     * Updates the end scene with the final scores.
     *
     * @param leaderboard a map of player names to their final scores
     */
    @Override
    public void updateAllLeaderboard(Map<String, Integer> leaderboard) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case END ->
                Platform.runLater(() -> {
                    if (endController != null) {
                        endController.setClientAndScores(leaderboard);
                    }
                });
        }
    }

    /**
     * Called when a full game is loaded from a save.
     * Switches the GUI to the appropriate scene based on the current phase.
     *
     * @param clientModel the fully restored {@link ClientModel}
     */
    @Override
    public void updateAllGameLoaded(ClientModel clientModel) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case BUILDING -> Platform.runLater(() -> switchToScene("/it/polimi/ingsw/is25am22new/BuildingShip.fxml"));
            case PLACECREWMEMBERS ->  Platform.runLater(() -> switchToScene("/it/polimi/ingsw/is25am22new/PlaceCrewMembersPhase.fxml"));
            case CORRECTINGSHIP ->  Platform.runLater(() -> switchToScene("/it/polimi/ingsw/is25am22new/CorrectingShipPhase.fxml"));
            case CARD ->  Platform.runLater(() -> switchToScene("/it/polimi/ingsw/is25am22new/CardPhase.fxml"));
            case END ->  Platform.runLater(() -> switchToScene("/it/polimi/ingsw/is25am22new/End.fxml"));
        }
    }

    /**
     * Updates the tile currently held in hand by the specified player.
     * Only applies to the local player.
     *
     * @param player the name of the player
     * @param ct the {@link ComponentTile} currently in hand
     */
    @Override
    public void updateTileInHand(String player, ComponentTile ct) {
        if(player.equals(clientModel.getPlayerName()))
            Platform.runLater(() -> buildingShipController.drawTileInHand(ct));
    }

    /**
     * Returns the shared {@link ClientModel} used by the GUI.
     *
     * @return the {@link ClientModel}
     */
    public static ClientModel getClientModel() {
        return clientModel;
    }

    /**
     * Returns the reference to the {@link VirtualServer}.
     *
     * @return the {@link VirtualServer}
     */
    public static VirtualServer getVirtualServer() {
        return virtualServer;
    }

    /**
     * Returns the primary JavaFX {@link Stage} of the application.
     *
     * @return the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sets the primary JavaFX stage of the application.
     *
     * @param primaryStage the new primary stage
     */
    public static void setPrimaryStage(Stage primaryStage) {
        GalaxyTruckerGUI.primaryStage = primaryStage;
    }

    /**
     * Updates the lobby screen with the current list of players and their readiness.
     *
     * @param players the list of connected players
     * @param readyStatus a map of player names to their ready status
     * @param gameType the type of game selected
     * @param isHost true if the local player is the host
     */
    @Override
    public void displayLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType, boolean isHost) {
        Platform.runLater(() -> lobbyController.displayLobbyUpdate(players, readyStatus, gameType, isHost));
    }

    /**
     * Displays the result of a connection attempt to the server.
     *
     * @param isHost true if the player is the host
     * @param success true if the connection was successful
     * @param message the error message to show if the connection failed
     */
    @Override
    public void displayConnectionResult(boolean isHost, boolean success, String message) {
        if(success) {
            Platform.runLater(() -> lobbyController.displayConnectionResult(isHost));
        } else {
            Platform.runLater(() -> lobbyController.showError(message));
        }
    }

    /**
     * Displays the result of nickname validation.
     * If the nickname is invalid, an error is shown and the scene is changed back.
     *
     * @param valid true if the nickname is valid
     * @param message the error message to show if invalid
     */
    @Override
    public void displayNicknameResult(boolean valid, String message) {
        if (!valid) {
            Platform.runLater(() ->{
                connectToServerController.showError(message);
                switchToScene("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml");
                connectToServerController.showError(message);
            });
        } else {
            Platform.runLater(() -> {
                switchToScene("/it/polimi/ingsw/is25am22new/Lobby.fxml");
            });
        }
    }

    /**
     * Called when the game starts.
     */
    @Override
    public void displayGameStarted() {
        /*Platform.runLater(() -> {
            switchToScene("/it/polimi/ingsw/is25am22new/BuildingShip.fxml");
        });*/
    }

    /**
     * Displays a full game object from the server.
     *
     * @param game the {@link Game} to display
     */
    @Override
    public void displayGame(Game game) {

    }

    /**
     * Notifies the GUI that a new player has joined the lobby.
     *
     * @param playerName the name of the player who joined
     */
    @Override
    public void displayPlayerJoined(String playerName) {
        Platform.runLater(() -> lobbyController.displayPlayerJoined(playerName));
    }

    /**
     * Returns whether the local nickname has already been validated.
     *
     * @return true if nickname is valid, false otherwise
     */
    @Override
    public boolean isNicknameValid() {
        return false;
    }

    /**
     * Resets the nickname validation status.
     */
    @Override
    public void resetNicknameStatus() {

    }

    /**
     * Switches the main scene to a new FXML layout.
     * Also initializes the corresponding controller using its {@code setup()} method.
     *
     * @param resourcePath the path to the FXML resource
     */
    public void switchToScene(String resourcePath) {
        try {
            java.net.URL resource = getClass().getResource(resourcePath);
            if (resource == null) {
                throw new IllegalArgumentException("FXML file not found: " + resourcePath);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent root = loader.load();

            // First store the controller reference in our controller maps
            // This ensures our controller references exist before any RMI callbacks can happen
            Object controller = loader.getController();
            Consumer<Object> controllerSetter = controllerMap.get(resourcePath);
            if (controllerSetter != null) {
                controllerSetter.accept(controller);
            } else {
                System.err.println("No controller setter found for: " + resourcePath);
            }

            // Now set up the controller with required references
            ((FXMLController) controller).setup(this, clientModel, primaryStage, virtualServer);

            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private final Map<String, Consumer<Object>> controllerMap = Map.of(
            "/it/polimi/ingsw/is25am22new/StartMenu.fxml", this::castStartMenuController,
            "/it/polimi/ingsw/is25am22new/ConnectToServer.fxml", this::castConnectToServerController,
            "/it/polimi/ingsw/is25am22new/Lobby.fxml", this::castLobbyController,
            "/it/polimi/ingsw/is25am22new/BuildingShip.fxml",this::castBuildingShipController,
            "/it/polimi/ingsw/is25am22new/CorrectingShipPhase.fxml",this::castCorrectingShipController,
            "/it/polimi/ingsw/is25am22new/PlaceCrewMembersPhase.fxml",this::castPlaceCrewMembersController,
            "/it/polimi/ingsw/is25am22new/CardPhase.fxml", this::castCardPhaseController,
            "/it/polimi/ingsw/is25am22new/End.fxml", this::castEndController
    );

    private void castPlaceCrewMembersController(Object controller) {
        placeCrewMemberController = (PlaceCrewMemberController) controller;
    }

    private void castStartMenuController(Object controller) {
        startMenuController = (StartMenuController) controller;
    }
    private void castConnectToServerController(Object controller) {
        connectToServerController = (ConnectToServerController) controller;
    }
    private void castLobbyController(Object controller) {
        lobbyController = (LobbyController) controller;
    }
    private void castBuildingShipController(Object controller) {
        buildingShipController = (BuildingShipController) controller;
    }
    private void castCardPhaseController(Object controller) {
        cardPhaseController = (CardPhaseController) controller;
    }
    private void castEndController(Object controller) {
        endController = (EndController) controller;
    }
    private void castCorrectingShipController(Object controller) {
        correctingShipController = (CorrectingShipController) controller;
    }

    public static void setClientModel(ClientModel model) {
        GalaxyTruckerGUI.clientModel = model;
    }

    public void setVirtualServer(VirtualServer server) {
        GalaxyTruckerGUI.virtualServer = server;
    }
}
