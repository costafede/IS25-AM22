package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.CommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TUI implements ClientModelObserver, ViewAdapter{

    private final CommandManager commandManager;

    public TUI(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void modelChanged(ClientModel model) {
        boolean commandInputNotValid;
        CommandType chosen = askToChooseAvailableCommandType(commandManager.getAvailableCommandTypes(model));
        do {
            commandInputNotValid = false;
            if(chosen.getInputLength() == 0) {
                ParametrizedCommand cmd = commandManager.createCommand(model, chosen, null, this);  //no input
                cmd.execute();
            }
            else {
                ParametrizedCommand cmd;
                List<Integer> input = askToInsertInput(chosen);
                cmd = commandManager.createCommand(model, chosen, input, this);
                commandInputNotValid = !cmd.isValid(model);
                if(commandInputNotValid) {
                    System.out.println("Invalid input, try again");
                }
                else
                    cmd.execute();
            }
        } while (commandInputNotValid);
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

    @Override
    public void showCardPile(int idx, ClientModel model) {
        CardPile card = model.getCardPiles().get(idx);
        /* Logica di stampa delle carte della pila TO DO*/
    }

    @Override
    public void showShipboardGrid(String player, ClientModel clientModel) {
        Map<String, Shipboard> shipboards = clientModel.getShipboards();
        Shipboard ship = shipboards.get(player);
        System.out.println("=== LA TUA NAVE ===");
        System.out.println("Giorni di volo: " + ship.getDaysOnFlight());
        System.out.println("Crediti: " + ship.getCosmicCredits());
        System.out.println("Equipaggio di volo: " + ship.getCrewNumber());
        System.out.println("Astronauti: " + ship.getOnlyHumanNumber());
        //print welded components

    }

    @Override
    public void showShipboardStandByComponents(String player, ClientModel clientModel) {
        /*TO DO*/
    }

    @Override
    public void showFlightboard(ClientModel clientModel) {
        /*TO DO*/
    }

    @Override
    public void showCard(AdventureCard card, ClientModel clientModel) {
        /*TO DO*/
    }

    @Override
    public void showUncoveredComponentTiles(ClientModel clientModel) {
        /*TO DO*/
        /* mostra accanto ad ogni tile il suo indice nella lista*/
    }
}
