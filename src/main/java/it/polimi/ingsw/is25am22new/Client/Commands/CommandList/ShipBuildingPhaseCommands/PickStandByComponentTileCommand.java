package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class PickStandByComponentTileCommand extends AbstractCommand {
    public PickStandByComponentTileCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "PickStandByComponentTile";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        ComponentTile standbyComponent1 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        ComponentTile standbyComponent2 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        return !model.getShipboard(model.getPlayerName()).isFinishedShipboard() &&
                model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                model.getShipboard(model.getPlayerName()).getTileInHand() == null &&
                (standbyComponent1 != null || standbyComponent2 != null);
    }

    @Override
    public int getInputLength() {
        return 1;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        int index;
        if(!super.isInputValid(model))
            return false;
        try {
            index = Integer.parseInt(input.getFirst());
        }
        catch (NumberFormatException e) {
            return false;
        }
        ComponentTile standbyComponent1 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        ComponentTile standbyComponent2 = model.getShipboard(model.getPlayerName()).getStandbyComponent()[0].orElseGet(() -> null);
        return index == 0 && standbyComponent1 != null || index == 1 && standbyComponent2 != null;
    }

    @Override
    public void execute(ClientModel model) {
        try{
            virtualServer.pickStandbyComponentTile(model.getPlayerName(), Integer.parseInt(input.getFirst()));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
