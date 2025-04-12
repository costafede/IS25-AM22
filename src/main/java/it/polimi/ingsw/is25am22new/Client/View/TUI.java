package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.CommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TUI implements ClientModelObserver {

    private final CommandManager commandManager;

    public TUI(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void modelChanged(ClientModel model) {
        CommandType chosen = askToChooseAvailableCommandType(commandManager.getAvailableCommandTypes(model));
        if(chosen.getInputLength() == 0) {
            ParametrizedCommand cmd = commandManager.createCommand(model, chosen, null);  //no input
            cmd.execute();
        }
        else {
            boolean commandNotValid = false;
            ParametrizedCommand cmd;
            do {
                List<Integer> input = askToInsertInput(chosen);
                cmd = commandManager.createCommand(model, chosen, input);
                commandNotValid = !cmd.isValid(model);
                if(commandNotValid) {
                    System.out.println("Invalid input, try again");
                }
            } while (commandNotValid);
            cmd.execute();
        }
    }

    private List<Integer> askToInsertInput(CommandType chosen) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> input = new ArrayList<>();
        ParametrizedCommand cmd = null;
        System.out.println(chosen.getInputRequest());
        for(int i = 0; i < chosen.getInputLength(); i++){
            System.out.print("> ");
            input.add(scanner.nextInt());
        }
        return input;
    }

    private CommandType askToChooseAvailableCommandType(List<CommandType> commandsAvailable) {
        Scanner scanner = new Scanner(System.in);
        int commandIndex;
        do {
            System.out.println("Select one of the following commands:");
            for (CommandType command : commandsAvailable) {
                System.out.println(commandsAvailable.indexOf(command) + " - " + command.getName());
            }
            System.out.print("> ");
            commandIndex = scanner.nextInt();
            if (commandIndex < 0 || commandIndex >= commandsAvailable.size())
                System.out.println("Invalid command, try again");
        } while (commandIndex < 0 || commandIndex >= commandsAvailable.size());
        return commandsAvailable.get(commandIndex);
    }

}
