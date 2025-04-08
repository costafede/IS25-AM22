package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientSocket implements VirtualView, VirtualServer {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread listenerThread;

    public ClientSocket() throws Exception {
        socket = new Socket("localhost", 5000);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void showUpdateBank(Bank bank) throws RemoteException {

    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) throws RemoteException {

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
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) throws RemoteException {

    }

    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) throws RemoteException {

    }

    @Override
    public void showNicknameResult(boolean valid, String message) throws RemoteException {

    }

    @Override
    public void showGameStarted() throws RemoteException {

    }

    @Override
    public void showPlayerJoined(String player) throws RemoteException {

    }

    @Override
    public void connect(VirtualView client, String nickname) throws RemoteException {
        //Used only for RMI
    }

    @Override
    public void addPlayer(String nickname) throws IOException {

    }

    @Override
    public void removePlayer(String nickname) throws IOException {

    }

    @Override
    public void setPlayerReady(String nickname) throws IOException {

    }

    @Override
    public void startGameByHost(String nickname) throws IOException {

    }

    @Override
    public void setPlayerNotReady(String nickname) throws IOException {

    }

    @Override
    public void setGameType(String gameType) throws IOException {

    }

    @Override
    public void pickCoveredTile(String nickname) throws IOException {

    }

    @Override
    public void pickUncoveredTile(String nickname, int index) throws IOException {

    }

    @Override
    public void weldComponentTile(String nickname, int i, int j, int numOfRotations) throws IOException {

    }

    @Override
    public void standbyComponentTile(String nickname) throws IOException {

    }

    @Override
    public void pickStandbyComponentTile(String nickname, int index) throws IOException {

    }

    @Override
    public void discardComponentTile(String nickname) throws IOException {

    }

    @Override
    public void finishBuilding(String nickname) throws IOException {

    }

    @Override
    public void finishBuilding(String nickname, int index) throws IOException {

    }

    @Override
    public void finishedAllShipboards() throws IOException {

    }

    @Override
    public void flipHourglass() throws IOException {

    }

    @Override
    public void pickCard() throws IOException {

    }

    @Override
    public void activateCard(InputCommand inputCommand) throws IOException {

    }

    @Override
    public void playerAbandons(String nickname) throws IOException {

    }

    @Override
    public void destroyComponentTile(String nickname, int i, int j) throws IOException {

    }

    @Override
    public void endGame() throws IOException {

    }

    @Override
    public void setCurrPlayer(String currPlayer) throws RemoteException {

    }

    @Override
    public void setCurrPlayerToLeader() throws RemoteException {

    }
}
