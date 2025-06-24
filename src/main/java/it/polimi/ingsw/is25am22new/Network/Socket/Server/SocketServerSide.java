package it.polimi.ingsw.is25am22new.Network.Socket.Server;

import it.polimi.ingsw.is25am22new.Controller.GameController;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketServerSide implements ObserverModel {
    final ServerSocket listenSocket;
    final GameController controller;
    final Map<SocketClientHandler, Thread> clients;
    List<Socket> clientSockets;

    /**
     * Constructor for the SocketServerSide class. Initializes the server-side
     * socket, the game controller, and the data structures used for managing
     * connected clients and their sockets.
     *
     * @param gameController the GameController instance responsible for managing game logic and updates
     * @param listenSocket the ServerSocket instance used to listen for incoming client connections
     */
    public SocketServerSide(GameController gameController, ServerSocket listenSocket) {
        this.listenSocket = listenSocket;
        this.controller = gameController;
        this.clients = new HashMap<>();
        this.clientSockets = new ArrayList<>();
    }

    /**
     * Starts the server and handles incoming client connections.
     *
     * This method listens for incoming client connections on the pre-configured server socket
     * and establishes a new connection for each client. For every connected client, it creates
     * a new SocketClientHandler to manage the client interaction and spawns a new thread to handle
     * the client's requests in parallel.
     *
     * If the server socket is closed, it stops accepting connections and properly releases resources.
     *
     * Throws:
     * - IOException - if there is an error with the server socket or during connections.
     *
     * Any exceptions during client handling are logged, and resources are cleaned up before the server shuts down.
     */
    public void runServer() throws IOException{
        System.out.println("Server started, waiting for clients...");
        Socket clientSocket = null;
        try {
            while ((clientSocket = this.listenSocket.accept()) != null) {
                System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                clientSockets.add(clientSocket);
                SocketClientHandler handler = new SocketClientHandler(
                        this.controller,
                        this,
                        clientSocket.getInputStream(),
                        clientSocket.getOutputStream()
                );

                Thread t = new Thread(() -> {
                    handler.runVirtualView(Thread.currentThread());
                });
                t.start();
            }
        } catch (SocketException e) {
            System.out.println("Server socket correctly closed");
        } catch (IOException e) {
            System.out.println("Error accepting client connection: " + e.getMessage());
        } finally {
            try {
                if(!listenSocket.isClosed())
                    this.listenSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing listen socket: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a handler and its associated thread to the collection of active client handlers
     * managed by the server. This method is synchronized to ensure thread safety
     * when updating the clients collection.
     *
     * @param handler the {@code SocketClientHandler} instance representing the client handler
     * @param thread the {@code Thread} associated with the given client handler
     */
    public void addHandlerToClients(SocketClientHandler handler, Thread thread) {
        synchronized (this.clients.keySet()){
            clients.put(handler, thread);
        }
    }

    /**
     * Updates the state of the bank for all connected clients. This method propagates
     * the updated Bank object to each client by invoking their respective showUpdateBank
     * method. The bank update is synchronized to ensure thread safety during client notifications.
     *
     * @param bank the updated Bank object containing the current state of the bank
     */
    @Override
    public void updateBank(Bank bank){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateBank(bank);
            }
        }
    }
    /**
     * Updates the tile in the current player's hand and notifies all clients about the update.
     *
     * @param player the identifier of the player whose hand's tile is being updated
     * @param ct the new {@link ComponentTile} to update in the player's hand
     */
    @Override
    public void updateTileInHand(String player, ComponentTile ct){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateTileInHand(player, ct);
            }
        }
    }

    /**
     * Updates the list of uncovered component tiles for all connected clients.
     * This method synchronizes on the set of clients and invokes the {@code showUpdateUncoveredComponentTiles}
     * method on each client to send the updated list of {@code ComponentTile} objects.
     *
     * @param ctList the list of {@code ComponentTile} objects representing the uncovered component tiles to be updated
     */
    @Override
    public void updateUncoveredComponentTiles(List<ComponentTile> ctList) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateUncoveredComponentTiles(ctList);
            }
        }
    }
    /**
     * Updates the shipboard of a specific player and notifies all connected clients
     * about the change.
     *
     * @param player the name of the player whose shipboard is updated
     * @param shipboard the updated shipboard instance associated with the player
     */
    @Override
    public void updateShipboard(String player, Shipboard shipboard){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateShipboard(player, shipboard);
            }
        }
    }
    /**
     * Updates the flightboard for all connected clients.
     * This method iterates through all connected clients and sends them an
     * updated flightboard representation.
     *
     * @param flightboard the updated Flightboard object containing the state of the game to be
     *                    communicated to the clients
     */
    @Override
    public void updateFlightboard(Flightboard flightboard){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateFlightboard(flightboard);
            }
        }
    }
    /**
     * Updates all connected clients with the current adventure card being used in the game.
     * Iterates through the list of registered clients and notifies each of them
     * with the updated adventure card.
     *
     * @param adventureCard the current adventure card to be sent to all connected clients
     */
    @Override
    public void updateCurrCard(AdventureCard adventureCard){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateCurrCard(adventureCard);
            }
        }
    }
    /**
     * Updates the state of the dices for all connected clients by invoking their
     * respective showUpdateDices method.
     *
     * @param dices the Dices object containing the updated values or state of the dice to be
     *              communicated to all clients.
     */
    @Override
    public void updateDices(Dices dices) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateDices(dices);
            }
        }
    }
    /**
     * Updates the current player for all connected clients by notifying them
     * of the change through their respective socket connections.
     *
     * @param currPlayer the username or identifier of the current player to be updated.
     */
    @Override
    public void updateCurrPlayer(String currPlayer) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateCurrPlayer(currPlayer);
            }
        }
    }

    /**
     * Updates all clients with the current game phase.
     * Sends the provided {@link GamePhase} to all connected clients by invoking their respective
     * showUpdateGamePhase method. Thread-safe functionality is implemented to ensure consistent
     * behavior when updating multiple clients simultaneously.
     *
     * @param gamePhase the current phase of the game to be communicated to all clients
     */
    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateGamePhase(gamePhase);
            }
        }
    }

    /**
     * Updates the covered component tiles for all connected clients by notifying them with the provided
     * list of component tiles. This method ensures thread-safety during the update process by synchronizing
     * on the client list.
     *
     * @param ctList the list of {@link ComponentTile} instances representing the newly updated
     *               covered component tiles to be propagated to all clients
     */
    @Override
    public void updateCoveredComponentTiles(List<ComponentTile> ctList) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateCoveredComponentTiles(ctList);
            }
        }
    }

    /**
     * Updates the deck of adventure cards for all connected clients.
     * This method synchronizes on the set of connected clients and sends the updated deck
     * to each client by invoking their {@code showUpdateDeck} method.
     *
     * @param deck the list of {@link AdventureCard} objects representing the updated deck
     */
    @Override
    public void updateDeck(List<AdventureCard> deck) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateDeck(deck);
            }
        }
    }

    /**
     * Updates the game state by notifying all connected clients about the new game instance.
     *
     * @param game the updated game instance to be broadcasted to all clients
     */
    @Override
    public void updateGame(Game game) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateGame(game);
            }
        }
    }

    /**
     * Updates the start of the hourglass at a specific spot for all connected clients.
     * This method iterates through all clients and invokes the appropriate function
     * to notify each client of the hourglass start.
     *
     * @param hourglassSpot the position or identifier of the hourglass being started,
     *                      which is communicated to all connected clients.
     */
    @Override
    public void updateStartHourglass(int hourglassSpot) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateStartHourglass(hourglassSpot);
            }
        }
    }

    /**
     * Notifies all connected clients to stop the hourglass timer.
     * This method iterates through the set of connected clients and invokes the `showUpdateStopHourglass`
     * method on each client. The notification is sent within a synchronized block to ensure thread safety
     * while accessing the shared client list.
     *
     * This functionality is typically used to broadcast the "stop hourglass" update to all clients in the system,
     * ensuring consistent game state across the network.
     */
    @Override
    public void updateStopHourglass() {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateStopHourglass();
            }
        }
    }

    /**
     * Notifies all connected clients that the game has started.
     *
     * This method iterates over the set of connected clients and invokes
     * the {@code showGameStarted} method on each client to send a notification
     * that the game has begun. The iteration and notification process is
     * synchronized to ensure thread safety when accessing the clients' set.
     *
     * Thread-safety is ensured by synchronizing on the key set of the
     * {@code clients} map to prevent concurrent modifications while iterating
     * through the clients.
     */
    @Override
    public void updateGameStarted() {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showGameStarted();
            }
        }
    }

    /**
     * Updates the lobby state for all connected clients. This method retrieves
     * the current list of players, their ready statuses, and the selected game type
     * from the game controller, and sends this information to each client.
     *
     * The method synchronizes on the client map to ensure thread safety when
     * iterating through the connected clients. Each client is notified using
     * the {@code showLobbyUpdate} method of the {@code VirtualViewSocket} interface.
     *
     * The information sent to clients includes:
     * - Current players in the lobby
     * - Ready status of each player
     * - The game type selected
     */
    @Override
    public void updateLobby() {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showLobbyUpdate(this.controller.getPlayers(), this.controller.getReadyStatus(), this.controller.getGameType());
            }
        }
    }

    /**
     * Notifies all connected clients that a new player has joined the game.
     *
     * @param player the name of the player who has joined the game
     */
    @Override
    public void updatePlayerJoined(String player) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showPlayerJoined(player);
            }
        }
    }

    /**
     * Shuts down the server by releasing all resources, closing all client connections,
     * and terminating associated threads. The method ensures that all operations are
     * thread-safe and properly handles exceptions during the shutdown process.
     *
     * Functionality:
     * - Iterates through all connected clients and unregisters them from the
     *   heartbeat manager.
     * - Closes all open input and output streams for each client.
     * - Closes all active client sockets if they are not already closed.
     * - Interrupts all threads associated with the clients to ensure proper termination.
     * - Closes the listening socket if it is still open.
     *
     * Thread Safety:
     * The method synchronizes on the collection of clients to ensure thread-safe
     * access and modification of shared resources.
     *
     * Error Handling:
     * Proper error messages are printed in case of I/O exceptions when closing
     * client streams, sockets, or the listening socket. These exceptions do not
     * prevent the method from proceeding with the shutdown process.
     */
    @Override
    public void shutdown() {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.getHeartbeatManager().unregisterAll();
                try {
                    client.getObjectInput().close();
                    client.getObjectOutput().close();
                } catch (IOException e) {
                    System.out.println("Error closing client streams on server: " + e.getMessage());
                }
            }
            for (var socket : this.clientSockets) {
                try {
                    if(!socket.isClosed())
                        socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing client socket on server: " + e.getMessage());
                }
            }
            for (var t : this.clients.values()) {
                t.interrupt();
            }
        }
        try {
            if(!listenSocket.isClosed())
                this.listenSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing listen socket: " + e.getMessage());
        }
    }

    /**
     * Updates the shipboard list for all connected clients. The method broadcasts
     * the provided map of shipboard updates to each client, allowing them to display
     * and process the updated information.
     *
     * @param shipboards a map where the key is the player's identifier as a string
     *                   and the value is the corresponding updated Shipboard object
     *                   for that player
     */
    @Override
    public void updateShipboardList(Map<String, Shipboard> shipboards) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                for (Map.Entry<String, Shipboard> entry : shipboards.entrySet()) {
                    String player = entry.getKey();
                    Shipboard shipboard = entry.getValue();
                    client.showUpdateShipboard(player, shipboard);
                }
            }
        }
    }

    /**
     * Updates the leaderboard for all connected clients by sending the provided leaderboard data.
     * The leaderboard is displayed to each client through the showUpdateLeaderboard method.
     *
     * @param leaderboard a map where keys represent player names and values represent their scores
     */
    @Override
    public void updateAllLeaderboard(Map<String, Integer> leaderboard) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                try {
                    client.showUpdateLeaderboard(leaderboard);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
