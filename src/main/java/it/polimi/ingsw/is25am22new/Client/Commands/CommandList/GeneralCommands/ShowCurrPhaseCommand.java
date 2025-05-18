package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The ShowCurrPhaseCommand class is a concrete implementation of AbstractCommand
 * used to display the current phase of the game in the user interface. This
 * command interacts with the associated ViewAdapter to communicate the current phase
 * of the game state to the player.
 */
public class ShowCurrPhaseCommand extends AbstractCommand {

    public ShowCurrPhaseCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowCurrPhase";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showCurrPhase(model);
    }
}
