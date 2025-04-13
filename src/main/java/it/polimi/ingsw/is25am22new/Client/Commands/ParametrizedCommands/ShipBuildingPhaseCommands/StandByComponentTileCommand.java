package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class StandByComponentTileCommand extends AbstractParametrizedCommand {
    public StandByComponentTileCommand(VirtualServer virtualServer, ClientModel clientModel) {
        super(virtualServer, clientModel);
    }

    @Override
    public void execute() {
        try{
            virtualServer.standbyComponentTile(clientModel.getPlayerName());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isValid(ClientModel model) {
        return true;
    }
}
