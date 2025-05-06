package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ShowRemainingSecondsCommand extends AbstractCommand {
    public ShowRemainingSecondsCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowRemainingSeconds";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getGametype().equals(GameType.LEVEL2) &&
                model.getHourglass() != null;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showRemainingSeconds(model);
    }
}
