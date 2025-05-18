package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * ShowShipCommand is a concrete implementation of the AbstractCommand class,
 * responsible for displaying the shipboard grid for the current player.
 * This command utilizes the ViewAdapter to render the visual representation
 * of the player's shipboard based on the game state encapsulated in the ClientModel.
 *
 * The command can be applied universally to any ClientModel instance as the
 * isApplicable method always returns true.
 */
public class ShowShipCommand extends AbstractCommand {

    public ShowShipCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowShip";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showShipboardGrid(model.getPlayerName(), model);
    }
}
