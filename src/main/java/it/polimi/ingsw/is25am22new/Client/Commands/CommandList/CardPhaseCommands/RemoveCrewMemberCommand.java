package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.StringConverter;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The RemoveCrewMemberCommand class is responsible for handling the removal of a crew member
 * from a specified location on a player's shipboard during the game. This command is applicable
 * in specific game phases and card states where player interaction with the crew is permitted.
 *
 * This class extends the AbstractCommand, inheriting basic command functionality, and provides
 * additional behavior specific to the removal of crew members.
 */
public class RemoveCrewMemberCommand extends AbstractCommand {
    public RemoveCrewMemberCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "RemoveCrewMember";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("SlaversState_4") ||
                model.getCurrCard().getStateName().equals("CombatZoneState_5") ||
                model.getCurrCard().getStateName().equals("AbandonedShipState_2"));
    }

    public int getInputLength() {
        return 2;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        int row_1, col_1;
        try {
            row_1 = StringConverter.stringToGridRow(input.getFirst());
            col_1 = StringConverter.stringToGridCol(input.get(1));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).isEmpty() ||
            model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).get().getCrewNumber() <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        int row_1 = StringConverter.stringToGridRow(input.getFirst());
        int col_1 = StringConverter.stringToGridCol(input.get(1));
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(row_1);
        inputCommand.setCol(col_1);
        activateCard(inputCommand);
    }
}
