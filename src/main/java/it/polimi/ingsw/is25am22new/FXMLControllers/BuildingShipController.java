package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyStarsEffect;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class BuildingShipController extends FXMLController implements Initializable {
    @FXML private ImageView coveredTilesHeap;
    @FXML private ImageView tileInHand;
    @FXML private GridPane componentTilesGrid;
    @FXML private GridPane standByComponentsGrid;
    @FXML private ImageView shipboardImage;
    @FXML private ImageView backGround;

    private GalaxyStarsEffect animatedBackground;

    private int numOfRotations; //set it to zero each time a weld is succesfully done

    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (GalaxyTruckerGUI.getClientModel().getGametype() == GameType.TUTORIAL) {
            backGround.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BuildingShipSceneBackground.png")).toString()));
        } else {
            backGround.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BuildingShipSceneBackground2.png")).toString()));
        }

        animatedBackground = new GalaxyStarsEffect(1280, 720);

        if (backGround.getParent() instanceof Pane pane) {
            animatedBackground.setWidth(backGround.getFitWidth());
            animatedBackground.setHeight(backGround.getFitHeight());

            pane.getChildren().add(animatedBackground);

            animatedBackground.toFront();
        }
    }

    /**
     * This method draws the scene in the corresponding fxml file by showing different elements depending on the game type (level 2 or tutorial).
     * It Must be called after having initialized the attributes through the setup method.
     */
    public void drawScene() {
        if(model.getGametype().equals(GameType.LEVEL2)) {
            shipboardImage.setImage(new Image(getClass().getResource("GraficheGioco/cardboard/cardboard-1b.jpg").toExternalForm()));
            // TODO: devo mostrare la clessidra e la flightboard rendendole interagibili
        }
        drawShipInBuildingPhase(model.getShipboard(model.getPlayerName()));
    }

    @FXML
    public void pickCoveredTile(MouseEvent event) {
        // Qui implementerai la logica per selezionare una tessera coperta
        try {
            virtualServer.pickCoveredTile(model.getPlayerName());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            virtualServer.discardComponentTile(model.getPlayerName());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * scorro i tile in client model
     * se è vuoto metto un drag dropped che chiama la weld component tile con num of rotation pari all'attributo in questo controller, che viene settato a zero dopo
     * se non è vuoto disegno il tile lì presente con il giusto numero di rotazioni
     * */
    public void drawShipInBuildingPhase(Shipboard shipboard) {
        if(shipboard.getNickname().equals(model.getPlayerName())) {
            //draw grid
            componentTilesGrid.getChildren().clear();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(i, j);
                    if (ct.isEmpty() || !ConditionVerifier.gridCoordinatesAreNotOutOfBound(i, j, model)) {
                        setDropZone(i, j);
                    } else {
                        StackPane wrapper = getComponentTileImageForGrid(ct.get().getPngName(), ct.get().getNumOfRotations());
                        componentTilesGrid.add(wrapper, j, i);
                    }
                }
            }
            //draw tile in hand
            if(shipboard.getTileInHand() == null) {
                tileInHand.setImage(null);
                numOfRotations = 0;
            }
            else {
                tileInHand.setImage(new Image(getClass().getResource("GraficheGioco/tiles/" + shipboard.getTileInHand().getPngName()).toExternalForm()));
                tileInHand.setRotate(90 * numOfRotations);
            }
        }
        else {
            //draw ship without buttons
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
        if (tileInHand.getImage() == null) return;

        Dragboard db = tileInHand.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(tileInHand.getImage());
        db.setContent(content);

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

    /**
     * Set the cell of the grid pane at the given coordinates as a drop zone to weld a tile
     */
    private void setDropZone(int i, int j) {
        Pane dropZone = new Pane();
        dropZone.setStyle("-fx-background-color: transparent;");
        dropZone.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHgrow(dropZone, Priority.ALWAYS);
        GridPane.setVgrow(dropZone, Priority.ALWAYS);
        componentTilesGrid.add(dropZone, j, i);
        dropZone.setOnDragOver(event -> {
            if (event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        dropZone.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasImage()) {
                try {
                    virtualServer.weldComponentTile(model.getPlayerName(), i, j, numOfRotations);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Returns a StackPane wrapping the image with the given name
     */
    private StackPane getComponentTileImageForGrid(String pngName, int numOfRotations) {
        Image image = new Image(getClass().getResource("GraficheGioco/tiles/" + pngName).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setRotate(90 * numOfRotations);

        StackPane wrapper = new StackPane(imageView);
        wrapper.setStyle("-fx-padding: 5;");

        GridPane.setHgrow(wrapper, Priority.ALWAYS);
        GridPane.setVgrow(wrapper, Priority.ALWAYS);

        imageView.fitWidthProperty().bind(wrapper.widthProperty());
        imageView.fitHeightProperty().bind(wrapper.heightProperty());

        return wrapper;
    }
}