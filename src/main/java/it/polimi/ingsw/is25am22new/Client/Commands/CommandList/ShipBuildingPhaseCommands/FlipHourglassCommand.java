package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class FlipHourglassCommand extends AbstractCommand {
    public FlipHourglassCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "FlipHourglass";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        if(model.getHourglass() == null) //timer has not been initialized yet
            return false;
        if(model.getGametype().equals(GameType.TUTORIAL) ||
                !model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING))
            return false;
        String player = model.getPlayerName();
        if(model.getHourglassSpot() == 1 && !model.getShipboard(player).isFinishedShipboard()) //player wants to flip the hourglass on the last spot but he's not finished yet
            return false;
        if(model.getHourglass().getRemainingSeconds() > 0) //timer has not finished yet
            return false;
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.flipHourglass();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
