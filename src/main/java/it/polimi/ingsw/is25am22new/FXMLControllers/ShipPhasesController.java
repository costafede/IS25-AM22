package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CorrectingShipPhaseCommands.DestroyTileCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands.FinishBuildingCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyStarsEffect;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;

public abstract class ShipPhasesController extends FXMLController{

    @FXML protected GridPane componentTilesGrid;
    @FXML protected ImageView rocketImage;
    @FXML protected ImageView background;
    @FXML protected ImageView player1ShipImage;
    @FXML protected ImageView player2ShipImage;
    @FXML protected ImageView player3ShipImage;
    @FXML protected ImageView myShipImage;
    @FXML protected Label player1Name;
    @FXML protected Label player2Name;
    @FXML protected Label player3Name;
    @FXML protected AnchorPane level2FlightboardPane;
    @FXML protected AnchorPane tutorialFlightboardPane;
    @FXML protected GridPane player1ShipGrid;
    @FXML protected GridPane player2ShipGrid;
    @FXML protected GridPane player3ShipGrid;
    @FXML protected Label metalScrapCounter;
    @FXML protected Label metalScrapCounter1;
    @FXML protected Label metalScrapCounter2;
    @FXML protected Label metalScrapCounter3;
    @FXML protected ImageView scrapImage;
    @FXML protected ImageView scrapImage1;
    @FXML protected ImageView scrapImage2;
    @FXML protected ImageView scrapImage3;

    protected GalaxyStarsEffect animatedBackground;
    protected Map<String, Image> colorToRocketImage = new HashMap<>();
    //Maps other players to their grid
    protected Map<String, GridPane> playerToShipGrid = new HashMap<>();
    protected Map<String, Label> playerToScrap = new HashMap<>();
    protected AnchorPane flightboardPane;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setup(null, GalaxyTruckerGUI.getClientModel(), GalaxyTruckerGUI.getPrimaryStage() ,GalaxyTruckerGUI.getVirtualServer());
        Image shipImage;
        if (model.getGametype() == GameType.TUTORIAL) {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BlueBackground.png")).toString()));
            shipImage = new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cardboard/cardboard-1.jpg")).toString());
            tutorialFlightboardPane.setVisible(true);
            level2FlightboardPane.setVisible(false);
            tutorialFlightboardPane.toFront();
            flightboardPane = tutorialFlightboardPane;
        } else {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/PurpleBackground.png")).toString()));
            shipImage = new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cardboard/cardboard-1b.jpg")).toString());
            level2FlightboardPane.setVisible(true);
            tutorialFlightboardPane.setVisible(false);
            level2FlightboardPane.toFront();
            flightboardPane = level2FlightboardPane;
        }

        List<ImageView> shipImages = new ArrayList<>(List.of(player1ShipImage, player2ShipImage, player3ShipImage));
        List<Label> shipLabels = new ArrayList<>(List.of(player1Name, player2Name, player3Name));
        List<GridPane> shipGrids = new ArrayList<>(List.of(player1ShipGrid, player2ShipGrid, player3ShipGrid));
        initializeRocketColorMap();
        for(Shipboard ship : model.getShipboards().values()) {
            if(!ship.getNickname().equals(model.getPlayerName())) {
                ImageView imageView = shipImages.removeFirst();
                Label label = shipLabels.removeFirst();
                imageView.setImage(shipImage);
                label.setText(ship.getNickname());
                GridPane gridPane = shipGrids.removeFirst();
                playerToShipGrid.put(ship.getNickname(), gridPane);
            }
            else {
                myShipImage.setImage(shipImage);
                rocketImage.setImage(colorToRocketImage.get(ship.getColor()));
                playerToShipGrid.put(ship.getNickname(), componentTilesGrid);
            }

            drawPlayerShip(ship, playerToShipGrid.get(ship.getNickname()), null);
        }

        animatedBackground = new GalaxyStarsEffect(1280, 720);

        if (background.getParent() instanceof Pane pane) {
            animatedBackground.setWidth(background.getFitWidth());
            animatedBackground.setHeight(background.getFitHeight());

            // Inserisce l'animazione come primo elemento del pane (dietro a tutti gli altri elementi)
            pane.getChildren().add(0, animatedBackground);

            // Assicurati che lo sfondo sia dietro tutti gli elementi
            animatedBackground.toBack();
            background.toBack();
        }
    }

    protected void initializeRocketColorMap() {
        colorToRocketImage.put("yellow", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/yellowRocket.png")).toString()));
        colorToRocketImage.put("blue", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/blueRocket.png")).toString()));
        colorToRocketImage.put("green", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/greenRocket.png")).toString()));
        colorToRocketImage.put("red", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/redRocket.png")).toString()));
    }

    protected void drawPlayerShip(Shipboard ship, GridPane tilesGrid, GridPane standByGrid) {
        for(Node child : tilesGrid.getChildren()) {
            int i = GridPane.getRowIndex(child) != null ? GridPane.getRowIndex(child) : 0;
            int j = GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0;
            Optional<ComponentTile> ct = ship.getComponentTileFromGrid(i, j);
            if (ct.isPresent() && ConditionVerifier.gridCoordinatesAreNotOutOfBound(i, j, model)) {
                drawComponentTileImageForGrid((ImageView) child, ct.get().getPngName(), ct.get().getNumOfRotations());
            }
            else {
                ((ImageView) child).setImage(null);
            }
        }
    }

    protected void drawComponentTileImageForGrid(ImageView imageView, String pngName, int numOfRotations) {
        Image image = new Image(getClass().getResource("/GraficheGioco/tiles/" + pngName).toExternalForm());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setRotate(90 * numOfRotations);
        imageView.setImage(image);
    }

    @FXML
    public void handleDragDoneRocket(DragEvent event) {
        if (event.getTransferMode() == TransferMode.MOVE) {
            rocketImage.setImage(null); // Rimuove l'immagine dalla sorgente
        }
        event.consume();
    }

    @FXML
    public void handleDragDetectedRocket(MouseEvent event) {
        if (rocketImage.getImage() == null) return;

        Dragboard db = rocketImage.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(rocketImage.getImage());
        content.putString(rocketImage.getId());
        db.setContent(content);

        event.consume();
    }

    @FXML
    public void handleDragDroppedRocket(DragEvent event) {
        Dragboard db = event.getDragboard();
        ImageView position = (ImageView) event.getSource();
        int idx = Integer.parseInt(position.getId());
        String sourceId = db.getString();
        boolean success = false;
        Command cmd = new FinishBuildingCommand(virtualServer,null);
        cmd.setInput(new ArrayList<>(List.of(String.valueOf(idx))));
        if(cmd.isApplicable(model) && cmd.isInputValid(model) && db.hasImage() && db.hasString() && position.getImage() == null && sourceId.equals("rocket")) {
            new Thread(() -> Platform.runLater(() -> cmd.execute(model))).start();
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    public void handleDragOverRocket(DragEvent event) {
        if (event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    public void updateFlightBoard(Flightboard flightboard) {
        for(Node child : flightboardPane.getChildren()) {
            int childId;
            try {
                childId = Integer.parseInt(child.getId());
            } catch (NumberFormatException e) {
                childId = -1;
            }
            if(child.getId() != null && childId >= 0 && childId <= 6) {
                ((ImageView) child).setImage(null);
            }
        }

        for(String player : flightboard.getPositions().keySet()) {
            Image rocket = colorToRocketImage.get(model.getShipboard(player).getColor());
            int position = flightboard.getStartingPositions().indexOf(flightboard.getPositions().get(player)) + 1; //converts absolute positions (6, 3, 1, 0) to starting positions (1, 2, 3, 4)
            for(Node child : flightboardPane.getChildren()) {
                int childId;
                try {
                    childId = Integer.parseInt(child.getId());
                } catch (NumberFormatException e) {
                    childId = -1;
                }
                if(child.getId() != null && childId == position) {
                    ((ImageView) child).setImage(rocket);
                }
            }
        }

    }

    public void destroyTile(MouseEvent event) {
        ImageView cell = (ImageView) event.getSource();
        int row = GridPane.getRowIndex(cell)!= null ? GridPane.getRowIndex(cell) : 0;
        int col = GridPane.getColumnIndex(cell)!= null ? GridPane.getColumnIndex(cell) : 0;
        Command cmd = new DestroyTileCommand(virtualServer,null);
        cmd.setInput(new ArrayList<>(List.of(String.valueOf(row + 5), String.valueOf(col + 4))));
        if(cmd.isApplicable(model) && cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
    }
}
