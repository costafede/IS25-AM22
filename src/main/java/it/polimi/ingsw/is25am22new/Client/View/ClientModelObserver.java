package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.List;
import java.util.Map;

public interface ClientModelObserver {
    void updateGame(ClientModel model);

    void updateStopHourglass();

    void updateStartHourglass(int hourglassSpot);

    void updateGamePhase(GamePhase gamePhase);

    void updateBank(Bank bank);

    void updateCoveredComponentTiles(List<ComponentTile> coveredComponentTiles);

    void updateUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles);

    void updateShipboards(Map<String, Shipboard> shipboards);

    void updateFlightboard(Flightboard flightboard);

    void updateShipboard(Shipboard shipboard);

    void updateDeck(List<AdventureCard> deck);

    void updateCurrPlayer(String player);

    void updateCurrCard(AdventureCard currCard);

    void updateDices(Dices dices);

    void updateGameStartMessageReceived(boolean gameStartMessageReceived);
}
