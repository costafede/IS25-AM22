package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.ShowPileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class ShowPileCommandType extends AbstractCommandType {
    public ShowPileCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Show Pile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getGametype().equals(GameType.LEVEL2) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() == null;
    }

    @Override
    public int getInputLength() {
        return 1;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new ShowPileCommand(virtualServer, clientModel, input);
    }

    @Override
    public String getInputRequest() {
        return "Choose a pile to see: [0, 1, 2]";
    }
}
