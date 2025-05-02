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

public class SwitchGoodBlocksCommand extends AbstractCommand {
    public SwitchGoodBlocksCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "SwitchGoodBlocks";
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
        return 6;
    }
    //input: gb_1, row_1, col_1, gb_2, row_2, col_2.
    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        GoodBlock gb_1, gb_2;
        int row_1, col_1, row_2, col_2;
        try {
            row_1 = StringConverter.stringToGridRow(input.get(1));
            col_1 = StringConverter.stringToGridCol(input.get(2));
            row_2 = StringConverter.stringToGridRow(input.get(4));
            col_2 = StringConverter.stringToGridCol(input.get(5));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(!ConditionVerifier.stringIsGoodBlock(input.getFirst()) || !ConditionVerifier.stringIsGoodBlock(input.get(3)))
            return false;
        gb_1 = StringConverter.stringToGoodBlock(input.getFirst());
        gb_2 = StringConverter.stringToGoodBlock(input.get(3));
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).isEmpty() ||
                !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).get().isStorageCompartment() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).get().getGoodBlocks().get(gb_1) <= 0 ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_2, col_2).isEmpty() ||
                !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_2, col_2).get().isStorageCompartment() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_2, col_2).get().getGoodBlocks().get(gb_2) <= 0) {
            return false;
        }
        if(gb_1.equals(GoodBlock.REDBLOCK) && !(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_2, col_2).get() instanceof SpecialStorageCompartment) ||
            gb_2.equals(GoodBlock.REDBLOCK) && !(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).get() instanceof SpecialStorageCompartment)) {
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
        GoodBlock gb_2 = StringConverter.stringToGoodBlock(input.get(3));
        int row_2 = StringConverter.stringToGridRow(input.get(4));
        int col_2 = StringConverter.stringToGridCol(input.get(5));
        inputCommand.setRow(row_1);
        inputCommand.setCol(col_1);
        inputCommand.setRow_1(row_2);
        inputCommand.setCol_1(col_2);
        inputCommand.setGoodBlock(gb_1);
        inputCommand.setGoodBlock_1(gb_2);
        inputCommand.flagSwitchingGoodBlock();
        activateCard(inputCommand);
    }
}
