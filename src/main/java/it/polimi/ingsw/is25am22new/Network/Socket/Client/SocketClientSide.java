package it.polimi.ingsw.is25am22new.Network.Socket.Client;

import it.polimi.ingsw.is25am22new.Client.View.GameCliView;
import it.polimi.ingsw.is25am22new.Client.View.GameView;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.Socket.Server.VirtualViewSocket;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SocketClientSide implements VirtualViewSocket {

    GameCliView gameCliView;
    final ObjectInputStream objectInput;
    final SocketServerHandler output;
    String thisPlayerName;

    protected SocketClientSide(InputStream is, OutputStream os) throws IOException {
        this.output = new SocketServerHandler(os);
        this.objectInput = new ObjectInputStream(is);
        this.thisPlayerName = "Player";
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket serverSocket = new Socket(host, port);
        new SocketClientSide(serverSocket.getInputStream(), serverSocket.getOutputStream()).run();
    }

    private void run() throws IOException {
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
                    ComponentTile tile = (ComponentTile) msg.getObject();
                    this.showUpdateUncoveredComponentTiles(tile);
                }
                case "Shipboard" -> {
                    Shipboard shipboard = (Shipboard) msg.getObject();
                    this.showUpdateShipboard(shipboard);
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
                case "updateTest" -> {
                    System.out.println(msg.getPayload());
                    System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
                    System.out.flush();
                }
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void runCli() throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your cool trucker name: ");
        System.out.flush();
        thisPlayerName = scan.nextLine();
        int numOfRotations = 0;
        while (true) {
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
                    output.setGameType(gameType);
                }
                case 6 -> {
                    System.out.println("Picked a covered tile!");
                    output.pickCoveredTile(thisPlayerName);
                }
                case 7 -> {
                    System.out.println("Which tile you want to pick?: ");
                    int index = scan.nextInt();
                    scan.nextLine();
                    output.pickUncoveredTile(thisPlayerName, index);
                }
                case 8 -> {
                    System.out.println("Rotated tile to the right!");
                    numOfRotations++;
                }
                case 9 -> {
                    System.out.println("Rotated tile to the left!");
                    numOfRotations--;
                }
                case 10 -> {
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
                    System.out.println("Putting tile in standby!");
                    output.standbyComponentTile(thisPlayerName);
                }
                case 12 -> {
                    System.out.println("Which tile do you want to pick?: ");
                    int index = scan.nextInt();
                    scan.nextLine();
                    output.pickStandbyComponentTile(thisPlayerName, index);
                }
                case 13 -> {
                    System.out.println("Discarded tile!");
                    output.discardComponentTile(thisPlayerName);
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
                    System.out.println("Flipped hourglass!");
                    output.flipHourglass();
                }
                case 18 -> {
                    System.out.println("Picked a card!");
                    output.pickCard();
                }
                case 19 -> {
                    System.out.println("Activated card!");
                    InputCommand inputCommand = constructInputCommand();
                    output.activateCard(inputCommand);
                }
                case 20 -> {
                    System.out.println("You have abandoned the game!");
                    output.playerAbandons(thisPlayerName);
                }
                case 21 -> {
                    System.out.println("Which tile do you want to destroy?: ");
                    System.out.println("X coordinate: ");
                    int x = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Y coordinate: ");
                    int y = scan.nextInt();
                    scan.nextLine();
                    output.destroyComponentTile(thisPlayerName, x, y);
                }
                case 22 -> {
                    System.out.println("Game is over!");
                    output.endGame();
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
        return new InputCommand();
    }

    //
    // TUTTE LE FUNZIONI SONO DA SINCRONIZZARE
    //
    @Override
    public void showUpdateBank(Bank bank) throws RemoteException {
        gameCliView.setBank(bank);
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) throws RemoteException {

    }

    @Override
    public void showUpdateUncoveredComponentTiles(ComponentTile tile) throws RemoteException {

    }

    @Override
    public void showUpdateShipboard(Shipboard shipboard) throws RemoteException {

    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) throws RemoteException {

    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) throws RemoteException {

    }

    @Override
    public void showUpdateDices(Dices dices) throws RemoteException {

    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer) throws RemoteException {

    }
}
