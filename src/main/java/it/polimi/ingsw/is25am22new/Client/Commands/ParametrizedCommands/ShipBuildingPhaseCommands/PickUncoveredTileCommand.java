package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class PickUncoveredTileCommand extends AbstractParametrizedCommand {
    //input is the index of the chosen tile
    public PickUncoveredTileCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input) {
        super(virtualServer, clientModel, input);
    }

    @Override
    public void execute() {
        try{
            virtualServer.pickUncoveredTile(clientModel.getPlayerName(), input.getFirst());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isValid(ClientModel model) {
        return input.getFirst() >= 0 && input.getFirst() < model.getUncoveredComponentTiles().size();
    }
}
