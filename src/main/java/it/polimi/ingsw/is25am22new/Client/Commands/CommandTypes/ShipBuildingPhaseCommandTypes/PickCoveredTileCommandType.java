package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.PickCoveredTileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class PickCoveredTileCommandType extends AbstractCommandType {
    public PickCoveredTileCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Pick Covered Tile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                !model.getCoveredComponentTiles().isEmpty() &&
                model.getShipboard(model.getPlayerName()).getTileInHand() == null;
    }

    @Override
    public int getInputLength() {
        return 0;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new PickCoveredTileCommand(virtualServer, clientModel);
    }

}
