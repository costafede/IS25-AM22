package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.EndPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * QuitCommand is a concrete implementation of AbstractCommand that allows a player
 * to quit the game. This command is applicable only during the END phase of the game.
 * Once executed, it contacts the VirtualServer to handle the quitting logic for the
 * player and notifies the ViewAdapter to update the user interface accordingly.
 */
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
