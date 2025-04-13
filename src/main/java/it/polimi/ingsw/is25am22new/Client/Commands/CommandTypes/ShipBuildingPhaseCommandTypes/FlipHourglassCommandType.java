package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.FlipHourglassCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class FlipHourglassCommandType extends AbstractCommandType {

    public FlipHourglassCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Flip Hourglass";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        if(model.getGametype().equals(GameType.TUTORIAL) ||
           !model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING))
            return false;
        String player = model.getPlayerName();
        if(model.getHourglassSpot() == 1 && !model.getShipboard(player).isFinishedShipboard()) //player wants to flip the hourglass on the last spot but he's not finished yet
            return false;
        if(model.getHourglass().getRemainingSeconds() > 0) //timer has not finished yet
            return false;
        return true;
    }

    @Override
    public int getInputLength() {
        return 0;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new FlipHourglassCommand(virtualServer, clientModel);
    }
}
