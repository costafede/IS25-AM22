package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * Represents a command to allow a player to abandon the game.
 * This command handles the server-side logic and ensures that
 * abandoning is only permitted under specific conditions.
 *
 * The command is applicable if:
 * - The current game phase is of type CARD.
 * - The current card being processed is null.
 * - The player has not been kicked out.
 */
public class AbandonGameCommand extends AbstractCommand {

    public AbandonGameCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "AbandonGame";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() == null &&
                !model.getShipboards().get(model.getPlayerName()).hasBeenKickedOut();
    }

    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.playerAbandons(model.getPlayerName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
