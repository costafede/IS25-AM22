package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * Command implementation for displaying all shipboards for players in the game.
 * This command, when executed, iterates over all players and displays their respective
 * shipboards using the provided ViewAdapter instance.
 *
 * This command is applicable in any game state as defined by the {@code isApplicable} method.
 */
public class ShowAllShipsCommand extends AbstractCommand {

    public ShowAllShipsCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowAllShips";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        for (String player : model.getShipboards().keySet()) {
            viewAdapter.showShipboardGrid(player, model);
            System.out.println("---------------------------------");
        }
    }
}
