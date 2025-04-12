package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.LobbyCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class SetPlayerNotReadyCommand extends AbstractParametrizedCommand {

    public SetPlayerNotReadyCommand(VirtualServer virtualServer, ClientModel clientModel) {
        super(virtualServer, clientModel);
    }

    @Override
    public void execute() {
        try{
            virtualServer.setPlayerNotReady(clientModel.getPlayerName());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isValid(ClientModel model) {
        return true;    //the command needs no inputs
    }
}
