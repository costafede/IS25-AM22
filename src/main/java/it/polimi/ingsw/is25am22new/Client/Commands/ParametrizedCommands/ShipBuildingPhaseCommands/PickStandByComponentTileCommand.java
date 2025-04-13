package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class PickStandByComponentTileCommand extends AbstractParametrizedCommand {
    //input can be 0 or 1
    public PickStandByComponentTileCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input) {
        super(virtualServer, clientModel, input);
    }

    @Override
    public void execute() {
        try{
            virtualServer.pickStandbyComponentTile(clientModel.getPlayerName(), input.getFirst());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isValid(ClientModel model) {
        int index = input.getFirst();
        ComponentTile standbyComponent1 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        ComponentTile standbyComponent2 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        return index == 0 && standbyComponent1 != null || index == 1 && standbyComponent2 != null;
    }
}
