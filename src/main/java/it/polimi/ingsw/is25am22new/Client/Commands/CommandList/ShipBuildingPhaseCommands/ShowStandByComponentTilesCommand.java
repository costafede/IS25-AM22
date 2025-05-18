package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * Represents a command to display standby component tiles on a shipboard during
 * the BUILDING phase of the game. The command interacts with the model to verify
 * applicability and utilizes the view adapter to render standby components for the
 * current player.
 *
 * This class extends the {@code AbstractCommand}, inheriting common functionalities
 * for handling commands, and overrides methods specific to this command's behavior.
 */
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
