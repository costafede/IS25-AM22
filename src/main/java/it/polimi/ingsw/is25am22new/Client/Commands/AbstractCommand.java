package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements Command {
    protected VirtualServer virtualServer;
    protected List<String> input;
    protected ViewAdapter viewAdapter;

    public AbstractCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        this.virtualServer = virtualServer;
        this.viewAdapter = viewAdapter;
    }

    @Override
    public void setInput(List<String> input) {
        this.input = input;
    }

    //I assume there is no input by default
    @Override
    public boolean isInputValid(ClientModel model) {
        return input.size() == getInputLength();
    }

    @Override
    public int getInputLength() {
        return 0;   //no input
    }
}
