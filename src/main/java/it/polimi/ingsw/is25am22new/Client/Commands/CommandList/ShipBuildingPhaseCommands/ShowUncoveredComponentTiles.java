package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The ShowUncoveredComponentTiles command is responsible for visualizing
 * the uncovered component tiles during the BUILDING phase of the game.
 * This command interacts with the ViewAdapter to trigger the display of the
 * uncovered component tiles based on the current state of the client model.
 *
 * This command can only be executed when the game is in the BUILDING phase.
 */
public class ShowUncoveredComponentTiles extends AbstractCommand {
    public ShowUncoveredComponentTiles(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowUncoveredComponentTiles";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING);
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showUncoveredComponentTiles(model);
    }
}
