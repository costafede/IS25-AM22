package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CorrectingShipPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class DestroyTileCommand extends AbstractCommand {
    public DestroyTileCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "DestroyTile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CORRECTINGSHIP) &&
                model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                !model.getShipboard(model.getPlayerName()).checkShipboard();
    }

    public int getInputLength() {
        return 2;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        int row, col;
        try {
            row = Integer.parseInt(input.getFirst());
            col = Integer.parseInt(input.get(1));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(ConditionVerifier.coordinatesAreNotOutOfBound(row, col, model) && model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row, col).isPresent())
            return true;
        return false;
    }

    //input = (row, col)
    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.destroyComponentTile(model.getPlayerName(), Integer.parseInt(input.getFirst()), Integer.parseInt(input.get(1)));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
