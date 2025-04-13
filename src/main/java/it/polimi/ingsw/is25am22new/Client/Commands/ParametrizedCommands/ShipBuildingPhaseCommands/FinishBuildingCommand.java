package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class FinishBuildingCommand extends AbstractParametrizedCommand {

    //input: one of the four position: 0, 1, 2, 3 (if it's free)
    public FinishBuildingCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input) {
        super(virtualServer, clientModel, input);
    }

    @Override
    public void execute() {
        try {
            virtualServer.finishBuilding(clientModel.getPlayerName(), input.getFirst());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isValid(ClientModel model) {
        int idx = input.getFirst() - 1;
            if(idx < 0 || idx > 3) return false;
        int pos = model.getFlightboard().getStartingPositions().get(idx);
        boolean alreadyPresent = model.getFlightboard().getPositions().containsValue(pos);
        return !alreadyPresent;
    }
}
