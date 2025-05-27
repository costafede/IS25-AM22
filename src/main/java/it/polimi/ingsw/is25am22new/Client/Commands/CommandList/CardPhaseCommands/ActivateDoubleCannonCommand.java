package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.StringConverter;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * ActivateDoubleCannonCommand is a command object that allows a player to activate a
 * double cannon on their shipboard. This requires the player to use a battery on the
 * specified position, and the cannon must meet certain conditions to be activated.
 *
 * This command checks the applicability based on the game phase, the current card, and
 * the player's turn. It validates input positions for compatibility with the command's
 * requirements and performs the necessary actions to activate the double cannon.
 *
 * The command is part of the Command design pattern and extends the AbstractCommand class.
 */
public class ActivateDoubleCannonCommand extends AbstractCommand {
    public ActivateDoubleCannonCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ActivateDoubleCannon";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("CombatZoneState_6") ||
                model.getCurrCard().getStateName().equals("CombatZoneState2_1") ||
                model.getCurrCard().getStateName().equals("SmugglersState_1") ||
                model.getCurrCard().getStateName().equals("MeteorSwarmState_1") ||
                model.getCurrCard().getStateName().equals("SlaversState_1") ||
                model.getCurrCard().getStateName().equals("PiratesState_1"));
    }

    public int getInputLength() {
        return 4;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        int row_batt, col_batt, row_cann, col_cann;
        try {
            row_batt = StringConverter.stringToGridRow(input.getFirst());
            col_batt = StringConverter.stringToGridCol(input.get(1));
            row_cann = StringConverter.stringToGridRow(input.get(2));
            col_cann = StringConverter.stringToGridCol(input.get(3));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_batt, col_batt).isEmpty() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_batt, col_batt).get().getNumOfBatteries() <= 0) {
            return false;
        }
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_cann, col_cann).isEmpty() ||
                !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_cann, col_cann).get().isDoubleCannon() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_cann, col_cann).get().getCannonStrength() > 0)
            return false;
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        int row_batt = StringConverter.stringToGridRow(input.getFirst());
        int col_batt = StringConverter.stringToGridCol(input.get(1));
        int row_cann = StringConverter.stringToGridRow(input.get(2));
        int col_cann = StringConverter.stringToGridCol(input.get(3));
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(row_batt);
        inputCommand.setCol(col_batt);
        activateCard(inputCommand);
        inputCommand = new InputCommand();
        inputCommand.setRow(row_cann);
        inputCommand.setCol(col_cann);
        activateCard(inputCommand);
    }
}
