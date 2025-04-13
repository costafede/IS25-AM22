package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.PickUncoveredTileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class PickUncoveredTileCommandType extends AbstractCommandType {
    public PickUncoveredTileCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Pick Uncovered Tile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return  !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() == null &&
                !model.getUncoveredComponentTiles().isEmpty();
    }

    @Override
    public int getInputLength() {
        return 1;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new PickUncoveredTileCommand(virtualServer, clientModel, input);
    }

    @Override
    public String getInputRequest() {
        return "Enter the index of the tile you want to pick:";
    }
}
