package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.StringConverter;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ActivateDoubleEngineCommand extends AbstractCommand {
    public ActivateDoubleEngineCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ActivateDoubleEngine";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("OpenSpaceState_1") ||
                        model.getCurrCard().getStateName().equals("CombatZoneState_1"));
    }

    public int getInputLength() {
        return 4;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        int row_batt, col_batt, row_en, col_en;
        try {
            row_batt = StringConverter.stringToGridRow(input.getFirst());
            col_batt = StringConverter.stringToGridCol(input.get(1));
            row_en = StringConverter.stringToGridRow(input.get(2));
            col_en = StringConverter.stringToGridCol(input.get(3));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_batt, col_batt).isEmpty() ||
            model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_batt, col_batt).get().getNumOfBatteries() <= 0) {
            return false;
        }
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_en, col_en).isEmpty() ||
            !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_en, col_en).get().isDoubleEngine() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_en, col_en).get().getEngineStrength() > 0)
            return false;
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        int row_batt = StringConverter.stringToGridRow(input.getFirst());
        int col_batt = StringConverter.stringToGridCol(input.get(1));
        int row_en = StringConverter.stringToGridRow(input.get(2));
        int col_en = StringConverter.stringToGridCol(input.get(3));
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(row_batt);
        inputCommand.setCol(col_batt);
        activateCard(inputCommand);
        inputCommand = new InputCommand();
        inputCommand.setRow(row_en);
        inputCommand.setCol(col_en);
        activateCard(inputCommand);
    }
}
