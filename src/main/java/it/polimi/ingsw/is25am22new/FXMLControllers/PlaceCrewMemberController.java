package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CorrectingShipPhaseCommands.DestroyTileCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.PlaceCrewMembersPhaseCommands.PlaceAstronautCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.PlaceCrewMembersPhaseCommands.PlaceBrownAlienCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.PlaceCrewMembersPhaseCommands.PlacePurpleAlienCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands.StandByComponentTileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlaceCrewMemberController extends ShipPhasesController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientModel model = GalaxyTruckerGUI.getClientModel();
        super.initialize(url, resourceBundle);
        updateFlightBoard(model.getFlightboard());
        updateFlightBoard(model.getFlightboard());
        List<Label> scrapsLabels = new ArrayList<>(List.of(metalScrapCounter1, metalScrapCounter2, metalScrapCounter3));
        List<ImageView> scrapsImages = new ArrayList<>(List.of(scrapImage1, scrapImage2, scrapImage3));
        for(Shipboard ship : model.getShipboards().values()) {
            if(!ship.getNickname().equals(model.getPlayerName())) {
                Label label = scrapsLabels.removeFirst();
                label.setVisible(true);
                label.setText(String.valueOf(ship.getDiscardedTiles()));
                ImageView imageView = scrapsImages.removeFirst();
                imageView.setVisible(true);
                playerToScrap.put(ship.getNickname(), label);
            }
            else {
                playerToScrap.put(ship.getNickname(), metalScrapCounter);
            }
        }
        rocketImage.setVisible(false);
    }

    public void drawShipInPlaceMembersPhase(Shipboard shipboard) {
        drawPlayerShip(shipboard, playerToShipGrid.get(shipboard.getNickname()), null);
    }

    @FXML
    private void handleDragDetectedCrewMember(MouseEvent event) {
        ImageView imageView = ((ImageView) event.getSource());

        Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        content.putString(imageView.getId());
        db.setContent(content);
        db.setDragView(imageView.getImage(), imageView.getImage().getWidth() / 100, imageView.getImage().getHeight() / 100 );

        event.consume();
    }

    public void handleDragDroppedCrewMember(DragEvent event) {
        ImageView cell = (ImageView) event.getSource();
        int row = GridPane.getRowIndex(cell)!= null ? GridPane.getRowIndex(cell) : 0;
        int col = GridPane.getColumnIndex(cell)!= null ? GridPane.getColumnIndex(cell) : 0;
        Dragboard db = event.getDragboard();
        boolean success = false;
        String sourceId = db.getString();
        Command cmd = selectCommandType(sourceId);
        cmd.setInput(new ArrayList<>(List.of(String.valueOf(row + 5), String.valueOf(col + 4))));

        if (db.hasImage() && db.hasString() &&  cmd.isApplicable(model) && cmd.isInputValid(model)) {
            new Thread(() -> cmd.execute(model)).start();
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    private Command selectCommandType(String sourceId) {
        Command cmd  = null;
        switch (sourceId) {
            case "astronaut" -> cmd = new PlaceAstronautCommand(virtualServer,null);
            case "brown" -> cmd = new PlaceBrownAlienCommand(virtualServer, null);
            case "purple" -> cmd = new PlacePurpleAlienCommand(virtualServer, null);
        }
        return cmd;
    }

    public void handleDragOverCrewMember(DragEvent event) {
        if (event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }
}
