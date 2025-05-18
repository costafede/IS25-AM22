package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * StandByComponentTileCommand is responsible for invoking the action of placing a component tile
 * into the standby area of the shipboard during the BUILDING phase of the game.
 * This command facilitates interaction with the VirtualServer to execute the standby action.
 *
 * This command will only be applicable if the game is in the BUILDING phase, the player's
 * shipboard is not complete, there is a tile in hand, and there is at least one empty slot
 * in the standby area of the shipboard.
 */
public class StandByComponentTileCommand extends AbstractCommand {
    public StandByComponentTileCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "StandByComponentTile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        ComponentTile standbyComponent1 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        ComponentTile standbyComponent2 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[1].orElseGet(() -> null);
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() != null &&
                (standbyComponent1 == null || standbyComponent2 == null);
    }

    @Override
    public void execute(ClientModel model) {
        try{
            virtualServer.standbyComponentTile(model.getPlayerName());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
