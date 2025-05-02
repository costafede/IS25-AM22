package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ShowTileInHandCommand extends AbstractCommand {
    public ShowTileInHandCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowTileInHand";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING);
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showTileInHand(model.getPlayerName(), model);
    }
}
