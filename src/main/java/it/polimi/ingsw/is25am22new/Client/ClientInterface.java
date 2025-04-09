package it.polimi.ingsw.is25am22new.Client;

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

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ClientInterface extends Remote {

    void showUpdateBank(Bank bank) throws IOException;
    void showUpdateTileInHand(String player, ComponentTile tile) throws IOException;
    void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList) throws IOException;
    void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) throws IOException;
    void showUpdateShipboard(String player, Shipboard shipboard) throws IOException;
    void showUpdateFlightboard(Flightboard flightboard) throws IOException;
    void showUpdateCurrCard(AdventureCard adventureCard) throws IOException;
    void showUpdateDices(Dices dices) throws IOException;
    void showUpdateCurrPlayer(String currPlayer) throws IOException;


    void showUpdateGamePhase(GamePhase gamePhase) throws IOException;
    void showUpdateDeck(List<AdventureCard> deck) throws IOException;
    void showUpdateGame(Game game) throws IOException;
    void showUpdateHourglassSpot(int hourglassSpot) throws IOException;

    //Methods for lobby management
    void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) throws RemoteException;
    void showConnectionResult(boolean isHost, boolean success, String message) throws RemoteException;
    void showNicknameResult(boolean valid, String message) throws RemoteException;
    void showGameStarted() throws RemoteException;
    void showPlayerJoined(String player) throws RemoteException;


    void connect(VirtualView client, String nickname) throws RemoteException;
    void addPlayer(String nickname) throws IOException;
    void removePlayer(String nickname) throws IOException;
    void setPlayerReady(String nickname) throws IOException;
    void startGameByHost(String nickname) throws IOException;
    void setPlayerNotReady(String nickname) throws IOException;
    void setGameType(String gameType) throws IOException;

    void pickCoveredTile(String nickname) throws IOException;
    void pickUncoveredTile(String nickname, int index) throws IOException;
    void weldComponentTile(String nickname, int i, int j, int numOfRotations) throws IOException;
    void standbyComponentTile(String nickname) throws IOException;
    void pickStandbyComponentTile(String nickname, int index) throws IOException;
    void discardComponentTile(String nickname) throws IOException;
    void finishBuilding(String nickname) throws IOException;
    void finishBuilding(String nickname, int index) throws IOException;
    void finishedAllShipboards() throws IOException;
    void flipHourglass() throws IOException;
    void pickCard() throws IOException;
    void activateCard(InputCommand inputCommand) throws IOException;
    void playerAbandons(String nickname) throws IOException;
    void destroyComponentTile(String nickname, int i, int j) throws IOException;
    void endGame() throws IOException;

}