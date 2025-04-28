package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;

public class SwitchGoodBlocksCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "SwitchGoodBlocks";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return false;
    }

    public int getInputLength() {
        return 6;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        int row_1, col_1, row_2, col_2;
        try {
            row_1 = Integer.parseInt(input.getFirst());
            col_1 = Integer.parseInt(input.get(1));
            row_2 = Integer.parseInt(input.get(2));
            col_2 = Integer.parseInt(input.get(3));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(!model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).isPresent() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).get().get) {
            return false;
        }
        if(!model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_cann, col_cann).isPresent() ||
                !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_cann, col_cann).get().isDoubleCannon())
            return false;
        return true;
    }

    @Override
    public void execute(ClientModel model) {

    }
}
