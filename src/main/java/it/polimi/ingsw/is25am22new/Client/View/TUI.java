package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
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
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Client.View.AdventureCardView;

import java.util.*;

import static it.polimi.ingsw.is25am22new.Client.View.AdventureCardView.*;

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
                    showAbandonedShipCard((AbandonedShipCard) card);
                    break;
                case "AbandonedStationCard":
                    showAbandonedStationCard((AbandonedStationCard) card);
                    break;
                case "CombatZoneCard":
                    showCombatZoneCard((CombatZoneCard) card);
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
        Flightboard flightboard = clientModel.getFlightboard();
        Map <String, Integer> positions = flightboard.getPositions();
        System.out.println("=== FLIGHTBOARD ===");
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    @Override
    public void showCard(AdventureCard card, ClientModel clientModel) {
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
            // scores.put(betterShipboard(), scores.get(betterShipboard()) + 2);
            // betterShipboard() come va usato?
        }
        else {
            for(int i = 0; i < orderedRockets.size(); i++) {
                scores.put(orderedRockets.get(i), scores.get(orderedRockets.get(i)) + 2*(4 - orderedRockets.indexOf(orderedRockets.get(i))));
            }
            // scores.put(ClientModel.betterShipboard(), scores.get(betterShipboard()) + 4);
            // betterShipboard() come va usato?
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
