package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.PlaceCrewMembersPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.Commands.StringConverter;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.Optional;

public class PlaceBrownAlienCommand extends AbstractCommand {
    public PlaceBrownAlienCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "PlaceBrownAlien";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.PLACECREWMEMBERS) &&
                !model.getShipboard(model.getPlayerName()).allCabinsArePopulated() &&
                !model.getShipboard(model.getPlayerName()).isBrownAlienPresent();
    }

    public int getInputLength() {
        return 2;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;

        int row, col;
        try {
            row = Integer.parseInt(input.getFirst());
            col = Integer.parseInt(input.get(1));
        }
        catch(NumberFormatException e) {
            return false;
        }
        Optional<ComponentTile> ct = model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(StringConverter.stringToGridRow(input.getFirst()), StringConverter.stringToGridCol(input.get(1)));
        return ConditionVerifier.coordinatesAreNotOutOfBound(row, col, model) &&
                ct.isPresent() &&
                ct.get().getCrewNumber() == 0 &&
                model.getShipboard(model.getPlayerName()).isAlienPlaceable(StringConverter.stringToGridRow(input.getFirst()), StringConverter.stringToGridCol(input.get(1)), "brown");
    }

    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.placeBrownAlien(model.getPlayerName(), StringConverter.stringToGridRow(input.getFirst()), StringConverter.stringToGridCol(input.get(1)));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
