package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class DeclareEngineStrengthCommand extends AbstractCommand {
    public DeclareEngineStrengthCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "DeclareEngineStrength";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return false;
    }

    @Override
    public void execute(ClientModel model) {

    }
}
