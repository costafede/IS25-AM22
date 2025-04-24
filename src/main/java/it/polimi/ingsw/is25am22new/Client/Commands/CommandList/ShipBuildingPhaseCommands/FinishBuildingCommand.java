package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class FinishBuildingCommand extends AbstractCommand {
    public FinishBuildingCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "FinishBuilding";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return (model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) ||
                model.getGamePhase().getPhaseType().equals(PhaseType.CORRECTINGSHIP)) &&
                !model.getShipboard(model.getPlayerName()).isFinishedShipboard();
    }

    @Override
    public int getInputLength() {
        return 1;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;

        int idx;
        try {
            idx = Integer.parseInt(input.getFirst()) - 1;
        }
        catch(NumberFormatException e) {
            return false;
        }

        if(idx < 0 || idx > 3) return false;
        int pos = model.getFlightboard().getStartingPositions().get(idx);
        boolean alreadyPresent = model.getFlightboard().getPositions().containsValue(pos);
        return !alreadyPresent;
    }

    //input: one of the four position: 0, 1, 2, 3 (if it's free)
    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.finishBuilding(model.getPlayerName(), Integer.parseInt(input.getFirst()));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
