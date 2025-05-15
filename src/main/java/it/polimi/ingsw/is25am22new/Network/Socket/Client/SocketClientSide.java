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
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SocketClientSide implements VirtualView {

    ClientModel clientModel;
    final ObjectInputStream objectInput;
    final SocketServerHandler output;
    String thisPlayerName;
    LobbyView view;
    boolean isHost;
    private ScheduledExecutorService heartbeatScheduler;
    Socket socket;
    private Map<String, Consumer<SocketMessage>> commandMap;

    List<String> players = new ArrayList<>();
    String gameType = "ERROR";
    String message = "ERROR";

    protected SocketClientSide(Socket socket, ObjectInputStream objectInput, SocketServerHandler output, String thisPlayerName, ClientModel clientModel, LobbyView view) throws IOException {
        this.socket = socket;
        this.output = output;
        this.objectInput = objectInput;
        this.thisPlayerName = thisPlayerName;
        this.view = view;
        this.clientModel = clientModel;
        commandMap = new HashMap<>();
        initializeCommandMap();
    }
    private void initializeCommandMap() {
        commandMap.put("Bank", this::handleBank);
        commandMap.put("TileInHand", this::handleTileInHand);
        commandMap.put("UncoveredComponentTile", this::handleUncoveredComponentTile);
        commandMap.put("CoveredComponentTile", this::handleCoveredComponentTile);
        commandMap.put("Shipboard", this::handleShipboard);
        commandMap.put("Flightboard", this::handleFlightboard);
        commandMap.put("CurrCard", this::handleCurrCard);
        commandMap.put("Dices", this::handleDices);
        commandMap.put("CurrPlayer", this::handleCurrPlayer);
        commandMap.put("GamePhase", this::handleGamePhase);
        commandMap.put("Deck", this::handleDeck);
        commandMap.put("Game", this::handleGame);
        commandMap.put("LobbyUpdate", this::handleLobbyUpdate);
        commandMap.put("ReadyStatus", this::handleReadyStatus);
        commandMap.put("ConnectionResult", this::handleConnectionResult);
        commandMap.put("Success", this::handleSuccess);
        commandMap.put("NicknameResult", this::handleNicknameResult);
        commandMap.put("GameStarted", msg -> showGameStarted());
        commandMap.put("PlayerJoined", this::handlePlayerJoined);
        commandMap.put("MessageToEveryone", this::handleMessageToEveryone);
        commandMap.put("StopHourglass", msg -> showUpdateStopHourglass());
        commandMap.put("StartHourglass", this::handleStartHourglass);
        commandMap.put("updateTest", this::handleUpdateTest);
    }
    public static SocketServerHandler connectToServer(String[] args, ClientModel clientModel, Scanner scanner) throws InterruptedException, ClassNotFoundException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
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
                        default -> {
                            System.out.println("Host is configuring the lobby...please retry");
                        }
                    }
                }

                if(!joined) System.out.println("Try again!");
            }
            LobbyView view;
            if(Integer.parseInt(args[2]) == 1) {
                 view = new LobbyView(clientModel);
            }  else {
                // GUI would be implemented here
                System.out.println("GUI not yet implemented. Defaulting to TUI.");
                view = new LobbyView(clientModel);
            }
            SocketClientSide newSocket = new SocketClientSide(socket, objectInput, output, thisPlayerName, clientModel, view);
            newSocket.run(scanner);
            return newSocket.getServerHandler();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
        return null;
    }

    private void run(Scanner scanner) throws IOException, InterruptedException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(150);
        startHeartbeat(thisPlayerName, output);
        clientModel.setPlayerName(thisPlayerName);
        view.startCommandLoopSocket(this.output, thisPlayerName, scanner);
    }

    public void runVirtualServer() {
        SocketMessage msg;

        try {
            while ((msg = (SocketMessage) objectInput.readObject()) != null) {
                commandMap.getOrDefault(msg.getCommand(), this::handleInvalidMessage).accept(msg);
            }
        } catch (Exception e) {
            System.out.println("Connection closed");
            System.out.flush();
        } finally {
            shutdown();
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
    public void showUpdateStopHourglass(){
        clientModel.stopHourglass();
    }

    @Override
    public void showUpdateStartHourglass(int hourglassSpot) {
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
    public void terminate() throws RemoteException {
        /// TODO
    }

    @Override
    public void showGameStarted() {
        this.view.displayGameStarted();
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

    private void startHeartbeat(String playerName, VirtualServer output) {
        //System.out.println("Starting heartbeat for: " + playerName);
        heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
        heartbeatScheduler.scheduleAtFixedRate(() -> {
            try {
                //System.out.println("Sending heartbeat...");
                output.heartbeat(playerName);
                //System.out.println("Heartbeat sent successfully");
            } catch (IOException e) {
                System.err.println("Failed to send heartbeat: " + e.getMessage());
                heartbeatScheduler.shutdown();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    private void shutdown() {
        try {
            if(objectInput != null) {
                objectInput.close();
            }
            if(output.objectOutput != null) {
                output.objectOutput.close();
            }
            if(socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        System.exit(0);
    }

    private void handleBank(SocketMessage msg) {
        Bank bank = (Bank) msg.getObject();
        showUpdateBank(bank);
    }

    private void handleTileInHand(SocketMessage msg) {
        ComponentTile tile = (ComponentTile) msg.getObject();
        String player = msg.getPayload();
        showUpdateTileInHand(player, tile);
    }

    private void handleUncoveredComponentTile(SocketMessage msg) {
        List<ComponentTile> ctList = (ArrayList<ComponentTile>) msg.getObject();
        showUpdateUncoveredComponentTiles(ctList);
    }

    private void handleCoveredComponentTile(SocketMessage msg) {
        List<ComponentTile> ctList = (ArrayList<ComponentTile>) msg.getObject();
        showUpdateCoveredComponentTiles(ctList);
    }

    private void handleShipboard(SocketMessage msg) {
        String player = msg.getPayload();
        Shipboard shipboard = (Shipboard) msg.getObject();
        showUpdateShipboard(player, shipboard);
    }

    private void handleFlightboard(SocketMessage msg) {
        Flightboard flightboard = (Flightboard) msg.getObject();
        showUpdateFlightboard(flightboard);
    }

    private void handleCurrCard(SocketMessage msg) {
        AdventureCard card = (AdventureCard) msg.getObject();
        showUpdateCurrCard(card);
    }

    private void handleDices(SocketMessage msg) {
        Dices dices = (Dices) msg.getObject();
        showUpdateDices(dices);
    }

    private void handleCurrPlayer(SocketMessage msg) {
        String currPlayer = msg.getPayload();
        showUpdateCurrPlayer(currPlayer);
    }

    private void handleGamePhase(SocketMessage msg) {
        GamePhase gamePhase = (GamePhase) msg.getObject();
        showUpdateGamePhase(gamePhase);
    }

    private void handleDeck(SocketMessage msg) {
        List<AdventureCard> deck = (ArrayList<AdventureCard>) msg.getObject();
        showUpdateDeck(deck);
    }

    private void handleGame(SocketMessage msg) {
        Game game = (Game) msg.getObject();
        showUpdateGame(game);
    }

    private void handleLobbyUpdate(SocketMessage msg) {
        players = (ArrayList<String>) msg.getObject();
        gameType = msg.getPayload();
    }

    private void handleReadyStatus(SocketMessage msg) {
        Map<String, Boolean> readyStatus = (Map<String, Boolean>) msg.getObject();
        showLobbyUpdate(players, readyStatus, gameType);
    }

    private void handleConnectionResult(SocketMessage msg) {
        isHost = (boolean) msg.getObject();
        message = msg.getPayload();
    }

    private void handleSuccess(SocketMessage msg) {
        boolean success = (boolean) msg.getObject();
        showConnectionResult(isHost, success, message);
    }

    private void handleNicknameResult(SocketMessage msg) {
        boolean valid = (boolean) msg.getObject();
        message = msg.getPayload();
        showNicknameResult(valid, message);
    }

    private void handlePlayerJoined(SocketMessage msg) {
        String player = msg.getPayload();
        showPlayerJoined(player);
    }

    private void handleMessageToEveryone(SocketMessage msg) {
        showMessage(msg.getPayload());
    }

    private void handleStartHourglass(SocketMessage msg) {
        int hourglassSpot = (int) msg.getObject();
        showUpdateStartHourglass(hourglassSpot);
    }

    private void handleUpdateTest(SocketMessage msg) {
        System.out.println(msg.getPayload());
        System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
        System.out.flush();
    }

    public String getThisPlayerName() {
        return thisPlayerName;
    }

    private void handleInvalidMessage(SocketMessage msg) {
        System.err.println("[INVALID MESSAGE]: " + msg.getCommand());
    }
}
