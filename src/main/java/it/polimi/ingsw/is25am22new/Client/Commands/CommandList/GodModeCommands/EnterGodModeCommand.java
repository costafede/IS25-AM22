package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GodModeCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.io.IOException;

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
