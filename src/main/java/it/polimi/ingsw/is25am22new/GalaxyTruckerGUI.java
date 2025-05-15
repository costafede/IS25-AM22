package it.polimi.ingsw.is25am22new;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ClientModelObserver;
import it.polimi.ingsw.is25am22new.FXMLControllers.BuildingShipController;
import it.polimi.ingsw.is25am22new.FXMLControllers.CardPhaseController;
import it.polimi.ingsw.is25am22new.FXMLControllers.GalaxyBackground;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.application.Application;
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

public class GalaxyTruckerGUI extends Application implements ClientModelObserver {

    private final BuildingShipController buildingShipController = new BuildingShipController();
    private final CardPhaseController cardPhaseController = new CardPhaseController();
    private static ClientModel clientModel;

    public static void setClientModel(ClientModel model) {
        clientModel = model;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
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

    @Override
    public void updateGame(ClientModel model) {

    }

    @Override
    public void updateStopHourglass() {

    }

    @Override
    public void updateStartHourglass(int hourglassSpot) {

    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        switch(gamePhase.getPhaseType()){
            case BUILDING -> buildingShipController.drawScene();
            case CARD -> cardPhaseController.drawScene();
        }
    }

    @Override
    public void updateBank(Bank bank) {

    }

    @Override
    public void updateCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {

    }

    @Override
    public void updateUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {

    }

    @Override
    public void updateShipboards(Map<String, Shipboard> shipboards) {

    }

    @Override
    public void updateFlightboard(Flightboard flightboard) {

    }

    @Override
    public void updateShipboard(Shipboard shipboard) {
        switch (clientModel.getGamePhase().getPhaseType()) {
            case BUILDING -> buildingShipController.drawShipInBuildingPhase(shipboard);
            case PLACECREWMEMBERS -> buildingShipController.drawShipInPlaceMembersPhase(shipboard);
            case CORRECTINGSHIP -> buildingShipController.drawShipInCorrectingShipPhase(shipboard);
            case CARD -> cardPhaseController.drawShip();
        }
    }

    @Override
    public void updateDeck(List<AdventureCard> deck) {

    }

    @Override
    public void updateCurrPlayer(String player) {

    }

    @Override
    public void updateCurrCard(AdventureCard currCard) {

    }

    @Override
    public void updateDices(Dices dices) {

    }

    @Override
    public void updateGameStartMessageReceived(boolean gameStartMessageReceived) {

    }
}