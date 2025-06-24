package it.polimi.ingsw.is25am22new.Network.Socket.Client;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.EnhancedClientView;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * SocketServerHandler is a class responsible for handling socket-based server communication
 * in a multiplayer game environment. It serves as an implementation of the VirtualServer
 * interface, facilitating client-server interactions by managing player states, game actions,
 * and client connections.
 *
 * This class handles various game-related operations such as setting player readiness,
 * starting a game, picking and discarding components, manipulating tiles on the game board,
 * flipping hourglasses, activating cards, and more. It also manages the connection state of
 * players and the communication between the client and server via sockets.
 */
public class SocketServerHandler implements VirtualServer {
    final ObjectOutputStream objectOutput;


    /**
     * Constructs a new instance of the SocketServerHandler class.
     * This constructor initializes an ObjectOutputStream using the provided OutputStream.
     *
     * @param os The OutputStream used to create the ObjectOutputStream for handling socket communication.
     * @throws IOException If an I/O error occurs while creating the ObjectOutputStream.
     */
    public SocketServerHandler(OutputStream os) throws IOException {
        this.objectOutput = new ObjectOutputStream(os);
    }

    /**
     * Sends a request to check the availability of the specified nickname.
     * This method constructs a `SocketMessage` object with the "checkAvailability" command and
     * sends it to the output stream using the `objectOutput`.
     *
     * @param nickname the nickname to be checked for availability
     */
    public void checkAvailability(String nickname) {
        SocketMessage msg = new SocketMessage("checkAvailability", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in checkAvailability: " + e.getMessage());
        }
    }

    /**
     * Removes a player identified by their nickname and sends a corresponding message through the socket connection.
     *
     * @param nickname the nickname of the player to be removed. Must not be null or empty.
     */
    @Override
    public void removePlayer(String nickname) {
        SocketMessage msg = new SocketMessage("removePlayer", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in removePlayer: " + e.getMessage());
        }
    }

    /**
     * Sets the specified player as ready by sending a socket message with
     * the player's nickname to the output stream.
     *
     * @param nickname the nickname of the player to be marked as ready
     */
    @Override
    public void setPlayerReady(String nickname)  {
        SocketMessage msg = new SocketMessage("setPlayerReady", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setPlayerReady: " + e.getMessage());
        }
    }

    /**
     * Sends a request to start the game initiated by the host to the connected clients.
     * The method creates a SocketMessage containing the command "startGameByHost" and the host's nickname,
     * which is then sent over the established connection.
     *
     * @param nickname the nickname of the host who requests to start the game
     */
    @Override
    public void startGameByHost(String nickname){
        SocketMessage msg = new SocketMessage("startGameByHost", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in startGameByHost: " + e.getMessage());
        }
    }

    /**
     * Sends a message to set a player as not ready based on their nickname.
     * The method constructs a {@code SocketMessage} with the command "setPlayerNotReady"
     * and sends it through the {@code objectOutput} stream.
     *
     * @param nickname the nickname of the player to be marked as not ready
     */
    @Override
    public void setPlayerNotReady(String nickname) {
        SocketMessage msg = new SocketMessage("setPlayerNotReady", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setPlayerNotReady: " + e.getMessage());
        }
    }

    /**
     * Sets the game type for the current session by sending a message
     * to the server. This method constructs a `SocketMessage` object
     * with the provided game type and transmits it through the socket's
     * output stream.
     *
     * @param gameType the type of the game to be set, represented as a String.
     */
    @Override
    public void setGameType(String gameType) {
        SocketMessage msg = new SocketMessage("setGameType", null, gameType);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setGameType: " + e.getMessage());
        }
    }

    /**
     * Sends a "godMode" command to the server for the player associated with the specified nickname.
     * This method constructs a {@link SocketMessage} with the command "godMode",
     * the provided configuration string, and the player's nickname,
     * then sends the message through the {@code objectOutput} stream.
     *
     * @param nickname the nickname of the player for whom the "godMode" command is being executed
     * @param conf the configuration or additional data related to the "godMode" command
     */
    @Override
    public void godMode(String nickname, String conf) {
        SocketMessage msg = new SocketMessage("godMode", conf, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending godMode: " + e.getMessage());
        }
    }

    /**
     * Sends a request to pick a covered tile for a player with the specified nickname.
     * The message is encapsulated in a {@link SocketMessage} and sent through the socket output stream.
     *
     * @param nickname the nickname of the player who is requesting to pick a covered tile
     */
    @Override
    public void pickCoveredTile(String nickname)  {
        SocketMessage msg = new SocketMessage("pickCoveredTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickCoveredTile: " + e.getMessage());
        }
    }

    /**
     * Sends a request to pick an uncovered tile on the client's side by transmitting a
     * socket message with the provided player nickname and tile image name.
     *
     * @param nickname the nickname of the player making the request
     * @param pngName the name of the PNG file representing the tile being picked
     */
    @Override
    public void pickUncoveredTile(String nickname, String pngName) {
        SocketMessage msg = new SocketMessage("pickUncoveredTile", pngName, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickUncoveredTile: " + e.getMessage());
        }
    }

    /**
     * Sends a command to weld a component tile at a specified position on a game board,
     * with a specified number of rotations. The method prepares the input command,
     * wraps it in a socket message, and transmits it to the server for processing.
     *
     * @param nickname the nickname of the player performing the action
     * @param i the row position of the component tile to be welded
     * @param j the column position of the component tile to be welded
     * @param numOfRotations the number of clockwise rotations to apply to the component tile before welding
     */
    @Override
    public void weldComponentTile(String nickname, int i, int j, int numOfRotations)  {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        inputCommand.setIndexChosen(numOfRotations);
        SocketMessage msg = new SocketMessage("weldComponentTile", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in weldComponentTile: " + e.getMessage());
        }
    }

    /**
     * Sends a command to set the state of a component tile to standby using the specified nickname.
     * This method encapsulates the command, along with the nickname, in a {@code SocketMessage}
     * and writes it to the output stream for communication.
     *
     * @param nickname the nickname of the player or entity for which the component tile
     *                 should be set to standby
     */
    @Override
    public void standbyComponentTile(String nickname) {
        SocketMessage msg = new SocketMessage("standbyComponentTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in standbyComponentTile: " + e.getMessage());
        }
    }

    /**
     * Sends a request to select a specific standby component tile for a player.
     * This method constructs and sends a SocketMessage containing the necessary
     * information about the player's nickname and the index of the tile to be picked.
     * The message is then transmitted via the object output stream.
     *
     * @param nickname the nickname of the player making the request
     * @param index the index of the standby component tile to be picked
     */
    @Override
    public void pickStandbyComponentTile(String nickname, int index)  {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(index);
        SocketMessage msg = new SocketMessage("pickStandByComponentTile", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickStandbyComponentTile: " + e.getMessage());
        }
    }

    /**
     * Sends a request to discard a component tile associated with the specified user nickname.
     * This method creates and sends a SocketMessage with the "discardComponentTile" command
     * to the corresponding object output stream.
     *
     * @param nickname the nickname of the player who intends to discard a component tile
     */
    @Override
    public void discardComponentTile(String nickname) {
        SocketMessage msg = new SocketMessage("discardComponentTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in discardComponentTile: " + e.getMessage());
        }
    }

    /**
     * Sends a socket message indicating that the building phase has been completed
     * for a player identified by their nickname.
     *
     * @param nickname the nickname of the player who has finished building
     */
    @Override
    public void finishBuilding(String nickname) {
        SocketMessage msg = new SocketMessage("finishBuilding1", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in finishBuilding: " + e.getMessage());
        }
    }

    /**
     * Sends a message indicating the completion of a building action, along with
     * relevant details such as the player's nickname and the index of the building.
     * The method serializes the message and transmits it via the socket connection.
     *
     * @param nickname the nickname of the player who has finished the building action
     * @param index the index representing the specific building action completed
     */
    @Override
    public void finishBuilding(String nickname, int index) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(index);
        SocketMessage msg = new SocketMessage("finishBuilding2", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in finishBuilding: " + e.getMessage());
        }
    }

    /**
     * Notifies that all shipboards have been completed and sends the relevant notification
     * via a socket connection to the connected client(s).
     *
     * This method creates a new `SocketMessage` instance with the command "finishedAllShipboards"
     * and sends it through the `objectOutput` stream. If an IOException occurs during
     * the communication process, an error message is printed to the standard output.
     */
    @Override
    public void finishedAllShipboards() {
        SocketMessage msg = new SocketMessage("finishedAllShipboards", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in finishedAllShipboards: " + e.getMessage());
        }
    }

    /**
     * Sends a command to flip the hourglass in a connected socket-based system.
     * This method creates a `SocketMessage` object with the "flipHourglass" command and sends it
     * through the `objectOutput` stream to notify the remote system.
     *
     * In case of an I/O error, the method catches and logs the exception.
     *
     * The method is overridden to provide specific functionality for handling
     * the "flipHourglass" operation within the context of the implemented interface or superclass.
     */
    @Override
    public void flipHourglass()  {
        SocketMessage msg = new SocketMessage("flipHourglass", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in flipHourglass: " + e.getMessage());
        }
    }

    /**
     * Sends a "pickCard" command to the server through the socket connection.
     * This operation encapsulates the command into a SocketMessage object and
     * submits it using the objectOutput stream.
     *
     * If any I/O error occurs during the operation, it catches the exception
     * and prints an error message to the console.
     */
    @Override
    public void pickCard()  {
        SocketMessage msg = new SocketMessage("pickCard", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickCard: " + e.getMessage());
        }
    }

    /**
     * Sends a command to activate a card through the socket connection.
     *
     * @param inputCommand an instance of {@code InputCommand} containing data related to the card activation.
     */
    @Override
    public void activateCard(InputCommand inputCommand)  {
        SocketMessage msg = new SocketMessage("activateCard", inputCommand, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in activateCard: " + e.getMessage());
        }
    }

    /**
     * Sends a notification to indicate that a player has abandoned the game.
     * This method constructs a socket message with the player's nickname and sends
     * it to the connected client via the output stream.
     *
     * @param nickname the nickname of the player who has abandoned the game
     */
    @Override
    public void playerAbandons(String nickname) {
        SocketMessage msg = new SocketMessage("playerAbandons", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in playerAbandons: " + e.getMessage());
        }
    }

    /**
     * Sends a request to destroy a tile at the specified location in the game, associated
     * with the given player's nickname. Creates and sends a message containing the player's
     * nickname, the specified tile location (row and column), and the action type "destroyTile".
     *
     * @param nickname the nickname of the player initiating the tile destruction
     * @param i the row index of the tile to destroy
     * @param j the column index of the tile to destroy
     */
    @Override
    public void destroyComponentTile(String nickname, int i, int j)  {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("destroyTile", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in destroyComponentTile: " + e.getMessage());
        }
    }

    /**
     * Signals the end of the game by sending an appropriate message to the connected clients.
     * Constructs a {@code SocketMessage} with the "endGame" command and sends it over the object output stream.
     * Handles potential {@code IOException} that may occur during the operation.
     */
    @Override
    public void endGame() {
        SocketMessage msg = new SocketMessage("endGame", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in endGame: " + e.getMessage());
        }
    }

    /**
     * Sends a request to place a brown alien on the game board at the specified location
     * for the given player. Communicates this action through an output stream.
     *
     * @param playerName the name of the player executing the action
     * @param i the row index of the location where the brown alien will be placed
     * @param j the column index of the location where the brown alien will be placed
     */
    @Override
    public void placeBrownAlien(String playerName, int i, int j) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("placeBrownAlien", inputCommand, playerName);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending placeBrownAlien: " + e.getMessage());
        }
    }

    /**
     * Sends a command to place astronauts on a specific position in the game board.
     *
     * @param playerName the name of the player placing the astronauts
     * @param i the row index where the astronauts should be placed
     * @param j the column index where the astronauts should be placed
     */
    @Override
    public void placeAstronauts(String playerName, int i, int j) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("placeAstronauts", inputCommand, playerName);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending placeAstronauts: " + e.getMessage());
        }
    }

    /**
     * Sends a message to place a purple alien at the specified coordinates for a given player.
     *
     * @param playerName the name of the player who is placing the purple alien
     * @param i the row index where the purple alien should be placed
     * @param j the column index where the purple alien should be placed
     */
    @Override
    public void placePurpleAlien(String playerName, int i, int j){
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("placePurpleAlien", inputCommand, playerName);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending placePurpleAlien: " + e.getMessage());
        }
    }

    /**
     * Sends a heartbeat signal to the server for a specific player.
     * This method creates a heartbeat message and writes it to the output stream.
     * If an error occurs during the process, it prints an error message to the console.
     *
     * @param playerName the name of the player for whom the heartbeat signal is being sent
     */
    @Override
    public void heartbeat(String playerName) {
        SocketMessage msg = new SocketMessage("heartbeat", null, playerName);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending heartbeat: " + e.getMessage());
        }
    }

    /**
     * Establishes a connection between the server and the client.
     * This method is primarily used for RMI (Remote Method Invocation).
     *
     * @param client   The client's virtual view instance, which enables server-client communication.
     * @param nickname The nickname of the client trying to establish a connection.
     */
    @Override
    public void connect(VirtualView client, String nickname){
        //Used only for RMI
    }

    /**
     * Disconnects the current connection by sending a "disconnect"
     * message through the output stream. This method communicates
     * the intent to terminate the connection to the other end of the socket.
     *
     * If an I/O error occurs while attempting to send the disconnect message,
     * the error is logged to the console with a descriptive message.
     */
    @Override
    public void disconnect() {
        SocketMessage msg = new SocketMessage("disconnect", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending disconnect: " + e.getMessage());
        }
    }

    /**
     * Sets the number of players for the game and sends the corresponding command
     * to the server through a socket connection.
     *
     * @param numPlayers the number of players to set in the game
     */
    @Override
    public void setNumPlayers(int numPlayers) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(numPlayers);
        SocketMessage msg = new SocketMessage("setNumPlayers", inputCommand, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setNumPlayers: " + e.getMessage());
        }
    }

    /**
     * Sends a connection test message using the provided parameters to verify or test the
     * functionality of the server-client connection.
     *
     * @param a a string parameter used as a payload in the connection test message
     * @param b an integer parameter representing an index that is set into the input command for testing
     */
    public void connectionTester(String a, int b) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(b);
        SocketMessage msg = new SocketMessage("connectionTester", inputCommand, a);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in connectionTester: " + e.getMessage());
        }
    }

    /**
     * Sends a "quit" command for a user identified by the provided nickname.
     * This method communicates the user's intent to disconnect or leave
     * the current session by using a `SocketMessage` object.
     *
     * @param nickname the nickname of the user who wishes to quit the session
     */
    @Override
    public void quit(String nickname) {
        try {
            SocketMessage msg = new SocketMessage("quit", null, nickname);
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Connection closed");
        }
    }

    /**
     * Retrieves the type of the virtual server.
     *
     * @return A string indicating the type of the virtual server, specifically "socket".
     */
    public String getVirtualServerType() {
        return "socket";
    }

    /**
     * Establishes a connection to the server using the provided arguments and initializes
     * various components for the client-side of the application. This method facilitates
     * user interaction via the terminal user interface (TUI) and allows the user to join
     * the server lobby.
     *
     * @param args The command-line arguments, where the first element should be the server
     *             host and the second element should be the server port.
     * @param clientModel The data model representing the client's state.
     * @param scanner An instance of Scanner to handle user input via the terminal.
     * @param enhancedClientView The view component responsible for rendering enhanced client-side
     *                           interactions.
     * @return A SocketServerHandler representing the connection to the server, or null if
     *         the connection fails.
     * @throws InterruptedException If the current thread is interrupted while waiting.
     * @throws ClassNotFoundException If a serialized class cannot be found during object deserialization.
     */
    public static SocketServerHandler connectToServerTUI(String[] args, ClientModel clientModel, Scanner scanner, EnhancedClientView enhancedClientView) throws InterruptedException, ClassNotFoundException {
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
            SocketClientSide newSocket = new SocketClientSide(socket, objectInput, output, thisPlayerName, clientModel, enhancedClientView);
            newSocket.run(scanner);
            return newSocket.getServerHandler();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
        return null;
    }

    /**
     * Establishes a connection to the server using the provided parameters and integrates it with the GUI.
     * The method attempts to connect to the server, verify the availability of the player's nickname,
     * and handles server responses for different scenarios. If successful, sets up a new client-side socket
     * and starts interaction with the server.
     *
     * @param host The hostname or IP address of the server to connect to.
     * @param port The port number on the server to connect to.
     * @param name The nickname of the player to be used in the game.
     * @param clientModel A reference to the client model which contains the game's state and logic.
     * @param view A reference to the GUI for the client to display and manage interaction messages.
     * @return A SocketServerHandler object to facilitate server communication if the connection is successful; otherwise null.
     * @throws InterruptedException If the thread executing the connection is interrupted during the process.
     */
    public static SocketServerHandler connectToServerGUI(String host, int port, String name, ClientModel clientModel, GalaxyTruckerGUI view) throws InterruptedException {
        try {
            Socket socket = new Socket(host, port);
            SocketServerHandler output = new SocketServerHandler(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            String thisPlayerName = "Player";

            thisPlayerName = name;
            output.checkAvailability(thisPlayerName);
            SocketMessage msg = null;
            if((msg = (SocketMessage) objectInput.readObject()) != null) {
                switch (msg.getCommand()) {
                    case "LobbyFullOrOutsideLobbyState" -> view.displayNicknameResult(false, "Lobby is full or game is already started");
                    case "PlayerAlreadyInLobby" -> view.displayNicknameResult(false, "Player already in lobby");
                    case "PlayerAdded" -> {
                        view.displayNicknameResult(true, "You've successfully joined the lobby!");
                        SocketClientSide newSocket = new SocketClientSide(socket, objectInput, output, thisPlayerName, clientModel, view);
                        newSocket.run();
                        return newSocket.getServerHandler();
                    }
                    default -> view.displayNicknameResult(false, "Host is configuring the lobby...please retry");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
        return null;
    }
}
