package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class AbandonGameCommand extends AbstractCommand {

    public AbandonGameCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "AbandonGame";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return !model.getGamePhase().getPhaseType().equals(PhaseType.SETUP) &&
                !model.getGamePhase().getPhaseType().equals(PhaseType.BUILDING) &&
                !model.getGamePhase().getPhaseType().equals(PhaseType.END);
    }

    @Override
    public void execute(ClientModel model) {
        //viewAdapter.abandonGame(model);
        try {
            //virtualServer.abandonGame(model.getPlayerName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
