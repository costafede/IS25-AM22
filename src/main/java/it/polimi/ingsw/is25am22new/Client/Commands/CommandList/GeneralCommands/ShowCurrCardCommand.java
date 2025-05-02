package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

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
