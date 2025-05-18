package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.Commands.StringConverter;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.SpecialStorageCompartment;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The MoveGoodBlockCommand class is responsible for handling the game command that moves
 * a good block from one location on the shipboard to another. It extends the AbstractCommand
 * and encapsulates logic for validating the input, checking applicability, and executing
 * the movement of a good block during the gameplay.
 */
public class MoveGoodBlockCommand extends AbstractCommand {
    public MoveGoodBlockCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "MoveGoodBlock";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("PlanetsState_2") ||
                model.getCurrCard().getStateName().equals("SmugglersState_4") ||
                model.getCurrCard().getStateName().equals("AbandonedStationState_2"));
    }

    public int getInputLength() {
        return 5;
    }
    //input: gb_1, row_1, col_1, row_2, col_2. I move gb_1 from (row_1, col_1) to (row_2, col_2)
    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        GoodBlock gb_1;
        int row_1, col_1, row_2, col_2;
        try {
            row_1 = StringConverter.stringToGridRow(input.get(1));
            col_1 = StringConverter.stringToGridCol(input.get(2));
            row_2 = StringConverter.stringToGridRow(input.get(3));
            col_2 = StringConverter.stringToGridCol(input.get(4));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(!ConditionVerifier.stringIsGoodBlock(input.getFirst()))
            return false;
        gb_1 = StringConverter.stringToGoodBlock(input.getFirst());
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).isEmpty() ||
                !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).get().isStorageCompartment() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).get().getGoodBlocks().get(gb_1) <= 0 ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_2, col_2).isEmpty() ||
                !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_2, col_2).get().isStorageCompartment() ||
                !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_2, col_2).get().isBlockPlaceable(gb_1)) {
            return false;
        }
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        GoodBlock gb_1 = StringConverter.stringToGoodBlock(input.getFirst());
        int row_1 = StringConverter.stringToGridRow(input.get(1));
        int col_1 = StringConverter.stringToGridCol(input.get(2));
        int row_2 = StringConverter.stringToGridRow(input.get(3));
        int col_2 = StringConverter.stringToGridCol(input.get(4));
        inputCommand.setRow(row_1);
        inputCommand.setCol(col_1);
        inputCommand.setRow_1(row_2);
        inputCommand.setCol_1(col_2);
        inputCommand.setGoodBlock(gb_1);
        inputCommand.setGoodBlock_1(null);
        inputCommand.flagSwitchingGoodBlock();
        activateCard(inputCommand);
    }
}
