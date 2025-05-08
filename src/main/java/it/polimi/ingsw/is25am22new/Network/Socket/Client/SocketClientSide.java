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
import java.rmi.RemoteException;
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
        this.view = new LobbyView(clientModel);
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
                    case "StopHourglass" -> {
                        this.showUpdateStopHourglass();
                    }
                    case "StartHourglass" -> {
                        int hourglassSpot = (int) msg.getObject();
                        this.showUpdateStartHourglass(hourglassSpot);
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
    public void showUpdateStopHourglass() throws RemoteException {
        clientModel.stopHourglass();
    }

    @Override
    public void showUpdateStartHourglass(int hourglassSpot) throws RemoteException {
        clientModel.startHourglass(hourglassSpot);
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
    public void placeBrownAlien(String playerName, int i, int j) throws IOException {
        output.placeBrownAlien(playerName, i, j);
    }

    @Override
    public void placeAstronauts(String playerName, int i, int j) throws IOException {
        output.placeAstronauts(playerName, i, j);
    }

    @Override
    public void placePurpleAlien(String playerName, int i, int j) throws IOException {
        output.placePurpleAlien(playerName, i, j);
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
