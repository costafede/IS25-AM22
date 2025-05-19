package it.polimi.ingsw.is25am22new.Network.RMI.Client;

import it.polimi.ingsw.is25am22new.Client.LobbyView;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
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

/**
 * The RmiClient class serves as a Remote Method Invocation (RMI) client for connecting
 * and interacting with a remote VirtualServer. It provides a variety of methods to manage
 * the client's interaction with the game, including connecting to the server, managing
 * players, and executing game-related actions asynchronously. The class also handles
 * communication between the EnhancedClientView and the server.
 */
public class RmiClient extends UnicastRemoteObject implements VirtualView, VirtualServer {

    ClientModel clientModel;
    private VirtualServer server;
    private final EnhancedClientView clientView;
    private static final String SERVER_NAME = "GalaxyTruckerServer";
    private boolean isHost = false;
    private String playerName;

    private ScheduledExecutorService heartbeatScheduler;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean isConnectionValid = false;

    public RmiClient(EnhancedClientView clientView, ClientModel clientModel) throws RemoteException {
        super();
        this.clientView = clientView;
        this.clientModel = clientModel;
    }

    /**
     * Establishes a connection to a remote server using Java RMI. This method locates
     * the registry running on the provided host and port, and retrieves a reference
     * to the remote server object.
     *
     * @param host the hostname or IP address of the RMI registry
     * @param port the port number on which the RMI registry is running
     * @throws RemoteException if an error occurs during communication with the registry
     * @throws NotBoundException if the specified name is not bound in the registry
     */
    public void connectToServer(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        VirtualServer server = (VirtualServer) registry.lookup(SERVER_NAME);
        System.out.println("Found server: " + host + ":" + port);
        this.server = server;
    }

    /**
     * Establishes a connection to the server using a Graphical User Interface (GUI).
     * This method handles connecting to the RMI server, registering the client, and
     * initiating the heartbeat mechanism.
     *
     * @param host the server's hostname or IP address
     * @param port the port number on which the server is listening
     * @param name the player's chosen nickname
     * @param clientModel the client-side representation of the game model
     * @param view the GUI object used for display and user interaction
     * @return an RmiClient instance that facilitates communication with the server,
     *         or null if the connection fails
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public static RmiClient connectToServerRMI_GUI(String host, int port, String name, ClientModel clientModel, GalaxyTruckerGUI view) throws InterruptedException {
        try {
            RmiClient client = new RmiClient(view, clientModel);
            client.connectToServer(host, port);
            client.setPlayerName(name);
            client.connectWithNickname(name);
            client.startHeartbeat(name, client);
            return client;
        } catch (Exception e) {
            System.out.println("Error connecting to RMI server: " + e.getMessage());
            // Usa Platform.runLater per aggiornare l'interfaccia utente dal thread JavaFX
            return null;
        }
    }

    /**
     * Executes the main client logic for connecting to the server and initializing the game.
     * This method handles user input for the player's nickname, validates it with the server,
     * and starts the client's command loop upon successful connection.
     *
     * @param playerName the initial nickname of the player; can be null or empty to prompt for input
     * @param scanner a {@code Scanner} object used to read input from the console
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
                    try {
                        connectWithNickname(playerName);
                        // Wait a moment for any asynchronous responses
                        Thread.sleep(500);
                        nickAccepted = clientView.isNicknameValid();
                        if (!nickAccepted) {
                            playerName = null; // Reset to prompt again
                        }
                    } catch (RemoteException e) {
                        // Handle specific error messages for better user experience
                        if (e.getMessage() != null && e.getMessage().contains("Host is configuring")) {
                            System.out.println("\n╔═══════════════════════════════���══════════════════════════════════════╗");
                            System.out.println("║                Host is configuring the lobby...                       ║");
                            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                            // Don't wait, just shut down
                            shutdown();
                            return;
                        } else if (e.getMessage() != null && e.getMessage().contains("Nickname already taken")) {
                            System.err.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                            System.err.println("║               Nickname already taken. Please try again.              ║");
                            System.err.println("╚══════════════════════════════════════════════════════════════════════╝");
                            playerName = null; // Reset to prompt again
                        } else {
                            // Handle the "count is negative" error (which happens when host is configuring)
                            // as well as any other connection errors
                            System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                            System.out.println("║          Cannot connect to server. Host may be configuring.          ║");
                            System.out.println("║                      Please try again later.                         ║");
                            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                            // Don't show the detailed error message to the user, just shut down gracefully
                            shutdown();
                            return;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║          Cannot connect to server. Please try again later.           ║");
                    System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                    shutdown();
                    return;
                }
            }
            this.playerName = playerName;
            clientModel.setPlayerName(playerName);
            ((LobbyView) clientView).startCommandLoopRMI(this, playerName, scanner);
        } catch (Exception e) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                         Connection error.                            ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
            shutdown();
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
    public void disconnect() {
        shutdown();
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

    public void godMode(String playerName, String conf) {
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

    public void pickStandbyComponentTile(String playerName, int index) {
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

    public void finishedAllShipboards() {
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

    public void destroyComponentTile(String playerName, int i, int j) {
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
    public void placeBrownAlien(String playerName, int i, int j) {
        executor.submit(() -> {
            try {
                server.placeBrownAlien(playerName, i, j);
            } catch (IOException e) {
                System.out.println("Error in placeBrownAlienAsync: " + e.getMessage());
            }
        });
    }

    @Override
    public void placeAstronauts(String playerName, int i, int j) {
        executor.submit(() -> {
            try {
                server.placeAstronauts(playerName, i, j);
            } catch (IOException e) {
                System.out.println("Error in placeAstronautsAsync: " + e.getMessage());
            }
        });
    }

    @Override
    public void placePurpleAlien(String playerName, int i, int j) {
        executor.submit(() -> {
            try {
                server.placePurpleAlien(playerName, i, j);
            } catch (IOException e) {
                System.out.println("Error in placePurpleAlienAsync: " + e.getMessage());
            }
        });
    }

    @Override
    public void heartbeat(String playerName) {

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

    /**
     * Terminates the client's operations by performing the following actions:
     * 1. Stops the heartbeat scheduler if it is running to cease ongoing periodic tasks.
     * 2. Attempts to shut down the thread executor gracefully, allowing tasks to finish within a specified timeout.
     * 3. If tasks do not complete within the timeout period, forces an immediate shutdown of the executor.
     * 4. Handles any interruptions during the shutdown process by forcing an immediate shutdown.
     * 5. Exits the application with a status code of zero, indicating a normal termination.
     */
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
    public void terminate() throws RemoteException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                Host is configuring the lobby...please retry           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");

        // Mostra un messaggio di errore
        if (clientView != null) {
            clientView.displayNicknameResult(false, "Host is configuring the lobby...please retry");

            // Torna alla schermata di login se si sta usando la GUI
            if (clientView instanceof it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI) {
                ((it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI) clientView).switchToScene("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml");
            } else {
                // Termina l'applicazione se si sta usando la TUI
                System.exit(0);
            }
        }
    }

    @Override
    public void showUpdateBank(Bank bank)  {
        clientModel.setBank(bank);
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile)  {
        clientModel.getShipboard(player).setTileInHand(tile);
        clientModel.setShipboard(player, clientModel.getShipboard(player));
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

    /**
     * Starts a periodic heartbeat task for the specified player to maintain communication with the virtual server.
     * The task sends a heartbeat signal to the server at regular intervals of 3 seconds. If the server fails to
     * acknowledge the heartbeat, the scheduler is shut down and the client is terminated.
     *
     * @param playerName the name of the player whose heartbeat is being monitored
     * @param server the virtual server to which the heartbeat signal is sent
     */
    private void startHeartbeat(String playerName, VirtualServer server) {
        //System.out.println("Starting heartbeat for: " + playerName);
        heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
        heartbeatScheduler.scheduleAtFixedRate(() -> {
            try {
                //System.out.println("Sending heartbeat...");
                server.heartbeat(playerName);
                //System.out.println("Heartbeat sent successfully");
            } catch (IOException e) {
                System.err.println("Failed to send heartbeat - server is down");
                heartbeatScheduler.shutdown();
                shutdown();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    public String getVirtualServerType() {
        return "rmi";
    }
}

