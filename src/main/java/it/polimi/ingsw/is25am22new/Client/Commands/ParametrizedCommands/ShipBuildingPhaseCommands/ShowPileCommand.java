package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class ShowPileCommand extends AbstractParametrizedCommand {
    //input is the index of one of the three card piles
    public ShowPileCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input) {
        super(virtualServer, clientModel, input);
    }

    @Override
    public void execute() {
        viewAdapter.showCardPile(input.getFirst(), clientModel);
    }

    @Override
    public boolean isValid(ClientModel model) {
        return input.getFirst() >= 0 && input.getFirst() <= 2;
    }
}
