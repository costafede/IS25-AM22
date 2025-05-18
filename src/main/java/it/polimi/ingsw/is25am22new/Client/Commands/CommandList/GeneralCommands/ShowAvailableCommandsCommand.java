package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The ShowAvailableCommandsCommand class is responsible for displaying a list of
 * commands available to the user. This command retrieves the available commands
 * from the client model and uses the view adapter to present them to the user.
 * It extends the AbstractCommand class and provides the specific behavior for
 * showing commands.
 *
 * This command is always applicable and does not require input parameters.
 */
public class ShowAvailableCommandsCommand extends AbstractCommand {
    public ShowAvailableCommandsCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowAvailableCommands";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showAvailableCommands(model);
    }
}
