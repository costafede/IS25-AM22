package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;

import java.util.ArrayList;
import java.util.List;

public class ObservableModelView {
    List<ObserverModel> listeners = new ArrayList<>();
    //remember to add listeners to the list
    public void addListener(ObserverModel ld) {
        listeners.add(ld);
    }

    public void removeListener(ObserverModel ld) {
        listeners.remove(ld);
    }

    protected void updateAllBanks(Bank bank) {
        for(ObserverModel ld : listeners)
            ld.updateBank(bank);
    }

    protected void updateAllTileInHand(String player, ComponentTile ct) {
        for(ObserverModel ld : listeners)
            ld.updateTileInHand(player, ct);
    }

    protected void updateAllUncoveredComponentTiles(List<ComponentTile> ctList) {
        for(ObserverModel ld : listeners)
            ld.updateUncoveredComponentTiles(ctList);
    }

    protected void updateAllShipboard(String player, Shipboard shipboard) {
        for(ObserverModel ld : listeners)
            ld.updateShipboard(player, shipboard);
    }

    protected void updateAllFlightboard(Flightboard flightboard) {
        for(ObserverModel ld : listeners)
            ld.updateFlightboard(flightboard);
    }

    protected void updateAllCurrCard(AdventureCard adventureCard) {
        for(ObserverModel ld : listeners)
            ld.updateCurrCard(adventureCard);
    }

    protected void updateAllDices(Dices dices) {
        for(ObserverModel ld : listeners)
            ld.updateDices(dices);
    }

    protected void updateAllCurrPlayer(String currPlayer) {
        for(ObserverModel ld : listeners)
            ld.updateCurrPlayer(currPlayer);
    }

    protected void updateAllGamePhase(GamePhase gamePhase) {
        for(ObserverModel ld : listeners)
            ld.updateGamePhase(gamePhase);
    }

    protected void updateAllCoveredComponentTiles(List<ComponentTile> ctList) {
        for(ObserverModel ld : listeners)
            ld.updateCoveredComponentTiles(ctList);
    }

    protected void updateAllDeck(List<AdventureCard> deck) {
        for(ObserverModel ld : listeners)
            ld.updateDeck(deck);
    }

    protected void updateAllHourglassSpot(int hourglassSpot) {
        for(ObserverModel ld : listeners)
            ld.updateHourglassSpot(hourglassSpot);
    }

    protected void updateAllGame(Game game) {
        for(ObserverModel ld : listeners)
            ld.updateGame(game);
    }
}
