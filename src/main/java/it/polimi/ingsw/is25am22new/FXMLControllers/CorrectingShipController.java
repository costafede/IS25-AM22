package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CorrectingShipPhaseCommands.DestroyTileCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands.PickStandByComponentTileCommand;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CorrectingShipController extends ShipPhasesController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }

    public void destroyTile(MouseEvent event) {
        ImageView cell = (ImageView) event.getSource();
        int row = GridPane.getRowIndex(cell)!= null ? GridPane.getRowIndex(cell) : 0;
        int col = GridPane.getColumnIndex(cell)!= null ? GridPane.getColumnIndex(cell) : 0;
        Command cmd = new DestroyTileCommand(virtualServer,null);
        cmd.setInput(new ArrayList<>(List.of(String.valueOf(row), String.valueOf(col))));
        if(cmd.isApplicable(model) && cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
    }
}
