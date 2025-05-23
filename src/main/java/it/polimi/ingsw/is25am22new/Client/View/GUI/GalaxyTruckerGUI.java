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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

    @Override
    public void start(Stage primaryStage) throws IOException {
        clientModel.addListener(this);
        setPrimaryStage(primaryStage);
        // Create a modified version of the FXML loader that doesn't try to use custom components
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml"));
        // Load the FXML content
        Parent root = loader.load();

        // If the FXML loading was successful
        if (root instanceof StackPane) {
            StackPane stackPane = (StackPane) root;

            // Create and add the galaxy background programmatically as the first child
            GalaxyBackground galaxyBackground = new GalaxyBackground(1280, 720);
            stackPane.getChildren().add(0, galaxyBackground);

            // Get the controller and set the background reference
            try {
                it.polimi.ingsw.is25am22new.FXMLControllers.StartMenuController controller =
                        loader.getController();
                controller.setGalaxyBackground(galaxyBackground);
                controller.setup(this, clientModel, primaryStage, virtualServer);
            } catch (Exception e) {
                System.out.println("Warning: Could not set galaxy background in controller");
            }
        }

        Scene scene = new Scene(root, 1280, 720);
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/Icon.png")).toString());
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Called only when the model is initialized server side
     */


    @Override
    public void updateGame(ClientModel model) {
        Platform.runLater(() -> {
            switchToScene("/it/polimi/ingsw/is25am22new/BuildingShip.fxml");
        });
    }

    @Override
    public void updateStopHourglass() {
        if(clientModel.getGamePhase().getPhaseType().equals(PhaseType.BUILDING))
            Platform.runLater(() -> buildingShipController.updateStopHourglass());
    }

    @Override
    public void updateStartHourglass(int hourglassSpot) {
        Platform.runLater(() -> buildingShipController.updateStartHourglass(hourglassSpot));
    }

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

    @Override
    public void updateCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {

    }

    @Override
    public void updateUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        Platform.runLater(() -> buildingShipController.updateUncoveredComponentTiles(uncoveredComponentTiles));
    }

    @Override
    public void updateShipboards(Map<String, Shipboard> shipboards) {
        for(Shipboard shipboard : shipboards.values()){
            updateShipboard(shipboard);
        }
    }

    @Override
    public void updateFlightboard(Flightboard flightboard) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case BUILDING -> Platform.runLater(() -> buildingShipController.updateFlightBoard(flightboard));
            case CORRECTINGSHIP -> Platform.runLater(() -> correctingShipController.updateFlightBoard(flightboard));
            case PLACECREWMEMBERS -> Platform.runLater(() -> placeCrewMemberController.updateFlightBoard(flightboard));
            case CARD ->  Platform.runLater(() -> cardPhaseController.drawFlightboardInCardPhase(flightboard));
        }
    }

    @Override
    public void updateShipboard(Shipboard shipboard) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case BUILDING -> Platform.runLater(() -> buildingShipController.drawShipInBuildingPhase(shipboard));
            case PLACECREWMEMBERS ->  Platform.runLater(() -> placeCrewMemberController.drawShipInPlaceMembersPhase(shipboard));
            case CORRECTINGSHIP ->  Platform.runLater(() -> correctingShipController.drawShipInCorrectingShipPhase(shipboard));
            case CARD ->  Platform.runLater(() -> cardPhaseController.drawShips());
        }
    }

    @Override
    public void updateDeck(List<AdventureCard> deck) {

    }

    @Override
    public void updateCurrPlayer(String player) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case CARD -> Platform.runLater(() -> cardPhaseController.updateCurrPlayerInfo());
        }
    }

    @Override
    public void updateCurrCard(AdventureCard currCard) {
       switch (clientModel.getGamePhase().getPhaseType()) {
           case CARD -> Platform.runLater(() -> cardPhaseController.drawCard());
       }
    }

    @Override
    public void updateDices(Dices dices) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case CARD -> Platform.runLater(() -> cardPhaseController.drawDices());
        }
    }

    @Override
    public void updateGameStartMessageReceived(boolean gameStartMessageReceived) {

    }

    @Override
    public void updateTileInHand(String player, ComponentTile ct) {
        if(player.equals(clientModel.getPlayerName()))
            Platform.runLater(() -> buildingShipController.drawTileInHand(ct));
    }

    public static ClientModel getClientModel() {
        return clientModel;
    }

    public static VirtualServer getVirtualServer() {
        return virtualServer;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        GalaxyTruckerGUI.primaryStage = primaryStage;
    }

    @Override
    public void displayLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType, boolean isHost) {
        Platform.runLater(() -> lobbyController.displayLobbyUpdate(players, readyStatus, gameType, isHost));
    }

    @Override
    public void displayConnectionResult(boolean isHost, boolean success, String message) {
        if(success) {
            Platform.runLater(() -> lobbyController.displayConnectionResult(isHost));
        } else {
            Platform.runLater(() -> lobbyController.showError(message));
        }
    }

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

    @Override
    public void displayGameStarted() {
        /*Platform.runLater(() -> {
            switchToScene("/it/polimi/ingsw/is25am22new/BuildingShip.fxml");
        });*/
    }

    @Override
    public void displayGame(Game game) {

    }

    @Override
    public void displayPlayerJoined(String playerName) {
        Platform.runLater(() -> lobbyController.displayPlayerJoined(playerName));
    }

    @Override
    public boolean isNicknameValid() {
        return false;
    }

    @Override
    public void resetNicknameStatus() {

    }

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
