package it.polimi.ingsw.is25am22new.Network.RMI;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RmiClient extends UnicastRemoteObject implements VirtualViewRMI {

    private final VirtualServerRMI server;
    private final ClientView clientView;
    private static final String SERVER_NAME = "GalaxyTruckerServer";

    protected RmiClient(String host, int port, ClientView clientView) throws RemoteException, NotBoundException {
        super();
        this.clientView = clientView;

        Registry registry = LocateRegistry.getRegistry(host, port);
        // Let RMI handle the proxy creation directly
        this.server = (VirtualServerRMI) registry.lookup(SERVER_NAME);
        this.server.connect( this);
        System.out.println("Connected to server: " + host + ":" + port);
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
            ClientView view = new SimpleClientView();

            // Create the RMI client
            RmiClient client = new RmiClient(host, port, view);

            // Interaction logic to join game, etc.
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine();

            client.addPlayer(playerName);

            // Main command loop
            boolean running = true;
            while(running) {
                System.out.println("\nCommands: ready, unready, gametype tut, gametype lvl2, start, exit");
                String command = scanner.nextLine().trim();

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
            }

            // Clean disconnect
            System.out.println("Disconnecting...");
            client.removePlayer(playerName);

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
        clientView.displayShipboard(shipboard);
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
class SimpleClientView implements ClientView {
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
    public void displayShipboard(Shipboard shipboard) {
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
}