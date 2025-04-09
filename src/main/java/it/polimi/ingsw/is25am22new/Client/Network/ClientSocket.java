package it.polimi.ingsw.is25am22new.Client.network;

import it.polimi.ingsw.is25am22new.Client.Network.ClientInterface;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientSocket implements ClientInterface {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public ClientSocket(String host, int port) throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }


    @Override
    public void showUpdateBank(Bank bank) throws IOException {

    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) throws IOException {

    }

    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList) throws IOException {

    }

    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) throws IOException {

    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) throws IOException {

    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) throws IOException {

    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) throws IOException {

    }

    @Override
    public void showUpdateDices(Dices dices) throws IOException {

    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer) throws IOException {

    }

    @Override
    public void showUpdateGamePhase(GamePhase gamePhase) throws IOException {

    }

    @Override
    public void showUpdateDeck(List<AdventureCard> deck) throws IOException {

    }

    @Override
    public void showUpdateGame(Game game) throws IOException {

    }

    @Override
    public void showUpdateHourglassSpot(int hourglassSpot) throws IOException {

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
}