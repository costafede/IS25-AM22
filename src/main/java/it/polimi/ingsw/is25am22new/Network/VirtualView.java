package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
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
    void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType);
    void showConnectionResult(boolean isHost, boolean success, String message);
    void showNicknameResult(boolean valid, String message);
    void showGameStarted();
    void showPlayerJoined(String player);
}
