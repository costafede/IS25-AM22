package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class WeldComponentTileCommand extends AbstractParametrizedCommand {
    //input is a list of 1) row, 2) col, 3) num of rotation
    public WeldComponentTileCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input) {
        super(virtualServer, clientModel, input);
    }

    @Override
    public void execute() {
        try{
            virtualServer.weldComponentTile(clientModel.getPlayerName(), input.getFirst(), input.get(1), input.get(2));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isValid(ClientModel model) {
        return ConditionVerifier.coordinatesAreNotOutOfBound(input.getFirst(), input.get(1), model) &&
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(input.getFirst(), input.get(1)).isEmpty();
    }
}
