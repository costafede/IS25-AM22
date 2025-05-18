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
    public SocketServerHandler(OutputStream os) throws IOException {
        this.objectOutput = new ObjectOutputStream(os);
    }

    public void checkAvailability(String nickname) {
        SocketMessage msg = new SocketMessage("checkAvailability", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in checkAvailability: " + e.getMessage());
        }
    }

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

    @Override
    public void connect(VirtualView client, String nickname){
        //Used only for RMI
    }

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

    public String getVirtualServerType() {
        return "socket";
    }

    /**
     * Establishes a connection to the server using a Text-Based User Interface (TUI).
     * This method handles user input for selecting a nickname and interacts with the server
     * to join the lobby, ensuring the connection is properly initiated.
     *
     * @param args an array of strings containing the host address and port number as the first and second elements respectively
     * @param clientModel the client-side representation of the game model
     * @param scanner a Scanner object for reading user input
     * @param enhancedClientView the enhanced view object used for display purposes
     * @return a SocketServerHandler object that facilitates communication with the server, or null if the connection fails
     * @throws InterruptedException if the thread is interrupted while waiting
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
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
     * Establishes a connection to the server using a Graphical User Interface (GUI) and performs
     * the necessary interactions to join the lobby or handle connection issues.
     * This method ensures proper communication setup between the client and server.
     *
     * @param host the server's hostname or IP address
     * @param port the port number on which the server is listening
     * @param name the player's chosen nickname
     * @param clientModel the client-side representation of the game model
     * @param view the GUI object used for display and user interaction
     * @return a SocketServerHandler object that facilitates communication with the server,
     *         or null if the connection fails
     * @throws InterruptedException if the thread is interrupted while waiting
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
