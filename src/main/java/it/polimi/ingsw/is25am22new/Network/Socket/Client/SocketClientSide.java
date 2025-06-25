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
import it.polimi.ingsw.is25am22new.Network.RMI.Client.EnhancedClientView;
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

/**
 * The SocketClientSide class serves as a client-side representation for handling socket-based communication
 * between the client and server in a multiplayer game. It implements the VirtualView interface
 * to update and display client-side data, and manages incoming and outgoing socket messages.
 * This class provides mechanisms for handling real-time updates, managing player interactions,
 * and keeping the state in sync with the server.
 */
public class SocketClientSide implements VirtualView {

    ClientModel clientModel;
    final ObjectInputStream objectInput;
    final SocketServerHandler output;
    String thisPlayerName;
    EnhancedClientView view;
    boolean isHost;
    private ScheduledExecutorService heartbeatScheduler;
    Socket socket;
    private Map<String, Consumer<SocketMessage>> commandMap;

    List<String> players = new ArrayList<>();
    String gameType = "ERROR";
    String message = "ERROR";

    /**
     * Initializes a new instance of the SocketClientSide class.
     *
     * @param socket the socket connection to the server.
     * @param objectInput the input stream for reading data from the server.
     * @param output the handler for sending data to the server.
     * @param thisPlayerName the name of the current player.
     * @param clientModel the model representing the client-side game state.
     * @param view the enhanced client view for rendering output to the user.
     * @throws IOException if an I/O error occurs when creating the input or output streams.
     */
    protected SocketClientSide(Socket socket, ObjectInputStream objectInput, SocketServerHandler output, String thisPlayerName, ClientModel clientModel, EnhancedClientView view) throws IOException {
        this.socket = socket;
        this.output = output;
        this.objectInput = objectInput;
        this.thisPlayerName = thisPlayerName;
        this.view = view;
        this.clientModel = clientModel;
        commandMap = new HashMap<>();
        initializeCommandMap();
    }

    /**
     * Initializes the command map with key-value pairs, where the key represents a specific message
     * type and the value is the corresponding method reference or lambda function to handle that message type.
     * These handler methods are invoked dynamically based on the message type received during execution.
     * The message handling logic ranges from processing game states, player actions, or system events
     * to updating the client-side view with new game information.
     */
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
        commandMap.put("leaderboard", this::handleLeaderboard);
        commandMap.put("GameLoaded",this::handleGameLoaded);
    }

    /**
     * Executes the main sequence of client-side operations, including running a virtual server,
     * starting a heartbeat mechanism, setting the player's nickname in the model,
     * and initiating the socket-based command loop for the lobby view.
     *
     * @param scanner the {@code Scanner} instance used to read input, typically for parsing commands or user input
     * @throws InterruptedException if the thread is interrupted during a sleep or execution process
     */
    public void run(Scanner scanner){

        new Thread(this::runVirtualServer).start();

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted during sleep: " + e.getMessage());
        }

        startHeartbeat(thisPlayerName, output);
        clientModel.setPlayerName(thisPlayerName);
        ((LobbyView) view).startCommandLoopSocket(this.output, thisPlayerName, scanner);
    }

    /**
     * Executes a sequence of actions for the client-side component of the networked game.
     * It starts by launching a new thread to execute the virtual server loop, which handles
     * incoming socket messages and dispatches them to corresponding handlers. Afterward, it
     * introduces a short delay before triggering essential client-side operations.
     *
     * Specifically, it initiates a heartbeat mechanism that periodically sends signals to indicate
     * that the client is active. It also sets the player's nickname in the client model, allowing
     * synchronization between the server and client regarding the player's identity.
     *
     * @throws InterruptedException if the thread sleep operation is interrupted during execution
     */
    public void run() {

        new Thread(this::runVirtualServer).start();

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            // Handle the interruption gracefully
            System.out.println("Thread interrupted during sleep: " + e.getMessage());
        }

        startHeartbeat(thisPlayerName, output);
    }

    /**
     * Executes the virtual server loop, which listens for incoming socket messages
     * and processes them dynamically based on their associated commands.
     *
     * An incoming message is deserialized from the input stream and matched against
     * a pre-initialized command map. The map defines handlers for recognized commands.
     * If a command is unrecognized, a default handler is invoked to manage invalid messages.
     *
     * The loop continues until all messages are processed or an exception occurs, such as
     * when the connection is closed or an error interrupts the reading process.
     *
     * On termination of the loop, a cleanup procedure is executed to release resources
     * and close the connection gracefully.
     */
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

    /**
     * Updates the client's representation of the bank with the provided bank instance,
     * ensuring synchronization between the server and client states.
     *
     * @param bank the {@code Bank} instance containing the updated state of the game's bank
     */
    @Override
    public void showUpdateBank(Bank bank) {
        clientModel.setBank(bank);
    }

    /**
     * Updates the tile currently in hand for a specific player.
     *
     * @param player the identifier of the player whose tile in hand is to be updated
     * @param tile the {@code ComponentTile} object that represents the tile to be updated in the player's hand
     */
    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) {
        clientModel.setTileInHand(player, tile);

    }

    /**
     * Updates the uncovered component tiles in the client model with the provided list of tiles.
     *
     * @param ctList a list of {@code ComponentTile} objects representing the uncovered tiles to update in the client model
     */
    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList) {
        clientModel.setUncoveredComponentTiles(ctList);
    }

    /**
     * Updates the list of covered component tiles in the client model
     * and reflects the latest game state.
     *
     * @param ctList the list of covered component tiles to be updated in the client model
     */
    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) {
        clientModel.setCoveredComponentTiles(ctList);
    }

    /**
     * Updates the client model with the given player's shipboard state.
     *
     * @param player    the name of the player whose shipboard is being updated
     * @param shipboard the updated shipboard associated with the player
     */
    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) {
        clientModel.setShipboard(player, shipboard);
    }

    /**
     *
     */
    @Override
    public void showUpdateFlightboard(Flightboard flightboard) {
        clientModel.setFlightboard(flightboard);
    }

    /**
     *
     */
    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard)  {
        clientModel.setCurrCard(adventureCard);
    }

    /**
     * Updates the current dice state in the client model using the provided {@link Dices} instance.
     *
     * @param dices the {@link Dices} instance containing the updated values of the dice
     */
    @Override
    public void showUpdateDices(Dices dices) {
        clientModel.setDices(dices);
    }

    /**
     *
     */
    @Override
    public void showUpdateCurrPlayer(String currPlayer)  {
        clientModel.setCurrPlayer(currPlayer);
    }

    /**
     *
     */
    @Override
    public void showUpdateGamePhase(GamePhase gamePhase)  {
        clientModel.setGamePhase(gamePhase);
    }

    /**
     * Updates the client-side model with the provided deck of adventure cards.
     * This method is typically called to refresh the deck state on the client,
     * ensuring it remains synchronized with the server's data.
     *
     * @param deck the list of {@code AdventureCard} objects representing the updated deck
     */
    @Override
    public void showUpdateDeck(List<AdventureCard> deck)  {
        clientModel.setDeck(deck);
    }

    /**
     * Updates the*/
    @Override
    public void showUpdateGame(Game game)  {
        clientModel.setGame(game);
    }

    /**
     *
     */
    @Override
    public void showUpdateStopHourglass(){
        clientModel.stopHourglass();
    }

    /**
     *
     */
    @Override
    public void showUpdateStartHourglass(int hourglassSpot) {
        clientModel.startHourglass(hourglassSpot);
    }

    /**
     * Displays the updated state of the game lobby to the client, including the list of players,
     * their ready statuses, and the game type, using the view layer.
     *
     * @param players the list of players currently in the lobby
     * @param readyStatus a map indicating the ready status of each player, where the key is the
     *                    player's name and the value is a boolean representing whether the player
     *                    is ready
     * @param gameType a string representing the type of game being*/
    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) {
        this.view.displayLobbyUpdate(players, readyStatus, gameType, isHost);
    }

    /**
     * Displays the result of a connection attempt, providing feedback about whether the connection
     * was successful or not, and whether the current user is the host.
     *
     * @param isHost  a boolean indicating if the current user is the host of the connection
     * @param success a boolean indicating whether the connection attempt succeeded
     * @param message a String containing an additional message or description related to the connection result
     */
    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) {
        this.view.displayConnectionResult(isHost, success, message);
    }

    /**
     * Displays the result of the nickname validation process.
     *
     * @param valid   whether the provided nickname*/
    @Override
    public void showNicknameResult(boolean valid, String message) {
        this.view.displayNicknameResult(valid, message);
    }

    /**
     *
     */
    @Override
    public void showPlayerJoined(String player) {
        this.view.displayPlayerJoined(player);
    }

    /**
     * Updates the client-side leaderboard by setting the provided data.
     * The leaderboard contains player names as keys and their respective scores as values.
     *
     * @param leaderboard a map where the key is the player's name (String) and the value is their score (Integer)
     */
    @Override
    public void showUpdateLeaderboard(Map<String, Integer> leaderboard) {
        clientModel.setLeaderboard(leaderboard);
    }

    /**
     *
     */
    @Override
    public void terminate() throws RemoteException {
        /// TODO
    }

    @Override
    public void showUpdateGameLoaded(Game game) {
        clientModel.setGameLoaded(game);
    }

    /**
     * Notifies the client-side view to display that the game has started.
     * This method delegates the action to the view's {@code displayGameStarted} method,
     * ensuring the user interface reflects the transition to the game-play phase.
     */
    @Override
    public void showGameStarted() {
        this.view.displayGameStarted();
    }

    /**
     *
     */
    public void showMessage(String message) {
        System.out.println("Message from server: " + message);
        System.out.flush();
    }

    /**
     *
     */
    public void setNumPlayers(int numPlayers) {
        output.setNumPlayers(numPlayers);
    }

    /**
     * Retrieves the {@code SocketServerHandler} instance associated with this client-side connection.
     * The {@code SocketServerHandler} is responsible for managing server-related operations
     * such as sending and receiving messages within the networked environment.
     *
     * @return the {@code SocketServerHandler} instance used for communication with the server
     */
    public SocketServerHandler getServerHandler() {
        return output;
    }

    /**
     *
     */
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

    /**
     * Shuts down the socket client-side connection and releases associated resources.
     *
     * This method performs the following cleanup tasks:
     * - Closes the `ObjectInputStream` if it is not null.
     * - Closes the `ObjectOutputStream` (accessed via the `output` field) if it is not null.
     * - Closes the socket if it is not null and open.
     *
     * In the event of an IOException while closing any of these resources,
     * an error message is printed to `System.err`, and the shutdown process continues.
     *
     * After releasing resources, the method exits the application by calling `System.exit(0)`.
     */
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

    /**
     * Processes a leaderboard update received in a socket message and updates the client-side view accordingly.
     *
     * @param msg the {@code SocketMessage} containing the leaderboard data. The message payload should be a {@code Map<String, Integer>}
     *            where the key represents the player's name and the value represents their respective score.
     */
    private void handleLeaderboard(SocketMessage msg) {
        Map<String, Integer> leaderboard = (Map<String, Integer>) msg.getObject();
        showUpdateLeaderboard(leaderboard);
    }

    /**
     * Processes a socket message containing a Bank object and updates the client-side view
     * with the new state of the Bank.
     *
     * @param msg the {@code SocketMessage} containing the {@code Bank} object to be processed
     */
    private void handleBank(SocketMessage msg) {
        Bank bank = (Bank) msg.getObject();
        showUpdateBank(bank);
    }

    /**
     * Handles an incoming {@code SocketMessage} related to a tile in a player's hand.
     * The method processes the message by extracting the tile and player information
     * and updates the client-side view to reflect the changes.
     *
     * @param msg the {@code Socket*/
    private void handleTileInHand(SocketMessage msg) {
        ComponentTile tile = (ComponentTile) msg.getObject();
        String player = msg.getPayload();
        showUpdateTileInHand(player, tile);
    }

    /**
     *
     */
    private void handleUncoveredComponentTile(SocketMessage msg) {
        List<ComponentTile> ctList = (ArrayList<ComponentTile>) msg.getObject();
        showUpdateUncoveredComponentTiles(ctList);
    }

    /**
     * Processes a "covered component tile" message received through the socket.
     * Retrieves the list of covered component tiles from the message and updates the view accordingly.
     *
     * @param msg the {@code SocketMessage} containing the covered component tiles as its payload
     */
    private void handleCoveredComponentTile(SocketMessage msg) {
        List<ComponentTile> ctList = (ArrayList<ComponentTile>) msg.getObject();
        showUpdateCoveredComponentTiles(ctList);
    }

    /**
     *
     */
    private void handleShipboard(SocketMessage msg) {
        String player = msg.getPayload();
        Shipboard shipboard = (Shipboard) msg.getObject();
        showUpdateShipboard(player, shipboard);
    }

    /**
     * Handles the "Flightboard" message received through the socket and updates the flightboard*/
    private void handleFlightboard(SocketMessage msg) {
        Flightboard flightboard = (Flightboard) msg.getObject();
        showUpdateFlightboard(flightboard);
    }

    /**
     * Handles the current adventure card by extracting it from the given socket message
     * and updating the client-side view with the received card.
     *
     * @param msg*/
    private void handleCurrCard(SocketMessage msg) {
        AdventureCard card = (AdventureCard) msg.getObject();
        showUpdateCurrCard(card);
    }

    /**
     * Handles the processing of a dice-related {@link SocketMessage} received from the server.
     * Extracts and processes the {@link Dices} object contained within the message and updates the view
     * by invoking {@code showUpdateDices}.
     *
     * @param msg the {@link SocketMessage} containing the {@link Dices} object to be processed
     */
    private void handleDices(SocketMessage msg) {
        Dices dices = (Dices) msg.getObject();
        showUpdateDices(dices);
    }

    /**
     *
     */
    private void handleCurrPlayer(SocketMessage msg) {
        String currPlayer = msg.getPayload();
        showUpdateCurrPlayer(currPlayer);
    }

    /**
     *
     */
    private void handleGamePhase(SocketMessage msg) {
        GamePhase gamePhase = (GamePhase) msg.getObject();
        showUpdateGamePhase(gamePhase);
    }

    /**
     *
     */
    private void handleDeck(SocketMessage msg) {
        List<AdventureCard> deck = (ArrayList<AdventureCard>) msg.getObject();
        showUpdateDeck(deck);
    }

    /**
     *
     */
    private void handleGame(SocketMessage msg) {
        Game game = (Game) msg.getObject();
        showUpdateGame(game);
    }

    /**
     *
     */
    private void handleLobbyUpdate(SocketMessage msg) {
        players = (ArrayList<String>) msg.getObject();
        gameType = msg.getPayload();
    }

    /**
     *
     */
    private void handleReadyStatus(SocketMessage msg) {
        Map<String, Boolean> readyStatus = (Map<String, Boolean>) msg.getObject();
        showLobbyUpdate(players, readyStatus, gameType);
    }

    /**
     * Handles the result of a connection by processing the received socket message.
     *
     * @param msg the socket message containing connection details and payload
     */
    private void handleConnectionResult(SocketMessage msg) {
        isHost = (boolean) msg.getObject();
        message = msg.getPayload();
    }

    /**
     *
     */
    private void handleSuccess(SocketMessage msg) {
        boolean success = (boolean) msg.getObject();
        showConnectionResult(isHost, success, message);
    }

    /**
     *
     */
    private void handleNicknameResult(SocketMessage msg) {
        boolean valid = (boolean) msg.getObject();
        message = msg.getPayload();
        showNicknameResult(valid, message);
    }

    /**
     * Handles the event where a player joins the game. It extracts the player's name
     * from the incoming socket message and informs the view to display the joined player.
     *
     * @param msg the socket message containing the player's name in its payload
     */
    private void handlePlayerJoined(SocketMessage msg) {
        String player = msg.getPayload();
        showPlayerJoined(player);
    }

    /**
     * Handles a message intended to be displayed to all connected players or users.
     * Extracts the message payload from the provided socket message and displays it
     * using the appropriate view mechanism.
     *
     * @param msg the {@code SocketMessage} containing the payload to be displayed
     */
    private void handleMessageToEveryone(SocketMessage msg) {
        showMessage(msg.getPayload());
    }

    /**
     * Processes the given socket message to handle the "start hourglass" action.
     * Extracts the hourglass spot identifier from the message and triggers an
     * update to display the hourglass start event.
     *
     * @param msg the {@code SocketMessage} containing information about the
     *            hourglass start, which includes an identifier for the hourglass spot
     */
    private void handleStartHourglass(SocketMessage msg) {
        int hourglassSpot = (int) msg.getObject();
        showUpdateStartHourglass(hourglassSpot);
    }

    /**
     * Processes an incoming test update message by retrieving and printing the message payload
     * and the index chosen from the encapsulated command object.
     *
     * @param msg the {@code SocketMessage} instance containing the payload
     *            and associated command object to be processed
     */
    private void handleUpdateTest(SocketMessage msg) {
        System.out.println(msg.getPayload());
        System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
        System.out.flush();
    }

    private void handleGameLoaded(SocketMessage msg) {
        Game game = (Game) msg.getObject();
        showUpdateGameLoaded(game);
    }

    /**
     * Retrieves the name of the current player.
     *
     * @return the name of the current player as a String
     */
    public String getThisPlayerName() {
        return thisPlayerName;
    }

    /**
     * Handles the processing of invalid messages by logging the command of
     * the invalid message to the error output stream.
     *
     * @param msg the SocketMessage object representing the invalid message to be handled
     */
    private void handleInvalidMessage(SocketMessage msg) {
        System.err.println("[INVALID MESSAGE]: " + msg.getCommand());
    }
}
