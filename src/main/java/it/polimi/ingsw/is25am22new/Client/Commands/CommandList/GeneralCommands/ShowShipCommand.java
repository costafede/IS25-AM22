package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

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
