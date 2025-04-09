package it.polimi.ingsw.is25am22new.Network.Socket.Client;

import it.polimi.ingsw.is25am22new.Client.View.GameCliView;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SocketClientSide implements VirtualView {

    GameCliView gameCliView;
    final ObjectInputStream objectInput;
    final SocketServerHandler output;
    String thisPlayerName;

    protected SocketClientSide(InputStream is, OutputStream os) throws IOException {
        this.output = new SocketServerHandler(os);
        this.objectInput = new ObjectInputStream(is);
        this.thisPlayerName = "Player";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket serverSocket = new Socket(host, port);
        new SocketClientSide(serverSocket.getInputStream(), serverSocket.getOutputStream()).run();
    }

    private void run() throws IOException, InterruptedException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }

    // comunicazione dal server al client
    private void runVirtualServer() throws IOException, ClassNotFoundException {
        SocketMessage msg;
        List<String> players = List.of("ERROR");
        String gameType = "ERROR";
        boolean isHost = false;
        String message = "ERROR";
        while ((msg = (SocketMessage) objectInput.readObject()) != null) {
            switch (msg.getCommand()) {
                case "Bank" -> {
                    Bank bank = (Bank) msg.getObject();
                    this.showUpdateBank(bank);
                }
                case "TileInHand" -> {
                    ComponentTile tile = (ComponentTile) msg.getObject();
                    String player = msg.getPayload();
                    this.showUpdateTileInHand(player, tile);
                }
                case "UncoveredComponentTile" -> {
                    List<ComponentTile> ctList = (List<ComponentTile>) msg.getObject();
                    this.showUpdateUncoveredComponentTiles(ctList);
                }
                case "CoveredComponentTile" -> {
                    List<ComponentTile> ctList = (List<ComponentTile>) msg.getObject();
                    this.showUpdateCoveredComponentTiles(ctList);
                }
                case "Shipboard" -> {
                    String player = msg.getPayload();
                    Shipboard shipboard = (Shipboard) msg.getObject();
                    this.showUpdateShipboard(player, shipboard);
                }
                case "Flightboard" -> {
                    Flightboard flightboard = (Flightboard) msg.getObject();
                    this.showUpdateFlightboard(flightboard);
                }
                case "CurrCard" -> {
                    AdventureCard card = (AdventureCard) msg.getObject();
                    this.showUpdateCurrCard(card);
                }
                case "Dices" -> {
                    Dices dices = (Dices) msg.getObject();
                    this.showUpdateDices(dices);
                }
                case "CurrPlayer" -> {
                    String currPlayer = msg.getPayload();
                    this.showUpdateCurrPlayer(currPlayer);
                }
                case "GamePhase" -> {
                    GamePhase gamePhase = (GamePhase) msg.getObject();
                    this.showUpdateGamePhase(gamePhase);
                }
                case "Deck" -> {
                    List<AdventureCard> deck = (List<AdventureCard>) msg.getObject();
                    this.showUpdateDeck(deck);
                }
                case "Game" -> {
                    Game game = (Game) msg.getObject();
                    this.showUpdateGame(game);
                }
                case "HourglassSpot" -> {
                    int hourglassSpot = (int) msg.getObject();
                    this.showUpdateHourglassSpot(hourglassSpot);
                }
                case "LobbyUpdate" -> {
                    players = (List<String>) msg.getObject();
                    gameType = msg.getPayload();
                }
                case "ReadyStatus" -> {
                    // might be wrong
                    Map<String, Boolean> readyStatus = (Map<String, Boolean>) msg.getObject();
                    this.showLobbyUpdate(players, readyStatus, gameType);
                }
                case "ConnectionResult" -> {
                    // might be wrong
                    isHost = (boolean) msg.getObject();
                    message = msg.getPayload();
                }
                case "Success" -> {
                    boolean success = (boolean) msg.getObject();
                    this.showConnectionResult(isHost, success, message);
                }
                case "NicknameResult" -> {
                    boolean valid = (boolean) msg.getObject();
                    message = msg.getPayload();
                    this.showNicknameResult(valid, message);
                }
                case "GameStarted" -> {
                    this.showGameStarted();
                }
                case "PlayerJoined" -> {
                    String player = msg.getPayload();
                    this.showPlayerJoined(player);
                }
                case "updateTest" -> {
                    System.out.println(msg.getPayload());
                    System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
                    System.out.flush();
                }
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void runCli() throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        int numOfRotations = 0;

        System.out.println("Enter your cool trucker name: ");
        System.out.flush();
        thisPlayerName = scan.nextLine();

        while(thisPlayerName == null || thisPlayerName.isEmpty()) {
            System.out.println("Please enter a valid name: ");
            thisPlayerName = scan.nextLine();
        }

        while (true) {
            Thread.sleep(50);
            System.out.print(">>> ");
            int command = scan.nextInt();
            scan.nextLine();
            switch (command) {
                case 0 -> {
                    output.addPlayer(thisPlayerName);
                }
                case 1 -> {
                    System.out.println("Enter the name of the player to remove: ");
                    String player = scan.nextLine();

                    while(player == null || player.isEmpty()) {
                        System.out.println("Please enter a valid name: ");
                        player = scan.nextLine();
                    }

                    output.removePlayer(player);
                }
                case 2 -> {
                    System.out.println("You're ready!");
                    output.setPlayerReady(thisPlayerName);
                }
                case 3 -> {
                    System.out.println("Let's start the game!");
                    output.startGameByHost(thisPlayerName);
                }
                case 4 -> {
                    System.out.println("You're not ready!");
                    output.setPlayerNotReady(thisPlayerName);
                }
                case 5 -> {
                    System.out.println("What type of game do you want to play? (tutorial/level2)");
                    String gameType = scan.nextLine();

                    while(gameType == null || (!gameType.equals("tutorial") && !gameType.equals("level2"))) {
                        System.out.println("Please enter a valid type: ");
                        gameType = scan.nextLine();
                    }

                    output.setGameType(gameType);
                }
                case 6 -> {
                    if(gameCliView.getShipboard(thisPlayerName).getTileInHand() != null) {
                        System.out.println("You already have a tile in your hand!");
                    }
                    else {
                        System.out.println("Covered tile picked!");
                        output.pickCoveredTile(thisPlayerName);
                    }
                }
                case 7 -> {
                    if(gameCliView.getShipboard(thisPlayerName).getTileInHand() != null) {
                        System.out.println("You already have a tile in your hand!");
                    }
                    else {
                        System.out.println("Which tile you want to pick?: ");
                        int index = scan.nextInt();
                        scan.nextLine();

                        while(index < 0) {
                            System.out.println("Please enter a valid index: ");
                            index = scan.nextInt();
                            scan.nextLine();
                        }
                        System.out.println("Uncovered tile picked!");
                        output.pickUncoveredTile(thisPlayerName, index);
                    }
                }
                case 8 -> {
                    if(gameCliView.getShipboard(thisPlayerName).getTileInHand() != null) {
                        System.out.println("Tile rotated to the right!");
                        numOfRotations++;
                    }
                    else {
                        System.out.println("You don't have a tile in your hand!");
                    }
                }
                case 9 -> {
                    if(gameCliView.getShipboard(thisPlayerName).getTileInHand() != null) {
                        System.out.println("Tile rotated to the left!");
                        numOfRotations--;
                    }
                    else {
                        System.out.println("You don't have a tile in your hand!");
                    }
                }
                case 10 -> {
                    // based on the type of game HAVE TO SET LIMITS THE COORDINATES
                    System.out.println("Where do you want to place the tile?: \n");
                    System.out.println("X coordinate: ");
                    int x = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Y coordinate: ");
                    int y = scan.nextInt();
                    scan.nextLine();
                    output.weldComponentTile(thisPlayerName, x, y, numOfRotations);
                    numOfRotations = 0;
                }
                case 11 -> {
                    try {
                        gameCliView.getShipboard(thisPlayerName).pickStandByComponentTile(0);
                        try{
                            gameCliView.getShipboard(thisPlayerName).pickStandByComponentTile(1);
                            System.out.println("Standby positions are full!");
                        }
                        catch(IllegalStateException e){
                            System.out.println("Putting tile in standby!");
                            output.standbyComponentTile(thisPlayerName);
                        }
                    }
                    catch (IllegalStateException e){
                        System.out.println("Putting tile in standby!");
                        output.standbyComponentTile(thisPlayerName);
                    }
                }
                case 12 -> {
                    System.out.println("Which tile do you want to pick?: (1/2) ");
                    int index = scan.nextInt();
                    scan.nextLine();

                    while(index != 1 && index != 2) {
                        System.out.println("Please enter a valid index: ");
                        index = scan.nextInt();
                        scan.nextLine();
                    }

                    if(index == 1) {
                        try {
                            gameCliView.getShipboard(thisPlayerName).pickStandByComponentTile(0);
                            output.pickStandbyComponentTile(thisPlayerName, 0);
                        } catch (IllegalStateException e) {
                            System.out.println("You don't have a tile in that position!");
                        }
                    }
                    else {
                        try {
                            gameCliView.getShipboard(thisPlayerName).pickStandByComponentTile(1);
                            output.pickStandbyComponentTile(thisPlayerName, 1);
                        } catch (IllegalStateException e) {
                            System.out.println("You don't have a tile in that position!");
                        }
                    }

                }
                case 13 -> {
                    if(gameCliView.getShipboard(thisPlayerName).getTileInHand() != null) {
                        System.out.println("Tile discarded!");
                        output.discardComponentTile(thisPlayerName);
                    }
                    else {
                        System.out.println("You don't have a tile in your hand!");
                    }
                }
                case 14 -> {
                    System.out.println("You've finally finished your ship! Wow!");
                    output.finishBuilding(thisPlayerName);
                }
                case 15 -> {
                    System.out.println("You've finished your ship! Congrats!");
                    System.out.println("Fast! Where do you want to place your rocket? (1/2/3/4)");
                    int pos = scan.nextInt();
                    scan.nextLine();
                    output.finishBuilding(thisPlayerName, pos);
                }
                case 16 -> {
                    System.out.println("All shipboards are finished! Let's go!");
                    output.finishedAllShipboards();
                }
                case 17 -> {
                    System.out.println("Hourglass flipped!");
                    output.flipHourglass();
                }
                case 18 -> {
                    System.out.println("Card picked!");
                    output.pickCard();
                }
                case 19 -> {
                    if(gameCliView.getCurrCard() == null) {
                        System.out.println("There is no card to activate!");
                    }
                    else {
                        System.out.println("Card activated!");
                        InputCommand inputCommand = constructInputCommand();
                        output.activateCard(inputCommand);
                    }
                }
                case 20 -> {
                    if(gameCliView.getShipboard(thisPlayerName).isAbandoned()) {
                        System.out.println("You have already abandoned the game!");
                    }
                    else {
                        System.out.println("Are you sure you want to abandon the game? (true/false)");
                        boolean choice = scan.nextBoolean();
                        scan.nextLine();
                        if(choice)  output.playerAbandons(thisPlayerName);
                    }
                }
                case 21 -> {
                    System.out.println("Which tile do you want to destroy?: ");
                    System.out.println("X coordinate: ");
                    int x = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Y coordinate: ");
                    int y = scan.nextInt();
                    scan.nextLine();

                    if(gameCliView.getShipboard(thisPlayerName).getComponentTileFromGrid(x, y).isEmpty()) {
                        System.out.println("You don't have a tile in that position!");
                    }
                    else {
                        System.out.println("Tile destroyed!");
                        output.destroyComponentTile(thisPlayerName, x, y);
                    }
                }
                case 22 -> {
                    if(gameCliView.getGamePhase().getPhaseType() == PhaseType.END) {
                        System.out.println("Game is over!");
                        output.endGame();
                    }
                    else {
                        System.out.println("Game is not over yet!");
                    }
                }
                case 99 -> {
                    System.out.println("String to send: ");
                    String test = scan.nextLine();
                    output.connectionTester(test, 33879);
                }
            }
        }
    }

    private InputCommand constructInputCommand(){
        Scanner scanner = new Scanner(System.in);
        InputCommand inputCommand = new InputCommand();
        System.out.println("Possible commands: \n");
        System.out.println("1. Choice command");
        System.out.println("2. Choose index command");
        System.out.println("3. Choose tile coordinates command");
        System.out.println("4. Manage good-blocks command");
        int index = scanner.nextInt();
        switch (index) {
            case 1 -> {
                System.out.println("Enter your choice: (true/false)");
                boolean choice = scanner.nextBoolean();
                scanner.nextLine();
                inputCommand.setChoice(choice);
            }
            case 2 -> {
                System.out.println("Enter the index you want to choose: ");
                int indexChosen = scanner.nextInt();
                scanner.nextLine();
                inputCommand.setIndexChosen(indexChosen);
            }
            case 3 -> {
                System.out.println("Enter the row you want to choose: ");
                int row = scanner.nextInt();
                scanner.nextLine();
                inputCommand.setRow(row);
                System.out.println("Enter the column you want to choose: ");
                int col = scanner.nextInt();
                scanner.nextLine();
                inputCommand.setCol(col);
            }
            case 4 -> {
                System.out.println("Would you like to add, remove or switch a good-block? (add/remove/switch)");
                String command = scanner.nextLine();
                switch (command) {
                    case "add" -> {
                        setCoordinates(scanner, inputCommand);
                        System.out.println("Choose the block you want to add: (RED/BLUE/GREEN/YELLOW)");
                        setGoodBlock(scanner, inputCommand);
                        inputCommand.flagIsAddingGoodBlock();
                    }
                    case "remove" -> {
                        setCoordinates(scanner, inputCommand);
                        System.out.println("Choose the block you want to remove: (RED/BLUE/GREEN/YELLOW)");
                        setGoodBlock(scanner, inputCommand);
                        inputCommand.flagIsRemovingGoodBlock();
                    }
                    case "switch" -> {
                        System.out.println("Enter the row of the first storage compartment :");
                        int row = scanner.nextInt();
                        scanner.nextLine();
                        inputCommand.setRow(row);
                        System.out.println("Enter the column of the first storage compartment :");
                        int col = scanner.nextInt();
                        scanner.nextLine();
                        inputCommand.setCol(col);

                        System.out.println("Choose the block you want to switch: (RED/BLUE/GREEN/YELLOW)");
                        setGoodBlock(scanner, inputCommand);

                        System.out.println("Enter the row of the second storage compartment :");
                        int row_1 = scanner.nextInt();
                        scanner.nextLine();
                        inputCommand.setRow_1(row_1);
                        System.out.println("Enter the column of the second storage compartment :");
                        int col_1 = scanner.nextInt();
                        scanner.nextLine();
                        inputCommand.setCol_1(col_1);

                        System.out.println("Choose the block you want to switch: (RED/BLUE/GREEN/YELLOW)");
                        setGoodBlock_1(scanner, inputCommand);
                    }
                    default -> {
                        System.out.println("Invalid command");
                        return null;
                    }
                }
            }
            
        }
        return inputCommand;
    }

    private void setCoordinates(Scanner scanner, InputCommand inputCommand) {
        System.out.println("Enter the row of the storage compartment: ");
        int row = scanner.nextInt();
        scanner.nextLine();
        inputCommand.setRow(row);
        System.out.println("Enter the column of the storage compartment: ");
        int col = scanner.nextInt();
        scanner.nextLine();
        inputCommand.setCol(col);
    }

    private void setGoodBlock(Scanner scanner, InputCommand inputCommand) {
        String block = scanner.nextLine();
        switch (block) {
            case "RED" -> inputCommand.setGoodBlock(GoodBlock.REDBLOCK);
            case "BLUE" -> inputCommand.setGoodBlock(GoodBlock.BLUEBLOCK);
            case "GREEN" -> inputCommand.setGoodBlock(GoodBlock.GREENBLOCK);
            case "YELLOW" -> inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);
        }
    }

    private void setGoodBlock_1(Scanner scanner, InputCommand inputCommand) {
        String block = scanner.nextLine();
        switch (block) {
            case "RED" -> inputCommand.setGoodBlock_1(GoodBlock.REDBLOCK);
            case "BLUE" -> inputCommand.setGoodBlock_1(GoodBlock.BLUEBLOCK);
            case "GREEN" -> inputCommand.setGoodBlock_1(GoodBlock.GREENBLOCK);
            case "YELLOW" -> inputCommand.setGoodBlock_1(GoodBlock.YELLOWBLOCK);
        }
    }

    //
    // TUTTE LE FUNZIONI SONO DA SINCRONIZZARE
    //
    @Override
    public void showUpdateBank(Bank bank) {
        gameCliView.setBank(bank);
        System.out.println("Bank updated:");
        System.out.println(bank);
        System.out.flush();
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) {
        gameCliView.getShipboard(player).setTileInHand(tile);
        System.out.println("Tile in hand updated:");
        System.out.println(gameCliView.getShipboard(player).getTileInHand());
        System.out.flush();
    }

    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList) {
        gameCliView.setCoveredComponentTiles(ctList);
        System.out.println("Uncovered component tiles list updated:");
        System.out.println(gameCliView.getUncoveredComponentTiles());
        System.out.flush();
    }

    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) {

    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) {
        gameCliView.getShipboards().put(player, shipboard);
        System.out.println("Shipboard updated:");
        System.out.println(gameCliView.getShipboard(player));
        System.out.flush();
    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) {
        gameCliView.setFlightboard(flightboard);
        System.out.println("Flightboard updated:");
        System.out.println(gameCliView.getFlightboard());
        System.out.flush();
    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard)  {
        gameCliView.setCurrCard(adventureCard);
        System.out.println("Current card updated:");
        System.out.println(gameCliView.getCurrCard());
        System.out.flush();
    }

    @Override
    public void showUpdateDices(Dices dices) {
        gameCliView.setDices(dices);
        System.out.println("Dices updated:");
        System.out.println(gameCliView.getDices());
        System.out.flush();
    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer)  {
        gameCliView.setCurrPlayer(currPlayer);
        System.out.println("Current player updated:");
        System.out.println(gameCliView.getCurrPlayer());
        System.out.flush();
    }

    @Override
    public void showUpdateGamePhase(GamePhase gamePhase)  {
        gameCliView.setGamePhase(gamePhase);
        System.out.println("Game phase updated:");
        System.out.println(gameCliView.getGamePhase());
        System.out.flush();
    }

    @Override
    public void showUpdateDeck(List<AdventureCard> deck)  {
        gameCliView.setDeck(deck);
        System.out.println("Deck updated:");
        System.out.println(gameCliView.getDeck());
        System.out.flush();
    }

    @Override
    public void showUpdateGame(Game game)  {
        gameCliView = new GameCliView(game);
        System.out.println("Game updated:");
        System.out.println(gameCliView);
        System.out.flush();
    }

    @Override
    public void showUpdateHourglassSpot(int hourglassSpot){

    }

    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) {

    }

    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) {

    }

    @Override
    public void showNicknameResult(boolean valid, String message) {

    }

    @Override
    public void showGameStarted() {

    }

    @Override
    public void showPlayerJoined(String player) {

    }
}
