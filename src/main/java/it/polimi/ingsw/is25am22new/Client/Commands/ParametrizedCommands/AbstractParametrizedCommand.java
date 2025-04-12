package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParametrizedCommand implements ParametrizedCommand {

    protected VirtualServer virtualServer;
    protected ClientModel clientModel;
    protected List<Integer> input;

    public AbstractParametrizedCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input) {
        this.virtualServer = virtualServer;
        this.clientModel = clientModel;
        this.input = input;
    }

    public AbstractParametrizedCommand(VirtualServer virtualServer, ClientModel clientModel) {
        this.virtualServer = virtualServer;
        this.clientModel = clientModel;
    }

}
