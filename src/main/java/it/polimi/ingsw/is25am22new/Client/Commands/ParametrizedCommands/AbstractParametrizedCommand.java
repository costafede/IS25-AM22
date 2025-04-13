package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import java.util.List;

public abstract class AbstractParametrizedCommand implements ParametrizedCommand {

    protected VirtualServer virtualServer;
    protected ClientModel clientModel;
    protected List<Integer> input;
    protected ViewAdapter viewAdapter;

    public AbstractParametrizedCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input) {
        this.virtualServer = virtualServer;
        this.clientModel = clientModel;
        this.input = input;
    }

    public AbstractParametrizedCommand(VirtualServer virtualServer, ClientModel clientModel) {
        this.virtualServer = virtualServer;
        this.clientModel = clientModel;
    }

    public AbstractParametrizedCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        this.virtualServer = virtualServer;
        this.clientModel = clientModel;
        this.input = input;
        this.viewAdapter = viewAdapter;
    }

}
