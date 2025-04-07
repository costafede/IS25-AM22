package it.polimi.ingsw.is25am22new.Network.RMI.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualViewRMI extends Remote, VirtualView {
    void showUpdateBank(Bank bank) throws RemoteException;
    void showUpdateTileInHand(String player, ComponentTile tile) throws RemoteException;
    void showUpdateUncoveredComponentTiles(ComponentTile tile) throws RemoteException;
    void showUpdateShipboard(String player, Shipboard shipboard) throws RemoteException;
    void showUpdateFlightboard(Flightboard flightboard) throws RemoteException;
    void showUpdateCurrCard(AdventureCard adventureCard) throws RemoteException;
    void showUpdateDices(Dices dices) throws RemoteException;
    void showUpdateCurrPlayer(String currPlayer) throws RemoteException;

    //Methods for lobby management
    void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) throws RemoteException;
    void showConnectionResult(boolean isHost, boolean success, String message) throws RemoteException;
    void showNicknameResult(boolean valid, String message) throws RemoteException;
    void showGameStarted() throws RemoteException;
    void showPlayerJoined(String player) throws RemoteException;
}
