package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes;

import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public abstract class AbstractCommandType implements CommandType {

    protected VirtualServer virtualServer;

    public AbstractCommandType(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public String getInputRequest() {
        return null;
    }
}
