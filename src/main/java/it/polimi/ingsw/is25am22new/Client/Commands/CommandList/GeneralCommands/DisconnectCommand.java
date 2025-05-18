package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;
import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.rmi.RemoteException;

/**
 * DisconnectCommand is responsible for disconnecting a client from the virtual server.
 * It extends the AbstractCommand class and provides the implementation for the disconnect operation.
 */
public class DisconnectCommand extends AbstractCommand {
    public DisconnectCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "Disconnect";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.disconnect();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
