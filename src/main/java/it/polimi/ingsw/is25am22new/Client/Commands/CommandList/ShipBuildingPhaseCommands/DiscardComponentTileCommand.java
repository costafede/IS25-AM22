package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * Represents a command that discards the component tile currently held by a player.
 * This command is particularly applicable during the BUILDING phase of the game and
 * ensures that the player's shipboard is not yet finished and that a tile is available
 * in their hand to discard.
 */
public class DiscardComponentTileCommand extends AbstractCommand {

    public DiscardComponentTileCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "DiscardComponentTile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() != null &&
                !model.getShipboard(model.getPlayerName()).isFinishedShipboard();
    }

    @Override
    public void execute(ClientModel model) {
        try{
            virtualServer.discardComponentTile(model.getPlayerName());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
