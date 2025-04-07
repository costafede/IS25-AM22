package it.polimi.ingsw.is25am22new.Network.RMI.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.RMI.Server.VirtualServerRMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RmiClient extends UnicastRemoteObject implements VirtualViewRMI {

    private final VirtualServerRMI server;
    private final EnhancedClientView clientView;
    private static final String SERVER_NAME = "GalaxyTruckerServer";
    private boolean isHost = false;

    protected RmiClient(String host, int port, EnhancedClientView clientView) throws RemoteException, NotBoundException {
        super();
        this.clientView = clientView;

        Registry registry = LocateRegistry.getRegistry(host, port);
        this.server = (VirtualServerRMI) registry.lookup(SERVER_NAME);

        //this.server.connect( this);
        //System.out.println("Connected to server: " + host + ":" + port);

        System.out.println("Found server: " + host + ":" + port);
    }

    public void connectWithNickname(String nickname) throws RemoteException {
        server.connect(this, nickname);
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        if(args.length >= 1) {
            host = args[0];
        }
        if(args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number. Using default port 1234.");
            }
        }

        try {
            // Create a console-based ClientView implementation
            ConsoleClientView view = new ConsoleClientView();

            // Create the RMI client
            RmiClient client = new RmiClient(host, port, view);

            // Interaction logic to join game, etc.
            Scanner scanner = new Scanner(System.in);

            boolean nickAccepted = false;
            String playerName = "";

            while(!nickAccepted) {
                System.out.print("Enter your name: ");
                playerName = scanner.nextLine();

                try{
                    view.resetNicknameStatus();
                    client.connectWithNickname(playerName);

                    Thread.sleep(1000);

                    nickAccepted = view.isNicknameValid();
                }catch (RemoteException | InterruptedException e){
                    System.err.println("Error connecting with nickname" + e.getMessage());
                }
            }

            view.startCommandLoop(client, playerName, scanner);

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }
        System.exit(0);
    }



    public void addPlayer(String playerName) throws RemoteException {
        server.addPlayer(playerName);
    }

    public void setPlayerReady(String playerName) throws RemoteException {
        server.setPlayerReady(playerName);
    }

    public void setPlayerNotReady(String playerName) throws RemoteException {
        server.setPlayerNotReady(playerName);
    }

    public void startGameByHost(String playerName) throws RemoteException {
        server.startGameByHost(playerName);
    }

    public void setGameType(String gameType) throws RemoteException {
        server.setGameType(gameType);
    }

    public void pickCoveredTile(String playerName) throws RemoteException {
        server.pickCoveredTile(playerName);
    }

    public void pickUncoveredTile(String playerName, int index) throws RemoteException {
        server.pickUncoveredTile(playerName, index);
    }

    public void rotateClockwise(String playerName, int rotationNum) throws RemoteException {
        server.rotateClockwise(playerName, rotationNum);
    }

    public void rotateCounterClockwise(String playerName, int rotationNum) throws RemoteException {
        server.rotateCounterClockwise(playerName, rotationNum);
    }

    public void weldComponentTile(String playerName, int i, int j) throws RemoteException {
        server.weldComponentTile(playerName, i, j);
    }

    public void standbyComponentTile(String playerName) throws RemoteException {
        server.standbyComponentTile(playerName);
    }

    public void pickStandbyComponentTile(String playerName, int index) throws RemoteException {
        server.pickStandbyComponentTile(playerName, index);
    }

    public void discardComponentTile(String playerName) throws RemoteException {
        server.discardComponentTile(playerName);
    }

    public void finishBuilding(String playerName) throws RemoteException {
        server.finishBuilding(playerName);
    }

    public void finishBuilding(String playerName, int index) throws RemoteException {
        server.finishBuilding(playerName, index);
    }

    public void finishedAllShipboards() throws RemoteException {
        server.finishedAllShipboards();
    }

    public void flipHourglass() throws RemoteException {
        server.flipHourglass();
    }

    public void pickCard() throws RemoteException {
        server.pickCard();
    }

    public void activateCard(InputCommand inputCommand) throws RemoteException {
        server.activateCard(inputCommand);
    }

    public void removePlayer(String playerName) throws RemoteException {
        server.removePlayer(playerName);
    }

    public void playerAbandons(String playerName) throws RemoteException {
        server.playerAbandons(playerName);
    }

    public void destroyComponentTile(String playerName, int i, int j) throws RemoteException {
        server.destroyComponentTile(playerName, i, j);
    }

    public void setCurrPlayer(String currPlayer) throws RemoteException {
        server.setCurrPlayer(currPlayer);
    }

    public void setCurrPlayerToLeader() throws RemoteException {
        server.setCurrPlayerToLeader();
    }

    public void endGame() throws RemoteException {
        server.endGame();
    }

    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) throws RemoteException {
        clientView.displayLobbyUpdate(players, readyStatus, gameType, isHost);
    }

    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) throws RemoteException {
        this.isHost = isHost;
        clientView.displayConnectionResult(isHost, success, message);
    }

    @Override
    public void showNicknameResult(boolean valid, String message) throws RemoteException {
        clientView.displayNicknameResult(valid, message);
    }

    @Override
    public void showGameStarted() throws RemoteException {
        clientView.displayGameStarted();
    }

    @Override
    public void showPlayerJoined(String playerName) throws RemoteException {
        clientView.displayPlayerJoined(playerName);
    }

    @Override
    public void showUpdateBank(Bank bank) throws RemoteException {
        clientView.displayBank(bank);
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) throws RemoteException {
        clientView.displayTileInHand(player, tile);
    }

    @Override
    public void showUpdateUncoveredComponentTiles(ComponentTile tile) throws RemoteException {
        clientView.displayUncoveredComponentTiles(tile);
    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) throws RemoteException {
        clientView.displayShipboard(player, shipboard);
    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) throws RemoteException {
        clientView.displayFlightboard(flightboard);
    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) throws RemoteException {
        clientView.displayCurrentCard(adventureCard);
    }

    @Override
    public void showUpdateDices(Dices dices) throws RemoteException {
        clientView.displayDices(dices);
    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer) throws RemoteException {
        clientView.displayCurrentPlayer(currPlayer);
    }
}

// Simple implementation of ClientView for console output
class ConsoleClientView implements EnhancedClientView {
    private boolean inGame = false;
    private boolean nicknameValid = false;

    public boolean isNicknameValid() {
        return nicknameValid;
    }

    public void resetNicknameStatus(){
        nicknameValid = false;
    }

    @Override
    public void displayBank(Bank bank) {
        System.out.println("Bank update received");
    }

    @Override
    public void displayTileInHand(String player, ComponentTile tile) {
        System.out.println("Tile in hand update received");
    }

    @Override
    public void displayUncoveredComponentTiles(ComponentTile tile) {
        System.out.println("Uncovered component tile update received");
    }

    @Override
    public void displayShipboard(String player, Shipboard shipboard) {
        System.out.println("Shipboard update received");
    }

    @Override
    public void displayFlightboard(Flightboard flightboard) {
        System.out.println("Flightboard update received");
    }

    @Override
    public void displayCurrentCard(AdventureCard card) {
        System.out.println("Current card update received");
    }

    @Override
    public void displayDices(Dices dices) {
        System.out.println("Dices update received");
    }

    @Override
    public void displayCurrentPlayer(String currPlayer) {
        System.out.println("Current player: " + currPlayer);
    }

    @Override
    public void displayLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType, boolean isHost) {
        System.out.println("\n=== LOBBY UPDATE ===");
        System.out.println("Players:");
        for (String player : players) {
            String status = readyStatus.getOrDefault(player, false) ? "READY" : "NOT READY";
            System.out.println("  " + player + " - " + status);
        }
        System.out.println("Game Type: " + gameType);
        if (isHost) {
            System.out.println("You are the HOST of this lobby");
        }
        System.out.println("===================\n");
    }

    @Override
    public void displayConnectionResult(boolean isHost, boolean success, String message) {
        System.out.println("\n=== CONNECTION RESULT ===");
        if (success) {
            System.out.println("Successfully connected to the game!");
            System.out.println(message);
            if (isHost) {
                System.out.println("As the host, you can start the game when at least 2 players are ready.");
            }
            nicknameValid = true;
        } else {
            System.out.println("Connection failed: " + message);
        }
        System.out.println("=========================\n");
    }

    @Override
    public void displayNicknameResult(boolean valid, String message){
        System.out.println("\n=== NICKNAME RESULT ===");
        if(valid){
            System.out.println("Nickname accepted!");
            nicknameValid = true;
        }else{
            System.out.println("Nickname error: " + message);
            nicknameValid = false;
        }
        System.out.println("======================\n");
    }

    @Override
    public void displayGameStarted() {
        System.out.println("\n🚀 GAME STARTED! 🚀");
        System.out.println("Welcome to Galaxy Trucker!");
        inGame = true;

        System.out.println("\nGame Commands: [Enter number for commands]");
        System.out.println("6: Pick covered tile | 7: Pick uncovered tile | 20: Abandon game | 22: End game");
    }

    @Override
    public void displayPlayerJoined(String playerName) {
        System.out.println("\n>>> " + playerName + " has joined the lobby! <<<\n");
    }

    @Override
    public void startCommandLoop(RmiClient client, String playerName, Scanner scanner) {
        boolean running = true;

        while(running && !inGame) {
            System.out.println("\nCommands: ready, unready, gametype tut, gametype lvl2, start, exit");
            String command = scanner.nextLine().trim();

            try {
                switch(command) {
                    case "ready":
                        client.setPlayerReady(playerName);
                        break;
                    case "unready":
                        client.setPlayerNotReady(playerName);
                        break;
                    case "gametype tut":
                        client.setGameType("tutorial");
                        break;
                    case "gametype lvl2":
                        client.setGameType("level2");
                        break;
                    case "start":
                        client.startGameByHost(playerName);
                        break;
                    case "exit":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            } catch (RemoteException e) {
                System.err.println("Error executing command: " + e.getMessage());
            }
        }

        // Game commands once in game
        while(running && inGame) {
//            System.out.println("\nGame Commands: [Enter number for commands]");
//            System.out.println("6: Pick covered tile | 7: Pick uncovered tile | 20: Abandon game | 22: End game");

            String command = scanner.nextLine().trim();
            try {
                int cmd = Integer.parseInt(command);
                switch(cmd) {
                    case 6:
                        client.pickCoveredTile(playerName);
                        break;
                    case 7:
                        System.out.println("Which tile do you want to pick?: ");
                        int index = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        client.pickUncoveredTile(playerName, index);
                        break;
                    case 20:
                        client.playerAbandons(playerName);
                        running = false;
                        break;
                    case 22:
                        client.endGame();
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown command: " + cmd);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number for commands");
            } catch (RemoteException e) {
                System.err.println("Error executing command: " + e.getMessage());
            }
        }

        // Clean disconnect
        try {
            System.out.println("Disconnecting...");
            client.removePlayer(playerName);
        } catch (RemoteException e) {
            System.err.println("Error during disconnect: " + e.getMessage());
        }
    }
}