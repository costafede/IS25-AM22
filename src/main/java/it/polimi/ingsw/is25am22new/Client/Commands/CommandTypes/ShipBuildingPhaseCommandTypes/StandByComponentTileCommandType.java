package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.StandByComponentTileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class StandByComponentTileCommandType extends AbstractCommandType {
    public StandByComponentTileCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Stand By Component Tile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        ComponentTile standbyComponent1 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        ComponentTile standbyComponent2 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() != null &&
                (standbyComponent1 == null || standbyComponent2 == null);
    }

    @Override
    public int getInputLength() {
        return 0;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new StandByComponentTileCommand(virtualServer, clientModel);
    }
}
