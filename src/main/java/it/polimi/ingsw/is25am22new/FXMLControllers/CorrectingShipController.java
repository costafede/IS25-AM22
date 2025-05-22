package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CorrectingShipPhaseCommands.DestroyTileCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands.PickStandByComponentTileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class CorrectingShipController extends ShipPhasesController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientModel model = GalaxyTruckerGUI.getClientModel();
        super.initialize(url, resourceBundle);
        updateFlightBoard(model.getFlightboard());
        List<Label> scrapsLabels = new ArrayList<>(List.of(metalScrapCounter1, metalScrapCounter2, metalScrapCounter3));
        List<ImageView> scrapsImages = new ArrayList<>(List.of(scrapImage1, scrapImage2, scrapImage3));
        for(Shipboard ship : model.getShipboards().values()) {
            if(!ship.getNickname().equals(model.getPlayerName())) {
                Label label = scrapsLabels.removeFirst();
                label.setVisible(true);
                ImageView imageView = scrapsImages.removeFirst();
                imageView.setVisible(true);
                playerToScrap.put(ship.getNickname(), label);
            }
            else {
                playerToScrap.put(ship.getNickname(), metalScrapCounter);
            }
        }
        if(model.getShipboard(model.getPlayerName()).isFinishedShipboard())
            rocketImage.setImage(null);
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

    public void drawShipInCorrectingShipPhase(Shipboard shipboard) {
        drawPlayerShip(shipboard, playerToShipGrid.get(shipboard.getNickname()), null);
        Label counterLabel = playerToScrap.get(shipboard.getNickname());
        counterLabel.setText(String.valueOf(shipboard.getDiscardedTiles()));
    }
}
