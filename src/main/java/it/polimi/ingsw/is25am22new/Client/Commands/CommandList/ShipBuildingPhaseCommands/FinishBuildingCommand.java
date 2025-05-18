package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The FinishBuildingCommand class represents a command issued by a player to indicate
 * that they have completed their shipboard construction during the BUILDING or
 * CORRECTINGSHIP phases of the game. This command ensures the player follows the
 * rules for finalizing their construction.
 *
 * The command is applicable only under specific conditions where:
 * - The game phase is either BUILDING or CORRECTINGSHIP.
 * - The player's shipboard is not already finalized.
 *
 * The input for this command is validated to ensure:
 * - The input consists of a single valid index (1 to 4).
 * - The position associated with the input index is free on the flightboard.
 *
 * Upon execution, the command communicates with the VirtualServer to inform
 * that the player has finished building their shipboard. It handles any exceptions
 * during server communication gracefully by logging the error message.
 */
public class FinishBuildingCommand extends AbstractCommand {
    public FinishBuildingCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "FinishBuilding";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return (model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) ||
                model.getGamePhase().getPhaseType().equals(PhaseType.CORRECTINGSHIP)) &&
                !model.getShipboard(model.getPlayerName()).isFinishedShipboard();
    }

    @Override
    public int getInputLength() {
        return 1;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;

        int idx;
        try {
            idx = Integer.parseInt(input.getFirst()) - 1;
        }
        catch(NumberFormatException e) {
            return false;
        }

        if(idx < 0 || idx > 3) return false;
        int pos = model.getFlightboard().getStartingPositions().get(idx);
        boolean alreadyPresent = model.getFlightboard().getPositions().containsValue(pos);
        return !alreadyPresent;
    }

    //input: one of the four positions: 1, 2, 3, 4 (if it's free)
    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.finishBuilding(model.getPlayerName(), Integer.parseInt(input.getFirst()) - 1);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
