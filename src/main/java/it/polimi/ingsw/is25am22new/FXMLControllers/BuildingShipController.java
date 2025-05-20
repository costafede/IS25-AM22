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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The BuildingShipController class is responsible for managing the logic and UI interactions
 * during the ship-building phase of the game. It extends from FXMLController and implements
 * the Initializable interface to initialize the user interface and its behaviors.
 */
public class BuildingShipController extends FXMLController implements Initializable {
    @FXML private ImageView coveredTilesHeap;
    @FXML private ImageView tileInHand;
    @FXML private GridPane componentTilesGrid;
    @FXML private GridPane standByComponentsGrid;
    @FXML private ImageView shipboardImage;
    @FXML private ImageView backGround;

    private GalaxyStarsEffect animatedBackground;

    /**
     * num of rotations of the tile in hand
     */
    private int numOfRotations; //set it to zero each time a weld is succesfully done

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setup(null, GalaxyTruckerGUI.getClientModel(), GalaxyTruckerGUI.getPrimaryStage() ,GalaxyTruckerGUI.getVirtualServer());
        if (model.getGametype() == GameType.TUTORIAL) {
            backGround.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BuildingShipSceneBackground.png")).toString()));
        } else {
            backGround.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BuildingShipSceneBackground2.png")).toString()));
        }

        /*animatedBackground = new GalaxyStarsEffect(1280, 720);

        if (backGround.getParent() instanceof Pane pane) {
            animatedBackground.setWidth(backGround.getFitWidth());
            animatedBackground.setHeight(backGround.getFitHeight());

            // Inserisce l'animazione come primo elemento del pane (dietro a tutti gli altri elementi)
            pane.getChildren().add(0, animatedBackground);

            // Assicurati che lo sfondo sia dietro tutti gli elementi
            backGround.toBack();
            animatedBackground.toBack();
        }
            animatedBackground.toFront();
        }*/

        drawShipInBuildingPhase(model.getShipboard(model.getPlayerName()));

    }

    /**
     * This method draws the scene in the corresponding fxml file by showing different elements depending on the game type (level 2 or tutorial).
     * It Must be called after having initialized the attributes through the setup method.
     */
    public void drawScene() {
        if(model.getGametype().equals(GameType.LEVEL2)) {
            shipboardImage.setImage(new Image(getClass().getResource("/GraficheGioco/cardboard/cardboard-1b.jpg").toExternalForm()));
            // TODO: devo mostrare la clessidra e la flightboard rendendole interagibili
        }
        drawShipInBuildingPhase(model.getShipboard(model.getPlayerName()));
    }

    @FXML
    public void pickCoveredTile(MouseEvent event) {
        // Qui implementerai la logica per selezionare una tessera coperta
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
            // draw standbytiles
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
            //draw ship without buttons (copy the things done above)
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
        boolean success = false;
        Node gridCell = (Node) event.getSource();
        int i = GridPane.getRowIndex(gridCell) != null ? GridPane.getRowIndex(gridCell) : 0;
        int j = GridPane.getColumnIndex(gridCell) != null ? GridPane.getColumnIndex(gridCell) : 0;
        if (db.hasImage() && ((ImageView) gridCell).getImage() == null && ConditionVerifier.gridCoordinatesAreNotOutOfBound(i,j,model)) { //se non c'è l'immagine nella griglia o se non è out of bound
            new Thread(() -> Platform.runLater(() -> {
                try {
                    virtualServer.weldComponentTile(model.getPlayerName(), i, j, numOfRotations);
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
        int idx = GridPane.getColumnIndex(standByCell);
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
}

