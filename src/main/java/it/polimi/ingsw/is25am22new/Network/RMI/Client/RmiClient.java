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

    /**
     * Constructs an RmiClient instance to manage the interaction between the client view
     * and the client model in an RMI-based game application.
     *
     * @param clientView the enhanced client view responsible for handling the user interface
     *                   and displaying updates to the player
     * @param clientModel the client-side representation of the game model used to store
     *                    and handle game-related data and logic
     * @throws RemoteException if there is an error during the creation of the remote object
     */
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
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                     ENTER YOUR COOL TRUCKER NAME                     ║");
                    System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                    System.out.print("➤ ");
                    System.out.flush();
                    playerName = scanner.nextLine();
                }

                try {
                    clientView.resetNicknameStatus();
                    try {
                        connectWithNickname(playerName);
                        // Wait a moment for any asynchronous responses
                        //Thread.sleep(500);
                        nickAccepted = clientView.isNicknameValid();
                        if (!nickAccepted) {
                            playerName = null; // Reset to prompt again
                        }
                    } catch (RemoteException e) {
                        // Handle specific error messages for better user experience
                        if (e.getMessage() != null && e.getMessage().contains("Host is configuring")) {
                            System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                            System.out.println("║                Host is configuring the lobby...                      ║");
                            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                            // Reset playerName to prompt again, don't shut down
                            playerName = null;
                        } else if (e.getMessage() != null && e.getMessage().contains("Nickname already taken")) {
                            System.err.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                            System.err.println("║               Nickname already taken. Please try again.              ║");
                            System.err.println("╚══════════════════════════════════════════════════════════════════════╝");
                            playerName = null; // Reset to prompt again
                        } else {
                            // Cattura anche il caso in cui l'host sta configurando ma l'errore non contiene il messaggio specifico
                            // Come nel caso dell'errore "count is negative" che può verificarsi quando l'host sta configurando
                            System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                            System.out.println("║                Host is configuring the lobby...please retry          ║");
                            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                            // Reset playerName to prompt again, don't shut down
                            playerName = null;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║               Host is configuring the lobby...please retry           ║");
                    System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                    // Reset playerName to prompt again, don't shut down
                    playerName = null;
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

    /**
     * Retrieves the name of the player associated with this client.
     *
     * @return the name of the player as a String
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Connects the client to the server using the specified nickname and starts
     * a heartbeat mechanism to maintain an active connection.
     *
     * @param nickname the player's chosen nickname to be used during the connection
     * @throws RemoteException if an error occurs while communicating with the server
     */
    public void connectWithNickname(String nickname) throws RemoteException {
        server.connect(this, nickname);
        startHeartbeat(nickname, server);
    }


    /**
     * Establishes a connection between a virtual client view and the server, using the provided nickname.
     *
     * @param client the virtual view representing the client connecting to the server
     * @param nickname the unique identifier or name chosen by the client for the connection
     * @throws RemoteException if a remote communication error occurs during the connection
     */
    @Override
    public void connect(VirtualView client, String nickname) throws RemoteException {
        this.connectWithNickname(nickname);
    }

    /**
     * Disconnects the RMI client from the server and shuts down client-side resources.
     * This operation terminates ongoing tasks, releases resources, and stops the application.
     * It ensures to halt any running executors or schedulers in the client environment.
     */
    @Override
    public void disconnect() {
        shutdown();
    }

    /**
     * Sets the number of players for the game on the server side.
     *
     * @param numPlayers the number of players to set for the game
     * @throws IOException if an I/O error occurs while communicating with the server
     */
    public void setNumPlayers(int numPlayers) throws IOException {
        server.setNumPlayers(numPlayers);
    }

    /**
     * Marks the specified player as ready in the game session on the server.
     *
     * @param playerName the name of the player to be marked as ready
     * @throws IOException if there is an error during communication with the server
     */
    public void setPlayerReady(String playerName) throws IOException {
        server.setPlayerReady(playerName);
    }

    /**
     * Marks the specified player as not ready in the game session by delegating the
     * action to the server. This is generally used when a player indicates that they
     * are not prepared for the next phase of the game or wishes to update their ready
     * status.
     *
     * @param playerName the name of the player to mark as not ready
     * @throws IOException if an I/O error occurs during the update process
     */
    public void setPlayerNotReady(String playerName) throws IOException {
        server.setPlayerNotReady(playerName);
    }

    /**
     * Starts the game session initiated by the host player. This method delegates
     * the operation to the server to trigger the game start process.
     *
     * @param playerName the nickname of the host player starting the game
     * @throws IOException if a network or I/O error occurs while communicating with the server
     */
    public void startGameByHost(String playerName) throws IOException {
        server.startGameByHost(playerName);
    }

    /**
     * Sets the game type for the server. This method communicates with the server
     * to configure the type of game being played.
     *
     * @param gameType the type of game to be set; must be a valid game type recognized by the server
     * @throws IOException if an I/O error occurs during communication with the server
     */
    public void setGameType(String gameType) throws IOException {
        server.setGameType(gameType);
    }

    public void loadGame() throws IOException {
        server.loadGame();
    }

    /**
     * Activates the "God Mode" for a player on the server, allowing custom configurations
     * to be applied based on the provided parameters. This method executes the action
     * asynchronously and handles any potential I/O errors encountered during execution.
     *
     * @param playerName the name of the player for whom "God Mode" is being enabled
     * @param conf the configuration string specifying the details or parameters
     *             of the "God Mode" to be applied
     */
    public void godMode(String playerName, String conf) {
        executor.submit(() -> {
            try {
                server.godMode(playerName, conf);
            } catch (IOException e) {
                System.out.println("Error in godMode: " + e.getMessage());
            }
        });
    }

    /**
     * Requests the server to allow the player with the specified nickname
     * to pick a covered tile asynchronously. The request is submitted for execution
     * and handled in a separate thread to avoid blocking the caller.
     *
     * @param nickname the nickname of the player attempting to pick a covered tile
     */
    public void pickCoveredTile(String nickname) {
        executor.submit(() -> {
            try {
                server.pickCoveredTile(nickname);
            } catch (IOException e) {
                System.out.println("Error in pickCoveredTileAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Submits a task to asynchronously request the server to allow the specified player
     * to pick an uncovered tile identified by its PNG file name. This method ensures
     * that the operation is performed without blocking the main thread.
     *
     * @param playerName the nickname of the player making the request to pick an uncovered tile
     * @param pngName the name of the PNG file representing the uncovered tile to be picked
     * @throws IOException if an I/O error occurs during the communication with the server
     */
    public void pickUncoveredTile(String playerName, String pngName) throws IOException {
        executor.submit(() -> {
            try {
                server.pickUncoveredTile(playerName, pngName);
            } catch (IOException e) {
                System.out.println("Error in pickUncoveredTileAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Asynchronously welds a component tile for the specified player by invoking the server's
     * weldComponentTile method. This involves placing the component tile on the given coordinates
     * with a specified number of rotations.
     *
     * @param playerName the name of the player attempting to weld the component tile
     * @param i the x-coordinate on the grid where the component tile should be placed
     * @param j the y-coordinate on the grid where the component tile should be placed
     * @param numOfRotation the number of 90-degree clockwise rotations to apply to the tile
     * @throws IOException if an input or output error occurs during the operation
     */
    public void weldComponentTile(String playerName, int i, int j, int numOfRotation) throws IOException {
        executor.submit(() -> {
            try {
                server.weldComponentTile(playerName, i, j, numOfRotation);
            } catch (IOException e) {
                System.out.println("Error in weldComponentTileAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Requests the server to place the currently held component tile into standby mode for the specified player.
     * The operation is executed asynchronously using a separate thread.
     *
     * @param playerName the name of the player requesting to place the component tile into standby
     * @throws IOException if an I/O error occurs during the operation
     */
    public void standbyComponentTile(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.standbyComponentTile(playerName);
            } catch (IOException e) {
                System.out.println("Error in standbyComponentTileAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Sends a request to pick a standby component tile for the specified player at the given index.
     * The operation is executed asynchronously to prevent blocking the main application thread.
     *
     * @param playerName the nickname of the player making the request
     * @param index the position of the tile to be picked from the standby tiles
     */
    public void pickStandbyComponentTile(String playerName, int index) {
        executor.submit(() -> {
            try {
                server.pickStandbyComponentTile(playerName, index);
            } catch (IOException e) {
                System.out.println("Error in pickStandbyComponentTileAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Asynchronously requests the server to discard a component tile associated with the specified player.
     * This method delegates the request to the server in a separate thread to prevent blocking the main execution flow.
     *
     * @param playerName the name of the player whose component tile is to be discarded
     * @throws IOException if an I/O error occurs during the communication with the server
     */
    public void discardComponentTile(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.discardComponentTile(playerName);
            } catch (IOException e) {
                System.out.println("Error in discardComponentTileAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Notifies the server that the specified player has finished building their ship
     * asynchronously. This method submits the task to an executor to avoid blocking
     * the calling thread.
     *
     * @param playerName the name of the player who has finished building
     * @throws IOException if an I/O error occurs when communicating with the server
     */
    public void finishBuilding(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.finishBuilding(playerName);
            } catch (IOException e) {
                System.out.println("Error in finishBuildingAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Submits a request to finish the building process for a player asynchronously.
     * This method delegates the task to the server to mark a player's building
     * process as completed based on the given player name and index of the
     * building stage.
     *
     * @param playerName the name of the player who is completing the building process
     * @param index the index indicating the specific building stage to complete
     * @throws IOException if an error occurs during communication with the server
     */
    public void finishBuilding(String playerName, int index) throws IOException {
        executor.submit(() -> {
            try {
                server.finishBuilding(playerName, index);
            } catch (IOException e) {
                System.out.println("Error in finishBuildingAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Signals the server that all shipboards have been finalized.
     * This method is executed asynchronously using a separate thread in the
     * executor service to avoid blocking the main thread. If an IOException
     * occurs during the communication with the server, an error message is
     * printed to the console.
     *
     * The server's responsibility is to handle the logic associated with the
     * finalization of all shipboards. This action might signify the advancement
     * to the next phase in the game or the conclusion of a specific segment
     * within the game logic.
     *
     * Note: The server-side implementation of this action is defined in the
     * `server.finishedAllShipboards()` method, which is executed in the try block.
     */
    public void finishedAllShipboards() {
        executor.submit(() -> {
            try {
                server.finishedAllShipboards();
            } catch (IOException e) {
                System.out.println("Error in finishedAllShipboardsAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Initiates an asynchronous operation to flip the hourglass in the game.
     * This method delegates the flipping operation to the server by submitting a task to an executor.
     * If an IOException occurs during the server operation, the error message will be logged to the console.
     *
     * @throws IOException if an I/O error occurs during the operation initiated by the server's implementation
     */
    public void flipHourglass() throws IOException {
        executor.submit(() -> {
            try {
                server.flipHourglass();
            } catch (IOException e) {
                System.out.println("Error in flipHourglassAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Initiates the process of picking a card by delegating the call asynchronously to the server.
     * This method submits a task to an executor service, ensuring that the server's pickCard
     * operation is performed on a separate thread to avoid blocking the main thread. If an
     * IOException occurs during the server's pickCard operation, it is caught and its message
     * is logged to the standard output.
     *
     * @throws IOException if an I/O error occurs during the pickCard operation on the server
     */
    public void pickCard() throws IOException {
        executor.submit(() -> {
            try {
                server.pickCard();
            } catch (IOException e) {
                System.out.println("Error in pickCardAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Activates a card using the provided input command. This method submits the
     * activation task to an executor, which handles it asynchronously. In case of
     * an error during card activation, the exception is logged.
     *
     * @param inputCommand the command containing the details required to activate the card
     * @throws IOException if an I/O error occurs during the card activation process
     */
    public void activateCard(InputCommand inputCommand) throws IOException {
        executor.submit(() -> {
            try {
                server.activateCard(inputCommand);
            } catch (IOException e) {
                System.out.println("Error in activateCardAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Removes a player from the game by delegating the operation to the server.
     * This method executes asynchronously using a background task to ensure
     * non-blocking operation.
     *
     * @param playerName the name of the player to be removed from the game
     * @throws IOException if an I/O error occurs during the removal process
     */
    public void removePlayer(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.removePlayer(playerName);
            } catch (IOException e) {
                System.out.println("Error in removePlayerAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Notifies the server that the specified player has abandoned the game.
     * The method submits an asynchronous task to handle the server notification,
     * ensuring that the operation does not block the calling thread.
     *
     * @param playerName the name of the player who is abandoning the game
     * @throws IOException if an I/O error occurs while notifying the server
     */
    public void playerAbandons(String playerName) throws IOException {
        executor.submit(() -> {
            try {
                server.playerAbandons(playerName);
            } catch (IOException e) {
                System.out.println("Error in playerAbandonsAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Asynchronously requests the server to destroy a component tile located
     * at the specified coordinates for the given player.
     *
     * @param playerName the name of the player who owns the component tile
     * @param i the horizontal coordinate of the component tile
     * @param j the vertical coordinate of the component tile
     */
    public void destroyComponentTile(String playerName, int i, int j) {
        executor.submit(() -> {
            try {
                server.destroyComponentTile(playerName, i, j);
            } catch (IOException e) {
                System.out.println("Error in destroyComponentTileAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Ends the current game session by asynchronously invoking the server's endGame method.
     * The method submits a task to the internal executor to handle the operation in a separate thread.
     *
     * If an IOException occurs during the operation, the error is caught and logged to the console.
     *
     * @throws IOException if an I/O error occurs during the game termination process
     */
    public void endGame() throws IOException {
        executor.submit(() -> {
            try {
                server.endGame();
            } catch (IOException e) {
                System.out.println("Error in endGameAsync: " + e.getMessage());
            }
        });
    }

    /**
     * Asynchronously places a brown alien on the game board for the specified player
     * at the given coordinates. This method uses a separate thread to process the
     * request to avoid blocking the main flow of execution.
     *
     * @param playerName the name of the player performing the action
     * @param i the row index where the brown alien is to be placed
     * @param j the column index where the brown alien is to be placed
     */
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

    /**
     * Places astronauts on the specified coordinates for the given player asynchronously.
     *
     * @param playerName the name of the player placing astronauts
     * @param i the row index on the game board where the astronauts should be placed
     * @param j the column index on the game board where the astronauts should be placed
     */
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

    /**
     * Places a purple alien on the game grid at the specified position for a given player.
     * This method executes the operation asynchronously, delegating the request to the server.
     *
     * @param playerName the name of the player requesting to place the purple alien
     * @param i the row index where the purple alien will be placed
     * @param j the column index where the purple alien will be placed
     */
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

    /**
     * Sends a heartbeat signal to the server to indicate that the client is still active
     * and connected. This method helps in maintaining the connection and detecting
     * client-side disconnections.
     *
     * @param playerName the name of the player sending the heartbeat signal
     */
    @Override
    public void heartbeat(String playerName) {

    }

    /**
     * Terminates the player's session and shuts down the client application.
     * This method invokes the server-side quit operation for the specified player
     * and subsequently shuts down the client resources, including thread pools
     * and heartbeat mechanisms.
     *
     * @param playerName the name of the player who is quitting the session
     */
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
     * Shuts down the client application and its associated resources.
     *
     * This method performs the following operations:
     * 1. Shuts down the heartbeat scheduler if it is not null.
     * 2. Initiates the shutdown of the executor service and attempts
     *    to await its termination within a specified time limit.
     * 3. If the executor does not terminate within the time limit,
     *    it forcefully shuts it down.
     * 4. Exits the application by terminating the JVM.
     *
     * Note: Invoking this method will terminate the entire application.
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

    /**
     * Displays an update of the lobby to the client view, including the list of players,
     * their readiness status, and the selected game type. This method is invoked whenever
     * there is a change in the lobby state.
     *
     * @param players a list of player nicknames currently in the lobby
     * @param readyStatus a map containing each player's readiness status, where the key is the player's nickname
     *                    and the value is a boolean indicating whether the player is ready (true) or not (false)
     * @param gameType a string representing the type of game selected for the session
     */
    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) {
        clientView.displayLobbyUpdate(players, readyStatus, gameType, isHost);
    }

    /**
     * Displays the result of a connection attempt to the client view, indicating whether
     * the connection was successful or not and providing an associated message.
     *
     * @param isHost  {@code true} if the client is acting as the host; {@code false} otherwise
     * @param success {@code true} if the connection was successful; {@code false} if the connection failed
     * @param message the message providing details about the connection result
     */
    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) {
        this.isHost = isHost;
        clientView.displayConnectionResult(isHost, success, message);
    }

    /**
     * Displays the result of a nickname validation attempt to the client view.
     *
     * @param valid   a boolean indicating whether the provided nickname is valid
     * @param message a string containing additional information or feedback about the result
     */
    @Override
    public void showNicknameResult(boolean valid, String message) {
        clientView.displayNicknameResult(valid, message);
    }

    /**
     * Notifies the client view that the game has started.
     * Invokes the {@code displayGameStarted} method on the associated {@code ClientView}
     * to update the user interface and inform the player about the beginning of the game.
     */
    @Override
    public void showGameStarted() {
        clientView.displayGameStarted();
    }

    /**
     * Displays a message indicating that a player has joined the game.
     *
     * @param playerName the name of the player who joined the game
     */
    @Override
    public void showPlayerJoined(String playerName) {
        clientView.displayPlayerJoined(playerName);
    }

    /**
     * Updates and displays the leaderboard for the client view.
     * This method synchronizes the provided leaderboard with the client model's internal data
     * and is intended to eventually trigger a display update in the client view.
     *
     * @param leaderboard a map containing player names as keys and their corresponding scores as values
     * @throws RemoteException if there is an error in remote communication
     */
    @Override
    public void showUpdateLeaderboard(Map<String, Integer> leaderboard) throws RemoteException {
        clientModel.setLeaderboard(leaderboard);
        /// TODO Da implementare?? clientView.displayLeaderboard(leaderboard);
    }

    /**
     * Terminates the current client's session and performs necessary cleanup steps.
     * This method is typically invoked when the host is reconfiguring the game lobby,
     * providing feedback to the user about the current state of the server and allowing
     * reattempts to connect or perform other operations without forcibly ending the application.
     * If the client is using a graphical interface, the view may be switched to the server
     * connection screen, enabling the user to retry.
     *
     * @throws RemoteException if a communication-related error occurs during the execution of the method
     */
    @Override
    public void terminate() throws RemoteException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                Host is configuring the lobby...please retry          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");

        // Mostra un messaggio di errore
        if (clientView != null) {
            //clientView.displayNicknameResult(false, "Host is configuring the lobby...please retry");

            // Torna alla schermata di login se si sta usando la GUI
            if (clientView instanceof it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI) {
                ((it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI) clientView).switchToScene("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml");
            }
            // Non terminiamo l'applicazione anche per la TUI, permettendo all'utente di riprovare
        }
    }

    @Override
    public void showUpdateGameLoaded(Game game) throws RemoteException {
        clientModel.setGameLoaded(game);
    }

    /**
     * Updates the client model with the given bank information.
     *
     * @param bank the {@code Bank} object representing the current state of the bank
     *             to be updated in the client model
     */
    @Override
    public void showUpdateBank(Bank bank)  {
        clientModel.setBank(bank);
    }

    /**
     * Updates the tile in hand for a specific player in the client model.
     *
     * @param player the name of the player whose tile in hand is being updated
     * @param tile the component tile to be set in the player's hand
     */
    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile)  {
        clientModel.setTileInHand(player, tile);
    }

    /**
     * Updates the list of uncovered component tiles in the client model.
     *
     * @param ctList the list of {@code ComponentTile} objects representing the updated
     *               uncovered tiles to be displayed in the client model
     */
    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList)  {
        clientModel.setUncoveredComponentTiles(ctList);
    }

    /**
     * Updates the covered component tiles in the client's model.
     * This method is used to synchronize the client-side representation
     * of covered component tiles with the data provided by the server.
     *
     * @param ctList the list of covered component tiles to be updated in the client model
     */
    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) {
        clientModel.setCoveredComponentTiles(ctList);
    }

    /**
     * Updates the shipboard for a specified player in the client model.
     * This method ensures that the client model reflects the latest state
     * of the specified player's shipboard as provided by the server.
     *
     * @param player the name of the player whose shipboard is being updated
     * @param shipboard the new state of the player's shipboard to be set
     */
    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard)  {
        clientModel.setShipboard(player, shipboard);
    }

    /**
     * Updates the client model with the latest flightboard data.
     *
     * @param flightboard the {@code Flightboard} object representing the current state
     *                    of the game environment to be displayed or processed.
     */
    @Override
    public void showUpdateFlightboard(Flightboard flightboard)  {
        clientModel.setFlightboard(flightboard);
        //// TODO
    }

    /**
     * Updates the current adventure card in the client's model.
     * This method is used to synchronize the state of the client
     * with the current adventure card being played in the game.
     *
     * @param adventureCard the current adventure card to update in the client model
     */
    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard)  {
        clientModel.setCurrCard(adventureCard);
    }

    /**
     * Updates the client model with the provided dices information.
     *
     * @param dices the Dices object containing the current state of the dice values
     */
    @Override
    public void showUpdateDices(Dices dices)  {
        clientModel.setDices(dices);
    }

    /**
     * Updates the current player in the client model.
     *
     * @param currPlayer the nickname of the player who is currently active
     */
    @Override
    public void showUpdateCurrPlayer(String currPlayer)  {
        clientModel.setCurrPlayer(currPlayer);
    }

    /**
     * Updates the game phase in the client model with the provided game phase.
     * This method is used to keep the client-side representation of the game synchronized
     * with the current state of the game.
     *
     * @param gamePhase the current game phase to be updated in the client model
     */
    @Override
    public void showUpdateGamePhase(GamePhase gamePhase) {
        clientModel.setGamePhase(gamePhase);
    }

    /**
     * Updates the client's view of the deck with the provided list of adventure cards.
     *
     * @param deck a list of {@code AdventureCard} objects representing the current deck state
     */
    @Override
    public void showUpdateDeck(List<AdventureCard> deck) {
        clientModel.setDeck(deck);
    }

    /**
     * Updates the current game state in the client model.
     *
     * @param game the updated game instance to be set in the client model
     */
    @Override
    public void showUpdateGame(Game game) {
        clientModel.setGame(game);
    }

    /**
     * Handles the logic to stop the hourglass mechanism for the client. This is
     * invoked to update the client-side model to disable any time-based countdowns
     * or timers that may be associated with the game.
     *
     * This method utilizes the {@code stopHourglass} function within the
     * {@code ClientModel} to perform the necessary operations for halting the
     * hourglass, including deactivation of the timer and notifying relevant
     * components about this change.
     *
     * @throws RemoteException if a communication-related error occurs during the
     * execution of this remote method
     */
    @Override
    public void showUpdateStopHourglass() throws RemoteException {
        clientModel.stopHourglass();
    }

    /**
     * Displays the start of an hourglass timing period in the client view.
     * This method delegates the action to the client model to initiate
     * the hourglass timer based on the specified spot.
     *
     * @param hourglassSpot an integer representing the specific position or
     *                      identifier of the hourglass to start.
     * @throws RemoteException if a communication-related exception occurs
     *                         during the remote method call.
     */
    @Override
    public void showUpdateStartHourglass(int hourglassSpot) throws RemoteException {
        clientModel.startHourglass(hourglassSpot);
    }

    /**
     * Sets the name of the player.
     *
     * @param testPlayer the name of the player to be set
     */
    public void setPlayerName(String testPlayer) {
        this.playerName = testPlayer;
    }

    /**
     * Starts a heartbeat mechanism to maintain a connection with the server.
     * A scheduled task is executed periodically to send a heartbeat to the server
     * to ensure the connection is alive. If the server is unreachable, the heartbeat
     * task will stop, and the client will shut down.
     *
     * @param playerName the name of the player sending the heartbeat
     * @param server the server to which the heartbeat is sent
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

    /**
     * Retrieves the type of the virtual server used for this client.
     *
     * @return a string representing the type of the virtual server, which is "rmi"
     */
    public String getVirtualServerType() {
        return "rmi";
    }
}