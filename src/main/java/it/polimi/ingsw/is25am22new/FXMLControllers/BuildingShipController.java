package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyStarsEffect;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The BuildingShipController class is responsible for managing the logic and UI interactions
 * during the ship-building phase of the game. It extends from FXMLController and implements
 * the Initializable interface to initialize the user interface and its behaviors.
 */
public class BuildingShipController extends FXMLController implements Initializable {

    @FXML private ImageView tileInHand;
    @FXML private GridPane componentTilesGrid;
    @FXML private GridPane standByComponentsGrid;
    @FXML private ImageView rocketImage;
    @FXML private ImageView background;
    @FXML private ImageView player1ShipImage;
    @FXML private ImageView player2ShipImage;
    @FXML private ImageView player3ShipImage;
    @FXML private ImageView myShipImage;
    @FXML private Label player1Name;
    @FXML private Label player2Name;
    @FXML private Label player3Name;
    @FXML private AnchorPane level2FlightboardPane;
    @FXML private AnchorPane tutorialFlightboardPane;
    @FXML private GridPane player1ShipGrid;
    @FXML private GridPane player2ShipGrid;
    @FXML private GridPane player3ShipGrid;
    @FXML private GridPane player1ShipStandByTiles;
    @FXML private GridPane player2ShipStandByTiles;
    @FXML private GridPane player3ShipStandByTiles;

    private GalaxyStarsEffect animatedBackground;
    private Map<String, Image> colorToRocketImage = new HashMap<>();
    //Maps other players to their grid and stand by tiles
    private Map<String, GridPane> playerToShipGrid = new HashMap<>();
    private Map<String, GridPane> playerToShipStandByTiles = new HashMap<>();
    /**
     * num of rotations of the tile in hand
     */
    private int numOfRotations; //set it to zero each time a weld is succesfully done

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setup(null, GalaxyTruckerGUI.getClientModel(), GalaxyTruckerGUI.getPrimaryStage() ,GalaxyTruckerGUI.getVirtualServer());
        Image shipImage;
        if (model.getGametype() == GameType.TUTORIAL) {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BlueBackground.png")).toString()));
            shipImage = new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cardboard/cardboard-1.jpg")).toString());
        } else {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/PurpleBackground.png")).toString()));
            shipImage = new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cardboard/cardboard-1b.jpg")).toString());
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
            }

            drawShipInBuildingPhase(ship);
        }

        // TODO: disegna la flightboard giusta, gestisci la clessidra, gestisci le pile le level2 game, gestisci gli uncovered tiles e la finish building

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



    private void initializeRocketColorMap() {
        colorToRocketImage.put("yellow", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/yellowRocket.png")).toString()));
        colorToRocketImage.put("blue", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/blueRocket.png")).toString()));
        colorToRocketImage.put("green", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/greenRocket.png")).toString()));
        colorToRocketImage.put("red", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/redRocket.png")).toString()));
    }

    @FXML
    public void pickCoveredTile(MouseEvent event) {
        new Thread(() -> Platform.runLater(() -> {
            try {
                virtualServer.pickCoveredTile(model.getPlayerName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        })).start();
    }

    @FXML
    public void rotateTileInHand(MouseEvent event) {
        numOfRotations++;
        ((ImageView) event.getSource()).setRotate(90 * numOfRotations);
    }

    @FXML
    public void discardComponentTile(MouseEvent event) {
        numOfRotations = 0;
        tileInHand.setImage(null);
        new Thread(() -> Platform.runLater(() -> {
            try {
                virtualServer.discardComponentTile(model.getPlayerName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        })).start();
    }

    /*
     * scorro i tile in client model
     * se è vuoto metto un drag dropped che chiama la weld component tile con num of rotation pari all'attributo in questo controller, che viene settato a zero dopo
     * se non è vuoto disegno il tile lì presente con il giusto numero di rotazioni
     * */
    public void drawShipInBuildingPhase(Shipboard shipboard) {
        if(shipboard.getNickname().equals(model.getPlayerName())) {
            //draw grid
            for(Node child : componentTilesGrid.getChildren()) {
                int i = GridPane.getRowIndex(child) != null ? GridPane.getRowIndex(child) : 0;
                int j = GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0;
                Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(i, j);
                if (ct.isPresent() && ConditionVerifier.gridCoordinatesAreNotOutOfBound(i, j, model)) {
                    drawComponentTileImageForGrid((ImageView) child, ct.get().getPngName(), ct.get().getNumOfRotations());
                }
                else {
                    ((ImageView) child).setImage(null);
                }
            }
            // draw standby tiles
            for(Node child : standByComponentsGrid.getChildren()) {
                int j = GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0;
                Optional<ComponentTile> ct = shipboard.getStandbyComponent()[j];
                if (ct.isPresent()) {
                    drawComponentTileImageForGrid((ImageView) child, ct.get().getPngName(), ct.get().getNumOfRotations());
                }
                else {
                    ((ImageView) child).setImage(null);
                }
            }
        }
        else {
            // TODO: draw others' ship without buttons (copy the things done above)
        }


    }

    public void drawShipInPlaceMembersPhase(Shipboard shipboard) {
    }

    public void drawShipInCorrectingShipPhase(Shipboard shipboard) {
    }
    /*
     * NOTE PER I METODI DELLE ALTRE UPDATE
     * per il tile in hand se è presente lo disegno e metto un bottone che mi aumenta il numero di rotazioni e lo rendo draggable, altrimenti nulla
     * il bottone per la pick covered tile è statico, c'è sempre
     * per gli stand by se il tile c'è lo disegno e abilito la pick standby on click, se non c'è abilito la standby on drag dropped
     * per gli uncovered creo un bottone che mi apre una lista di tile. Per i tile che ci sono abilito una pickuncovered on mouse click
     * TUTTI I METODI CHE RIMUOVONO IL TILE IN HAND DI MANO DEVONO SETTARE NUM OF ROTATIONS A ZERO
     * */

    @FXML
    private void handleDragDetectedTileInHand(MouseEvent event) {
        Image image = tileInHand.getImage();
        if (tileInHand.getImage() == null) return;

        Dragboard db = tileInHand.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(tileInHand.getImage());
        db.setContent(content);
        db.setDragView(image, image.getWidth() / 6, image.getHeight() /6 );

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
        Node gridCell = (Node) event.getSource();
        int i = GridPane.getRowIndex(gridCell) != null ? GridPane.getRowIndex(gridCell) : 0;
        int j = GridPane.getColumnIndex(gridCell) != null ? GridPane.getColumnIndex(gridCell) : 0;
        if (db.hasImage() && ((ImageView) gridCell).getImage() == null && ConditionVerifier.gridCoordinatesAreNotOutOfBound(i,j,model)) { //se non c'è l'immagine nella griglia o se non è out of bound
            new Thread(() -> Platform.runLater(() -> {
                try {
                    virtualServer.weldComponentTile(model.getPlayerName(), i, j, rotations);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })).start();
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void handleDragDroppedStandByArea(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasImage() && (((ImageView) standByComponentsGrid.getChildren().get(0)).getImage() == null ||
                ((ImageView) standByComponentsGrid.getChildren().get(1)).getImage() == null)) { //se c'è uno spazio vuoto
            new Thread(() -> Platform.runLater(() -> {
                try {
                    virtualServer.standbyComponentTile(model.getPlayerName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })).start();
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
        new Thread(() -> Platform.runLater(() -> {
            try {
                virtualServer.pickStandbyComponentTile(model.getPlayerName(), idx);
            } catch (IOException e) {
                e.printStackTrace();
            }
        })).start();
    }

    /**
     * Returns a StackPane wrapping the image with the given name to put on the grid pane
     */
    private void drawComponentTileImageForGrid(ImageView imageView, String pngName, int numOfRotations) {
        Image image = new Image(getClass().getResource("/GraficheGioco/tiles/" + pngName).toExternalForm());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setRotate(90 * numOfRotations);
        imageView.setImage(image);
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

    /**
     * Metodo temporaneo per switchare alla scena CardPhase per testing
     * @param event evento del click sul bottone
     */
    @FXML
    public void switchToCardPhase(ActionEvent event) {
        try {
            // Notify the server that this player has finished building
            // This should trigger a change in the current player on the server side
            new Thread(() -> {
                try {
                    virtualServer.finishBuilding(model.getPlayerName(), new Random().nextInt(4) % 4);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Errore durante la comunicazione con il server per finishBuilding");
                }
            }).start();

            // Caricamento della scena CardPhase.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/CardPhase.fxml"));
            Parent root = loader.load();

            // Ottenimento della stage corrente
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Creazione di una nuova scene e impostazione nella stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Ferma l'animazione dello sfondo quando si cambia scena
            if (animatedBackground != null) {
                animatedBackground.stopAnimation();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante il caricamento della scena CardPhase.fxml");
        }
    }

    /**
     * Metodo di test per la schermata finale.
     * Permette di visualizzare la pagina End.fxml con dati di test per la leaderboard.
     */
    @FXML
    private void testEndScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/End.fxml"));
            Parent endRoot = loader.load();

            // Otteniamo il controller e impostiamo dei dati di test
            EndController endController = loader.getController();

            // Creiamo una mappa di test con alcuni punteggi
            Map<String, Integer> testScores = new HashMap<>();
            testScores.put("Player 1", 120);
            testScores.put("Player 2", 85);
            testScores.put("Player 3", 150);
            testScores.put("Player 4", 65);

            // Impostiamo i punteggi nel controller
            endController.setClientAndScores(null, testScores);

            // Cambiamo la scena
            Scene scene = new Scene(endRoot);
            Stage stage = (Stage) componentTilesGrid.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore nel caricamento della pagina End.fxml: " + e.getMessage());
        }
    }

    /*@FXML
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
        db.setContent(content);

        event.consume();
    }

    @FXML
    public void handleDragDroppedLevel2(DragEvent event) {
        Dragboard db = event.getDragboard();
        ImageView position = (ImageView) event.getSource();

        boolean success = false;
        if (db.hasImage() && position.getImage() == null) {
            new Thread(() -> Platform.runLater(() -> {
                try {
                    virtualServer.finishBuilding(model.getPlayerName(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })).start();
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }*/
}

