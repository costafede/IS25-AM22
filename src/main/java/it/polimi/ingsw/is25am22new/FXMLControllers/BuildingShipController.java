package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildingShipController extends FXMLController implements Initializable {
    @FXML
    private Canvas galaxyBackground;

    @FXML
    private ImageView coveredTilesHeap;

    private GalaxyBackground animatedBackground;
    @FXML
    private ImageView tileInHand;
    @FXML
    private GridPane gridPane;

    private int numOfRotations; //set it to zero each time a weld is succesfully done

    public BuildingShipController(VirtualServer virtualServer, ClientModel model, Stage primaryStage) {
        super(virtualServer, model, primaryStage);
    }

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

    public void drawScene(){

    }

    public void drawShipInBuildingPhase(Shipboard shipboard) {
        AtomicInteger newi = new AtomicInteger();
        AtomicInteger newj = new AtomicInteger();
        gridPane.getChildren().clear();
        /*
        * scorro i tile in client model
        * se è vuoto metto un drag dropped che chiama la weld component tile con num of rotation pari all'attributo in questo controller, che viene settato a zero dopo
        * se non è vuoto disegno il tile lì presente con il giusto numero di rotazioni
        *
        * */
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(i,j);
                if(ct.isEmpty()){
                    Pane dropZone = new Pane();
                    dropZone.setStyle("-fx-background-color: transparent;");
                    dropZone.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    GridPane.setHgrow(dropZone, Priority.ALWAYS);
                    GridPane.setVgrow(dropZone, Priority.ALWAYS);
                    gridPane.add(dropZone, i, j);
                    dropZone.setOnDragOver(event -> {
                        event.acceptTransferModes(TransferMode.ANY);
                        event.consume();
                    });
                    newi.set(i);
                    newj.set(j);
                    dropZone.setOnDragDropped(event -> {
                        try {
                            virtualServer.weldComponentTile(model.getPlayerName(), newi.get(), newj.get(), numOfRotations);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        event.setDropCompleted(true);
                        event.consume();
                    });
                    numOfRotations = 0;
                }
                else {
                    ImageView imageView = new ImageView(new Image("/GraficheGioco/tiles/" + ct.get().getPngName()));
                    imageView.setFitWidth(205);
                    imageView.setFitHeight(130);
                    imageView.setPreserveRatio(true);
                    gridPane.add(imageView, i, j);
                    gridPane.setHgap(4);
                }
            }
        }
        primaryStage.show();
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
}