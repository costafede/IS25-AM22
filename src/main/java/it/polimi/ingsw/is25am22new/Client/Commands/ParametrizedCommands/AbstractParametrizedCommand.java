package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public abstract class AbstractParametrizedCommand implements ParametrizedCommand {

    protected VirtualServer virtualServer;
    protected ClientModel clientModel;

    public AbstractParametrizedCommand(VirtualServer virtualServer, ClientModel clientModel) {
        this.virtualServer = virtualServer;
        this.clientModel = clientModel;
    }

}
