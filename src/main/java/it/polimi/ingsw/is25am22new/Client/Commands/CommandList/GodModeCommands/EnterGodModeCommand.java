package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GodModeCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The EnterGodModeCommand class extends the AbstractCommand abstract class
 * and represents a command that allows a player to activate "God Mode"
 * in the game. This mode may provide specific privileges or configurations
 * to the player.
 *
 * The command interacts with the VirtualServer to execute the God Mode feature
 * on behalf of the player, passing the necessary configuration as input.
 */
public class EnterGodModeCommand extends AbstractCommand {

    public EnterGodModeCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "EnterGodMode";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    public int getInputLength() {
        return 1;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        return true;
    }


    //the second parameter has to be the concatenation of the strings in input
    @Override
    public void execute(ClientModel model) {
        try {
            StringBuilder inputString = new StringBuilder();
            boolean first = true;

            for (String s : input) {
                if (first) {
                    inputString.append(s);
                    first = false;
                } else {
                    inputString.append(",").append(s);
                }
            }

            virtualServer.godMode(model.getPlayerName(), inputString.toString());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
