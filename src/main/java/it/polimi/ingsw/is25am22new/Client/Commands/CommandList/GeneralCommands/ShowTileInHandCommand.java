package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The ShowTileInHandCommand class represents a command that displays the player's
 * current tile in hand during the game's building phase. This command is applicable
 * only when the player's shipboard construction process is incomplete and the game
 * is in the building phase.
 *
 * It interacts with the ViewAdapter to show the tile in hand and requires the
 * VirtualServer for server-related operations. The class ensures the command is
 * applicable based on the ClientModel's state.
 *
 * This class extends AbstractCommand, inheriting its core functionalities and adhering
 * to the Command interface.
 */
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
