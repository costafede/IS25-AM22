package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import java.util.*;

/**
 * The TUI class represents the Text User Interface for interacting with the game.
 * It serves as an implementation of both the ClientModelObserver and ViewAdapter interfaces,
 * allowing it to respond to game model updates and provide a text-based interface for the user.
 */
public class TUI implements ClientModelObserver,ViewAdapter{

    private final CommandManager commandManager;
    private boolean cliRunning;
    private ClientModel model;
    private List<String> input;
    private String commandName;
    private final List<Command> allCommands;

    public TUI(CommandManager commandManager, ClientModel model) {
        super();
        this.commandManager = commandManager;
        this.cliRunning = true;
        this.model = model;
        this.input = new ArrayList<>();
        this.allCommands = commandManager.getAllCommandTypes();
    }

    /**
     * Parses a command input from the user and processes it by extracting the command name
     * and parameters, if valid. The method ensures the input follows the expected syntax,
     * which includes a command name followed by optional parameters enclosed within parentheses.
     *
     * @param scanner the Scanner object used to read user input from the console
     * @return true if the input adheres to the expected command format and is successfully processed,
     *         false otherwise
     */
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

    /**
     * Searches for a command in the list of all available commands by its name.
     *
     * @param CommandName the name of the command to search for; it is case-insensitive
     * @return the Command object if found, or null if no command with the specified name exists
     */
    private Command findCommand(String CommandName) {
        for(Command command : allCommands) {
            if(commandName.equalsIgnoreCase(command.getName())) {
                return command;
            }
        }
        return null;
    }

    /**
     * Displays the cards from a specified pile of the card deck.
     * Only the first three cards from the pile are shown.
     *
     * @param idx the index of the card pile to display
     * @param model the client model containing the game state and card piles
     */
    @Override
    public void showCardPile(int idx, ClientModel model) {
        List<AdventureCard> deck = model.getCardPiles().get(idx).getCards();
        for (int i = 0; i < 3; i++) {
            AdventureCard card = deck.get(i);
            showCard(card, model);
        }
    }

    /**
     * Displays the shipboard grid for a specific player, including detailed ship information and the grid
     * representation that shows the components positioned on the shipboard.
     *
     * @param player the name of the player whose shipboard grid is to be displayed
     * @param clientModel the client model containing the player's shipboard and associated game state
     */
    @Override
    public void showShipboardGrid(String player, ClientModel clientModel) {
        Map<String, Shipboard> shipboards = clientModel.getShipboards();
        Shipboard ship = shipboards.get(player);

        //shows the information of the ship
        System.out.println("=== " + player + "'s SHIP ===");
        System.out.println("Days on flight: " + ship.getDaysOnFlight());
        System.out.println("Credits: " + ship.getCosmicCredits());
        System.out.println("Flight crew: " + ship.getCrewNumber());
        System.out.println("Astronauts: " + ship.getOnlyHumanNumber());
        System.out.println("Cannon strength: " + ship.getCannonStrength());
        System.out.println("Engine strength: " + ship.getEngineStrength());

        //coordinates of the grid
        int x = 4;
        int y = 5;

        System.out.print("    ");
        for (int column = 0; column < 7; column++) {
            System.out.print("    " + x + "   ");
            x++;
        }
        System.out.println();

        /**
         * the actual grid with the components inside
         */
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

                //prints the components in the grid
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

    /**
     * Displays the shipboard standby component area for a specified player.
     * The method retrieves the player's shipboard from the client model and
     * prints a visual representation of the standby components section.
     *
     * @param player the name of the player whose standby components are to be displayed
     * @param clientModel the client model containing the player's shipboard and game state
     */
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

    /**
     * Displays the flightboard by printing the contents of the flightboard positions
     * retrieved from the given client model. The flightboard shows the names of items
     * and their corresponding positions.
     *
     * @param clientModel the client model containing the flightboard and game state
     */
    @Override
    public void showFlightboard(ClientModel clientModel) {
        Flightboard flightboard = clientModel.getFlightboard();
        Map <String, Integer> positions = flightboard.getPositions();
        System.out.println("=== FLIGHTBOARD ===");
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Displays the provided adventure card in a suitable format using the given client model.
     * If the card is null, a message indicating the absence of a card is displayed.
     * If the card is a type that can be viewed, it will invoke its specific visualization logic.
     * Unsupported card types will result in an error message.
     *
     * @param card the adventure card to be displayed; can be null or an instance of a supported card type
     * @param model the client model containing the game state and other necessary data for card display
     */
    @Override
    public void showCard(AdventureCard card, ClientModel model) {
        AdventureCardViewTUI adventureCardViewTUI = new AdventureCardViewTUI();
        if (card == null) {
            System.out.println("There is no card");
        } else if (card instanceof ViewableCard) {
            ((ViewableCard) card).show(adventureCardViewTUI, model);
        } else {
            System.out.println("Unsupported card type: " + card.getClass().getSimpleName());
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

    /**
     * Displays the leaderboard for the game by calculating and ranking players' scores based on their shipboard
     * scores and additional bonuses. The leaderboard is printed in descending order of scores.
     *
     * @param clientModel the client model containing the game state, including shipboards and flightboard data,
     *                    used to calculate and display the leaderboard
     */
    @Override
    public void showLeaderboard(ClientModel clientModel) {
        Map<String, Shipboard> shipboards = clientModel.getShipboards();
        List <String> players = new ArrayList<>();

        /**
         * Creating the list of players and map for scores of the players
         */

        for (Map.Entry<String, Shipboard> entry : shipboards.entrySet()){
            players.add(entry.getKey());
        }

        Map<String, Integer> scores = new HashMap<>();

        /**
         * calculates the base score of each player
         */

        for (int i = 0; i < players.size(); i++){
            scores.put(players.get(i), shipboards.get(players.get(i)).getScore());
        }

        Flightboard flightboard = clientModel.getFlightboard();
        List<String> orderedRockets = flightboard.getOrderedRockets();

        /**
         * adds points for the better ship
         */

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

        /**
         * sorts the scores in descending order and puts them in a map with
         * the nickname as key and the score as value
         */

        scores = scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        LinkedHashMap::putAll
                );

        /**
         * prints the leaderboard
         */

        System.out.println("=== LEADERBOARD ===");
        int x = 1;
        for (Map.Entry<String, Integer> entry : scores.entrySet()){
            System.out.println(x + ". " + entry.getKey() + ": " + entry.getValue());
            x++;
        }

    }

    protected String betterShipboard(List <String> players, Map<String, Shipboard> shipboards) {
        return players.stream()
                .min(Comparator.comparingInt(nickname -> shipboards.get(nickname).countExposedConnectors()))
                .orElse(null);
    }

    /**
     * Displays a list of all available commands to the user by printing their names.
     * The commands are determined based on the current client model's state.
     *
     * @param clientModel the client model containing the state of the game,
     *                    which is used to determine the list of available commands
     */
    @Override
    public synchronized void showAvailableCommands(ClientModel clientModel) {
        List<Command> availableCommands = commandManager.getAvailableCommandTypes(model);
        for (Command command : availableCommands) {
            System.out.println(command.getName());
        }
    }

    /**
     * Displays the tile currently in hand for a specified player.
     * If no tile is in hand, a message indicating the absence of a tile is shown.
     * Otherwise, the tile is visually represented using ASCII characters.
     *
     * @param player the name of the player whose tile in hand is to be displayed
     * @param clientModel the client model containing the player's shipboard and game state
     */
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

    /**
     * Continuously runs the Text-based User Interface (TUI) for interacting with the game.
     * This method handles user input via a scanner to identify and execute commands from
     * the available command list based on the current game state.
     *
     * While the TUI is active, it ensures the following:
     * - Waits for the game start message before accepting inputs.
     * - Validates the format and existence of commands entered by the user.
     * - Ensures the command is applicable in the current game state and that its inputs are valid.
     * - Executes the command if all validations pass.
     * - Prompts the user with appropriate messages for invalid commands or inputs.
     *
     * Synchronization is used to ensure proper handling of game state changes
     * and communication with the ClientModel during execution.
     *
     * @param scanner the Scanner object for reading user input from the console
     */
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
            } while (commandNotValid);
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

    @Override
    public void showBank(ClientModel model) {
        Bank bank = model.getBank();
        System.out.println("=== BANK ===");
        System.out.println("RED BLOCKS: " + bank.getNumGoodBlock(GoodBlock.REDBLOCK));
        System.out.println("YELLOW BLOCKS: " + bank.getNumGoodBlock(GoodBlock.YELLOWBLOCK));
        System.out.println("GREEN BLOCKS: " + bank.getNumGoodBlock(GoodBlock.GREENBLOCK));
        System.out.println("BLUE BLOCKS: " + bank.getNumGoodBlock(GoodBlock.BLUEBLOCK));
    }

    @Override
    public void abandonGame(String player) {
        System.out.println(player + " have abandoned the game");
    }

    @Override
    public void quit() {
        this.cliRunning = false;
    }




    private synchronized void modelChanged() {
        this.notifyAll();
    }

    @Override
    public void updateGame(ClientModel model) {
        modelChanged();
    }

    @Override
    public void updateStopHourglass() {
        modelChanged();
    }

    @Override
    public void updateStartHourglass(int hourglassSpot) {
        modelChanged();
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        modelChanged();
    }

    @Override
    public void updateBank(Bank bank) {
        modelChanged();
    }

    @Override
    public void updateCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {
        modelChanged();
    }

    @Override
    public void updateUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        modelChanged();
    }

    @Override
    public void updateShipboards(Map<String, Shipboard> shipboards) {
        modelChanged();
    }

    @Override
    public void updateFlightboard(Flightboard flightboard) {
        modelChanged();
    }

    @Override
    public void updateShipboard(Shipboard shipboard) {
        modelChanged();
    }

    @Override
    public void updateDeck(List<AdventureCard> deck) {
        modelChanged();
    }

    @Override
    public void updateCurrPlayer(String player) {
        modelChanged();
    }

    @Override
    public void updateCurrCard(AdventureCard currCard) {
        modelChanged();
    }

    @Override
    public void updateDices(Dices dices) {
        modelChanged();
    }

    @Override
    public void updateGameStartMessageReceived(boolean gameStartMessageReceived) {
        modelChanged();
    }

    @Override
    public void updateTileInHand(String player, ComponentTile ct) {
        modelChanged();
    }

}
