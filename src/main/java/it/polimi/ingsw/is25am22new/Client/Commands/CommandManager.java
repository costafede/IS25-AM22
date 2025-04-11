package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<CommandType> allCommands =; //Devo inizializzarlo con tutti i tipi di comando

    public CommandManager(List<CommandType> allCommands) {
        this.allCommands = allCommands;
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

    public ParametrizedCommand createCommand(CommandType commandType, Object... args) {
        return commandType.createWithInput(args);
    }

}
