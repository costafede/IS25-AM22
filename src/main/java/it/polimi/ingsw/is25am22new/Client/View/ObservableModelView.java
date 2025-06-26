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
    /**
     * Registers a new observer to receive updates from the model.
     *
     * @param ld the {@link ClientModelObserver} to add
     */
    public void addListener(ClientModelObserver ld) {
        listeners.add(ld);
    }

    /**
     * Removes a previously registered observer from receiving updates.
     *
     * @param ld the {@link ClientModelObserver} to remove
     */
    public void removeListener(ClientModelObserver ld) {
        listeners.remove(ld);
    }

    /**
     * Notifies all observers with the current game model.
     *
     * @param model the updated {@link ClientModel}
     */
    protected void notifyGame(ClientModel model) {
        for (ClientModelObserver ld : listeners) {
            ld.updateGame(model);
        }
    }

    /**
     * Notifies all observers about the tile currently in hand for a player.
     *
     * @param player the player holding the tile
     * @param ct the {@link ComponentTile} in hand
     */
    protected void notifyTileInHand(String player, ComponentTile ct) {
        for (ClientModelObserver ld : listeners) {
            ld.updateTileInHand(player, ct);
        }
    }

    /**
     * Notifies all observers that the hourglass timer should be stopped.
     */
    protected void notifyStopHourglass() {
        for (ClientModelObserver ld : listeners) {
            ld.updateStopHourglass();
        }
    }

    /**
     * Notifies all observers that the hourglass timer should be started.
     *
     * @param hourglassSpot the spot associated with the hourglass
     */
    protected void notifyStartHourglass(int hourglassSpot) {
        for (ClientModelObserver ld : listeners) {
            ld.updateStartHourglass(hourglassSpot);
        }
    }

    /**
     * Notifies all observers of a change in the current game phase.
     *
     * @param gamePhase the new {@link GamePhase}
     */
    protected void notifyGamePhase(GamePhase gamePhase) {
        for (ClientModelObserver ld : listeners) {
            ld.updateGamePhase(gamePhase);
        }
    }

    /**
     * Notifies all observers of a change in the bank.
     *
     * @param bank the updated {@link Bank}
     */
    protected void notifyBank(Bank bank) {
        for (ClientModelObserver ld : listeners) {
            ld.updateBank(bank);
        }
    }

    /**
     * Notifies all observers of the list of covered component tiles.
     *
     * @param coveredComponentTiles the list of covered {@link ComponentTile}
     */
    protected void notifyCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {
        for (ClientModelObserver ld : listeners) {
            ld.updateCoveredComponentTiles(coveredComponentTiles);
        }
    }

    /**
     * Notifies all observers of the list of uncovered component tiles.
     *
     * @param uncoveredComponentTiles the list of uncovered {@link ComponentTile}
     */
    protected void notifyUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        for (ClientModelObserver ld : listeners) {
            ld.updateUncoveredComponentTiles(uncoveredComponentTiles);
        }
    }

    /**
     * Notifies all observers of the current state of all shipboards.
     *
     * @param shipboards a map of player names to their {@link Shipboard}
     */
    protected void notifyShipboards(Map<String, Shipboard> shipboards) {
        for (ClientModelObserver ld : listeners) {
            ld.updateShipboards(shipboards);
        }
    }

    /**
     * Notifies all observers of an update to a single shipboard.
     *
     * @param shipboard the updated {@link Shipboard}
     */
    protected void notifyShipboard(Shipboard shipboard) {
        for (ClientModelObserver ld : listeners) {
            ld.updateShipboard(shipboard);
        }
    }

    /**
     * Notifies all observers of an update to the flightboard.
     *
     * @param flightboard the updated {@link Flightboard}
     */
    protected void notifyFlightboard(Flightboard flightboard) {
        for (ClientModelObserver ld : listeners) {
            ld.updateFlightboard(flightboard);
        }
    }

    /**
     * Notifies all observers of an update to the adventure card deck.
     *
     * @param deck the updated list of {@link AdventureCard}
     */
    protected void notifyDeck(List<AdventureCard> deck) {
        for (ClientModelObserver ld : listeners) {
            ld.updateDeck(deck);
        }
    }

    /**
     * Notifies all observers of the current player's turn.
     *
     * @param player the name of the current player
     */
    protected void notifyCurrPlayer(String player) {
        for (ClientModelObserver ld : listeners) {
            ld.updateCurrPlayer(player);
        }
    }

    /**
     * Notifies all observers of the currently drawn adventure card.
     *
     * @param currCard the current {@link AdventureCard}
     */
    protected void notifyCurrCard(AdventureCard currCard) {
        for (ClientModelObserver ld : listeners) {
            ld.updateCurrCard(currCard);
        }
    }

    /**
     * Notifies all observers of a change in the dice values.
     *
     * @param dices the updated {@link Dices} object
     */
    protected void notifyDices(Dices dices) {
        for (ClientModelObserver ld : listeners) {
            ld.updateDices(dices);
        }
    }

    /**
     * Notifies all observers whether the game start message has been received.
     *
     * @param gameStartMessageReceived true if the game start message has been received, false otherwise
     */
    protected void notifyGameStartMessageReceived(boolean gameStartMessageReceived) {
        for (ClientModelObserver ld : listeners) {
            ld.updateGameStartMessageReceived(gameStartMessageReceived);
        }
    }

    /**
     * Notifies all observers of the leaderboard with players' scores.
     *
     * @param leaderCards a map of player names to their scores
     */
    protected void notifyAllLeaderboard(Map<String, Integer> leaderCards) {
        for (ClientModelObserver ld : listeners) {
            ld.updateAllLeaderboard(leaderCards);
        }
    }

    /**
     * Notifies all observers that the game has been loaded from a saved state.
     *
     * @param clientModel the restored {@link ClientModel}
     */
    protected void notifyGameLoaded(ClientModel clientModel) {
        for (ClientModelObserver ld : listeners) {
            ld.updateAllGameLoaded(clientModel);
        }
    }

}
