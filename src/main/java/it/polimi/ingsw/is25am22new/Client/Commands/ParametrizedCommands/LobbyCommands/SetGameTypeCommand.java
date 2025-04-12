package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.LobbyCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.AbstractParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class SetGameTypeCommand extends AbstractParametrizedCommand {

    private final List<Integer> input;   //0 for tutorial, 1 for level 2

    public SetGameTypeCommand(VirtualServer virtualServer, ClientModel clientModel, List<Integer> input) {
        super(virtualServer, clientModel);
        this.input = input;
    }

    @Override
    public void execute() {
        String game = input.getFirst() == 0 ? "tutorial" : "level2";
        try {
            virtualServer.setGameType(game);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isValid(ClientModel model) {
        return input.size() == 1 && (input.getFirst() == 0 || input.getFirst() == 1);
    }
}
