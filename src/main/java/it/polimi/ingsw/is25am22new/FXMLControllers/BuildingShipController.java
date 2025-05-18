package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyBackground;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
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
    @FXML private Canvas galaxyBackground;
    @FXML private ImageView coveredTilesHeap;
    @FXML private ImageView tileInHand;
    @FXML private GridPane componentTilesGrid;
    @FXML private GridPane standByComponentsGrid;
    @FXML private ImageView shipboardImage;
    @FXML private ImageView backGround;

    private GalaxyBackground animatedBackground;

    private int numOfRotations; //set it to zero each time a weld is succesfully done

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inizializzazione dello sfondo animato
        if (galaxyBackground != null) {
            galaxyBackground.setWidth(1280);
            galaxyBackground.setHeight(720);
            animatedBackground = new GalaxyBackground(1280, 720);

            if (galaxyBackground.getParent() != null) {
                int index = galaxyBackground.getParent().getChildrenUnmodifiable().indexOf(galaxyBackground);
                ((javafx.scene.layout.StackPane) galaxyBackground.getParent()).getChildren().set(index, animatedBackground);
            }
        }

        if(GalaxyTruckerGUI.getClientModel().getGametype().equals("tutorial")) {
            backGround.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BuildingShipSceneBackground.png")).toString()));
        } else {
            backGround.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BuildingShipSceneBackground2.png")).toString()));
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
    }

    @FXML
    public void pickCoveredTile(ActionEvent event) {
        // Qui implementerai la logica per selezionare una tessera coperta
        try {
            virtualServer.pickCoveredTile(model.getPlayerName());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rotateTileInHand(ActionEvent event) {
        numOfRotations++;

    }

    /*
     * scorro i tile in client model
     * se è vuoto metto un drag dropped che chiama la weld component tile con num of rotation pari all'attributo in questo controller, che viene settato a zero dopo
     * se non è vuoto disegno il tile lì presente con il giusto numero di rotazioni
     * */
    public void drawShipInBuildingPhase(Shipboard shipboard) {
        componentTilesGrid.getChildren().clear();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(i,j);
                if(ct.isEmpty() && ConditionVerifier.gridCoordinatesAreNotOutOfBound(i, j, model)){
                    setDropZone(i, j);
                    numOfRotations = 0;
                }
                else {
                    StackPane wrapper = getComponentTileImage(ct.get().getPngName(), ct.get().getNumOfRotations());
                    componentTilesGrid.add(wrapper, j, i);
                }
            }
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

    private Node getNodeAtPosition(GridPane gridPane, int row, int column) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null; // Return null if no node is found at the specified position
    }

    //debug method
    public void debug(){
        Shipboard shipboard = new Shipboard("red", "io", null);
        drawShipInBuildingPhase(shipboard);
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
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });
        dropZone.setOnDragDropped(event -> {
            try {
                virtualServer.weldComponentTile(model.getPlayerName(), i, j, numOfRotations);
            } catch (IOException e) {
                e.printStackTrace();
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    /**
     * Returns a StackPane wrapping the image with the given name
     */
    private StackPane getComponentTileImage(String pngName, int numOfRotations) {
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