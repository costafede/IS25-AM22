package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The ShowRemainingSecondsCommand class represents a specific command within the game
 * to display the remaining time for the current phase to the user interface. It extends
 * the AbstractCommand to inherit common command behaviors and to integrate with the
 * VirtualServer and ViewAdapter for execution and visualization.
 *
 * Responsibilities of this command include:
 * - Determining applicability based on the current game phase, game type, and hourglass status.
 * - Triggering the appropriate UI method to display the remaining seconds.
 */
public class ShowRemainingSecondsCommand extends AbstractCommand {
    public ShowRemainingSecondsCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowRemainingSeconds";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getGametype().equals(GameType.LEVEL2) &&
                model.getHourglass() != null;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showRemainingSeconds(model);
    }
}
