package it.polimi.ingsw.is25am22new.Network.Socket.Server;

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
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SocketClientHandler implements VirtualView {
    private final GameController controller;
    private final SocketServerSide server;
    private final ObjectInputStream objectInput;
    private final ObjectOutputStream objectOutput;
    private final HeartbeatManager heartbeatManager;
    private Map<String, Consumer<SocketMessage>> commandMap;
    private Thread thisThread;
    // CI SONO VARIE ISTANZE DI SOCKETCLIENTHANDLER, UNA PER OGNI GIOCATORE

    /**
     * Constructs a new SocketClientHandler to manage communication between the server and a single client.
     * This class initializes the input and output streams for communication, sets up a heartbeat manager
     * to detect connection loss, and prepares a map of supported commands.
     *
     * @param controller the game controller responsible for managing the game logic.
     * @param server the server instance to which this client handler belongs.
     * @param is the input stream used to receive data from the client.
     * @param os the output stream used to send data to the client.
     * @throws IOException if an error occurs during input or output stream initialization.
     */
    public SocketClientHandler(GameController controller, SocketServerSide server, InputStream is, OutputStream os) throws IOException {
        this.controller = controller;
        this.server = server;
        this.objectOutput = new ObjectOutputStream(os);
        objectOutput.flush();
        this.objectInput = new ObjectInputStream(is);
        this.heartbeatManager = new HeartbeatManager(5000, this::handleHeartbeatDisconnect);
        commandMap = new HashMap<>();
        initializeCommandMap();
    }

    /**
     * Initializes the command map with associations between commands and their corresponding handlers.
     * This method sets up a mapping of string commands (keys) to lambda functions or method references (values) that define specific behaviors.
     * Each command corresponds to a server-side operation or process triggered by a client action in the communication framework.
     *
     * The commands handled in the map include:
     * - Commands to manage player actions such as setting readiness, removing players, or abandoning.
     * - Commands related to gameplay mechanics like picking tiles, finishing building, activating cards, or flipping an hourglass.
     * - Administrative or system commands such as ending the game, setting player counts, enabling god mode, or testing connection.
     * - Other game-specific operations connected to alien placement, astronaut placement, and shipboard updates.
     *
     * The command handlers are method references or lambda expressions that process a `SocketMessage` or a related payload appropriate to the respective action.
     */
    private void initializeCommandMap() {
        commandMap.put("checkAvailability", this::handleCheckAvailability);
        commandMap.put("removePlayer", msg -> removePlayer(msg.getPayload()));
        commandMap.put("setPlayerReady", msg -> setPlayerReady(msg.getPayload()));
        commandMap.put("setPlayerNotReady", msg -> setPlayerNotReady(msg.getPayload()));
        commandMap.put("startGameByHost", msg -> startGameByHost(msg.getPayload()));
        commandMap.put("setGameType", msg -> setGameType(msg.getPayload()));
        commandMap.put("pickCoveredTile", msg -> pickCoveredTile(msg.getPayload()));
        commandMap.put("pickUncoveredTile", msg -> pickUncoveredTile(msg.getPayload(), (String) msg.getObject()));
        commandMap.put("weldComponentTile", this::handleWeldComponentTile);
        commandMap.put("standbyComponentTile", msg -> standbyComponentTile(msg.getPayload()));
        commandMap.put("pickStandByComponentTile", this::handlePickStandbyComponentTile);
        commandMap.put("discardComponentTile", msg -> discardComponentTile(msg.getPayload()));
        commandMap.put("finishBuilding1", msg -> finishBuilding(msg.getPayload()));
        commandMap.put("finishBuilding2", this::handleFinishBuilding2);
        commandMap.put("finishedAllShipboards", msg -> finishedAllShipboards());
        commandMap.put("flipHourglass", msg -> flipHourglass());
        commandMap.put("pickCard", msg -> pickCard());
        commandMap.put("activateCard", msg -> activateCard((InputCommand) msg.getObject()));
        commandMap.put("playerAbandons", msg -> playerAbandons(msg.getPayload()));
        commandMap.put("destroyTile", this::handleDestroyTile);
        commandMap.put("endGame", msg -> endGame());
        commandMap.put("setNumPlayers", this::handleSetNumPlayers);
        commandMap.put("quit", this::handleQuit);
        commandMap.put("godMode", this::handleGodMode);
        commandMap.put("placeBrownAlien", this::handlePlaceBrownAlien);
        commandMap.put("placePurpleAlien", this::handlePlacePurpleAlien);
        commandMap.put("heartbeat", msg -> heartbeat(msg.getPayload()));
        commandMap.put("placeAstronauts", this::handlePlaceAstronauts);
        commandMap.put("disconnect", msg -> controller.disconnect());
        commandMap.put("connectionTester", this::handleConnectionTester);
    }

    /**
     * Handles communication from the client to the server by processing incoming messages
     * through a socket connection. The method listens for serialized {@code SocketMessage}
     * objects, identifies the associated command, and executes the corresponding action.
     *
     * @param thread the thread on which this method is executed, used to manage the
     *               context of communication and interaction with the client.
     */
    //comunicazione dal client al server
    public void runVirtualView(Thread thread) {
        SocketMessage msg;
        thisThread = thread;
        try {
            while ((msg = (SocketMessage) objectInput.readObject()) != null) {
                commandMap.getOrDefault(msg.getCommand(), m -> System.out.println("Unknown command: " + m.getCommand())).accept(msg);
            }
        } catch (Exception e) {
            System.out.println("Connection closed on ServerClientHandler: " + this);
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (objectInput != null) objectInput.close();
                if (objectOutput != null) objectOutput.close();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Sends an update test message to the connected socket client.
     *
     * This method constructs a new `InputCommand` object and sets its index to 9033.
     * A `SocketMessage` instance is then created with the type "updateTest", the
     * `InputCommand` object as the payload, and the message "Update message received".
     * The method writes this `SocketMessage` to the output stream using the
     * `objectOutput` field, ensuring the data is flushed immediately.
     *
     * If an `IOException` occurs during the writing process, the method outputs
     * the error message to the console.
     *
     * Notes:
     * - The `objectOutput` field must be properly initialized before invoking this method.
     * - The `InputCommand` class is used to encapsulate the command payload sent
     *   within the `SocketMessage`.
     * - Intended for testing purposes to verify communication functionality with the client.
     */
    public void showUpdateTest() {
        InputCommand cmd = new InputCommand();
        cmd.setIndexChosen(9033);
        SocketMessage message = new SocketMessage("updateTest", cmd, "Update message received");
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error test: " + e.getMessage());
        }
    }

    /**
     * Receives and processes a heartbeat signal from a client.
     * This method is invoked whenever a client sends a heartbeat to inform
     * the server that the connection is still alive and active.
     *
     * @param nickname The unique nickname of the client sending the heartbeat.
     */
    public void heartbeat(String nickname) {
        System.out.println("Received heartbeat from: " + nickname);
        heartbeatManager.heartbeat(nickname);
    }

    /**
     * Handles the disconnection of a client due to heartbeat timeout. This method logs
     * the timeout event and attempts to disconnect the client from the system.
     *
     * @param nickname the nickname of the client that has experienced a heartbeat timeout
     */
    private void handleHeartbeatDisconnect(String nickname) {
        try {
            System.out.println("Heartbeat timeout for client: " + nickname);

            this.controller.disconnect();
        } catch (Exception e) {
            System.err.println("Error handling client disconnect: " + e.getMessage());
        }
    }

    /**
     * Sends an updated Bank object to the client via a socket connection.
     * This method serializes the provided Bank object and writes it to the output stream.
     * If an IOException occurs during the write operation, an error message is logged.
     *
     * @param bank the Bank object containing the updated state to be sent to the client
     */
    @Override
    public void showUpdateBank(Bank bank){
        SocketMessage message = new SocketMessage("Bank", bank, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating bank for client: " + e.getMessage());
        }
    }

    /**
     * Sends an update message to the connected client regarding a tile that the specified player
     * currently holds in their hand.
     *
     * @param player the name of the player who holds the tile
     * @param tile the tile object to be updated for the player
     */
    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) {
        SocketMessage message = new SocketMessage("TileInHand", tile, player);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating tile in hand for client: " + e.getMessage());
        }
    }

    /**
     * Sends a list of uncovered component tiles to the client for updating.
     *
     * @param ctList the list of {@code ComponentTile} objects representing the uncovered component tiles to be updated
     */
    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList)  {
        SocketMessage message = new SocketMessage("UncoveredComponentTile", ctList, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating uncovered tile for client: " + e.getMessage());
        }
    }

    /**
     * Sends an updated list of covered component tiles to the client.
     * Constructs a socket message containing the list of tiles and sends it
     * through the established output stream to notify the client of the update.
     *
     * @param ctList the list of covered component tiles to be updated and sent to the client
     */
    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList)  {
        SocketMessage message = new SocketMessage("CoveredComponentTile", ctList, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating covered tile for client: " + e.getMessage());
        }
    }

    /**
     * Sends an update to the client regarding the specified player's shipboard state.
     *
     * @param player The name of the player whose shipboard is being updated.
     * @param shipboard The shipboard object containing the updated state to be sent to the client.
     */
    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) {
        SocketMessage message = new SocketMessage("Shipboard", shipboard, player);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating shipboard for client: " + e.getMessage());
        }
    }

    /**
     * Sends an updated representation of the flightboard to the connected client.
     * The updated data is encapsulated in a {@link SocketMessage} and transmitted via an object output stream.
     * Handles any {@code IOException} that may occur during communication.
     *
     * @param flightboard the updated flightboard object to be sent to the client
     */
    @Override
    public void showUpdateFlightboard(Flightboard flightboard){
        SocketMessage message = new SocketMessage("Flightboard", flightboard, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating flightboard for client: " + e.getMessage());
        }
    }

    /**
     * Sends an update to the client about the current adventure card being used in the game.
     * A message containing the updated adventure card is serialized and sent to the client
     * through the object output stream.
     *
     * @param adventureCard the updated adventure card to be sent to the client
     */
    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) {
        SocketMessage message = new SocketMessage("CurrCard", adventureCard, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating current card for client: " + e.getMessage());
        }
    }

    /**
     * Sends an update containing the current state of the dice to the connected client
     * using a socket communication mechanism.
     *
     * @param dices the object containing the current values or state of the dices to be sent.
     */
    @Override
    public void showUpdateDices(Dices dices)  {
        SocketMessage message = new SocketMessage("Dices", dices, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating dices for client: " + e.getMessage());
        }
    }

    /**
     * Updates the current player for the client by sending a socket message with the player information.
     *
     * @param currPlayer the username or identifier of the current player to be updated.
     */
    @Override
    public void showUpdateCurrPlayer(String currPlayer){
        SocketMessage message = new SocketMessage("CurrPlayer", null, currPlayer);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating current player for client: " + e.getMessage());
        }
    }

    /**
     * Sends an update to the client with the current game phase.
     * Constructs a SocketMessage containing the game phase information and writes it to the output stream.
     * In case of an error, logs the issue to the standard output.
     *
     * @param gamePhase the current phase of the game to be sent to the client
     */
    @Override
    public void showUpdateGamePhase(GamePhase gamePhase) {
        SocketMessage message = new SocketMessage("GamePhase", gamePhase, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating game phase for client: " + e.getMessage());
        }
    }

    /**
     * Sends an updated deck of adventure cards to the client via a socket connection.
     * This method constructs a socket message containing the provided deck of adventure cards
     * and transmits it through the object output stream to update the client's state.
     *
     * @param deck the list of {@link AdventureCard} objects representing the updated deck
     */
    @Override
    public void showUpdateDeck(List<AdventureCard> deck){
        SocketMessage message = new SocketMessage("Deck", deck, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating deck for client: " + e.getMessage());
        }
    }

    /**
     * Sends an updated state of the game to the client through a socket connection.
     *
     * @param game the current state of the game to be sent to the client
     */
    @Override
    public void showUpdateGame(Game game)  {
        SocketMessage message = new SocketMessage("Game", game, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating game for client: " + e.getMessage());
        }
    }

    /**
     * Sends a `StopHourglass` command to the client via the socket connection.
     * This method is used to notify the client to stop the hourglass timer.
     * The message is constructed using a `SocketMessage` object with the command
     * "StopHourglass" and is sent through the ObjectOutputStream.
     *
     * If an IOException occurs during the message sending process, the error is
     * caught and an error message is logged to the console.
     */
    @Override
    public void showUpdateStopHourglass() {
        SocketMessage message = new SocketMessage("StopHourglass",null, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating hourglass for client: " + e.getMessage());
        }
    }

    /**
     * Sends a message to indicate the start of the hourglass at a specific spot
     * to the connected client through the socket output stream.
     *
     * @param hourglassSpot the position or identifier of the hourglass to be started
     *                      on the client side.
     */
    @Override
    public void showUpdateStartHourglass(int hourglassSpot){
        SocketMessage message = new SocketMessage("StartHourglass",hourglassSpot, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating hourglass for client: " + e.getMessage());
        }
    }

    /**
     * Sends a lobby update to the client, including the list of players, their ready statuses,
     * and the selected game type. This method uses SocketMessage objects to encapsulate
     * the relevant data and transmits it via an ObjectOutputStream.
     *
     * @param players a list of player nicknames currently in the lobby
     * @param readyStatus a map where each key is a player's nickname and each value is a boolean
     *        indicating whether the player is ready
     * @param gameType the type of game selected, represented as a string
     */
    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType)  {
        SocketMessage message1 = new SocketMessage("LobbyUpdate", players, gameType);
        try {
            objectOutput.writeObject(message1);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating players for client: " + e.getMessage());
        }
        SocketMessage message2 = new SocketMessage("ReadyStatus", readyStatus, null);
        try {
            objectOutput.writeObject(message2);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating ready status map for client: " + e.getMessage());
        }
    }

    /**
     * Sends the result of a connection operation to the client.
     *
     * This method sends two messages to the client via the objectOutput stream.
     * The first message contains information on whether the client is a host and an associated message.
     * The second message indicates the success or failure of the connection operation.
     *
     * @param isHost a boolean indicating whether the client is acting as the host
     * @param success a boolean indicating whether the connection attempt was successful
     * @param message a string containing additional information or context about the connection result
     */
    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message)  {
        SocketMessage message1 = new SocketMessage("ConnectionResult", isHost, message);
        try {
            objectOutput.writeObject(message1);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating connection result for client: " + e.getMessage());
        }
        SocketMessage message2 = new SocketMessage("Success", success, null);
        try {
            objectOutput.writeObject(message2);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating success for client: " + e.getMessage());
        }
    }

    /**
     * Sends the result of a nickname validation to the client.
     * This method constructs a {@code SocketMessage} containing the validation result
     * and attempts to send it over the established object output stream.
     *
     * @param valid indicates whether the nickname validation was successful or not
     * @param payload a string containing additional information about the validation result
     */
    @Override
    public void showNicknameResult(boolean valid, String payload) {
        SocketMessage message = new SocketMessage(payload, valid, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error handling availability error: " + e.getMessage());
        }
    }

    /**
     * Sends a "GameStarted" message to the connected client through a socket output stream.
     *
     * This method is responsible for notifying the client that the game has started by
     * creating a new instance of {@link SocketMessage} with the "GameStarted" command.
     * It then writes this message object to the socket output stream and flushes it,
     * ensuring the message is sent promptly.
     *
     * If an {@link IOException} occurs during the operation, it logs an error message
     * to the console detailing the issue.
     */
    @Override
    public void showGameStarted(){
        SocketMessage message = new SocketMessage("GameStarted", null, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating game started for client: " + e.getMessage());
        }
    }

    /**
     * Sends a notification to the client indicating that a new player has joined the game.
     *
     * @param player the name of the player who has joined the game
     */
    @Override
    public void showPlayerJoined(String player){
        SocketMessage message = new SocketMessage("PlayerJoined", null, player);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating player joined for client: " + e.getMessage());
        }
    }

    /**
     * Sends a message to update the leaderboard on the client's side.
     * The leaderboard data is encapsulated in a {@link SocketMessage} object
     * and transmitted through the associated output stream.
     *
     * @param leaderboard a map containing the leaderboard data, where the keys are player names
     *                    and the values are their respective scores.
     * @throws RemoteException if a remote communication error occurs during the operation.
     */
    @Override
    public void showUpdateLeaderboard(Map<String, Integer> leaderboard) throws RemoteException {
        SocketMessage message = new SocketMessage("leaderboard", leaderboard, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating leaderboard for client: " + e.getMessage());
        }
    }

    /**
     * Terminates the interaction with the socket client by sending a wait result message.
     * This method overrides the terminate behavior and triggers the communication
     * of the termination state to the client.
     */
    @Override
    public void terminate(){
        showWaitResult();
    }

    /**
     * Sends a "waitResult" command message to the associated output stream,
     * likely to indicate that the server or client should wait for further results
     * or operations to complete.
     *
     * This method constructs a `SocketMessage` object with the "waitResult" command
     * and no additional parameters, then attempts to write and flush this message
     * to the `objectOutput` stream.
     *
     * If an `IOException` occurs during the process, an error message is printed
     * to the console to describe the failure.
     *
     * Potential use cases include notifying connected clients or servers to pause their
     * actions and wait for a subsequent update or result.
     *
     * Exception Handling:
     * - Captures `IOException` if a failure occurs during the attempt to write or flush
     *   the message to the stream.
     *
     * Side Effects:
     * - Sends a serialized `SocketMessage` instance via the `objectOutput` stream.
     * - Outputs an error message to the console in case of failure.
     */
    private void showWaitResult() {
        SocketMessage message = new SocketMessage("waitResult", null, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error sending wait result from server: " + e.getMessage());
        }
    }

    /**
     * Activates or configures "god mode" for a specific player in the game based on the given configuration.
     *
     * @param playerName the name of the player for whom the "god mode" is being activated or configured
     * @param conf the configuration or parameters for enabling "god mode" for the player
     */
    private void godMode(String playerName, String conf){
        this.controller.godMode(playerName, conf);
    }


    /**
     * Sets the specified player as ready and updates all game lobbies.
     *
     * @param playerName the name of the player to be marked as ready
     */
    private void setPlayerReady(String playerName) {
        this.controller.setPlayerReady(playerName);
        this.controller.updateAllLobbies();
    }


    /**
     * Marks the specified player as not ready and updates all associated game lobbies.
     *
     * @param playerName the name of the player to mark as not ready
     */
    private void setPlayerNotReady(String playerName){
        this.controller.setPlayerNotReady(playerName);
        this.controller.updateAllLobbies();
    }


    /**
     * Starts the game in the lobby, ensuring only the host can initiate the process
     * and all players are marked as ready before proceeding.
     *
     * @param playerName the name of the player attempting to start the game
     *                   (must match the lobby creator's name to succeed)
     */
    public void startGameByHost(String playerName) {
        if(!playerName.equals(this.controller.getLobbyCreator())) {
            showConnectionResult(false, false, "Only the host can start the game");
        }
        else {
            Map<String, Boolean> readyStatus = this.controller.getReadyStatus();
            List<String> unreadyPlayers = new ArrayList<>();

            for (Map.Entry<String, Boolean> entry : readyStatus.entrySet()) {
                if (!entry.getValue()) {
                    unreadyPlayers.add(entry.getKey());
                }
            }

            if (!unreadyPlayers.isEmpty()) {
                // Some players are not ready
                String message = "Cannot start game: " + String.join(", ", unreadyPlayers) + " not ready";
                showConnectionResult(true, false, message);
            }
            else {
                boolean result = this.controller.startGameByHost(playerName);
                if(result) {
                    this.controller.updateAllGameStarted();
                }
                else {
                    System.out.println("Error starting game");
                }
            }
        }
    }


    /**
     * Sets the game type for the current session and updates relevant states.
     * This method sets the specified game type in the {@code GameController}, updates all lobbies
     * to reflect the new game type, and marks the game as started.
     *
     * @param gameType the type of game to be set (e.g. "Adventure", "Strategy"). It must be a non-null string representing the desired game format.
     */
    public void setGameType(String gameType) {
        this.controller.setGameType(gameType);
        this.controller.updateAllLobbies();
        this.controller.setStarted(true);
    }


    /**
     * Allows a player to pick a covered tile during the game. The request is forwarded
     * to the game controller for processing, and it is synchronized to ensure thread safety.
     *
     * @param playerName the name of the player attempting to pick a covered tile
     */
    public void pickCoveredTile(String playerName){
        this.controller.pickCoveredTile(playerName);
    }


    /**
     * Requests the server to allow the specified player to pick an uncovered tile
     * identified by its image name. The action is forwarded to the game controller.
     *
     * @param playerName the name of the player attempting to pick the uncovered tile
     * @param pngName the name of the PNG image representing the tile to be picked
     */
    public void pickUncoveredTile(String playerName, String pngName)  {
        this.controller.pickUncoveredTile(playerName, pngName);
    }


    /**
     * Rotates a component tile and welds it to the specified position on the game board.
     * The tile can be rotated a specified number of times in a clockwise or
     * counterclockwise direction before being welded.
     *
     * @param playerName the name of the player performing the action
     * @param i the row index of the target position on the game board
     * @param j the column index of the target position on the game board
     * @param numOfRotation the number of rotations to apply to the component tile;
     *                      positive values indicate clockwise rotation,
     *                      and negative values indicate counterclockwise rotation
     */
    public void weldComponentTile(String playerName, int i, int j, int numOfRotation) {
        if(numOfRotation > 0){
            this.controller.rotateClockwise(playerName, numOfRotation);
        }
        else {
            this.controller.rotateCounterClockwise(playerName, -numOfRotation);
        }
        this.controller.weldComponentTile(playerName, i, j);
    }


    /**
     * Moves a component tile into a standby state for the specified player.
     *
     * @param playerName the name of the player for whom the component tile is placed in standby.
     */
    private void standbyComponentTile(String playerName) {
        this.controller.standbyComponentTile(playerName);
    }


    /**
     * Allows the specified player to select a standby component tile at the specified index.
     *
     * @param playerName the name of the player attempting to pick a standby component tile
     * @param index the index of the standby component tile to be picked
     */
    private void pickStandbyComponentTile(String playerName, int index) {
        this.controller.pickStandByComponentTile(playerName, index);
    }


    /**
     * Discards a component tile associated with the specified player.
     * This method delegates the discard operation to the game controller instance.
     *
     * @param playerName the name of the player who is discarding the component tile
     */
    private void discardComponentTile(String playerName) {
        this.controller.discardComponentTile(playerName);
    }


    /**
     * Completes the building process for the specified player.
     *
     * @param playerName the name of the player who has finished building
     */
    private void finishBuilding(String playerName) {
        this.controller.finishBuilding(playerName);
    }


    /**
     * Completes the building phase for a specific player and updates the game state accordingly.
     *
     * @param playerName the name of the player who is completing the building phase
     * @param index the position or specific reference related to the player's building action
     */
    private void finishBuilding(String playerName, int index) {
        this.controller.finishBuilding(playerName, index);
    }


    /**
     * Notifies that all shipboards have been finalized during the game phase.
     * This method delegates the operation to the associated controller.
     */
    private void finishedAllShipboards()  {
        this.controller.finishedAllShipboards();
    }


    /**
     * Flips the hourglass in the game via the controller.
     * This method delegates the hourglass flipping action to the `GameController` instance.
     * The hourglass-flipping operation is typically used to track or toggle timing mechanisms in the game state.
     */
    private void flipHourglass(){
        this.controller.flipHourglass();
    }


    /**
     * Triggers the card-picking operation by delegating the request to the `controller` instance.
     * This method encapsulates a game-related operation to ensure proper handling by the controller.
     */
    private void pickCard() {
        this.controller.pickCard();
    }


    /**
     * Activates a card in the game based on the specified input command.
     * It delegations the activation process to the controller which performs
     * the actual operations on the card.
     *
     * @param inputCommand the command structure containing the details necessary
     *                     for the card activation, including user choices, indexes,
     *                     and relevant command flags or parameters.
     */
    private void activateCard(InputCommand inputCommand) {
        this.controller.activateCard(inputCommand);
    }


    /**
     * Removes a player from the game and updates all active lobbies accordingly.
     *
     * @param playerName the name of the player to be removed
     */
    private void removePlayer(String playerName) {
        this.controller.removePlayer(playerName);
        this.controller.updateAllLobbies();
    }


    /**
     * Handles the scenario where a player decides to abandon the game.
     *
     * @param playerName the name of the player who is abandoning the game
     */
    private void playerAbandons(String playerName) {
        this.controller.playerAbandons(playerName);
    }


    /**
     * Destroys a component tile at the specified location for a given player.
     *
     * @param playerName the name of the player initiating the destruction of the tile
     * @param i the row index of the tile to be destroyed
     * @param j the column index of the tile to be destroyed
     */
    private void destroyComponentTile(String playerName, int i, int j)  {
        this.controller.destroyTile(playerName, i, j);
    }


    /**
     * Terminates the game by delegating the end game process to the controller.
     * This method ensures that the current game session is concluded.
     */
    private void endGame() {
        this.controller.endGame();
    }


    /**
     * Places a brown alien on the game board for the specified player.
     *
     * @param playerName the name of the player attempting to place the brown alien
     * @param i the row index on the game board where the brown alien is to be placed
     * @param j the column index on the game board where the brown alien is to be placed
     */
    private void placeBrownAlien(String playerName, int i, int j) {
        this.controller.placeBrownAlien(playerName, i, j);
    }


    /**
     * Places astronauts on the specified coordinates for the player.
     *
     * @param playerName the name of the player placing the astronauts
     * @param i the row index where the astronauts should be placed
     * @param j the column index where the astronauts should be placed
     */
    private void placeAstronauts(String playerName, int i, int j) {
        this.controller.placeAstronauts(playerName, i, j);
    }


    /**
     * Places a purple alien on the specified coordinates on the game board for the given player.
     *
     * @param playerName the name of the player attempting to place the purple alien
     * @param i the row index on the game board where the purple alien will be placed
     * @param j the column index on the game board where the purple alien will be placed
     */
    private void placePurpleAlien(String playerName, int i, int j) {
        this.controller.placePurpleAlien(playerName, i, j);
    }

    //public void showMessageToEveryone(String mess) {
    //    SocketMessage message = new SocketMessage("MessageToEveryone", null, mess);
    //    try {
    //        objectOutput.writeObject(message);
    //        objectOutput.flush();
    //    } catch (IOException e) {
    //        System.out.println("Error updating message to everyone for client: " + e.getMessage());
    //    }
    //}

    /**
     * Returns the ObjectInputStream used for receiving serialized objects from the associated socket connection.
     *
     * @return the ObjectInputStream associated with the socket connection
     */
    public ObjectInputStream getObjectInput() {
        return objectInput;
    }

    /**
     * Retrieves the ObjectOutputStream associated with this instance.
     *
     * @return The ObjectOutputStream used for sending data.
     */
    public ObjectOutputStream getObjectOutput() {
        return objectOutput;
    }

    /**
     * Retrieves the HeartbeatManager associated with this instance.
     *
     * @return the current instance of HeartbeatManager used for managing heartbeat signals.
     */
    public HeartbeatManager getHeartbeatManager() {
        return heartbeatManager;
    }
    /**
     * Handles the process of welding a component tile based on the information
     * provided in the given socket message.
     *
     * @param msg the socket message containing the necessary data for the welding operation,
     *            including a command object and a payload. The command object is expected
     *            to be of type {@code InputCommand}, from which the row, column, and index
     *            information are extracted to execute the welding process.
     */
    private void handleWeldComponentTile(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        weldComponentTile(msg.getPayload(), cmd.getRow(), cmd.getCol(), cmd.getIndexChosen());
    }

    /**
     * Handles the selection of a standby component tile based on the provided socket message.
     *
     * @param msg The socket message containing the input command and payload necessary for picking a standby component tile.
     */
    private void handlePickStandbyComponentTile(SocketMessage msg) {
        int indexChosen = ((InputCommand) msg.getObject()).getIndexChosen();
        pickStandbyComponentTile(msg.getPayload(), indexChosen);
    }

    /**
     * Handles the completion of building process by extracting the required data from the provided message
     * and delegating the task to the finishBuilding method.
     *
     * @param msg the socket message containing the payload and input command data required for processing
     */
    private void handleFinishBuilding2(SocketMessage msg) {
        int indexChosen = ((InputCommand) msg.getObject()).getIndexChosen();
        finishBuilding(msg.getPayload(), indexChosen);
    }

    /**
     * Handles the destruction of a tile component based on the provided socket message.
     *
     * @param msg the socket message containing the necessary data to identify and destroy the tile;
     *            it includes the payload and command specifying the tile's row and column.
     */
    private void handleDestroyTile(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        destroyComponentTile(msg.getPayload(), cmd.getRow(), cmd.getCol());
    }

    /**
     * Handles the setting of the number of players in the game by retrieving the necessary
     * information from the provided socket message and updating the game controller.
     *
     * @param msg the SocketMessage containing the input command with the number of players to set.
     */
    private void handleSetNumPlayers(SocketMessage msg) {
        int numPlayers = ((InputCommand) msg.getObject()).getIndexChosen();
        controller.setNumPlayers(numPlayers);
    }

    /**
     * Handles the quit action for the client identified in the given {@code SocketMessage}.
     * This method performs the necessary steps to process a client's quit request,
     * including updating the game state and unregistering the client from the heartbeat manager.
     *
     * @param msg the {@code SocketMessage} containing the payload of the client that needs to quit.
     *            The payload should contain the identifier of the client requesting to quit.
     */
    private void handleQuit(SocketMessage msg) {
        controller.quit(msg.getPayload());
        heartbeatManager.unregisterClient(msg.getPayload());
    }

    /**
     * Handles the "God Mode" feature by invoking the appropriate method with the
     * necessary parameters extracted from the provided socket message.
     *
     * @param msg the socket message containing the payload and object required
     *            to enable or configure the "God Mode" functionality.
     */
    private void handleGodMode(SocketMessage msg) {
        godMode(msg.getPayload(), (String) msg.getObject());
    }

    /**
     * Handles the "Place Brown Alien" action by extracting details from the provided
     * {@code SocketMessage} and delegating the action to the appropriate method.
     *
     * @param msg the {@code SocketMessage} containing the payload and the command object.
     *            The message's payload specifies the player's name, while the command object
     *            contains the row and column coordinates where the brown alien should be placed.
     */
    private void handlePlaceBrownAlien(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        placeBrownAlien(msg.getPayload(), cmd.getRow(), cmd.getCol());
    }

    /**
     * Handles the server-side logic for processing a "place purple alien" request
     * received through a socket message. Extracts data from the provided socket
     * message and invokes the appropriate method to place a purple alien at the
     * specified location.
     *
     * @param msg the received socket message containing the necessary data to place
     *            the purple alien. The payload of the message contains additional
     *            details, while the object encapsulates an InputCommand with row
     *            and column coordinates.
     */
    private void handlePlacePurpleAlien(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        placePurpleAlien(msg.getPayload(), cmd.getRow(), cmd.getCol());
    }

    /**
     * Handles the placement of astronauts based on the received message and command.
     *
     * @param msg the socket message containing the necessary information for placing astronauts
     */
    private void handlePlaceAstronauts(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        placeAstronauts(msg.getPayload(), cmd.getRow(), cmd.getCol());
    }

    /**
     * Handles the connection tester by processing the provided socket message,
     * extracting necessary information, and triggering an update test display.
     *
     * @param msg the socket message containing payload and object data. The message
     *            should include an instance of InputCommand from which the index is retrieved.
     */
    private void handleConnectionTester(SocketMessage msg) {
        System.out.println(msg.getPayload());
        System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
        showUpdateTest();
    }

    /**
     * Handles the check availability process for a socket message.
     * This method verifies the current state of the controller,
     * processes the incoming player's request to join, manages the heartbeat
     * registration, and updates other players and lobby details accordingly.
     *
     * @param msg the socket message containing the payload and information required for the process
     */
    private void handleCheckAvailability(SocketMessage msg) {
        if (controller.isStarted() || controller.getPlayers().isEmpty()) {
            int res = controller.addPlayer(msg.getPayload());
            if (res == 1) {
                heartbeatManager.registerClient(msg.getPayload());
                server.addHandlerToClients(this, thisThread);
                showNicknameResult(true, "PlayerAdded");

                boolean isHost = controller.getPlayers().size() == 1;
                showConnectionResult(isHost, true, isHost ? "You are the host of the lobby" : "You joined an existing lobby");

                if (!isHost) controller.updateAllPlayerJoined(msg.getPayload());
                controller.updateAllLobbies();
            } else if (res == -2) {
                showNicknameResult(false, "PlayerAlreadyInLobby");
            } else if (res == -1) {
                showNicknameResult(false, "LobbyFullOrOutsideLobbyState");
            }
            System.out.println("List of players updated: " + controller.getPlayers());
        } else {
            terminate();
        }
    }
}
