package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.PlaceCrewMembersPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.Commands.StringConverter;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a command for placing an astronaut on a shipboard grid within the game.
 * This class extends the AbstractCommand base class and provides specific implementation
 * for validating input, determining applicability, and executing the placement logic.
 */
public class PlaceAstronautCommand extends AbstractCommand {
    public PlaceAstronautCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "PlaceAstronaut";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.PLACECREWMEMBERS) &&
                !model.getShipboard(model.getPlayerName()).allCabinsArePopulated();
    }

    public int getInputLength() {
        return 2;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;

        int row, col;
        Optional<ComponentTile> ct;
        try {
            row = Integer.parseInt(input.getFirst());
            col = Integer.parseInt(input.get(1));
            ct = model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(StringConverter.stringToGridRow(input.getFirst()), StringConverter.stringToGridCol(input.get(1)));

        }
        catch(NumberFormatException e) {
            return false;
        }
        return ConditionVerifier.coordinatesAreNotOutOfBound(row, col, model) &&
                ct.isPresent() &&
                ct.get().isCabin() &&
                ct.get().getCrewNumber() == 0;
    }

    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.placeAstronauts(model.getPlayerName(), StringConverter.stringToGridRow(input.getFirst()), StringConverter.stringToGridCol(input.get(1)));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
