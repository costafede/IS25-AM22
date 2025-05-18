package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The ShowFlightboardCommand class is responsible for executing the action
 * of displaying the flightboard within the game. It extends the AbstractCommand
 * class and implements the required behavior to interact with the associated
 * ViewAdapter for visual updates.
 *
 * The command encapsulates the functionality to present the game's flightboard,
 * relying on client model data. It ensures that this visualization can be
 * triggered appropriately during gameplay.
 */
public class ShowFlightboardCommand extends AbstractCommand {
    public ShowFlightboardCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowFlightboard";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showFlightboard(model);
    }
}
