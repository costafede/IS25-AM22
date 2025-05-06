package it.polimi.ingsw.is25am22new.Network.Socket.Client;

import it.polimi.ingsw.is25am22new.Client.LobbyView;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class SocketClientSide implements VirtualView {

    ClientModel clientModel;
    final ObjectInputStream objectInput;
    final SocketServerHandler output;
    String thisPlayerName;
    LobbyView view;
    boolean isHost;

    protected SocketClientSide(ObjectInputStream objectInput, SocketServerHandler output, String thisPlayerName, ClientModel clientModel) throws IOException {
        this.output = output;
        this.objectInput = objectInput;
        this.thisPlayerName = thisPlayerName;
        this.view = new LobbyView();
        this.clientModel = clientModel;
    }

    public static SocketServerHandler connectToServer(String[] args, ClientModel clientModel) throws InterruptedException, ClassNotFoundException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket(host, port);
            SocketServerHandler output = new SocketServerHandler(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            boolean joined = false;
            String thisPlayerName = "Player";

            while(!joined) {
                System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                     ENTER YOUR COOL TRUCKER NAME                     ║");
                System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                System.out.print("➤ ");
                System.out.flush();
                thisPlayerName = scanner.nextLine().trim();

                while(thisPlayerName == null || thisPlayerName.isEmpty()) {
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                      PLEASE ENTER A VALID NAME                       ║");
                    System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                    System.out.print("➤ ");
                    thisPlayerName = scanner.nextLine();
                }

                output.checkAvailability(thisPlayerName);
                SocketMessage msg = null;
                if((msg = (SocketMessage) objectInput.readObject()) != null) {
                    switch (msg.getCommand()) {
                        case "LobbyFullOrOutsideLobbyState" -> {
                            System.out.println("Lobby is full or you are outside the lobby state");
                        }
                        case "PlayerAlreadyInLobby" -> {
                            System.out.println("Player already in lobby");
                        }
                        case "PlayerAdded" -> {
                            System.out.println("You've successfully joined the lobby!");
                            joined = true;
                        }
                    }
                }

                if(!joined) System.out.println("Try again!");
            }

            SocketClientSide newSocket = new SocketClientSide(objectInput, output, thisPlayerName, clientModel);
            newSocket.run();
            return newSocket.getServerHandler();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
        scanner.close();
        return null;
    }

    private void run() throws IOException, InterruptedException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.output.disconnect(thisPlayerName)));

        Thread.sleep(500);
        clientModel.setPlayerName(thisPlayerName);
        this.view.startCommandLoopSocket(this, thisPlayerName, new Scanner(System.in));
    }

    // comunicazione dal server al client
    private void runVirtualServer() {
        SocketMessage msg;
        List<String> players = new ArrayList<>();
        String gameType = "ERROR";
        String message = "ERROR";
        try {
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
                        List<ComponentTile> ctList = (ArrayList<ComponentTile>) msg.getObject();
                        this.showUpdateUncoveredComponentTiles(ctList);
                    }
                    case "CoveredComponentTile" -> {
                        List<ComponentTile> ctList = (ArrayList<ComponentTile>) msg.getObject();
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
                        List<AdventureCard> deck = (ArrayList<AdventureCard>) msg.getObject();
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
                        players = (ArrayList<String>) msg.getObject();
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
                    case "MessageToEveryone" -> {
                        this.showMessage(msg.getPayload());
                    }
                    case "updateTest" -> {
                        System.out.println(msg.getPayload());
                        System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
                        System.out.flush();
                    }
                    default -> System.err.println("[INVALID MESSAGE]");
                }
            }
        } catch (Exception e) {
            System.out.println("Connection closed: " + e.getMessage());
            System.out.flush();
            this.output.disconnect(thisPlayerName);
            System.exit(0);
        }
    }

//    private void runCli() throws IOException, InterruptedException {
//        Scanner scan = new Scanner(System.in);
//        int numOfRotations = 0;
//        Set<String> validCommands = Set.of("remove", "ready", "start", "notready", "setgametype");
//
//        while (true) {
//            boolean validInput = false;
//            while (!validInput) {
//                // handle invalid input (e.g., show message or ask again)
//
//                String input = scan.nextLine().trim().toLowerCase();
//
//                if (validCommands.contains(input)) {
//                    validInput = true;
//                } else {
//                    System.out.println("❗ Invalid command: \"" + input + "\"");
//                }
//            }
//
//            String command = scan.nextLine().trim();
//
//            // process the command
//            switch (command) {
//                case "remove" -> {
//                    System.out.println("Enter the name of the player to remove: ");
//                    String player = scan.nextLine();
//
//                    while(player == null || player.isEmpty()) {
//                        System.out.println("Please enter a valid name: ");
//                        player = scan.nextLine();
//                    }
//
//                    output.removePlayer(player);
//                }
//                case "ready" -> {
//                    System.out.println("You're ready!");
//                    output.setPlayerReady(thisPlayerName);
//                }
//                case "start" -> {
//                    System.out.println("Let's start the game!");
//                    output.startGameByHost(thisPlayerName);
//                }
//                case "notready" -> {
//                    System.out.println("You're not ready!");
//                    output.setPlayerNotReady(thisPlayerName);
//                }
//                case "setgametype" -> {
//                    System.out.println("What type of game do you want to play? (tutorial/level2)");
//                    String gameType = scan.nextLine();
//
//                    while(gameType == null || (!gameType.equals("tutorial") && !gameType.equals("level2"))) {
//                        System.out.println("Please enter a valid type: ");
//                        gameType = scan.nextLine();
//                    }
//
//                    output.setGameType(gameType);
//                }
                //case 6 -> {
                //    if(clientModel.getShipboard(thisPlayerName).getTileInHand() != null) {
                //        System.out.println("You already have a tile in your hand!");
                //    }
                //    else {
                //        System.out.println("Covered tile picked!");
                //        output.pickCoveredTile(thisPlayerName);
                //    }
                //}
                //case 7 -> {
                //    if(clientModel.getShipboard(thisPlayerName).getTileInHand() != null) {
                //        System.out.println("You already have a tile in your hand!");
                //    }
                //    else {
                //        System.out.println("Which tile you want to pick?: ");
                //        int index = scan.nextInt();
                //        scan.nextLine();
                //
                //        while(index < 0) {
                //            System.out.println("Please enter a valid index: ");
                //            index = scan.nextInt();
                //            scan.nextLine();
                //        }
                //        System.out.println("Uncovered tile picked!");
                //        output.pickUncoveredTile(thisPlayerName, index);
                //    }
                //}
                //case 8 -> {
                //    if(clientModel.getShipboard(thisPlayerName).getTileInHand() != null) {
                //        System.out.println("Tile rotated to the right!");
                //        numOfRotations++;
                //    }
                //    else {
                //        System.out.println("You don't have a tile in your hand!");
                //    }
                //}
                //case 9 -> {
                //    if(clientModel.getShipboard(thisPlayerName).getTileInHand() != null) {
                //        System.out.println("Tile rotated to the left!");
                //        numOfRotations--;
                //    }
                //    else {
                //        System.out.println("You don't have a tile in your hand!");
                //    }
                //}
                //case 10 -> {
                //    // based on the type of game HAVE TO SET LIMITS THE COORDINATES
                //    System.out.println("Where do you want to place the tile?: \n");
                //    System.out.println("X coordinate: ");
                //    int x = scan.nextInt();
                //    scan.nextLine();
                //    System.out.println("Y coordinate: ");
                //    int y = scan.nextInt();
                //    scan.nextLine();
                //    output.weldComponentTile(thisPlayerName, x, y, numOfRotations);
                //    numOfRotations = 0;
                //}
                //case 11 -> {
                //    try {
                //        clientModel.getShipboard(thisPlayerName).pickStandByComponentTile(0);
                //        try{
                //            clientModel.getShipboard(thisPlayerName).pickStandByComponentTile(1);
                //            System.out.println("Standby positions are full!");
                //        }
                //        catch(IllegalStateException e){
                //            System.out.println("Putting tile in standby!");
                //            output.standbyComponentTile(thisPlayerName);
                //        }
                //    }
                //    catch (IllegalStateException e){
                //        System.out.println("Putting tile in standby!");
                //        output.standbyComponentTile(thisPlayerName);
                //    }
                //}
                //case 12 -> {
                //    System.out.println("Which tile do you want to pick?: (1/2) ");
                //    int index = scan.nextInt();
                //    scan.nextLine();
                //
                //    while(index != 1 && index != 2) {
                //        System.out.println("Please enter a valid index: ");
                //        index = scan.nextInt();
                //        scan.nextLine();
                //    }
                //
                //    if(index == 1) {
                //        try {
                //            clientModel.getShipboard(thisPlayerName).pickStandByComponentTile(0);
                //            output.pickStandbyComponentTile(thisPlayerName, 0);
                //        } catch (IllegalStateException e) {
                //            System.out.println("You don't have a tile in that position!");
                //        }
                //    }
                //    else {
                //        try {
                //            clientModel.getShipboard(thisPlayerName).pickStandByComponentTile(1);
                //            output.pickStandbyComponentTile(thisPlayerName, 1);
                //        } catch (IllegalStateException e) {
                //            System.out.println("You don't have a tile in that position!");
                //        }
                //    }
                //
                //}
                //case 13 -> {
                //    if(clientModel.getShipboard(thisPlayerName).getTileInHand() != null) {
                //        System.out.println("Tile discarded!");
                //        output.discardComponentTile(thisPlayerName);
                //    }
                //    else {
                //        System.out.println("You don't have a tile in your hand!");
                //    }
                //}
                //case 14 -> {
                //    System.out.println("You've finally finished your ship! Wow!");
                //    output.finishBuilding(thisPlayerName);
                //}
                //case 15 -> {
                //    System.out.println("You've finished your ship! Congrats!");
                //    System.out.println("Fast! Where do you want to place your rocket? (1/2/3/4)");
                //    int pos = scan.nextInt();
                //    scan.nextLine();
                //    output.finishBuilding(thisPlayerName, pos);
                //}
                //case 16 -> {
                //    System.out.println("All shipboards are finished! Let's go!");
                //    output.finishedAllShipboards();
                //}
                //case 17 -> {
                //    System.out.println("Hourglass flipped!");
                //    output.flipHourglass();
                //}
                //case 18 -> {
                //    System.out.println("Card picked!");
                //    output.pickCard();
                //}
                //case 19 -> {
                //    if(clientModel.getCurrCard() == null) {
                //        System.out.println("There is no card to activate!");
                //    }
                //    else {
                //        System.out.println("Card activated!");
                //        InputCommand inputCommand = constructInputCommand();
                //        output.activateCard(inputCommand);
                //    }
                //}
                //case 20 -> {
                //    if(clientModel.getShipboard(thisPlayerName).isAbandoned()) {
                //        System.out.println("You have already abandoned the game!");
                //    }
                //    else {
                //        System.out.println("Are you sure you want to abandon the game? (true/false)");
                //        boolean choice = scan.nextBoolean();
                //        scan.nextLine();
                //        if(choice)  output.playerAbandons(thisPlayerName);
                //    }
                //}
                //case 21 -> {
                //    System.out.println("Which tile do you want to destroy?: ");
                //    System.out.println("X coordinate: ");
                //    int x = scan.nextInt();
                //    scan.nextLine();
                //    System.out.println("Y coordinate: ");
                //    int y = scan.nextInt();
                //    scan.nextLine();
                //
                //    if(clientModel.getShipboard(thisPlayerName).getComponentTileFromGrid(x, y).isEmpty()) {
                //        System.out.println("You don't have a tile in that position!");
                //    }
                //    else {
                //        System.out.println("Tile destroyed!");
                //        output.destroyComponentTile(thisPlayerName, x, y);
                //    }
                //}
                //case 22 -> {
                //    if(clientModel.getGamePhase().getPhaseType() == PhaseType.END) {
                //        System.out.println("Game is over!");
                //        output.endGame();
                //    }
                //    else {
                //        System.out.println("Game is not over yet!");
                //    }
                //}
    //            case "test" -> {
    //                System.out.println("String to send: ");
    //                String test = scan.nextLine();
    //                output.connectionTester(test, 33879);
    //            }
    //            default -> {
    //                this.output.disconnect(thisPlayerName);
    //                System.out.println("Invalid command, you will be kicked out of the game!");
    //                System.out.flush();
    //                System.exit(0);
    //            }
    //        }
    //    }
    //}
//
    //private InputCommand constructInputCommand(){
    //    Scanner scanner = new Scanner(System.in);
    //    InputCommand inputCommand = new InputCommand();
    //    System.out.println("Possible commands: \n");
    //    System.out.println("1. Choice command");
    //    System.out.println("2. Choose index command");
    //    System.out.println("3. Choose tile coordinates command");
    //    System.out.println("4. Manage good-blocks command");
    //    int index = scanner.nextInt();
    //    switch (index) {
    //        case 1 -> {
    //            System.out.println("Enter your choice: (true/false)");
    //            boolean choice = scanner.nextBoolean();
    //            scanner.nextLine();
    //            inputCommand.setChoice(choice);
    //        }
    //        case 2 -> {
    //            System.out.println("Enter the index you want to choose: ");
    //            int indexChosen = scanner.nextInt();
    //            scanner.nextLine();
    //            inputCommand.setIndexChosen(indexChosen);
    //        }
    //        case 3 -> {
    //            System.out.println("Enter the row you want to choose: ");
    //            int row = scanner.nextInt();
    //            scanner.nextLine();
    //            inputCommand.setRow(row);
    //            System.out.println("Enter the column you want to choose: ");
    //            int col = scanner.nextInt();
    //            scanner.nextLine();
    //            inputCommand.setCol(col);
    //        }
    //        case 4 -> {
    //            System.out.println("Would you like to add, remove or switch a good-block? (add/remove/switch)");
    //            String command = scanner.nextLine();
    //            switch (command) {
    //                case "add" -> {
    //                    setCoordinates(scanner, inputCommand);
    //                    System.out.println("Choose the block you want to add: (RED/BLUE/GREEN/YELLOW)");
    //                    setGoodBlock(scanner, inputCommand);
    //                    inputCommand.flagIsAddingGoodBlock();
    //                }
    //                case "remove" -> {
    //                    setCoordinates(scanner, inputCommand);
    //                    System.out.println("Choose the block you want to remove: (RED/BLUE/GREEN/YELLOW)");
    //                    setGoodBlock(scanner, inputCommand);
    //                    inputCommand.flagIsRemovingGoodBlock();
    //                }
    //                case "switch" -> {
    //                    System.out.println("Enter the row of the first storage compartment :");
    //                    int row = scanner.nextInt();
    //                    scanner.nextLine();
    //                    inputCommand.setRow(row);
    //                    System.out.println("Enter the column of the first storage compartment :");
    //                    int col = scanner.nextInt();
    //                    scanner.nextLine();
    //                    inputCommand.setCol(col);
//
    //                    System.out.println("Choose the block you want to switch: (RED/BLUE/GREEN/YELLOW)");
    //                    setGoodBlock(scanner, inputCommand);
//
    //                    System.out.println("Enter the row of the second storage compartment :");
    //                    int row_1 = scanner.nextInt();
    //                    scanner.nextLine();
    //                    inputCommand.setRow_1(row_1);
    //                    System.out.println("Enter the column of the second storage compartment :");
    //                    int col_1 = scanner.nextInt();
    //                    scanner.nextLine();
    //                    inputCommand.setCol_1(col_1);
//
    //                    System.out.println("Choose the block you want to switch: (RED/BLUE/GREEN/YELLOW)");
    //                    setGoodBlock_1(scanner, inputCommand);
    //                }
    //                default -> {
    //                    System.out.println("Invalid command");
    //                    return null;
    //                }
    //            }
    //        }
    //    }
    //    return inputCommand;
    //}
//
    //private void setCoordinates(Scanner scanner, InputCommand inputCommand) {
    //    System.out.println("Enter the row of the storage compartment: ");
    //    int row = scanner.nextInt();
    //    scanner.nextLine();
    //    inputCommand.setRow(row);
    //    System.out.println("Enter the column of the storage compartment: ");
    //    int col = scanner.nextInt();
    //    scanner.nextLine();
    //    inputCommand.setCol(col);
    //}
//
    //private void setGoodBlock(Scanner scanner, InputCommand inputCommand) {
    //    String block = scanner.nextLine();
    //    switch (block) {
    //        case "RED" -> inputCommand.setGoodBlock(GoodBlock.REDBLOCK);
    //        case "BLUE" -> inputCommand.setGoodBlock(GoodBlock.BLUEBLOCK);
    //        case "GREEN" -> inputCommand.setGoodBlock(GoodBlock.GREENBLOCK);
    //        case "YELLOW" -> inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);
    //    }
    //}
//
    //private void setGoodBlock_1(Scanner scanner, InputCommand inputCommand) {
    //    String block = scanner.nextLine();
    //    switch (block) {
    //        case "RED" -> inputCommand.setGoodBlock_1(GoodBlock.REDBLOCK);
    //        case "BLUE" -> inputCommand.setGoodBlock_1(GoodBlock.BLUEBLOCK);
    //        case "GREEN" -> inputCommand.setGoodBlock_1(GoodBlock.GREENBLOCK);
    //        case "YELLOW" -> inputCommand.setGoodBlock_1(GoodBlock.YELLOWBLOCK);
    //    }
    //}

    @Override
    public void showUpdateBank(Bank bank) {
        clientModel.setBank(bank);
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) {
        clientModel.getShipboard(player).setTileInHand(tile);
    }

    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList) {
        clientModel.setUncoveredComponentTiles(ctList);
    }

    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) {
        clientModel.setCoveredComponentTiles(ctList);
    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) {
        clientModel.setShipboard(player, shipboard);
    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) {
        clientModel.setFlightboard(flightboard);
    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard)  {
        clientModel.setCurrCard(adventureCard);
    }

    @Override
    public void showUpdateDices(Dices dices) {
        clientModel.setDices(dices);
    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer)  {
        clientModel.setCurrPlayer(currPlayer);
    }

    @Override
    public void showUpdateGamePhase(GamePhase gamePhase)  {
        clientModel.setGamePhase(gamePhase);
    }

    @Override
    public void showUpdateDeck(List<AdventureCard> deck)  {
        clientModel.setDeck(deck);
    }

    @Override
    public void showUpdateGame(Game game)  {
        clientModel.setGame(game);
    }

    @Override
    public void showUpdateHourglassSpot(int hourglassSpot){
        System.out.println("Hourglass spot: " + hourglassSpot);
    }

    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) {
        this.view.displayLobbyUpdate(players, readyStatus, gameType, isHost);
    }

    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) {
        this.view.displayConnectionResult(isHost, success, message);
    }

    @Override
    public void showNicknameResult(boolean valid, String message) {
        this.view.displayNicknameResult(valid, message);
    }

    @Override
    public void showPlayerJoined(String player) {
        this.view.displayPlayerJoined(player);
    }

    @Override
    public void showGameStarted() {
        this.view.displayGameStarted();
    }


    @Override
    public void setPlayerReady(String playerName) throws IOException {
        output.setPlayerReady(playerName);
    }

    @Override
    public void setPlayerNotReady(String playerName) throws IOException {
        output.setPlayerNotReady(playerName);
    }

    @Override
    public void startGameByHost(String playerName) throws IOException {
        output.startGameByHost(playerName);
    }

    @Override
    public void setGameType(String gameType) throws IOException {
        output.setGameType(gameType);
    }

    @Override
    public void pickCoveredTile(String playerName) throws IOException {
        output.pickCoveredTile(playerName);
    }

    @Override
    public void pickUncoveredTile(String playerName, String pngName) throws IOException {
        output.pickUncoveredTile(playerName, pngName);
    }

    @Override
    public void weldComponentTile(String playerName, int i, int j, int numOfRotation) throws IOException {
        output.weldComponentTile(playerName, i, j, numOfRotation);
    }

    @Override
    public void standbyComponentTile(String playerName) throws IOException {
        output.standbyComponentTile(playerName);
    }

    @Override
    public void pickStandbyComponentTile(String playerName, int index) throws IOException {
        output.pickStandbyComponentTile(playerName, index);
    }

    @Override
    public void discardComponentTile(String playerName) throws IOException {
        output.discardComponentTile(playerName);
    }

    @Override
    public void finishBuilding(String playerName) throws IOException {
        output.finishBuilding(playerName);
    }

    @Override
    public void finishBuilding(String playerName, int index) throws IOException {
        output.finishBuilding(playerName, index);
    }

    @Override
    public void finishedAllShipboards() throws IOException {
        output.finishedAllShipboards();
    }

    @Override
    public void flipHourglass() throws IOException {
        output.flipHourglass();
    }

    @Override
    public void pickCard() throws IOException {
        output.pickCard();
    }

    @Override
    public void activateCard(InputCommand inputCommand) throws IOException {
        output.activateCard(inputCommand);
    }

    @Override
    public void removePlayer(String playerName) throws IOException {
        output.removePlayer(playerName);
    }

    @Override
    public void playerAbandons(String playerName) throws IOException {
        output.playerAbandons(playerName);
    }

    @Override
    public void destroyComponentTile(String playerName, int i, int j) throws IOException {
        output.destroyComponentTile(playerName, i, j);
    }

    @Override
    public void endGame() throws IOException {
        output.endGame();
    }

    @Override
    public void godMode(String playerName, String conf) throws IOException {
        output.godMode(playerName, conf);
    }

    public void showMessage(String message) {
        System.out.println("Message from server: " + message);
        System.out.flush();
    }

    public void setNumPlayers(int numPlayers) {
        output.setNumPlayers(numPlayers);
    }

    public SocketServerHandler getServerHandler() {
        return output;
    }
}
