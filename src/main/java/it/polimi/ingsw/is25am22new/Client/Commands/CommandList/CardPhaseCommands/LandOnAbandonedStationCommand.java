package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The LandOnAbandonedStationCommand class represents the command executed when a player
 * decides to land on an Abandoned Station during the relevant game phase. This command
 * is responsible for verifying the current state of the game, ensuring that specific
 * conditions are met before the action is executed, and activating the related card if allowed.
 *
 * This command extends the AbstractCommand class, utilizing its functionality to interact
 * with the virtual server and the view adapter. The command registration name is
 * "LandOnAbandonedStation".
 */
public class LandOnAbandonedStationCommand extends AbstractCommand {

    public LandOnAbandonedStationCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "LandOnAbandonedStation";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                model.getCurrCard().getStateName().equals("AbandonedStationState_1") &&
                model.getShipboard(model.getPlayerName()).getCrewNumber() >= ((AbandonedStationCard) model.getCurrCard()).getAstronautsNumber();
    }

    @Override
    public void execute(ClientModel model) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        activateCard(inputCommand);
    }
}
