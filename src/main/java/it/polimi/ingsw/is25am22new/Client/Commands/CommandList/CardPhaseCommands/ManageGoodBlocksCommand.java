package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * ManageGoodBlocksCommand is a command implementation that facilitates handling
 * specific card states within the game when the player interacts with "SmugglersState_3"
 * or "AbandonedStationState_2". It extends the AbstractCommand to inherit shared
 * behaviors and encapsulates functionality specific to managing these card scenarios.
 *
 * The command is applicable during the CARD phase of the game and only if the
 * current card is one of the specified states and the conditions related to the card's
 * astronauts or the player's crew are satisfied.
 *
 * The command execution triggers the activation of the current card using the
 * VirtualServer's `activateCard` method after setting up a valid InputCommand instance.
 */
public class ManageGoodBlocksCommand extends AbstractCommand {
    public ManageGoodBlocksCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ManageGoodBlocks";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("SmugglersState_3") ||
                model.getCurrCard().getStateName().equals("AbandonedStationState_2") &&
                ((AbandonedStationCard) model.getCurrCard()).getAstronautsNumber() <= model.getShipboard(model.getPlayerName()).getCrewNumber());
    }

    @Override
    public void execute(ClientModel model) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        activateCard(inputCommand);
    }
}
