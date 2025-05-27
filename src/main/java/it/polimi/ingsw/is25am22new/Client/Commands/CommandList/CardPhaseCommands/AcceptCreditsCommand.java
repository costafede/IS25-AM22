package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The AcceptCreditsCommand class implements a specific command for accepting credits during
 * the game's card phase. This command determines if it is applicable based on the current
 * state of the game and executes the corresponding action to activate the player's card
 * choice.
 *
 * The command is applicable only when the game is in the card phase, a card is currently
 * active, the current player matches the player executing the command, and the card is in
 * a specific state (either "SlaversState_3" or "PiratesState_3").
 *
 * Upon execution, the command activates the card with the appropriate input.
 */
public class AcceptCreditsCommand extends AbstractCommand {
    public AcceptCreditsCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "AcceptCredits";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("SlaversState_3") ||
                model.getCurrCard().getStateName().equals("PiratesState_3"));
    }

    @Override
    public void execute(ClientModel model) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        activateCard(inputCommand);
    }
}
