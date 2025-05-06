package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GodModeCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class SetShipboard1Command extends AbstractCommand {
    public SetShipboard1Command(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "SetShipboard1";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.isGodMode();
    }

    @Override
    public void execute(ClientModel model) {
        /*TO DO*/
    }
}
