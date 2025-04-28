package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ChooseShipWreckCommand extends AbstractCommand {
    public ChooseShipWreckCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ChooseShipWreck";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("PiratesState_6") ||
                model.getCurrCard().getStateName().equals("CombatZoneState_8") ||
                model.getCurrCard().getStateName().equals("MeteorSwarmState_3"));
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
            row_1 = Integer.parseInt(input.getFirst());
            col_1 = Integer.parseInt(input.get(1));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_1, col_1).isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        int row_1 = Integer.parseInt(input.getFirst());
        int col_1 = Integer.parseInt(input.get(1));
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(row_1);
        inputCommand.setCol(col_1);
        activateCard(inputCommand);
    }
}
