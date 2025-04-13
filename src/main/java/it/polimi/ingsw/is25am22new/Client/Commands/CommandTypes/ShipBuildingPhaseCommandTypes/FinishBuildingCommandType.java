package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.FinishBuildingCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class FinishBuildingCommandType extends AbstractCommandType {
    public FinishBuildingCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Finish Building";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return (model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) ||
                model.getGamePhase().getPhaseType().equals(PhaseType.CORRECTINGSHIP)) &&
                !model.getShipboard(model.getPlayerName()).isFinishedShipboard();
    }

    @Override
    public int getInputLength() {
        return 1;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new FinishBuildingCommand(virtualServer, clientModel, input);
    }

    @Override
    public String getInputRequest(){
        return "Select a starting position:/n1 - first place/n2 - second place/n3 - third place/n4 - second place/n";
    }
}
