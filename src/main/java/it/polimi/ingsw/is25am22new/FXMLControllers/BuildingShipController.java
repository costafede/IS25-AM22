package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class BuildingShipController implements Initializable {
    @FXML
    private Canvas galaxyBackground;

    @FXML
    private ImageView coveredTilesHeap;

    private GalaxyBackground animatedBackground;

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
    public void pickCoveredTile() {
        System.out.println("Tile clicked - pickCoveredTile() called");
        // Qui implementerai la logica per selezionare una tessera coperta
    }

    public void drawScene(){

    }

    public void drawShipInBuildingPhase(Shipboard shipboard) {
    }

    public void drawShipInPlaceMembersPhase(Shipboard shipboard) {
    }

    public void drawShipInCorrectingShipPhase(Shipboard shipboard) {
    }
}