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
import java.util.Map;

/**
 * The ObservableModelView class provides an implementation for managing a list of observers
 * and notifying them about updates in the application state. It follows the observable design
 * pattern and acts as a base class for models that require observer notification functionality.
 *
 * This class maintains a list of ClientModelObserver instances and provides methods to add
 * or remove observers. Various notification methods are available to inform all registered
 * observers of specific updates or changes in the game state.
 *
 * Subclasses of ObservableModelView can utilize these notification methods to invoke the
 * corresponding update methods in ClientModelObserver to propagate state changes.
 */
public abstract class ObservableModelView {
    List<ClientModelObserver> listeners = new ArrayList<>();
    //remember to add listeners to the list
    public void addListener(ClientModelObserver ld) {
        listeners.add(ld);
    }

    public void removeListener(ClientModelObserver ld) {
        listeners.remove(ld);
    }

    protected void notifyGame(ClientModel model) {
        for (ClientModelObserver ld : listeners) {
            ld.updateGame(model);
        }
    }

    protected void notifyTileInHand(String player, ComponentTile ct) {
        for (ClientModelObserver ld : listeners) {
            ld.updateTileInHand(player, ct);
        }
    }

    protected void notifyStopHourglass() {
        for (ClientModelObserver ld : listeners) {
            ld.updateStopHourglass();
        }
    }

    protected void notifyStartHourglass(int hourglassSpot) {
        for (ClientModelObserver ld : listeners) {
            ld.updateStartHourglass(hourglassSpot);
        }
    }

    protected void notifyGamePhase(GamePhase gamePhase) {
        for (ClientModelObserver ld : listeners) {
            ld.updateGamePhase(gamePhase);
        }
    }

    protected void notifyBank(Bank bank) {
        for (ClientModelObserver ld : listeners) {
            ld.updateBank(bank);
        }
    }

    protected void notifyCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {
        for (ClientModelObserver ld : listeners) {
            ld.updateCoveredComponentTiles(coveredComponentTiles);
        }
    }

    protected void notifyUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        for (ClientModelObserver ld : listeners) {
            ld.updateUncoveredComponentTiles(uncoveredComponentTiles);
        }
    }


    protected void notifyShipboards(Map<String, Shipboard> shipboards) {
        for (ClientModelObserver ld : listeners) {
            ld.updateShipboards(shipboards);
        }
    }

    protected void notifyShipboard(Shipboard shipboard) {
        for (ClientModelObserver ld : listeners) {
            ld.updateShipboard(shipboard);
        }
    }

    protected void notifyFlightboard(Flightboard flightboard) {
        for (ClientModelObserver ld : listeners) {
            ld.updateFlightboard(flightboard);
        }
    }

    protected void notifyDeck(List<AdventureCard> deck) {
        for (ClientModelObserver ld : listeners) {
            ld.updateDeck(deck);
        }
    }

    protected void notifyCurrPlayer(String player) {
        for (ClientModelObserver ld : listeners) {
            ld.updateCurrPlayer(player);
        }
    }

    protected void notifyCurrCard(AdventureCard currCard) {
        for (ClientModelObserver ld : listeners) {
            ld.updateCurrCard(currCard);
        }
    }

    protected void notifyDices(Dices dices) {
        for (ClientModelObserver ld : listeners) {
            ld.updateDices(dices);
        }
    }


    protected void notifyGameStartMessageReceived(boolean gameStartMessageReceived) {
        for (ClientModelObserver ld : listeners) {
            ld.updateGameStartMessageReceived(gameStartMessageReceived);
        }
    }

    protected void notifyAllLeaderboard(Map<String, Integer> leaderCards) {
        for (ClientModelObserver ld : listeners) {
            ld.updateAllLeaderboard(leaderCards);
        }
    }

    protected void notifyGameLoaded(ClientModel clientModel) {
        for (ClientModelObserver ld : listeners) {
            ld.updateAllGameLoaded(clientModel);
        }
    }
}
