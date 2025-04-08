package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualView extends Remote {

    void showUpdateBank(Bank bank) throws IOException;
    void showUpdateTileInHand(String player, ComponentTile tile) throws IOException;
    void showUpdateUncoveredComponentTiles(ComponentTile tile) throws IOException;
    void showUpdateShipboard(String player, Shipboard shipboard) throws IOException;
    void showUpdateFlightboard(Flightboard flightboard) throws IOException;
    void showUpdateCurrCard(AdventureCard adventureCard) throws IOException;
    void showUpdateDices(Dices dices) throws IOException;
    void showUpdateCurrPlayer(String currPlayer) throws IOException;

    //Methods for lobby management
    void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) throws RemoteException;
    void showConnectionResult(boolean isHost, boolean success, String message) throws RemoteException;
    void showNicknameResult(boolean valid, String message) throws RemoteException;
    void showGameStarted() throws RemoteException;
    void showPlayerJoined(String player) throws RemoteException;

}
