package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The DecideToRemoveCrewMembersCommand class represents a specific command in the game
 * that allows the current player to decide whether to remove crew members from their ship
 * during the "Abandoned Ship" card phase. This command interacts with the VirtualServer
 * and ViewAdapter to execute decision-making processes and update the game state accordingly.
 *
 * This command is only applicable if all the following conditions are met:
 * 1. The current game phase is the "Card" phase.
 * 2. There is a card currently active (non-null).
 * 3. The player executing the command is the current player.
 * 4. The state of the active card is "AbandonedShipState_1".
 * 5. The number of lost astronauts defined in the card is less than or equal to the
 *    number of crew members that the player has onboard.
 */
public class DecideToRemoveCrewMembersCommand extends AbstractCommand {

    public DecideToRemoveCrewMembersCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "DecideToRemoveCrewMembers";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                model.getCurrCard().getStateName().equals("AbandonedShipState_1") &&
                ((AbandonedShipCard) model.getCurrCard()).getLostAstronauts() <= model.getShipboard(model.getCurrPlayer()).getCrewNumber();
    }

    @Override
    public void execute(ClientModel model) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        activateCard(inputCommand);
    }
}
