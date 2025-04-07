package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.VirtualViewRMI;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI implements VirtualView, VirtualServer {
    private VirtualServer server;

    public ClientRMI() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        server = (VirtualServer) registry.lookup("Server");
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void showUpdateBank(Bank bank) throws RemoteException {

    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) throws RemoteException {

    }

    @Override
    public void showUpdateUncoveredComponentTiles(ComponentTile tile) throws RemoteException {

    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) throws RemoteException {

    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) throws RemoteException {

    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) throws RemoteException {

    }

    @Override
    public void showUpdateDices(Dices dices) throws RemoteException {

    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer) throws RemoteException {

    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
        // This method is not used in the RMI implementation
    }

    @Override
    public void connect(VirtualViewRMI client, String nickname) throws RemoteException {
        server.connect(client);
    }

    @Override
    public void addPlayer(String nickname) throws IOException {
        server.addPlayer(nickname);
    }

    @Override
    public void removePlayer(String nickname) throws IOException {
        server.removePlayer(nickname);
    }

    @Override
    public void setPlayerReady(String nickname) throws IOException {
        server.setPlayerReady(nickname);
    }

    @Override
    public void startGameByHost(String nickname) throws IOException {
        server.startGameByHost(nickname);
    }

    @Override
    public void setPlayerNotReady(String nickname) throws IOException {
        server.setPlayerNotReady(nickname);
    }

    @Override
    public void setGameType(String gameType) throws IOException {
        server.setGameType(gameType);
    }

    @Override
    public void pickCoveredTile(String nickname) throws IOException {
        server.pickCoveredTile(nickname);
    }

    @Override
    public void pickUncoveredTile(String nickname, int index) throws IOException {
        server.pickUncoveredTile(nickname, index);
    }

    @Override
    public void weldComponentTile(String nickname, int i, int j, int numOfRotations) throws IOException {
        server.weldComponentTile(nickname, i, j, numOfRotations);
    }

    @Override
    public void standbyComponentTile(String nickname) throws IOException {
        server.standbyComponentTile(nickname);
    }

    @Override
    public void pickStandbyComponentTile(String nickname, int index) throws IOException {
        server.pickStandbyComponentTile(nickname, index);
    }

    @Override
    public void discardComponentTile(String nickname) throws IOException {
        server.discardComponentTile(nickname);
    }

    @Override
    public void finishBuilding(String nickname) throws IOException {
        server.finishBuilding(nickname);
    }

    @Override
    public void finishBuilding(String nickname, int index) throws IOException {
        server.finishBuilding(nickname, index);
    }

    @Override
    public void finishedAllShipboards() throws IOException {
        server.finishedAllShipboards();
    }

    @Override
    public void flipHourglass() throws IOException {
        server.flipHourglass();
    }

    @Override
    public void pickCard() throws IOException {
        server.pickCard();
    }

    @Override
    public void activateCard(InputCommand inputCommand) throws IOException {
        server.activateCard(inputCommand);
    }

    @Override
    public void playerAbandons(String nickname) throws IOException {
        server.playerAbandons(nickname);
    }

    @Override
    public void destroyComponentTile(String nickname, int i, int j) throws IOException {
        server.destroyComponentTile(nickname, i, j);
    }

    @Override
    public void endGame() throws IOException {
        server.endGame();
    }
}
