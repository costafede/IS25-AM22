package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ShowPileCommand extends AbstractCommand {
    public ShowPileCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowPile";
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
    public boolean isInputValid(ClientModel model) {
        int index;
        if(!super.isInputValid(model))
            return false;
        try {
            index = Integer.parseInt(input.getFirst());
        }
        catch (NumberFormatException e) {
            return false;
        }
        return index >= 0 && index <= 2;
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showCardPile(Integer.parseInt(input.getFirst()), model);
    }
}
