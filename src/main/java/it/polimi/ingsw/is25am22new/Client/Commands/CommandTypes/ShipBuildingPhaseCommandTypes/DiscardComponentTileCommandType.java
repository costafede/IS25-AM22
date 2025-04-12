package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.DiscardComponentTileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class DiscardComponentTileCommandType extends AbstractCommandType {

    public DiscardComponentTileCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Discard Component Tile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() != null &&
                !model.getShipboard(model.getPlayerName()).isFinishedShipboard();
    }

    @Override
    public int getInputLength() {
        return 0;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input) {
        return new DiscardComponentTileCommand(virtualServer, clientModel);
    }
}
