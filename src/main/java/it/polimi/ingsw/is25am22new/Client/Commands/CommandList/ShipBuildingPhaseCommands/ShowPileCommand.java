package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The ShowPileCommand class represents a concrete implementation of the AbstractCommand
 * for displaying a specific card pile in the game interface. It interacts with the client
 * model and view adapter to retrieve and present the card pile information to the user.
 * This command is applicable only under specific game conditions in the LEVEL2 game type
 * during the BUILDING phase.
 */
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
