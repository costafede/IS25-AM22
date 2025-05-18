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

    @Override
    public void heartbeat(String nickname) {
        System.out.println("Received heartbeat from: " + nickname);
        heartbeatManager.heartbeat(nickname);
    }

    @Override
    public void quit(String playerName) {
        gameController.quit(playerName);
        heartbeatManager.unregisterClient(playerName);
    }

    @Override
    public String getVirtualServerType() throws RemoteException{
        return "rmi";
    }

    /**
     * Handles the disconnection of a client due to a heartbeat timeout.
     * Removes the client from the connectedClients list and clientMap.
     * Shuts down the server if an error occurs during the process.
     *
     * @param nickname the nickname of the client that has disconnected due to a heartbeat timeout
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
     * Connects a client to the game server. Validates the provided nickname and manages
     * the client's addition to the game lobby, ensuring the lobby's constraints are met.
     * Provides feedback to the client regarding the success of the connection attempt.
     *
     * @param client the VirtualView instance representing the client attempting to connect
     * @param nickname the nickname selected by the client for the connection
     * @throws RemoteException if a networking-related error occurs during interaction with the client
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
            client.terminate();
        }
    }

    @Override
    public void disconnect() {
        // TO DO
    }

    @Override
    public void setNumPlayers(int numPlayers) throws IOException {
        if(numPlayers < 2 || numPlayers > 4){
            throw new IOException("Invalid number of players: " + numPlayers);
        }
        gameController.setNumPlayers(numPlayers);
        //gameController.updateAllLobbies();
    }

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
     * Handles the disconnection of a client due to a RemoteException.
     * Removes the client from the connected clients list, cleans up the client-to-nickname mapping,
     * and updates the game controller to reflect the removal of the player.
     *
     * @param client the VirtualView instance representing the disconnected client
     * @param e the RemoteException that caused the disconnection
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

    @Override
    public void removePlayer(String nickname) {
        gameController.removePlayer(nickname);
        clientMap.remove(nickname);
        heartbeatManager.unregisterClient(nickname);
        //gameController.updateAllLobbies();
    }

    @Override
    public void setPlayerReady(String nickname) {
        gameController.setPlayerReady(nickname);
        gameController.updateAllLobbies();
    }

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

    @Override
    public void setPlayerNotReady(String nickname) {
        gameController.setPlayerNotReady(nickname);
        //gameController.updateAllLobbies();
    }

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

    @Override
    public void godMode(String nickname, String conf) {
        gameController.godMode(nickname, conf);
    }

    @Override
    public void pickCoveredTile(String nickname) {
        gameController.pickCoveredTile(nickname);
    }

    @Override
    public void pickUncoveredTile(String nickname, String pngName) {
        gameController.pickUncoveredTile(nickname, pngName);
    }

    public void rotateClockwise(String nickname, int rotationNum) {
        gameController.rotateClockwise(nickname, rotationNum);
    }

    public void rotateCounterClockwise(String nickname, int rotationNum) {
        gameController.rotateCounterClockwise(nickname, rotationNum);
    }

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

    @Override
    public void standbyComponentTile(String nickname) {
        gameController.standbyComponentTile(nickname);
    }

    @Override
    public void pickStandbyComponentTile(String nickname, int index) {
        gameController.pickStandByComponentTile(nickname, index);
    }

    @Override
    public void discardComponentTile(String nickname) {
        gameController.discardComponentTile(nickname);
    }

    @Override
    public void finishBuilding(String nickname) {
        gameController.finishBuilding(nickname);
    }

    @Override
    public void finishBuilding(String nickname, int index) {
        gameController.finishBuilding(nickname, index);
    }

    @Override
    public void finishedAllShipboards() {
        gameController.finishedAllShipboards();
    }

    @Override
    public void flipHourglass() {
        gameController.flipHourglass();
    }

    @Override
    public void pickCard() {
        gameController.pickCard();
    }

    @Override
    public void activateCard(InputCommand inputCommand) {
        gameController.activateCard(inputCommand);
    }

    @Override
    public void playerAbandons(String nickname) {
        gameController.playerAbandons(nickname);
    }

    @Override
    public void destroyComponentTile(String nickname, int i, int j) {
        gameController.destroyTile(nickname, i , j);
    }

    @Override
    public void endGame() {
        gameController.endGame();
    }

    @Override
    public void placeBrownAlien(String playerName, int i, int j) {
        gameController.placeBrownAlien(playerName, i, j);
    }

    @Override
    public void placeAstronauts(String playerName, int i, int j) {
        gameController.placeAstronauts(playerName, i, j);
    }

    @Override
    public void placePurpleAlien(String playerName, int i, int j) {
        gameController.placePurpleAlien(playerName, i, j);
    }
}
