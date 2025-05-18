package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The FlipHourglassCommand class represents a command to flip the hourglass
 * in the game. This command is applicable only during certain game phases
 * and under specific conditions. It ensures that the hourglass can only be
 * flipped when it is in a valid state, as defined by the game rules.
 *
 * This command interacts with the VirtualServer to perform the action of
 * flipping the hourglass and updates the game state accordingly.
 * The applicability of the command is determined by the client model.
 */
public class FlipHourglassCommand extends AbstractCommand {
    public FlipHourglassCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "FlipHourglass";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        if(model.getHourglass() == null) //timer has not been initialized yet
            return false;
        if(model.getGametype().equals(GameType.TUTORIAL) ||
                !model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING))
            return false;
        String player = model.getPlayerName();
        if(model.getHourglassSpot() == 1 && !model.getShipboard(player).isFinishedShipboard()) //player wants to flip the hourglass on the last spot but he's not finished yet
            return false;
        if(model.isHourglassActive() || model.getHourglassSpot() >= 2) //timer has not finished yet or hourglass is not flippable anymore
            return false;
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.flipHourglass();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
