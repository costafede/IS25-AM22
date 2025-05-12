package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.EndPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class QuitCommand extends AbstractCommand {
    public QuitCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "Quit";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.END);
    }

    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.quit(model.getPlayerName());
        } catch (Exception e) {
            System.out.println("Error while quitting: " + e.getMessage());
        }
        viewAdapter.quit();
    }
}
