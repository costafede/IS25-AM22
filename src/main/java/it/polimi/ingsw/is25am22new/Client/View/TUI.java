package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Client.View.AdventureCardView;

import java.util.*;

public class TUI implements ClientModelObserver, ViewAdapter{

    private final CommandManager commandManager;
    private boolean cliRunning;
    private ClientModel model;
    private List<String> input;
    private String commandName;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Command> allCommands;

    public TUI(CommandManager commandManager, ClientModel model) {
        this.commandManager = commandManager;
        this.cliRunning = true;
        this.model = model;
        this.input = new ArrayList<>();
        this.allCommands = commandManager.getAllCommandTypes();
    }

    @Override
    public synchronized void modelChanged(ClientModel model) {
        System.out.println("/n");
        List<Command> availableCommands = commandManager.getAvailableCommandTypes(model);
        for(Command command : availableCommands) {
            System.out.println(command.getName());
        }
        System.out.print("> ");
        this.model = model;
        if(model.getGamePhase().getPhaseType().equals(PhaseType.END))
            cliRunning = false;
    }


    //return true if the format is valid
    private boolean askCommand() {
        String inputLine = scanner.nextLine();
        char currChar = inputLine.charAt(0);
        this.input.clear();
        int i = 0;
        while(currChar != '(') {
            i++;
            currChar = inputLine.charAt(i);
        }
        if(i == 0)
            return false;
        this.commandName = inputLine.substring(0, i - 1).replaceAll("\\s+", "");
        while(currChar != ')') {
            int beginningIndex = i + 1;
            while(currChar !=  ',' && currChar != ')') {
                i++;
                currChar = inputLine.charAt(i);
            }
            this.input.add(inputLine.substring(beginningIndex, i - 1).replaceAll("\\s+", ""));
        }
        if(inputLine.length() == i + 1)
            return false;
        return true;
    }

    private Command findCommand(String CommandName) {
        for(Command command : allCommands) {
            if(commandName.equalsIgnoreCase(command.getName())) {
                return command;
            }
        }
        return null;
    }

    @Override
    public void showCardPile(int idx, ClientModel model) {
        List<AdventureCard> deck = model.getCardPiles().get(idx).getCards();
        for (int i = 0; i < 3; i++) {
            AdventureCard card = deck.get(i);
            switch (deck.get(i).getClass().getSimpleName()){
                case "AbandonedShipCard":
                    //showAbandonedShipCard(card);
                    break;
                case "AbandonedStationCard":
                    //showAbandonedStationCard(card);
                    break;
                case "CombatZoneCard":
                    break;
                case "EpidemicCard":
                    break;
                case "MeteorSwarmCard":
                    break;
                case "OpenSpaceCard":
                    break;
                case "PiratesCard":
                    break;
                case "PlanetsCard":
                    break;
                case "SlaversCard":
                    break;
                case "SmugglersCard":
                    break;
                case "StardustCard":
                    break;
                default: break;
            }
        }
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

        for (int y = 0; y < 7; y++){
            for (int x = 0; x < 7; x++){
                Optional<ComponentTile> c = ship.getComponentTileFromGrid(x, y);
                if(c.isPresent()){
                    System.out.println("Riga: " + y + " Colonna: " + x + c);
                }
                else{
                    System.out.println("Riga: " + y + " Colonna: " + x + " Vuoto");
                }
            }
        }
    }

    @Override
    public void showShipboardStandByComponents(String player, ClientModel clientModel) {
        Map<String, Shipboard> shipboards = clientModel.getShipboards();
        Shipboard ship = shipboards.get(player);
        System.out.println("=== COMPONENTI IN STAND BY ===");

        for (int i = 0; i < 2; i++){
            Optional<ComponentTile>[] standbyComponents = ship.getStandbyComponent();
            Optional<ComponentTile> c = standbyComponents[i];
            if (c.isPresent()){ System.out.println(c); }
            else{ System.out.println("Vuoto"); }
        }
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

    @Override
    public void showLeaderboard(ClientModel clientModel) {
        /*TO DO*/
    }

    public void run() {
        Command chosen = null;
        boolean commandNotValid;
        System.out.print("> ");
        while(cliRunning) {
            do {
                do {
                    while (!askCommand())
                        System.out.println("Invalid format, try again");
                    chosen = findCommand(commandName);
                    if (chosen == null)
                        System.out.println("Command does not exist, try again");
                } while(chosen == null);

                commandNotValid = false;

                chosen.setInput(this.input);
                synchronized(this) {
                    if(!chosen.isApplicable(model)) {
                        System.out.println("Command is not available, try again");
                        commandNotValid = true;
                    }
                    else {
                        commandNotValid = !chosen.isInputValid(model);
                        if (commandNotValid)
                            System.out.println("Invalid input, try again");
                        else
                            chosen.execute(model);
                    }
                }
            } while (commandNotValid);
        }

        synchronized(this) {
            showLeaderboard(model);
        }
    }

}
