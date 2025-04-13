package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes;


import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.WeldComponentTileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class WeldComponentTileCommandType extends AbstractCommandType {
    public WeldComponentTileCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Weld Component Tile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() != null;
    }

    @Override
    public int getInputLength() {
        return 3;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new WeldComponentTileCommand(virtualServer, clientModel, input);
    }

    @Override
    public String getInputRequest() {
        return "Enter the coordinates of where you want to weld the tile and the number of rotations (positive if clockwise, negative otherwise): [row, col, numOfRotations]";
    }
}
