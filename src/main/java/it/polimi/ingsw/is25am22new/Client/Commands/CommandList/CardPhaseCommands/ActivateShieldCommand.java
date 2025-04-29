package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ActivateShieldCommand extends AbstractCommand {
    public ActivateShieldCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ActivateShield";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                model.getCurrCard() != null &&
                (model.getCurrCard().getStateName().equals("MeteorSwarmState_1") ||
                model.getCurrCard().getStateName().equals("CombatZoneState_9") ||
                model.getCurrCard().getStateName().equals("PiratesState_4"));
    }

    public int getInputLength() {
        return 4;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        int row_batt, col_batt, row_sh, col_sh;
        try {
            row_batt = Integer.parseInt(input.getFirst());
            col_batt = Integer.parseInt(input.get(1));
            row_sh = Integer.parseInt(input.get(2));
            col_sh = Integer.parseInt(input.get(3));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(!model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_batt, col_batt).isPresent() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_batt, col_batt).get().getNumOfBatteries() <= 0) {
            return false;
        }
        if(!model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_sh, col_sh).isPresent() ||
                !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_sh, col_sh).get().isShieldGenerator() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_sh, col_sh).get().isTopSideShielded() ||
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row_sh, col_sh).get().isBottomSideShielded()
        )
            return false;
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        int row_batt = Integer.parseInt(input.getFirst());
        int col_batt = Integer.parseInt(input.get(1));
        int row_sh = Integer.parseInt(input.get(2));
        int col_sh = Integer.parseInt(input.get(3));
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(row_batt);
        inputCommand.setCol(col_batt);
        activateCard(inputCommand);
        inputCommand = new InputCommand();
        inputCommand.setRow(row_sh);
        inputCommand.setCol(col_sh);
        activateCard(inputCommand);
    }
}
