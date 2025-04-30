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
import it.polimi.ingsw.is25am22new.Network.ObserverModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RmiServer extends UnicastRemoteObject implements ObserverModel, VirtualServer {

    private final GameController gameController;
    private final List<VirtualView> connectedClients;
    private final Map<String, VirtualView> clientMap; //Map nickname to clients
    private static final String SERVER_NAME = "GalaxyTruckerServer";

    public RmiServer(GameController gameController, int port) throws RemoteException {
        super();
        this.gameController = gameController;
        this.connectedClients = new ArrayList<>();
        this.clientMap = new HashMap<>();

        //System.setProperty("java.rmi.server.hostname", "172.20.10.2");
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(SERVER_NAME, this);
        System.out.println("RMI Server bound to registry - it is running on port " + port + "...");
    }

    public void connect(VirtualView client, String nickname) throws RemoteException {
        if (client == null) {
            System.err.println("Client reference is null.");
            return;
        }

        if (nickname == null || nickname.trim().isEmpty()) {
            client.showConnectionResult(false, false, "Invalid nickname. Connection failed.");
            return;
        }

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

            String lobbyCreator = gameController.getLobbyCreator();
            boolean isHost = nickname.equals(lobbyCreator);

            client.showConnectionResult(isHost, true, isHost ? "You are the host of the lobby." : "You joined an existing lobby.");

            if (!isHost) {
                gameController.updateAllPlayerJoined(nickname);
            }

            gameController.updateAllLobbies();
        }
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
    public void updateHourglassSpot(int hourglassSpot) {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateHourglassSpot(hourglassSpot);
            } catch (RemoteException e) {
                System.err.println("Error updating client with hourglass spot: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateHourglassSpot exception
            }
        }
    }

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
        //gameController.updateAllLobbies();
    }

    @Override
    public void setPlayerReady(String nickname) {
        gameController.setPlayerReady(nickname);
        //gameController.updateAllLobbies();
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
        } else {
            System.err.println("Invalid game type: " + gameType);
        }
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
        if(numOfRotations < 0) {
            gameController.rotateCounterClockwise(nickname, -numOfRotations);
            gameController.weldComponentTile(nickname, i, j);
        } else if(numOfRotations > 0) {
            gameController.rotateClockwise(nickname, numOfRotations);
            gameController.weldComponentTile(nickname, i, j);
        } else {
            System.err.println("Invalid number of rotations: " + numOfRotations);
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
}
