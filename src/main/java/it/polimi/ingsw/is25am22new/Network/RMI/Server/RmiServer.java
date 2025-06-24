package it.polimi.ingsw.is25am22new.Network.RMI.Server;

import it.polimi.ingsw.is25am22new.Controller.GameController;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.HeartbeatManager;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RmiServer class is responsible for managing the server-side implementation of the virtual server
 * in a distributed RMI-based system. It extends UnicastRemoteObject and functions as both an ObserverModel
 * and a VirtualServer, handling communication between client-side views and the server-side game logic.
 * The class maintains the connection state of clients, handles updates to the game state, and implements
 * various functionalities required to manage a multiplayer game. It supports client connection and
 * disconnection, game setup, in-game operations, and real-time state updates. Additionally, the server
 * can monitor client heartbeats to manage client disconnections.
 */
public class RmiServer extends UnicastRemoteObject implements ObserverModel, VirtualServer {

    private final GameController gameController;
    private final List<VirtualView> connectedClients;
    private final Map<String, VirtualView> clientMap; //Map nickname to clients
    private static final String SERVER_NAME = "GalaxyTruckerServer";
    private final HeartbeatManager heartbeatManager;
    private final Registry registry;

    /**
     * Initializes the RMI server and binds it to the registry on the specified port.
     * The server manages client connections, communication, and disconnections, as well as
     * provides game-related updates through remote method invocation.
     *
     * @param gameController the GameController instance that manages the game logic and state
     * @param port the port number on which the RMI server will listen for incoming connections
     * @throws RemoteException if a networking-related issue occurs during initialization
     */
    public RmiServer(GameController gameController, int port) throws RemoteException {
        super();
        this.gameController = gameController;
        this.connectedClients = new ArrayList<>();
        this.clientMap = new HashMap<>();

        this.heartbeatManager = new HeartbeatManager(5000, this::handleHeartbeatDisconnect);

        //System.setProperty("java.rmi.server.hostname", "172.20.10.2");
        registry = LocateRegistry.createRegistry(port);
        registry.rebind(SERVER_NAME, this);
        System.out.println("RMI Server bound to registry - it is running on port " + port + "...");

    }

    /**
     * Shuts down the RMI server by performing the following actions:
     * - Unregisters the server instance from the RMI registry.
     * - Unexports the server object to make it unavailable for clients.
     * - Logs the shutdown status to the console.
     * - Handles any exceptions that might occur during the shutdown process, such as:
     *   - RemoteException: Issues related to networking or RMI infrastructure.
     *   - NotBoundException: Occurs if the server is not bound in the registry.
     * - Terminates the application using System.exit(0).
     */
    @Override
    public void shutdown() {
        try {
            // Unregister from RMI registry
            registry.unbind(SERVER_NAME);
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("RMI Server shut down successfully");
        } catch (RemoteException e) {
            System.err.println("Error shutting down RMI Server: " + e.getMessage());
        } catch (NotBoundException e) {
            System.err.println("Server not bound: " + e.getMessage());
        }
        System.exit(0);
    }

    /**
     * Updates the shipboard list for all connected clients by notifying
     * them of the current shipboards associated with each player.
     *
     * @param shipboards a map containing player names as keys and their
     *                   respective Shipboard objects as values
     */
    @Override
    public void updateShipboardList(Map<String, Shipboard> shipboards) {
        for (VirtualView client : connectedClients) {
            try {
                for(Map.Entry<String, Shipboard> entry : shipboards.entrySet()) {
                    String player = entry.getKey();
                    Shipboard shipboard = entry.getValue();
                    client.showUpdateShipboard(player, shipboard);
                }
            } catch (RemoteException e) {
                System.err.println("Error updating client with shipboard list: " + e.getMessage());
                handleClientError(client, e);
            } catch (Exception e) {
                //handle showShipboardList exception
            }
        }
    }

    /**
     * Updates all connected clients with the latest leaderboard data.
     * The method iterates through all connected clients and calls their
     * respective method to display the updated leaderboard. In case of
     * remote exceptions, it handles the error by logging and removing
     * the problematic client.
     *
     * @param leaderCards a map containing player nicknames as keys and
     *                    their associated leaderboard scores as values
     */
    @Override
    public void updateAllLeaderboard(Map<String, Integer> leaderCards) {
        for (VirtualView client : connectedClients) {
            try {
                client.showUpdateLeaderboard(leaderCards);
            } catch (RemoteException e) {
                System.err.println("Error updating client with leaderboard: " + e.getMessage());
                handleClientError(client, e);
            } catch (Exception e) {
                //handle showUpdateLeaderboard exception
            }
        }
    }

    /**
     * Handles the receipt of a heartbeat signal from a client.
     * This method is invoked by the client to indicate that it is still active.
     * The nickname of the client is logged, and the heartbeatManager is updated
     * to record the heartbeat for the client.
     *
     * @param nickname the unique identifier of the client sending the heartbeat
     */
    @Override
    public void heartbeat(String nickname) {
        System.out.println("Received heartbeat from: " + nickname);
        heartbeatManager.heartbeat(nickname);
    }

    /**
     * Handles the process of a player quitting the game.
     * Removes the player from the game and unregisters their heartbeat tracking.
     *
     * @param playerName the name of the player who is quitting the game
     */
    @Override
    public void quit(String playerName) {
        gameController.quit(playerName);
        heartbeatManager.unregisterClient(playerName);
    }

    /**
     * Returns the type of the virtual server.
     *
     * @return a string indicating the type of the virtual server, which is "rmi" in this implementation
     * @throws RemoteException if a communication-related error occurs
     */
    @Override
    public String getVirtualServerType() throws RemoteException{
        return "rmi";
    }

    /**
     * Handles the disconnection of a client due to a heartbeat timeout.
     * This method removes the client from the connected clients list and client map,
     * and then shuts down the server.
     *
     * @param nickname the unique identifier of the client that is being disconnected
     */
    private void handleHeartbeatDisconnect(String nickname) {
        try {
            System.out.println("Heartbeat timeout for client: " + nickname);

            VirtualView client = clientMap.get(nickname);
            if (client != null) {
                connectedClients.remove(client);
                clientMap.remove(nickname);
            }

            shutdown();

        } catch (Exception e) {
            System.err.println("Error handling client disconnect: " + e.getMessage());
        }
    }

    /**
     * Handles the connection of a client to the server lobby. This method checks if the nickname
     * is valid, if the lobby is full, or if the game is already in progress. Based on these checks,
     * it updates the client with the connection outcome and manages the client's involvement in
     * the current game or lobby.
     *
     * @param client a {@link VirtualView} instance representing the client attempting to connect
     * @param nickname a {@link String} representing the desired nickname of the connecting client
     * @throws RemoteException if a remote communication error occurs
     */
    public void connect(VirtualView client, String nickname) throws RemoteException {
        if (client == null) {
            System.err.println("Client reference is null.");
            return;
        }

        if (nickname == null || nickname.trim().isEmpty()) {
            client.showConnectionResult(false, false, "Invalid nickname. Connection failed.");
            return;
        }

        if(gameController.isStarted() || gameController.getPlayers().isEmpty()) {
            synchronized (connectedClients) {
                if (connectedClients.size() >= 4) {
                    client.showConnectionResult(false, false, "Lobby is full. Connecting failed.");
                    return;
                }

                if (clientMap.containsKey(nickname)) {
                    client.showNicknameResult(false, "Nickname already taken.");
                    return;
                }

                int result = gameController.addPlayer(nickname);

                if (result < 0) {
                    client.showConnectionResult(false, false, "Failed to join the lobby.");
                    return;
                }

                connectedClients.add(client);
                clientMap.put(nickname, client);

                heartbeatManager.registerClient(nickname);

                String lobbyCreator = gameController.getLobbyCreator();
                boolean isHost = nickname.equals(lobbyCreator);

                client.showConnectionResult(isHost, true, isHost ? "You are the host of the lobby." : "You joined an existing lobby.");

                if (!isHost) {
                    gameController.updateAllPlayerJoined(nickname);
                }

                gameController.updateAllLobbies();
            }
        }else{
            //client.showConnectionResult(false, false, "Host is configuring the game. Please try again.");
            client.showNicknameResult(false, "Host is configuring the lobby... please retry");
            client.terminate();
        }
    }

    /**
     * Disconnects the client from the RMI server. This method handles the disconnection
     * process, ensuring that any resources associated with the client are released,
     * and updates the server state to reflect the client's departure. Implementations
     * should ensure that the client is properly removed from any active sessions,
     * and other clients are notified of the disconnection if necessary.
     */
    @Override
    public void disconnect() {
        // TO DO
    }

    /**
     * Sets the number of players for the game. The value must be between the allowed range
     * (inclusive), typically 2 to 4 players. Throws an exception if the value is invalid.
     *
     * @param numPlayers the number of players to set for the game
     * @throws IOException if the provided number of players is not within the valid range
     */
    @Override
    public void setNumPlayers(int numPlayers) throws IOException {
        if(numPlayers < 2 || numPlayers > 4){
            throw new IOException("Invalid number of players: " + numPlayers);
        }
        gameController.setNumPlayers(numPlayers);
        //gameController.updateAllLobbies();
    }

    /**
     * Updates all connected clients about a new player joining the game.
     * This method sends a notification to all clients except the one associated with the joining player.
     * If an exception occurs during the update, it handles the client error appropriately.
     *
     * @param nickname the nickname of the player who has joined the game
     */
    @Override
    public void updatePlayerJoined(String nickname) {
        for (VirtualView client : connectedClients) {
            if (!client.equals(clientMap.get(nickname))) {
                try {
                    (client).showPlayerJoined(nickname);
                } catch (RemoteException e) {
                    System.err.println("Error updating client with player joined: " + e.getMessage());
                    handleClientError(client, e);
                } catch (Exception e) {
                    //handle showPlayerJoined exception
                }
            }
        }
    }

    /**
     * Updates the state of the game lobby by retrieving the list of players, their readiness
     * status, and the current game type from the game controller. This information is then
     * sent to all connected clients via their respective {@code VirtualView} objects.
     *
     * The method iterates through the list of connected clients and invokes the
     * {@code showLobbyUpdate} method on each client's {@code VirtualView} to synchronize the
     * lobby state. If an exception occurs during this operation, it is logged and handled
     * appropriately.
     *
     * The lobby state consists of:
     * - The list of players currently in the lobby.
     * - The readiness status of each player.
     * - The type of game currently selected.
     *
     * If a {@code RemoteException} is encountered while attempting to update a client, the
     * specific client causing the error is handled using the internal {@code handleClientError}
     * method to address the issue.
     */
    @Override
    public void updateLobby() {
        List<String> players = gameController.getPlayers();
        Map<String, Boolean> readyStatus = gameController.getReadyStatus();
        String gameType = gameController.getGameType();

        for (VirtualView client : connectedClients) {
            try {
                client.showLobbyUpdate(players, readyStatus, gameType);
            } catch (RemoteException e) {
                System.err.println("Error updating client with lobby information: " + e.getMessage());
                handleClientError(client, e);
            } catch (Exception e) {
                //handle showLobbyUpdate exception
            }
        }
    }

    /**
     * Notifies all connected clients that the game has started by invoking the
     * {@code showGameStarted()} method on each client. If a {@link RemoteException} occurs while
     * notifying a client, the error is logged, and the associated client is handled appropriately
     * using the {@link #handleClientError(VirtualView, RemoteException)} method.
     *
     * This method ensures that every client in the {@code connectedClients} list receives an
     * update about the game's start, maintaining synchronization across all connected clients.
     *
     * Exceptions:
     * - {@link RemoteException}: Handles communication errors between the server and the client,
     *   such as a disconnection during the update. This exception is logged and further handled
     *   using the {@code handleClientError} method.
     * - Generic exceptions are caught and ignored with no additional handling, ensuring the method
     *   continues processing other clients in case of an unexpected error.
     */
    @Override
    public void updateGameStarted() {
        for (VirtualView client : connectedClients) {
            try {
                client.showGameStarted();
            } catch (RemoteException e) {
                System.err.println("Error updating client with game started: " + e.getMessage());
                handleClientError(client, e);
            } catch (Exception e) {
                //handle showGameStarted exception
            }
        }
    }

    /**
     * Updates the bank information for all connected clients. This method iterates
     * through the list of connected clients, attempting to update each client
     * with the new bank state using the showUpdateBank method.
     * If an exception occurs during the update, the error is logged, and appropriate
     * error handling is performed.
     *
     * @param bank the updated Bank instance containing the latest state of resources
     *             to be communicated to the connected clients
     */
    @Override
    public void updateBank(Bank bank) {
        for (VirtualView connectedClient : connectedClients) {
            try{
                connectedClient.showUpdateBank(bank);
            } catch (RemoteException e) {
                System.err.println("Error updating client with bank information: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateBank exception
            }
        }
    }

    /**
     * Updates the specified player's hand with the given component tile by notifying all connected clients.
     * Each client receives the update through the `showUpdateTileInHand` method, with any errors during the update
     * process being handled.
     *
     * @param player the name of the player whose hand is being updated
     * @param ct the component tile to be added to the player's hand
     */
    @Override
    public void updateTileInHand(String player, ComponentTile ct) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateTileInHand(player, ct);
            } catch (RemoteException e) {
                System.err.println("Error updating client with tile in hand: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateTileInHand exception
            }
        }
    }

    /**
     * Updates all connected clients with the list of uncovered component tiles.
     * This method sends the provided list of tiles to all clients through their
     * respective virtual views, handling communication errors if they occur.
     *
     * @param tilesList the list of uncovered {@code ComponentTile} objects to be sent to clients
     */
    @Override
    public void updateUncoveredComponentTiles(List<ComponentTile> tilesList) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateUncoveredComponentTiles(tilesList);
            } catch (RemoteException e) {
                System.err.println("Error updating client with uncovered component tiles: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateUncoveredComponentTiles exception
            }
        }
    }

    /**
     * Updates the shipboard status for a specific player and communicates the changes
     * to all connected clients.
     *
     * @param player the nickname of the player whose shipboard needs to be updated
     * @param shipboard the updated state of the player's shipboard
     */
    @Override
    public void updateShipboard(String player, Shipboard shipboard) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateShipboard(player, shipboard);
            } catch (RemoteException e) {
                System.err.println("Error updating client with shipboard: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateShipboard exception
            }
        }
    }

    /**
     * Updates the flightboard and notifies all connected clients with the latest flightboard information.
     *
     * @param flightboard the updated flightboard object containing the latest game state or flight details to be communicated to the clients
     */
    @Override
    public void updateFlightboard(Flightboard flightboard) {
        for(VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateFlightboard(flightboard);
            } catch (RemoteException e) {
                System.err.println("Error updating client with flightboard: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateFlightboard exception
            }
        }
    }

    /**
     * Updates the current adventure card for all connected clients.
     * This method sends the provided AdventureCard instance to each connected client,
     * allowing them to reflect the update in their respective views.
     * Handles potential exceptions during the update process to maintain client communication.
     *
     * @param adventureCard the AdventureCard instance representing the current card to be updated
     */
    @Override
    public void updateCurrCard(AdventureCard adventureCard) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateCurrCard(adventureCard);
            } catch (RemoteException e) {
                System.err.println("Error updating client with current card: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateCurrCard exception
            }
        }
    }

    /**
     * Updates all connected clients with the current state of the dices.
     * Iterates through the list of connected clients and sends the dice values
     * to each client using the showUpdateDices method. Handles any RemoteException
     * or other potential exceptions during the update process.
     *
     * @param dices the Dices object containing the current values of the dice
     *              to be sent to all connected clients.
     */
    @Override
    public void updateDices(Dices dices) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateDices(dices);
            } catch (RemoteException e) {
                System.err.println("Error updating client with dices: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateDices exception
            }
        }
    }

    /**
     * Sends an update to all connected clients notifying them of the current player.
     * If an error occurs while communicating with a client, the error is logged,
     * and the client is handled and removed if necessary using the {@code handleClientError} method.
     *
     * @param currPlayer the nickname of the current player to be updated on all clients
     */
    @Override
    public void updateCurrPlayer(String currPlayer) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateCurrPlayer(currPlayer);
            } catch (RemoteException e) {
                System.err.println("Error updating client with current player: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateCurrPlayer exception
            }
        }
    }

    /**
     * Updates all connected clients with the current game phase.
     * This method iterates through the list of connected clients and
     * invokes the {@code showUpdateGamePhase} method on each {@code VirtualView} to
     * notify them of the updated game phase. Handles any {@code RemoteException}
     * or generic exceptions during the update process.
     *
     * @param gamePhase the {@code GamePhase} object representing the current phase
     *                  of the game to be communicated to clients
     */
    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateGamePhase(gamePhase);
            } catch (RemoteException e) {
                System.err.println("Error updating client with game phase: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateGamePhase exception
            }
        }
    }

    /**
     * Updates all connected clients by providing them with the most recent covered ComponentTile data.
     * This method invokes the `showUpdateCoveredComponentTiles` on each connected client's VirtualView instance.
     * Handles RemoteException for error handling in client communication.
     *
     * @param ctList the list of covered ComponentTiles to be sent to all connected clients
     */
    @Override
    public void updateCoveredComponentTiles(List<ComponentTile> ctList) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateCoveredComponentTiles(ctList);
            } catch (RemoteException e) {
                System.err.println("Error updating client with covered component tiles: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateCoveredComponentTiles exception
            }
        }
    }

    /**
     * Updates the deck of Adventure Cards for all connected clients.
     * Sends the updated deck to each client using their respective VirtualView.
     * Handles any exceptions that occur during the update process,
     * including remote method invocation errors for specific clients.
     *
     * @param deck the list of AdventureCard objects representing the updated deck
     */
    @Override
    public void updateDeck(List<AdventureCard> deck) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateDeck(deck);
            } catch (RemoteException e) {
                System.err.println("Error updating client with deck: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateDeck exception
            }
        }
    }

    /**
     * Updates the game state for all connected clients. Sends the updated game data
     * to each client through the `showUpdateGame` method. Handles any exceptions
     * that may occur during communication with the clients.
     *
     * @param game the updated Game instance containing the latest game state to be sent to clients
     */
    @Override
    public void updateGame(Game game) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateGame(game);
            } catch (RemoteException e) {
                System.err.println("Error updating client with game: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateGame exception
            }
        }
    }

    /**
     * Sends a stop hourglass update to all connected clients.
     * This method iterates over the list of connected clients and invokes the
     * {@code showUpdateStopHourglass()} method on each client to notify them of the update.
     *
     * If a client throws a {@code RemoteException}, it handles the error by logging the issue
     * and invoking {@code handleClientError()} to manage the disconnection and appropriately
     * update the server state.
     *
     * Any other exceptions encountered during the execution of this method are caught and ignored.
     */
    @Override
    public void updateStopHourglass() {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateStopHourglass();
            } catch (RemoteException e) {
                System.err.println("Error updating client with stop hourglass: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showStopHourglass exception
            }
        }
    }

    /**
     * Updates all connected clients with the start hourglass state for the specified hourglass spot.
     * The method iterates through the list of connected clients and invokes the
     * {@code showUpdateStartHourglass} method on each client to notify them of the update.
     * Handles remote exceptions by logging the error and calling the {@code handleClientError} method.
     *
     * @param hourglassSpot the identifier of the hourglass spot that has started
     */
    @Override
    public void updateStartHourglass(int hourglassSpot) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateStartHourglass(hourglassSpot);
            } catch (RemoteException e) {
                System.err.println("Error updating client with start hourglass: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showStartHourglass exception
            }
        }
    }

    /**
     * Handles a client disconnection caused by a RemoteException. It logs the disconnection,
     * removes the client from the list of connected clients and associated mappings,
     * and updates the game state accordingly.
     *
     * @param client the VirtualView instance representing the client that disconnected
     * @param e the RemoteException that caused the client disconnection
     */
    private void handleClientError(VirtualView client, RemoteException e) {
        System.err.println("Client " + client.getClass() + " disconnected: " + e.getMessage());
        synchronized (connectedClients) {
            connectedClients.remove(client);
            // Remove from nickname map
            for (Map.Entry<String, VirtualView> entry : clientMap.entrySet()) {
                if (entry.getValue().equals(client)) {
                    String nickname = entry.getKey();
                    clientMap.remove(nickname);
                    gameController.removePlayer(nickname);
                    break;
                }
            }
        }
        //gameController.updateAllLobbies();
    }

    /**
     * Removes a player from the game by their nickname. This method performs several actions:
     * - Removes the player from the game controller.
     * - Removes the player's associated data from the client map.
     * - Unregisters the player from the heartbeat manager.
     *
     * @param nickname the unique identifier of the player to be removed
     */
    @Override
    public void removePlayer(String nickname) {
        gameController.removePlayer(nickname);
        clientMap.remove(nickname);
        heartbeatManager.unregisterClient(nickname);
        //gameController.updateAllLobbies();
    }

    /**
     * Marks a player as ready in the game lobby. This method updates the player's
     * status within the game logic and triggers a refresh of all active lobbies.
     *
     * @param nickname the nickname of the player to be marked as ready
     */
    @Override
    public void setPlayerReady(String nickname) {
        gameController.setPlayerReady(nickname);
        gameController.updateAllLobbies();
    }

    /**
     * Starts the game when initiated by the host player in the lobby phase. The method validates
     * whether the requester is the host, checks if all players are ready, and initiates the game
     * if all conditions are met. If any condition fails, appropriate error messages are sent back
     * to the client (host or player).
     *
     * @param nickname the nickname of the player attempting to start the game. This should match
     *                 the host player's nickname for the game to start successfully.
     * @throws RemoteException if a communication-related exception occurs during the remote method call.
     */
    @Override
    public void startGameByHost(String nickname) throws RemoteException {
        if (!gameController.getLobbyCreator().equals(nickname)) {
            VirtualView client = clientMap.get(nickname);
            (client).showConnectionResult(false, false, "Only the host can start the game");
            return;
        }

        // Check if all players are ready
        Map<String, Boolean> readyStatus = gameController.getReadyStatus();
        List<String> unreadyPlayers = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : readyStatus.entrySet()) {
            if (!entry.getValue()) {
                unreadyPlayers.add(entry.getKey());
            }
        }

        if (!unreadyPlayers.isEmpty()) {
            // Some players are not ready
            VirtualView hostClient = clientMap.get(nickname);
            String message = "Cannot start game: " + String.join(", ", unreadyPlayers) + " not ready";
            (hostClient).showConnectionResult(true, false, message);
            return;
        }

        boolean result = gameController.startGameByHost(nickname);
        if (result) {
            gameController.updateAllGameStarted();
        } else {
            System.err.println("Error starting game");
        }
    }

    /**
     * Sets the specified player as not ready in the game's lobby.
     * If the game is in the lobby state, the player's readiness status will be updated.
     * Otherwise, the operation is not permitted outside the lobby state.
     *
     * @param nickname the unique identifier of the player whose readiness status is to be set to not ready
     */
    @Override
    public void setPlayerNotReady(String nickname) {
        gameController.setPlayerNotReady(nickname);
        //gameController.updateAllLobbies();
    }

    /**
     * Sets the game type for the server. Only supports "level2" and "tutorial" as valid game types.
     * If a supported game type is specified, it updates the game type in the game controller,
     * refreshes all associated lobbies, and marks the game as started. If the game type is invalid,
     * an error message is logged.
     *
     * @param gameType the type of game to be set. Supported values are "level2" and "tutorial".
     */
    @Override
    public void setGameType(String gameType) {
        if(gameType.equals("level2") || gameType.equals("tutorial")) {
            gameController.setGameType(gameType);
            gameController.updateAllLobbies();
            gameController.setStarted(true);
        } else {
            System.err.println("Invalid game type: " + gameType);
        }
    }

    /**
     * Activates god mode for the specified player with the provided configuration.
     * This method enables modified game mechanics, allowing the player to perform
     * special actions or have particular advantages as per the game's rules.
     *
     * @param nickname the nickname of the player for whom god mode will be enabled
     * @param conf the configuration string defining the parameters or settings for god mode
     */
    @Override
    public void godMode(String nickname, String conf) {
        gameController.godMode(nickname, conf);
    }

    /**
     * Allows a player, identified by their nickname, to pick a covered tile during the game.
     * This action is managed by the GameController.
     *
     * @param nickname the nickname of the player performing the action
     */
    @Override
    public void pickCoveredTile(String nickname) {
        gameController.pickCoveredTile(nickname);
    }

    /**
     * Allows a player to pick an uncovered tile during the game.
     * This action is handled by the {@code gameController} which ensures the game is in the appropriate state
     * and manages the tile selection process.
     *
     * @param nickname the nickname of the player picking the tile
     * @param pngName the name of the tile's image file (PNG format) to identify the selected tile
     */
    @Override
    public void pickUncoveredTile(String nickname, String pngName) {
        gameController.pickUncoveredTile(nickname, pngName);
    }

    /**
     * Rotates a player's component tiles clockwise a specified number of times.
     * This action is only permitted during the game phase.
     *
     * @param nickname the unique identifier of the player requesting the rotation
     * @param rotationNum the number of 90-degree clockwise rotations to apply
     */
    public void rotateClockwise(String nickname, int rotationNum) {
        gameController.rotateClockwise(nickname, rotationNum);
    }

    /**
     * Rotates the game component associated with a specified player counterclockwise
     * a given number of times. Each rotation step is applied sequentially.
     *
     * @param nickname the nickname of the player whose game component is to be rotated
     * @param rotationNum the number of times the component should be rotated counterclockwise
     */
    public void rotateCounterClockwise(String nickname, int rotationNum) {
        gameController.rotateCounterClockwise(nickname, rotationNum);
    }

    /**
     * Welds a component tile onto a specified position on the gameboard after performing
     * the necessary rotations based on the provided parameter. Positive values for rotations
     * indicate clockwise rotations, whereas negative values indicate counter-clockwise rotations.
     * If no rotations are provided, the tile is directly welded onto the specified position.
     *
     * @param nickname the nickname of the player who is performing the weld action
     * @param i the row index on the gameboard where the component tile should be welded
     * @param j the column index on the gameboard where the component tile should be welded
     * @param numOfRotations the number of rotations to apply to the tile before welding it;
     *                       positive for clockwise, negative for counter-clockwise
     * @throws IOException if an I/O error occurs during the execution of the operation
     */
    @Override
    public void weldComponentTile(String nickname, int i, int j, int numOfRotations) throws IOException {
        if(numOfRotations <= 0) {
            gameController.rotateCounterClockwise(nickname, -numOfRotations);
            gameController.weldComponentTile(nickname, i, j);
        } else {
            gameController.rotateClockwise(nickname, numOfRotations);
            gameController.weldComponentTile(nickname, i, j);
        }
    }

    /**
     * Puts the specified player's component tile into standby mode during the game phase.
     * This action is only valid if the game is currently in the GAME state.
     *
     * @param nickname the nickname of the player whose component tile is to be put in standby mode
     */
    @Override
    public void standbyComponentTile(String nickname) {
        gameController.standbyComponentTile(nickname);
    }

    /**
     * Allows a player to pick a standby component tile during the game. This method
     * forwards the request to the game controller for processing. If the current
     * state is not the game state, the action is not performed.
     *
     * @param nickname the nickname of the player making the request
     * @param index the index of the standby tile the player wants to pick
     */
    @Override
    public void pickStandbyComponentTile(String nickname, int index) {
        gameController.pickStandByComponentTile(nickname, index);
    }

    /**
     * Discards a component tile associated with the specified player.
     * This operation is managed by the game controller and typically affects
     * the state of the game if the player is currently in the game phase.
     *
     * @param nickname the nickname of the player who is discarding the component tile
     */
    @Override
    public void discardComponentTile(String nickname) {
        gameController.discardComponentTile(nickname);
    }

    /**
     * Completes the building phase for the player identified by the given nickname.
     * Delegates the operation to the associated game controller to manage game
     * logic and ensure proper state transitions.
     *
     * @param nickname the nickname of the player who has finished their building phase
     */
    @Override
    public void finishBuilding(String nickname) {
        gameController.finishBuilding(nickname);
    }

    /**
     * Completes the process of building within the game, corresponding to the specified player and item index.
     *
     * @param nickname the nickname of the player who is finishing the building process
     * @param index the index identifying the specific building or item to be finalized
     */
    @Override
    public void finishBuilding(String nickname, int index) {
        gameController.finishBuilding(nickname, index);
    }

    /**
     * Notifies the GameController that all shipboards in the game have been completed.
     * This method serves as a trigger for subsequent actions or state changes within
     * the game logic after verifying the completion of shipboards.
     *
     * Delegates the call to the GameController's finishedAllShipboards method.
     */
    @Override
    public void finishedAllShipboards() {
        gameController.finishedAllShipboards();
    }

    /**
     * Flips the hourglass in the game to reverse its state. This action triggers the
     * `flipHourglass` method in the associated game controller.
     * This method is typically used to change the flow of the game or manage a timing mechanism.
     */
    @Override
    public void flipHourglass() {
        gameController.flipHourglass();
    }

    /**
     * Delegates the action of picking a card to the underlying game controller.
     * This method is typically invoked during gameplay when a specific event
     * requires drawing a card from the deck.
     *
     * The actual logic for picking a card, handling synchronization, and
     * checking the game state is implemented in the GameController class
     * to ensure proper game flow and thread safety.
     */
    @Override
    public void pickCard() {
        gameController.pickCard();
    }

    /**
     * Activates a card in the game based on the specified input command. This method
     * delegates the operation to the game controller, which handles the game logic
     * for activating cards. It ensures the operation is executed in the current game state.
     *
     * @param inputCommand the command containing the inputs and parameters needed
     *                     for activating a card. This includes parameters such as
     *                     selection indexes, good block manipulations, and other
     *                     game-related flags or choices.
     */
    @Override
    public void activateCard(InputCommand inputCommand) {
        gameController.activateCard(inputCommand);
    }

    /**
     * Handles the event when a player abandons the game. The method delegates the
     * abandonment action to the game controller, which determines the appropriate
     * behavior based on the current game state.
     *
     * @param nickname the nickname of the player who is abandoning the game
     */
    @Override
    public void playerAbandons(String nickname) {
        gameController.playerAbandons(nickname);
    }

    /**
     * Destroys a component tile on the game board at the specified position
     * for the given player. This method delegates the operation to the
     * gameController instance to execute the logic and maintain game state.
     *
     * @param nickname the nickname of the player attempting to destroy the tile
     * @param i the row index of the tile to be destroyed
     * @param j the column index of the tile to be destroyed
     */
    @Override
    public void destroyComponentTile(String nickname, int i, int j) {
        gameController.destroyTile(nickname, i , j);
    }

    /**
     * Terminates the ongoing game and invokes the associated process to conclude
     * the game session. This method delegates the operation to the GameController
     * to ensure appropriate game state handling and finalization logic.
     *
     * In cases where the game is not in the active game state, the process will
     * not proceed, and an appropriate message will be logged. The exact operations
     * and results are managed within the GameController.
     */
    @Override
    public void endGame() {
        gameController.endGame();
    }

    /**
     * Places a brown alien at the specified location on the game board for the given player.
     *
     * @param playerName the name of the player attempting to place the brown alien
     * @param i the row index where the brown alien is to be placed
     * @param j the column index where the brown alien is to be placed
     */
    @Override
    public void placeBrownAlien(String playerName, int i, int j) {
        gameController.placeBrownAlien(playerName, i, j);
    }

    /**
     * Places astronauts on the game board at the specified position for the given player.
     *
     * @param playerName the name of the player for whom the astronauts are being placed
     * @param i the row index on the game board
     * @param j the column index on the game board
     */
    @Override
    public void placeAstronauts(String playerName, int i, int j) {
        gameController.placeAstronauts(playerName, i, j);
    }

    /**
     * Places a purple alien on the game board at the specified position for the given player.
     *
     * @param playerName the name of the player for whom the purple alien is being placed
     * @param i the row index where the purple alien will be placed
     * @param j the column index where the purple alien will be placed
     */
    @Override
    public void placePurpleAlien(String playerName, int i, int j) {
        gameController.placePurpleAlien(playerName, i, j);
    }
}
