package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2.CombatZoneCard2;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard.OpenSpaceCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard.PiratesCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard.SmugglersCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard.StardustCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.*;

import static it.polimi.ingsw.is25am22new.Client.View.AdventureCardView.*;

public class TUI implements ClientModelObserver, ViewAdapter{

    private final CommandManager commandManager;
    private boolean cliRunning;
    private ClientModel model;
    private List<String> input;
    private String commandName;
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
        this.model = model;
        if(model.getGamePhase().getPhaseType().equals(PhaseType.END))
            cliRunning = false;
        this.notifyAll();
    }


    //return true if the format is valid
    public boolean askCommand(Scanner scanner) {
        String inputLine = null;
        inputLine = scanner.nextLine();
        if(inputLine == null || inputLine.isEmpty())
            return false;
        char currChar = inputLine.charAt(0);
        this.input.clear();
        int i = 0;
        while(currChar != '(') {
            i++;
            if(i == inputLine.length())
                return false;
            currChar = inputLine.charAt(i);
        }
        if(i == 0)
            return false;
        this.commandName = inputLine.substring(0, i).replaceAll("\\s+", "");
        if(inputLine.charAt(inputLine.length() - 1) != ')')
            return false;
        inputLine = inputLine.substring(i + 1).replaceAll("\\s+", "");
        if(inputLine.equals(")"))
            return true;
        if(inputLine.contains("(") || inputLine.substring(0, inputLine.length() - 1).contains(")"))
            return false;
        i = 0;
        while(currChar != ')') {
            int beginningIndex = i;
            currChar = inputLine.charAt(i);
            while(currChar !=  ',' && currChar != ')') {
                i++;
                currChar = inputLine.charAt(i);
            }
            String inputParameter = inputLine.substring(beginningIndex, i).replaceAll("\\s+", "");
            if(!inputParameter.isEmpty())
                this.input.add(inputParameter);
            else
                return false;
            i++;
        }
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
            showCard(card, model);
        }
    }

    @Override
    public void showShipboardGrid(String player, ClientModel clientModel) {
        Map<String, Shipboard> shipboards = clientModel.getShipboards();
        Shipboard ship = shipboards.get(player);
        System.out.println("=== YOUR SHIP ===");
        System.out.println("Days on flight: " + ship.getDaysOnFlight());
        System.out.println("Credits: " + ship.getCosmicCredits());
        System.out.println("Flight crew: " + ship.getCrewNumber());
        System.out.println("Astronauts: " + ship.getOnlyHumanNumber());

        int x = 4;
        int y = 5;

        System.out.print("    ");
        for (int column = 0; column < 7; column++) {
            System.out.print("    " + x + "   ");
            x++;
        }
        System.out.println();


        for (int line = 0; line < 5; line++) {

            System.out.print("    ");
            for (int column = 0; column < 7; column++) {
                System.out.print("+");
                for (int i = 0; i < 7; i++) {
                    System.out.print("-");
                }
            }
            System.out.println("+");

            for (int h = 0; h < 5; h++) {
                if (h == 2) System.out.print(" " + y + "  "); // prints the line number on the left side of the grid
                else System.out.print("    ");
                for (int column = 0; column < 7; column++) {
                    System.out.print("|");
                    Optional<ComponentTile> c = ship.getComponentTileFromGrid(line, column);
                    if (c.isPresent()) {
                        String[] draw = c.get().draw();
                        System.out.print(draw[h]);
                    }
                    else {
                        System.out.print("       "); //no component = empty cell
                    }
                }
                System.out.println("|");
            }
            y++;
        }

        System.out.print("    ");
        for (int column = 0; column < 7; column++) {
            System.out.print("+");
            for (int i = 0; i < 7; i++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }


    @Override
    public void showShipboardStandByComponents(String player, ClientModel clientModel) {
        Map<String, Shipboard> shipboards = clientModel.getShipboards();
        Shipboard ship = shipboards.get(player);
        System.out.println("=== COMPONENTS IN STAND BY ===");

        for (int column = 0; column < 2; column++) {
            System.out.print("+");
            for (int i = 0; i < 7; i++) {
                System.out.print("-");
            }
        }
        System.out.println("+");

        for (int h = 0; h < 5; h++) {
            for (int column = 0; column < 2; column++) {
                System.out.print("|");
                Optional<ComponentTile>[] standbyComponents = ship.getStandbyComponent();
                Optional<ComponentTile> c = standbyComponents[column];
                if (c.isPresent()) {
                    String[] draw = c.get().draw();
                    System.out.print(draw[h]);
                }
                else {
                    System.out.print("       "); //no component = empty cell
                }
            }
            System.out.println("|");
        }


        for (int column = 0; column < 2; column++) {
            System.out.print("+");
            for (int i = 0; i < 7; i++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    @Override
    public void showFlightboard(ClientModel clientModel) {
        Flightboard flightboard = clientModel.getFlightboard();
        Map <String, Integer> positions = flightboard.getPositions();
        System.out.println("=== FLIGHTBOARD ===");
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    @Override
    public void showCard(AdventureCard card, ClientModel clientModel) {
        if(card == null)
            System.out.println("There is no card");
        else {
            switch (card.getClass().getSimpleName()){
                case "AbandonedShipCard":
                    showAbandonedShipCard((AbandonedShipCard) card);
                    break;
                case "AbandonedStationCard":
                    showAbandonedStationCard((AbandonedStationCard) card);
                    break;
                case "CombatZoneCard":
                    showCombatZoneCard((CombatZoneCard) card);
                    break;
                case "CombatZoneCard2":
                    showCombatZoneCard2((CombatZoneCard2) card);
                    break;
                case "EpidemicCard":
                    showEpidemicCard((EpidemicCard) card);
                    break;
                case "MeteorSwarmCard":
                    showMeteorSwarmCard((MeteorSwarmCard) card);
                    break;
                case "OpenSpaceCard":
                    showOpenSpaceCard((OpenSpaceCard) card);
                    break;
                case "PiratesCard":
                    showPiratesCard((PiratesCard) card);
                    break;
                case "PlanetsCard":
                    showPlanetsCard((PlanetsCard) card);
                    break;
                case "SlaversCard":
                    showSlaversCard((SlaversCard) card);
                    break;
                case "SmugglersCard":
                    showSmugglersCard((SmugglersCard) card);
                    break;
                case "StardustCard":
                    showStardustCard((StardustCard) card);
                    break;
                default: break;
            }
        }
    }

    @Override
    public void showUncoveredComponentTiles(ClientModel clientModel) {
        List <ComponentTile> componentTiles = clientModel.getUncoveredComponentTiles();
        System.out.println("=== UNCOVERED COMPONENTS ===");
        for (int i = 0; i < componentTiles.size(); i++){
            System.out.println(i + ": " + componentTiles.get(i));
        }
    }

    @Override
    public void showLeaderboard(ClientModel clientModel) {
        Map<String, Shipboard> shipboards = clientModel.getShipboards();
        List <String> players = new ArrayList<>();

        for (Map.Entry<String, Shipboard> entry : shipboards.entrySet()){
            players.add(entry.getKey());
        }

        Map<String, Integer> scores = new HashMap<>();

        for (int i = 0; i < players.size(); i++){
            scores.put(players.get(i), shipboards.get(players.get(i)).getScore());
        }

        Flightboard flightboard = clientModel.getFlightboard();
        List<String> orderedRockets = flightboard.getOrderedRockets();

        if (clientModel.getGametype().equals(GameType.TUTORIAL)){
            for(int i = 0; i < orderedRockets.size(); i++) {
                scores.put(orderedRockets.get(i), scores.get(orderedRockets.get(i)) + (4 - orderedRockets.indexOf(orderedRockets.get(i))));
            }
            scores.put(betterShipboard(players, shipboards), scores.get(betterShipboard(players, shipboards)) + 2);
        }
        else {
            for(int i = 0; i < orderedRockets.size(); i++) {
                scores.put(orderedRockets.get(i), scores.get(orderedRockets.get(i)) + 2*(4 - orderedRockets.indexOf(orderedRockets.get(i))));
            }
            scores.put(betterShipboard(players, shipboards), scores.get(betterShipboard(players, shipboards)) + 4);
        }

        scores = scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        LinkedHashMap::putAll
                );

        System.out.println("=== LEADERBOARD ===");
        for (Map.Entry<String, Integer> entry : scores.entrySet()){
            int x = 1;
            System.out.println(x + entry.getKey() + ": " + entry.getValue());
            x++;
        }

    }

    public String betterShipboard(List <String> players, Map<String, Shipboard> shipboards) {
        return players.stream()
                .min(Comparator.comparingInt(nickname -> shipboards.get(nickname).countExposedConnectors()))
                .orElse(null);
    }

    @Override
    public synchronized void showAvailableCommands(ClientModel clientModel) {
        List<Command> availableCommands = commandManager.getAvailableCommandTypes(model);
        for(Command command : availableCommands) {
            System.out.println(command.getName());
        }
    }

    @Override
    public void showTileInHand(String player, ClientModel clientModel) {
        ComponentTile ct = clientModel.getShipboard(player).getTileInHand();
        if(ct == null){
            System.out.println("No tile in hand");
        }
        else {
            System.out.println("Tile in hand: ");
            for (int column = 0; column < 1; column++) {
                System.out.print("+");
                for (int i = 0; i < 7; i++) {
                    System.out.print("-");
                }
            }
            System.out.println("+");

            for (int h = 0; h < 5; h++) {
                for (int column = 0; column < 1; column++) {
                    System.out.print("|");
                    String[] draw = ct.draw();
                    System.out.print(draw[h]);
                }
                System.out.println("|");
            }

            for (int column = 0; column < 1; column++) {
                System.out.print("+");
                for (int i = 0; i < 7; i++) {
                    System.out.print("-");
                }
            }
            System.out.println("+");
        }
    }

    public void run(Scanner scanner) {
        Command chosen = null;
        boolean commandNotValid;
        while(cliRunning) {
            do {
                synchronized (this){
                    while(!model.isGameStartMessageReceived()) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                System.out.println();
                System.out.print("> ");
                do {
                    while (!askCommand(scanner)) {
                        System.out.println("Invalid format, try again");
                        System.out.print("> ");
                    }
                    chosen = findCommand(commandName);
                    if (chosen == null) {
                        System.out.println("Command does not exist, try again");
                        System.out.print("> ");
                    }
                } while(chosen == null);

                commandNotValid = false;

                chosen.setInput(this.input);
                synchronized(this) {
                    while(model.getBank() == null) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }

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

    public List<String> getInput() {
        return this.input;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public void showRemainingSeconds(ClientModel clientModel) {
        if(!clientModel.isHourglassActive())
            System.out.println("Hourglass is not active");
        else
            System.out.println("There are " + clientModel.getHourglass().getRemainingSeconds() + " seconds left!");
    }

    @Override
    public void showCurrPhase(ClientModel model) {
        System.out.println("=== CURRENT PHASE ===");
        System.out.println("Current phase: " + model.getGamePhase().getPhaseType());
    }

}
