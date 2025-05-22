package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands.*;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyStarsEffect;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The BuildingShipController class is responsible for managing the logic and UI interactions
 * during the ship-building phase of the game. It extends from FXMLController and implements
 * the Initializable interface to initialize the user interface and its behaviors.
 */
public class BuildingShipController extends ShipPhasesController implements Initializable {

    @FXML private GridPane standByComponentsGrid;
    @FXML private ImageView tileInHand;
    @FXML private GridPane player1ShipStandByTiles;
    @FXML private GridPane player2ShipStandByTiles;
    @FXML private GridPane player3ShipStandByTiles;
    @FXML private GridPane uncoveredTilesGrid;
    @FXML private VBox uncoveredTilesPopUp;
    @FXML private ImageView hourglassImage0;
    @FXML private ImageView hourglassImage1;
    @FXML private ImageView hourglassImage2;
    @FXML private Label timerLabel1;
    @FXML private Label timerLabel2;
    @FXML private VBox cardPile0;
    @FXML private VBox cardPile1;
    @FXML private VBox cardPile2;
    @FXML private ImageView pileImage0;
    @FXML private ImageView pileImage1;
    @FXML private ImageView pileImage2;


    private Map<String, GridPane> playerToShipStandByTiles = new HashMap<>();
    /**
     * num of rotations of the tile in hand
     */
    private int numOfRotations; //set it to zero each time a weld is succesfully done
    private int hourglassSpot = 0;
    private int secondsLeft = 60;

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
            pileImage0.setVisible(false);
            pileImage1.setVisible(false);
            pileImage2.setVisible(false);
        } else {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/PurpleBackground.png")).toString()));
            shipImage = new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cardboard/cardboard-1b.jpg")).toString());
            level2FlightboardPane.setVisible(true);
            tutorialFlightboardPane.setVisible(false);
            level2FlightboardPane.toFront();
            flightboardPane = level2FlightboardPane;
            pileImage0.setVisible(true);
            drawCardPile(cardPile0, 0);
            pileImage1.setVisible(true);
            drawCardPile(cardPile1, 1);
            pileImage2.setVisible(true);
            drawCardPile(cardPile2, 2);
        }

        List<ImageView> shipImages = new ArrayList<>(List.of(player1ShipImage, player2ShipImage, player3ShipImage));
        List<Label> shipLabels = new ArrayList<>(List.of(player1Name, player2Name, player3Name));
        List<GridPane> shipGrids = new ArrayList<>(List.of(player1ShipGrid, player2ShipGrid, player3ShipGrid));
        List<GridPane> shipStandByTiles = new ArrayList<>(List.of(player1ShipStandByTiles, player2ShipStandByTiles, player3ShipStandByTiles));
        initializeRocketColorMap();
        for(Shipboard ship : model.getShipboards().values()) {
            if(!ship.getNickname().equals(model.getPlayerName())) {
                ImageView imageView = shipImages.removeFirst();
                Label label = shipLabels.removeFirst();
                imageView.setImage(shipImage);
                label.setText(ship.getNickname());
                GridPane gridPane = shipGrids.removeFirst();
                GridPane gridPane1 = shipStandByTiles.removeFirst();
                playerToShipGrid.put(ship.getNickname(), gridPane);
                playerToShipStandByTiles.put(ship.getNickname(), gridPane1);
            }
            else {
                myShipImage.setImage(shipImage);
                rocketImage.setImage(colorToRocketImage.get(ship.getColor()));
                playerToShipGrid.put(ship.getNickname(), componentTilesGrid);
                playerToShipStandByTiles.put(ship.getNickname(), standByComponentsGrid);
            }

            drawPlayerShip(ship, playerToShipGrid.get(ship.getNickname()), playerToShipStandByTiles.get(ship.getNickname()));
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

    @FXML
    public void pickCoveredTile(MouseEvent event) {
        if(tileInHand.getImage() != null) return;
        Command cmd = new PickCoveredTileCommand(virtualServer, null);
        if(cmd.isApplicable(model))
            new Thread(() -> cmd.execute(model)).start();
    }

    @FXML
    public void rotateTileInHand(MouseEvent event) {
        numOfRotations++;
        ((ImageView) event.getSource()).setRotate(90 * numOfRotations);
    }

    @FXML
    public void discardComponentTile(MouseEvent event) {
        if(tileInHand.getImage() == null) return;
        Command cmd = new DiscardComponentTileCommand(virtualServer, null);
        if(cmd.isApplicable(model)) {
            numOfRotations = 0;
            tileInHand.setImage(null);
            new Thread(() -> cmd.execute(model)).start();
        }
    }

    public void drawShipInBuildingPhase(Shipboard shipboard) {
        drawPlayerShip(shipboard, playerToShipGrid.get(shipboard.getNickname()), playerToShipStandByTiles.get(shipboard.getNickname()));
    }

    protected void drawPlayerShip(Shipboard ship, GridPane tilesGrid, GridPane standByGrid) {
        super.drawPlayerShip(ship, tilesGrid, standByGrid);
        for(Node child : standByGrid.getChildren()) {
            int j = GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0;
            Optional<ComponentTile> ct = ship.getStandbyComponent()[j];
            if (ct.isPresent()) {
                drawComponentTileImageForGrid((ImageView) child, ct.get().getPngName(), ct.get().getNumOfRotations());
            }
            else {
                ((ImageView) child).setImage(null);
            }
        }
    }

    @FXML
    private void handleDragDetectedTileInHand(MouseEvent event) {
        Image image = tileInHand.getImage();
        if(tileInHand.getImage() == null || model.getShipboard(model.getPlayerName()).isFinishedShipboard()) return;

        Dragboard db = tileInHand.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(tileInHand.getImage());
        content.putString(tileInHand.getId());
        db.setContent(content);
        db.setDragView(image, image.getWidth() / 100, image.getHeight() / 100 );

        event.consume();
    }

    @FXML
    private void handleDragDoneTileInHand(DragEvent event) {
        if (event.getTransferMode() == TransferMode.MOVE) {
            tileInHand.setImage(null); // Rimuove l'immagine dalla sorgente
            numOfRotations = 0;
        }
        event.consume();
    }

    @FXML
    private void handleDragDroppedGrid(DragEvent event) {
        Dragboard db = event.getDragboard();
        int rotations = numOfRotations;
        boolean success = false;
        String sourceId = db.getString();
        Node gridCell = (Node) event.getSource();
        int i = GridPane.getRowIndex(gridCell) != null ? GridPane.getRowIndex(gridCell) : 0;
        int j = GridPane.getColumnIndex(gridCell) != null ? GridPane.getColumnIndex(gridCell) : 0;
        Command cmd = new WeldComponentTileCommand(virtualServer, null);
        i+=5;   //adapt row and column to the command format
        j+=4;
        List<String> input = new ArrayList<>(List.of(String.valueOf(i),String.valueOf(j),String.valueOf(rotations)));
        cmd.setInput(input);
        if (db.hasImage() && db.hasString() && sourceId.equals("tile") && cmd.isApplicable(model) && cmd.isInputValid(model)) { //se non c'è l'immagine nella griglia o se non è out of bound
            new Thread(() -> cmd.execute(model)).start();
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void handleDragDroppedStandByArea(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        String sourceId = db.getString();
        Command cmd = new StandByComponentTileCommand(virtualServer,null);
        if (db.hasImage() && db.hasString() && sourceId.equals("tile") && cmd.isApplicable(model)) {
            new Thread(() -> cmd.execute(model)).start();
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void handleDragOverGrid(DragEvent event) {
        if (event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    @FXML
    private void pickStandByTile(MouseEvent event) {
        if(tileInHand.getImage() != null) return;

        ImageView standByCell = (ImageView) event.getTarget();
        int idx = GridPane.getColumnIndex(standByCell)!= null ? GridPane.getColumnIndex(standByCell) : 0;
        Command cmd = new PickStandByComponentTileCommand(virtualServer,null);
        cmd.setInput(new ArrayList<>(List.of(String.valueOf(idx))));
        if(cmd.isApplicable(model) && cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
    }

    public void drawTileInHand(ComponentTile ct) {
        if(ct == null) {
            tileInHand.setImage(null);
            numOfRotations = 0;
        }
        else {
            tileInHand.setImage(new Image(getClass().getResource("/GraficheGioco/tiles/" + ct.getPngName()).toExternalForm()));
            tileInHand.setRotate(90 * numOfRotations);
        }
    }

    public void updateFlightBoard(Flightboard flightboard) {
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

    public void updateUncoveredComponentTiles(List<ComponentTile> tiles) {
        uncoveredTilesGrid.getChildren().clear();
        uncoveredTilesGrid.getColumnConstraints().clear();

        for(int i = 0; i < tiles.size(); i++) {
            ComponentTile tile = tiles.get(i);
            ImageView tileImage = new ImageView(new Image(getClass().getResource("/GraficheGioco/tiles/" + tile.getPngName()).toExternalForm()));
            tileImage.setFitWidth(100);
            tileImage.setFitHeight(100);
            tileImage.setPreserveRatio(true);
            tileImage.setSmooth(true);
            tileImage.setCache(true);
            tileImage.setOnMouseClicked(this::pickUncoveredTile);
            uncoveredTilesGrid.add(tileImage, i,0);
        }
    }

    public void openUncoveredTilesPopUp(MouseEvent event) {
        uncoveredTilesPopUp.setLayoutY(50);
        uncoveredTilesPopUp.toFront();
    }

    public void closePopUp(ActionEvent event) {
        Node source = ((Node) event.getSource()).getParent().getParent(); //I get the Vbox (the pop up)
        source.setLayoutY(2000);
    }

    public void pickUncoveredTile(MouseEvent event) {
        if(tileInHand.getImage() != null || model.getShipboard(model.getPlayerName()).isFinishedShipboard()) return;

        ImageView standByCell = (ImageView) event.getTarget();
        int idx = GridPane.getColumnIndex(standByCell)!= null ? GridPane.getColumnIndex(standByCell) : 0;
        Command cmd = new PickUncoveredTileCommand(virtualServer,null);
        cmd.setInput(new ArrayList<>(List.of(String.valueOf(idx))));
        if(cmd.isApplicable(model) && cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();

    }

    public void flipHourglass(ActionEvent event) {
        Command cmd = new FlipHourglassCommand(virtualServer, null);
        if(cmd.isApplicable(model)) {
            ((Button) event.getSource()).setDisable(true);
            new Thread(() -> cmd.execute(model)).start();
        }
    }

    public void updateStopHourglass() {
        if(hourglassSpot == 1) {
            timerLabel1.setText(null);
        }
        else if (hourglassSpot == 2) {
            timerLabel2.setText(null);
        }
    }

    public void updateStartHourglass(int hourglassSpot) {
        this.hourglassSpot = hourglassSpot;
        secondsLeft = 60;
        if(hourglassSpot == 1) {
            hourglassImage0.setVisible(false);
            hourglassImage1.setVisible(true);
            displayTimer(timerLabel1);
        }
        else if (hourglassSpot == 2) {
            hourglassImage1.setVisible(false);
            hourglassImage2.setVisible(true);
            displayTimer(timerLabel2);
        }
    }

    private void displayTimer(Label timerLabel) {
        Timeline timeline = new Timeline();

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            secondsLeft--;
            timerLabel.setText(String.valueOf(secondsLeft));
            if (secondsLeft <= 0) {
                timeline.stop();
                timerLabel.setText(null);
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(secondsLeft);
        timeline.play();
    }

    public void cardPileClicked(MouseEvent event) {
        if(tileInHand.getImage() != null) return;
        int idx = Integer.parseInt(((ImageView) event.getSource()).getId());
        switch (idx) {
            case 0 -> openCardPilePopUp(cardPile0);
            case 1 -> openCardPilePopUp(cardPile1);
            case 2 -> openCardPilePopUp(cardPile2);
            default -> {}
        }
    }

    private void openCardPilePopUp(VBox cardPile) {
        cardPile.setLayoutY(291);
        cardPile.toFront();
    }

    private void drawCardPile(VBox pileBox, int idx) {
        List<AdventureCard> pile = model.getCardPiles().get(idx).getCards();
        AnchorPane pane = null;
        for(Node child : pileBox.getChildren()) {
            if(child.getId() != null && child.getId().equals("pane"))
                pane = (AnchorPane) child;
        }
        for(int i = 0; i < pile.size(); i++) {
            for(Node child : pane.getChildren())
                if(Integer.parseInt(child.getId()) == i)
                    ((ImageView) child).setImage(new Image(getClass().getResource("/GraficheGioco/cards/" + pile.get(i).getPngName()).toExternalForm()));
        }
    }
}
