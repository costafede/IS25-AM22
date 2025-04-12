package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.CommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<CommandType> allCommands;

    public CommandManager(List<CommandType> allCommands, VirtualServer virtualServer) {
        this.allCommands = allCommands //Devo inizializzarlo con tutti i tipi di comando  TO DO
    }

    public List<CommandType> getAvailableCommandTypes(ClientModel model) {
        List<CommandType> availableCommands = new ArrayList<>();

        for (CommandType cmd : allCommands) {
            if (cmd.isApplicable(model)) {
                availableCommands.add(cmd);
            }
        }

        return availableCommands;
    }

    public ParametrizedCommand createCommand(ClientModel clientModel, CommandType commandType, List<Integer> input) {
        return commandType.createWithInput(clientModel, input);
    }

}
