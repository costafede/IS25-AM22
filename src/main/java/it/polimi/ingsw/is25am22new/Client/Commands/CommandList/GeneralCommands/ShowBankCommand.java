package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The ShowBankCommand class represents a command that displays
 * the current state of the bank in the game. It extends the
 * AbstractCommand class and provides functionality to interact
 * with the game model and view to display the bank information
 * to the client.
 *
 * This command is always applicable and involves no additional input.
 * The behavior of this command is executed through the ViewAdapter,
 * which updates the user interface with the bank details from the
 * ClientModel.
 */
public class ShowBankCommand extends AbstractCommand {
    public ShowBankCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowBank";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showBank(model);
    }
}
