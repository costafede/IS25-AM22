package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * Represents a command in the game for picking an uncovered tile during the
 * building phase. This command handles the interaction between the client model
 * and the virtual server for selecting an uncovered tile.
 *
 * This class extends AbstractCommand to inherit the framework for processing
 * commands, ensuring command validation and execution.
 */
public class PickUncoveredTileCommand extends AbstractCommand {
    public PickUncoveredTileCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "PickUncoveredTile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return  !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() == null &&
                !model.getUncoveredComponentTiles().isEmpty();
    }

    @Override
    public int getInputLength() {
        return 1;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        int index;
        if(!super.isInputValid(model))
            return false;
        try {
            index = Integer.parseInt(input.getFirst());
        }
        catch (NumberFormatException e) {
            return false;
        }
        return index >= 0 && index < model.getUncoveredComponentTiles().size();
    }

    @Override
    public void execute(ClientModel model) {
        String tilePngName = model.getUncoveredComponentTiles().get(Integer.parseInt(input.getFirst())).getPngName();
        try{
            virtualServer.pickUncoveredTile(model.getPlayerName(), tilePngName);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
