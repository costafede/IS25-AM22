package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.Commands.StringConverter;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class WeldComponentTileCommand extends AbstractCommand {
    public WeldComponentTileCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "WeldComponentTile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() != null;
    }

    @Override
    public int getInputLength() {
        return 3;
    }

    //input is a list of 1) row, 2) col, 3) num of rotation
    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;

        int row, col, numOfRotation;
        try {
            row = Integer.parseInt(input.getFirst());
            col = Integer.parseInt(input.get(1));
            numOfRotation = Integer.parseInt(input.get(2));
        }
        catch(NumberFormatException e) {
            return false;
        }

        return ConditionVerifier.coordinatesAreNotOutOfBound(row, col, model) &&
                model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row, col).isEmpty();
    }

    @Override
    public void execute(ClientModel model) {
        try{
            virtualServer.weldComponentTile(model.getPlayerName(), StringConverter.stringToGridRow(input.getFirst()), StringConverter.stringToGridCol(input.get(1)), Integer.parseInt(input.get(2)));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
