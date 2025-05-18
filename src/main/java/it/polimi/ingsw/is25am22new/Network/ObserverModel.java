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
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * ObserverModel defines an interface for observing and updating the state of various game-related components
 * in a distributed or interactive gaming environment. Implementations of this interface will be notified
 * and provided with updated information whenever relevant changes occur in the game.
 */
public interface ObserverModel {
    void updateBank(Bank bank);
    void updateTileInHand(String player, ComponentTile ct);
    void updateUncoveredComponentTiles(List<ComponentTile> ctList);
    void updateShipboard(String player, Shipboard shipboard);
    void updateFlightboard(Flightboard flightboard);
    void updateCurrCard(AdventureCard adventureCard);
    void updateDices(Dices dices);
    void updateCurrPlayer(String currPlayer);

    void updateGamePhase(GamePhase gamePhase);
    void updateCoveredComponentTiles(List<ComponentTile> ctList);
    void updateDeck(List<AdventureCard> deck);
    void updateGame(Game game);
    void updateStopHourglass();
    void updateStartHourglass(int hourglassSpot);

    void updateLobby();
    void updateGameStarted();
    void updatePlayerJoined(String player);
    void shutdown();
    void updateShipboardList(Map<String, Shipboard> shipboards);
}
