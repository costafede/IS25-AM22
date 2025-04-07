package it.polimi.ingsw.is25am22new.Network.RMI;

import it.polimi.ingsw.is25am22new.Controller.GameController;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServer extends UnicastRemoteObject implements ObserverModel, VirtualServerRMI {

    private final GameController gameController;
    private final List<VirtualView> connectedClients;
    private static final String SERVER_NAME = "GalaxyTruckerServer";

    public RmiServer(GameController gameController) throws RemoteException {
        super();
        this.gameController = gameController;
        this.connectedClients = new ArrayList<>();

        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(SERVER_NAME, this);
        System.out.println("RMI Server bound to registry - it is running on port 1234...");
    }

    public static void main(String[] args) {
        try{
            GameController gameController = new GameController();
            RmiServer server = new RmiServer(gameController);
            System.out.println("RMI Server is running... waiting for clients to connect.");
        }catch (Exception e){
            System.err.println("Error starting RMI Server: " + e.getMessage());
        }
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
        String clientHost;

        try {
            clientHost = RemoteServer.getClientHost();
            System.out.println("Client connected from " + clientHost);
        } catch (ServerNotActiveException e) {
            System.err.println("Could not get client host: " + e.getMessage());
        }

        synchronized (connectedClients) {
            connectedClients.add(client);
        }
    }

    @Override
    public void updateBank(Bank bank) throws RemoteException {
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
    public void updateTileInHand(String player, ComponentTile ct) throws RemoteException {
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
    public void updateUncoveredComponentTiles(ComponentTile ct) throws RemoteException {
        for (VirtualView connectedClient : connectedClients) {
            try {
                connectedClient.showUpdateUncoveredComponentTiles(ct);
            } catch (RemoteException e) {
                System.err.println("Error updating client with uncovered component tiles: " + e.getMessage());
                handleClientError(connectedClient, e);
            } catch (Exception e) {
                //handle showUpdateUncoveredComponentTiles exception
            }
        }
    }

    @Override
    public void updateShipboard(String player, Shipboard shipboard) throws RemoteException {
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
    public void updateFlightboard(Flightboard flightboard) throws RemoteException {
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
    public void updateCurrCard(AdventureCard adventureCard) throws RemoteException {
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
    public void updateDices(Dices dices) throws RemoteException {
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
    public void updateCurrPlayer(String currPlayer) throws RemoteException {
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

    private void handleClientError(VirtualView client, RemoteException e) {
        System.err.println("Client " + client.getClass() + " disconnected: " + e.getMessage());
        synchronized (connectedClients) {
            connectedClients.remove(client);
        }
    }



    @Override
    public void addPlayer(String nickname) {
        gameController.addPlayer(nickname);
    }

    @Override
    public void removePlayer(String nickname) {
        gameController.removePlayer(nickname);
    }

    @Override
    public void setPlayerReady(String nickname) {
        gameController.setPlayerReady(nickname);
    }

    @Override
    public void startGameByHost(String nickname) {
        gameController.startGameByHost(nickname);
    }

    @Override
    public void setPlayerNotReady(String nickname) {
        gameController.setPlayerNotReady(nickname);
    }

    @Override
    public void setGameType(String gameType) {
        if(gameType.equals("level2") || gameType.equals("tutorial")) {
            gameController.setGameType(gameType);
        } else {
            System.err.println("Invalid game type: " + gameType);
        }
    }

    @Override
    public void pickCoveredTile(String nickname) {
        gameController.pickCoveredTile(nickname);
    }

    @Override
    public void pickUncoveredTile(String nickname, int index) {
        gameController.pickUncoveredTile(nickname, index);
    }

    @Override
    public void rotateClockwise(String nickname, int rotationNum) {
        gameController.rotateClockwise(nickname, rotationNum);
    }

    @Override
    public void rotateCounterClockwise(String nickname, int rotationNum) {
        gameController.rotateCounterClockwise(nickname, rotationNum);
    }

    @Override
    public void weldComponentTile(String nickname, int i, int j) {
        gameController.weldComponentTile(nickname, i, j);
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
    public void setCurrPlayer(String currPlayer) {
        gameController.setCurrPlayer(currPlayer);
    }

    @Override
    public void setCurrPlayerToLeader() {
        gameController.setCurrPlayerToLeader();
    }

    @Override
    public void endGame() {
        gameController.endGame();
    }
}
