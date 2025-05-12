package it.polimi.ingsw.is25am22new.Network.RMI.Client;

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
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RmiClient extends UnicastRemoteObject implements VirtualView, VirtualServer {

    ClientModel clientModel;
    private VirtualServer server;
    private final EnhancedClientView clientView;
    private static final String SERVER_NAME = "GalaxyTruckerServer";
    private boolean isHost = false;
    private String playerName;

    private ScheduledExecutorService heartbeatScheduler;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public RmiClient(EnhancedClientView clientView, ClientModel clientModel) throws RemoteException {
        super();
        this.clientView = clientView;
        this.clientModel = clientModel;
    }

    /*
     * Creates a VirtualServer connection through RMI
     */
    public void connectToServer(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        VirtualServer server = (VirtualServer) registry.lookup(SERVER_NAME);
        System.out.println("Found server: " + host + ":" + port);
        this.server = server;
    }

    /*
     * Run the RMI client
     */
    public void run(String playerName, Scanner scanner) {
        try {
            boolean nickAccepted = false;

            while(!nickAccepted) {
                if (playerName == null || playerName.isEmpty()) {
                    System.out.print("Enter your name: ");
                    playerName = scanner.nextLine();
                }

                try {
                    clientView.resetNicknameStatus();
                    connectWithNickname(playerName);
                    //Thread.sleep(1000);
                    nickAccepted = clientView.isNicknameValid();
                    if (!nickAccepted) {
                        playerName = null; // Reset to prompt again
                    }
                } catch (Exception e) {
                    System.err.println("Error connecting with nickname: " + e.getMessage());
                }
            }
            this.playerName = playerName;
            clientModel.setPlayerName(playerName);
            clientView.startCommandLoopRMI(this, playerName, scanner);
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }

    }

    public String getPlayerName() {
        return playerName;
    }

    public void connectWithNickname(String nickname) throws RemoteException {
        server.connect(this, nickname);
        startHeartbeat(nickname, server);
    }


    @Override
    public void connect(VirtualView client, String nickname) throws RemoteException {
        this.connectWithNickname(nickname);
    }

    @Override
    public void disconnect(String nickname) {
        // TO DO
    }

    public void setNumPlayers(int numPlayers) throws IOException {
        server.setNumPlayers(numPlayers);
    }

    public void setPlayerReady(String playerName) throws IOException {
        server.setPlayerReady(playerName);
    }

    public void setPlayerNotReady(String playerName) throws IOException {
        server.setPlayerNotReady(playerName);
    }

    public void startGameByHost(String playerName) throws IOException {
        server.startGameByHost(playerName);
    }

    public void setGameType(String gameType) throws IOException {
        server.setGameType(gameType);
    }

    public void godMode(String playerName, String conf) throws IOException {
        executor.submit(() -> {
            try {
                server.godMode(playerName, conf);
            } catch (IOException e) {
                System.out.println("Error in godMode: " + e.getMessage());
            }
        });
    }

    public void pickCoveredTile(String nickname) {
        executor.submit(() -> {
            try {
                server.pickCoveredTile(nickname);
            } catch (IOException e) {
                System.out.println("Error in pickCoveredTileAsync: " + e.getMessage());
            }
        });
    }

    public void pickUncoveredTile(String playerName, String pngName) throws IOException {
        executor.submit(() -> {
            try {
                server.pickUncoveredTile(playerName, pngName);
            } catch (IOException e) {
                System.out.println("Error in pickUncoveredTileAsync: " + e.getMessage());
            }
        });
    }

    public void weldComponentTile(String playerName, int i, int j, int numOfRotation) throws IOException {
        executor.submit(() -> {
            try {
                server.weldComponentTile(playerName, i, j, numOfRotation);
            } catch (IOException e) {
                System.out.println("Error in weldComponentTileAsync: " + e.getMessage());
            }
        });
    }

    public void standbyComponentTile(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.standbyComponentTile(playerName);
            } catch (IOException e) {
                System.out.println("Error in standbyComponentTileAsync: " + e.getMessage());
            }
        });
    }

    public void pickStandbyComponentTile(String playerName, int index) throws IOException {
        executor.submit(() -> {
            try {
                server.pickStandbyComponentTile(playerName, index);
            } catch (IOException e) {
                System.out.println("Error in pickStandbyComponentTileAsync: " + e.getMessage());
            }
        });
    }

    public void discardComponentTile(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.discardComponentTile(playerName);
            } catch (IOException e) {
                System.out.println("Error in discardComponentTileAsync: " + e.getMessage());
            }
        });
    }

    public void finishBuilding(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.finishBuilding(playerName);
            } catch (IOException e) {
                System.out.println("Error in finishBuildingAsync: " + e.getMessage());
            }
        });
    }

    public void finishBuilding(String playerName, int index) throws IOException {
        executor.submit(() -> {
            try {
                server.finishBuilding(playerName, index);
            } catch (IOException e) {
                System.out.println("Error in finishBuildingAsync: " + e.getMessage());
            }
        });
    }

    public void finishedAllShipboards() throws IOException {
        executor.submit(() -> {
            try {
                server.finishedAllShipboards();
            } catch (IOException e) {
                System.out.println("Error in finishedAllShipboardsAsync: " + e.getMessage());
            }
        });
    }

    public void flipHourglass() throws IOException {
        executor.submit(() -> {
            try {
                server.flipHourglass();
            } catch (IOException e) {
                System.out.println("Error in flipHourglassAsync: " + e.getMessage());
            }
        });
    }

    public void pickCard() throws IOException {
        executor.submit(() -> {
            try {
                server.pickCard();
            } catch (IOException e) {
                System.out.println("Error in pickCardAsync: " + e.getMessage());
            }
        });
    }

    public void activateCard(InputCommand inputCommand) throws IOException {
        executor.submit(() -> {
            try {
                server.activateCard(inputCommand);
            } catch (IOException e) {
                System.out.println("Error in activateCardAsync: " + e.getMessage());
            }
        });
    }

    public void removePlayer(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.removePlayer(playerName);
            } catch (IOException e) {
                System.out.println("Error in removePlayerAsync: " + e.getMessage());
            }
        });
    }

    public void playerAbandons(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.playerAbandons(playerName);
            } catch (IOException e) {
                System.out.println("Error in playerAbandonsAsync: " + e.getMessage());
            }
        });
    }

    public void destroyComponentTile(String playerName, int i, int j) throws IOException {
        executor.submit(() -> {
            try {
                server.destroyComponentTile(playerName, i, j);
            } catch (IOException e) {
                System.out.println("Error in destroyComponentTileAsync: " + e.getMessage());
            }
        });
    }

    public void endGame() throws IOException {
        executor.submit(() -> {
            try {
                server.endGame();
            } catch (IOException e) {
                System.out.println("Error in endGameAsync: " + e.getMessage());
            }
        });
    }

    @Override
    public void placeBrownAlien(String playerName, int i, int j) throws IOException {
        executor.submit(() -> {
            try {
                server.placeBrownAlien(playerName, i, j);
            } catch (IOException e) {
                System.out.println("Error in placeBrownAlienAsync: " + e.getMessage());
            }
        });
    }

    @Override
    public void placeAstronauts(String playerName, int i, int j) throws IOException {
        executor.submit(() -> {
            try {
                server.placeAstronauts(playerName, i, j);
            } catch (IOException e) {
                System.out.println("Error in placeAstronautsAsync: " + e.getMessage());
            }
        });
    }

    @Override
    public void placePurpleAlien(String playerName, int i, int j) throws IOException {
        executor.submit(() -> {
            try {
                server.placePurpleAlien(playerName, i, j);
            } catch (IOException e) {
                System.out.println("Error in placePurpleAlienAsync: " + e.getMessage());
            }
        });
    }

    @Override
    public void heartbeat(String playerName) throws IOException {

    }

    @Override
    public void quit(String playerName) {
        try {
            server.quit(playerName);
            shutdown();
        } catch (IOException e) {
            System.out.println("Error in quit: " + e.getMessage());
        }
    }

    public void shutdown() {
        if (heartbeatScheduler != null) {
            heartbeatScheduler.shutdown();
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        System.exit(0);
    }

    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) {
        clientView.displayLobbyUpdate(players, readyStatus, gameType, isHost);
    }

    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) {
        this.isHost = isHost;
        clientView.displayConnectionResult(isHost, success, message);
    }

    @Override
    public void showNicknameResult(boolean valid, String message) {
        clientView.displayNicknameResult(valid, message);
    }

    @Override
    public void showGameStarted() {
        clientView.displayGameStarted();
    }

    @Override
    public void showPlayerJoined(String playerName) {
        clientView.displayPlayerJoined(playerName);
    }

    @Override
    public void showUpdateBank(Bank bank)  {
        clientModel.setBank(bank);
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile)  {
        clientModel.getShipboard(player).setTileInHand(tile);
    }

    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList)  {
        clientModel.setUncoveredComponentTiles(ctList);
    }

    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) {
        clientModel.setCoveredComponentTiles(ctList);
    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard)  {
        clientModel.setShipboard(player, shipboard);
    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard)  {
        clientModel.setFlightboard(flightboard);
        //// TODO
    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard)  {
        clientModel.setCurrCard(adventureCard);
    }

    @Override
    public void showUpdateDices(Dices dices)  {
        clientModel.setDices(dices);
    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer)  {
        clientModel.setCurrPlayer(currPlayer);
    }

    @Override
    public void showUpdateGamePhase(GamePhase gamePhase) {
        clientModel.setGamePhase(gamePhase);
    }

    @Override
    public void showUpdateDeck(List<AdventureCard> deck) {
        clientModel.setDeck(deck);
    }

    @Override
    public void showUpdateGame(Game game) {
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

    public void setPlayerName(String testPlayer) {
        this.playerName = testPlayer;
    }

    private void startHeartbeat(String playerName, VirtualServer server) {
        //System.out.println("Starting heartbeat for: " + playerName);
        heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
        heartbeatScheduler.scheduleAtFixedRate(() -> {
            try {
                //System.out.println("Sending heartbeat...");
                server.heartbeat(playerName);
                //System.out.println("Heartbeat sent successfully");
            } catch (IOException e) {
                System.err.println("Failed to send heartbeat: " + e.getMessage());
                heartbeatScheduler.shutdown();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }
}