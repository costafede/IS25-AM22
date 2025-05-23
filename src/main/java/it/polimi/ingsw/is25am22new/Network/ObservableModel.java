package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.*;

/**
 * The ObservableModel serves as a core component in the observer design pattern, enabling the
 * functionality to register, remove, and notify observers about changes in the state of the game.
 * It maintains a list of ObserverModel instances and ensures they are notified when relevant
 * updates occur in the game's components.
 */
public class ObservableModel {
    List<ObserverModel> observers = new ArrayList<>();
    //remember to add listeners to the list
    public void addObserver(ObserverModel ld) {
        observers.add(ld);
    }

    public void removeObserver(ObserverModel ld) {
        observers.remove(ld);
    }

    public void updateAllBanks(Bank bank) {
        for(ObserverModel ld : observers)
            ld.updateBank(bank);
    }

    public void updateAllTileInHand(String player, ComponentTile ct) {
        for(ObserverModel ld : observers)
            ld.updateTileInHand(player, ct);
    }

    public void updateAllUncoveredComponentTiles(List<ComponentTile> ctList) {
        for(ObserverModel ld : observers)
            ld.updateUncoveredComponentTiles(ctList);
    }

    public void updateAllShipboard(String player, Shipboard shipboard) {
        correctShipboard(shipboard);
        for(ObserverModel ld : observers)
            ld.updateShipboard(player, shipboard);
    }

    public void updateAllShipboardList(Map<String, Shipboard> shipboards) {
        for(Shipboard shipboard : shipboards.values())
            correctShipboard(shipboard);
        for(ObserverModel ld : observers)
            ld.updateShipboardList(shipboards);
    }

    public void updateAllFlightboard(Flightboard flightboard) {
        for(ObserverModel ld : observers)
            ld.updateFlightboard(flightboard);
    }

    public void updateAllCurrCard(AdventureCard adventureCard) {
        for(ObserverModel ld : observers)
            ld.updateCurrCard(adventureCard);
    }

    public void updateAllDices(Dices dices) {
        for(ObserverModel ld : observers)
            ld.updateDices(dices);
    }

    public void updateAllCurrPlayer(String currPlayer) {
        for(ObserverModel ld : observers)
            ld.updateCurrPlayer(currPlayer);
    }

    public void updateAllGamePhase(GamePhase gamePhase) {
        for(ObserverModel ld : observers)
            ld.updateGamePhase(gamePhase);
    }

    public void updateAllCoveredComponentTiles(List<ComponentTile> ctList) {
        for(ObserverModel ld : observers)
            ld.updateCoveredComponentTiles(ctList);
    }

    public void updateAllDeck(List<AdventureCard> deck) {
        for(ObserverModel ld : observers)
            ld.updateDeck(deck);
    }

    public void updateAllStartHourglass(int hourglassSpot) {
        for(ObserverModel ld : observers)
            ld.updateStartHourglass(hourglassSpot);
    }

    public void updateAllStopHourglass() {
        for(ObserverModel ld : observers)
            ld.updateStopHourglass();
    }

    public void updateAllGame(Game game) {
        for(Shipboard s : game.getShipboards().values()){
            correctShipboard(s);
        }
        for(ObserverModel ld : observers)
            ld.updateGame(game);
    }

    public void updateAllLeaderboard(Map<String, Integer> leaderboard) {
        for(ObserverModel ld : observers)
            ld.updateAllLeaderboard(leaderboard);
    }

    private void correctShipboard(Shipboard shipboard){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                if(shipboard.getComponentTileFromGrid(i, j).isPresent()){
                    shipboard.setComponentTilesGridCopy(i, j, shipboard.getComponentTileFromGrid(i, j).get());
                } else {
                    shipboard.setComponentTilesGridCopy(i, j, null);
                }
            }
        }

        if(shipboard.getStandbyComponent()[0].isPresent())
            shipboard.setStandbyComponentCopy(0, shipboard.getStandbyComponent()[0].get());
        else
            shipboard.setStandbyComponentCopy(0, null);

        if(shipboard.getStandbyComponent()[1].isPresent())
            shipboard.setStandbyComponentCopy(1, shipboard.getStandbyComponent()[1].get());
        else
            shipboard.setStandbyComponentCopy(1, null);
    }
}
