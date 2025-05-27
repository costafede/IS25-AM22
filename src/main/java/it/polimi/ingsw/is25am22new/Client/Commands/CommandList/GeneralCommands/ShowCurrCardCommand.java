package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * ShowCurrCardCommand is responsible for displaying the current card associated with the
 * client's game model. It extends AbstractCommand to utilize shared command functionalities
 * and overrides specific behaviors for this operation.
 *
 * This command interacts with the ViewAdapter to trigger the display of the current card
 * and makes use of the data associated with the ClientModel passed to it during execution.
 */
public class ShowCurrCardCommand extends AbstractCommand {

    public ShowCurrCardCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowCurrCard";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showCard(model.getCurrCard(), model);
    }
}
