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

    /**
     * Adds an observer to the list of observers that will be notified of updates and changes.
     *
     * @param ld the ObserverModel instance to be added to the list of observers
     */
    public void addObserver(ObserverModel ld) {
        observers.add(ld);
    }

    /**
     * Removes an observer from the list of observers, preventing it from receiving future updates or notifications.
     *
     * @param ld the ObserverModel instance to be removed from the list of observers
     */
    public void removeObserver(ObserverModel ld) {
        observers.remove(ld);
    }

    /**
     * Notifies all registered observers about updates to the bank.
     * This method iterates through the list of observers and invokes
     * their {@code updateBank} method, passing the updated bank instance
     * as a parameter.
     *
     * @param bank the updated Bank object containing the current state
     *             of game resources to be shared with all observers.
     */
    public void updateAllBanks(Bank bank) {
        for(ObserverModel ld : observers)
            ld.updateBank(bank);
    }

    /**
     * Notifies all registered observers to update the state of the tile in the player's hand.
     * This method iterates through the list of observers, invoking their updateTileInHand method
     * with the given player identifier and component tile.
     *
     * @param player the identifier of the player whose hand is being updated
     * @param ct the {@code ComponentTile} instance representing the updated tile
     */
    public void updateAllTileInHand(String player, ComponentTile ct) {
        for(ObserverModel ld : observers)
            ld.updateTileInHand(player, ct);
    }

    /**
     * Notifies all registered observers to update the state of all uncovered component tiles in the game.
     * This method iterates through a list of observers and invokes their respective
     * {@code updateUncoveredComponentTiles} method, passing the provided list of component tiles as a parameter.
     *
     * @param ctList a {@code List} of {@code ComponentTile} objects representing the uncovered component tiles
     *               whose state needs to be updated across all observers
     */
    public void updateAllUncoveredComponentTiles(List<ComponentTile> ctList) {
        for(ObserverModel ld : observers)
            ld.updateUncoveredComponentTiles(ctList);
    }

    /**
     * Notifies all registered observers to update the state of a player's shipboard.
     * The method adjusts the provided shipboard to ensure its correctness
     * and then iterates through the list of observers to invoke their
     * {@code updateShipboard} method, passing the player identifier and the updated shipboard.
     *
     * @param player the identifier of the player whose shipboard is being updated
     * @param shipboard the {@code Shipboard} object representing the updated state
     *                   of the player's shipboard
     */
    public void updateAllShipboard(String player, Shipboard shipboard) {
        correctShipboard(shipboard);
        for(ObserverModel ld : observers)
            ld.updateShipboard(player, shipboard);
    }

    /**
     * Updates the state of all shipboards by applying corrections to each shipboard and
     * notifying all registered observers about the updated shipboard list.
     *
     * @param shipboards a map where the keys represent player identifiers and the values
     *                   are {@code Shipboard} objects containing the state of each player's shipboard
     */
    public void updateAllShipboardList(Map<String, Shipboard> shipboards) {
        for(Shipboard shipboard : shipboards.values())
            correctShipboard(shipboard);
        for(ObserverModel ld : observers)
            ld.updateShipboardList(shipboards);
    }

    /**
     * Notifies all registered observers to update the state of the flightboard.
     * This method iterates through the list of observers and invokes their
     * {@code updateFlightboard} method, passing the provided flightboard instance.
     *
     * @param flightboard the {@code Flightboard} object representing the current state
     *                    of the game flightboard to be shared with all observers
     */
    public void updateAllFlightboard(Flightboard flightboard) {
        for(ObserverModel ld : observers)
            ld.updateFlightboard(flightboard);
    }

    /**
     * Notifies all registered observers to update the current adventure card.
     * This method iterates through the list of observers and invokes their
     * {@code updateCurrCard} method, passing the provided AdventureCard instance.
     *
     * @param adventureCard the {@code AdventureCard} object representing the current
     *                      state of the adventure card to be shared with all observers
     */
    public void updateAllCurrCard(AdventureCard adventureCard) {
        for(ObserverModel ld : observers)
            ld.updateCurrCard(adventureCard);
    }

    /**
     * Notifies all registered observers to update the state of the dices.
     * The method iterates through each observer and invokes their respective
     * {@code updateDices} method, passing the provided {@code Dices} instance.
     *
     * @param dices the {@code Dices} object representing the current state of the dices,
     *              to be shared and updated by all observers
     */
    public void updateAllDices(Dices dices) {
        for(ObserverModel ld : observers)
            ld.updateDices(dices);
    }

    /**
     * Notifies all registered observers to update the current player information.
     * This method iterates through the list of observers, invoking their
     * {@code updateCurrPlayer} method with the provided current player identifier.
     *
     * @param currPlayer the identifier of the current player to be communicated
     *                   to all observers
     */
    public void updateAllCurrPlayer(String currPlayer) {
        for(ObserverModel ld : observers)
            ld.updateCurrPlayer(currPlayer);
    }

    /**
     * Notifies all registered observers to update the current game phase.
     * This method iterates through the list of observers and invokes their
     * {@code updateGamePhase} method, passing the provided {@code GamePhase} instance.
     *
     * @param gamePhase the {@code GamePhase} object representing the current
     *                  state of the game's phase to be communicated to all observers
     */
    public void updateAllGamePhase(GamePhase gamePhase) {
        for(ObserverModel ld : observers)
            ld.updateGamePhase(gamePhase);
    }

    /**
     * Notifies all registered observers to update the state of all covered component tiles in the game.
     * This method iterates through the list of observers and invokes their respective
     * {@code updateCoveredComponentTiles} method, passing the provided list of component tiles as a parameter.
     *
     * @param ctList a {@code List} of {@code ComponentTile} objects representing the covered component tiles
     *               whose state needs to be updated across all observers
     */
    public void updateAllCoveredComponentTiles(List<ComponentTile> ctList) {
        for(ObserverModel ld : observers)
            ld.updateCoveredComponentTiles(ctList);
    }

    /**
     * Notifies all registered observers to update the state of the deck.
     * This method iterates through the list of observers and invokes their
     * {@code updateDeck} method, passing the provided list of adventure cards.
     *
     * @param deck a {@code List} of {@code AdventureCard} objects representing
     *             the current state of the deck to be communicated to all observers
     */
    public void updateAllDeck(List<AdventureCard> deck) {
        for(ObserverModel ld : observers)
            ld.updateDeck(deck);
    }

    /**
     * Notifies all registered observers to update the start hourglass state.
     * This method iterates through the list of observers and invokes their
     * {@code updateStartHourglass} method with the provided hourglass spot.
     *
     * @param hourglassSpot the position of the hourglass to be shared with and updated by all observers
     */
    public void updateAllStartHourglass(int hourglassSpot) {
        for(ObserverModel ld : observers)
            ld.updateStartHourglass(hourglassSpot);
    }

    /**
     * Notifies all registered observers to update the stop hourglass state.
     * This method iterates through the list of observers and invokes their {@code updateStopHourglass} method.
     * It enables the synchronization of all observers with the updated state of the stop hourglass.
     */
    public void updateAllStopHourglass() {
        for(ObserverModel ld : observers)
            ld.updateStopHourglass();
    }

    /**
     * Updates all ships' boards in the provided game instance by applying corrections
     * and notifies all registered observers about the updated game state.
     *
     * @param game the {@code Game} object representing the current state of the game
     *             containing the shipboards to be corrected and updated
     */
    public void updateAllGame(Game game) {
        for(Shipboard s : game.getShipboards().values()){
            correctShipboard(s);
        }
        for(ObserverModel ld : observers)
            ld.updateGame(game);
    }

    /**
     * Notifies all registered observers to update the state of the leaderboard.
     * This method iterates through the list of observers and invokes their
     * {@code updateAllLeaderboard} method, passing the provided leaderboard data.
     *
     * @param leaderboard a map where the keys represent player identifiers and the values represent
     *                    their corresponding leaderboard scores, which need to be communicated to all observers
     */
    public void updateAllLeaderboard(Map<String, Integer> leaderboard) {
        for(ObserverModel ld : observers)
            ld.updateAllLeaderboard(leaderboard);
    }

    /**
     * Adjusts the provided shipboard to ensure its internal state is consistent.
     * This method updates the component tiles grid and standby components
     * to maintain correctness.
     *
     * @param shipboard the {@code Shipboard} object representing the player's
     *                  current shipboard, which needs to be corrected and updated.
     */
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
