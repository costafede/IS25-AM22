package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * Represents a command for a player to pick a covered tile during the building phase
 * of the game. This command validates whether the action is applicable based on the
 * current game state and executes the required server interaction to pick a covered tile.
 */
public class PickCoveredTileCommand extends AbstractCommand {
    public PickCoveredTileCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "PickCoveredTile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                !model.getCoveredComponentTiles().isEmpty() &&
                model.getShipboard(model.getPlayerName()).getTileInHand() == null;
    }

    @Override
    public void execute(ClientModel model) {
        try{
            virtualServer.pickCoveredTile(model.getPlayerName());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
