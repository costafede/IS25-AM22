package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.LobbyCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class StartGameByHostCommand extends AbstractParametrizedCommand {
    public StartGameByHostCommand(VirtualServer virtualServer, ClientModel clientModel) {
        super(virtualServer, clientModel);
    }

    @Override
    public void execute() {
        try{
            virtualServer.startGameByHost(clientModel.getPlayerName());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isValid(ClientModel model) {
        return true;    //there is no input to validate
    }
}
