package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ShowStandByComponentTilesCommand extends AbstractCommand {

    public ShowStandByComponentTilesCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowStandByComponentTiles";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING);
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showShipboardStandByComponents(model.getPlayerName(), model);
    }
}
