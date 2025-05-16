package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * Second state of the {@link AbandonedShipCard} effect logic.
 * In this state, the current player must remove a specific number of crew members from cabin tiles.
 * After all required removals, the player receives cosmic credits and the game proceeds to the next turn.
 */
public class AbandonedShipState_2 extends AbandonedShipState implements Serializable {

    /** Number of crew members still to be removed by the current player. */
    private int membersStillToRemove;

    /**
     * Constructs the second state of the Abandoned Ship card.
     *
     * @param abandonedShipCard the card this state is associated with
     */
    public AbandonedShipState_2(AbandonedShipCard abandonedShipCard) {
        super(abandonedShipCard);
        this.membersStillToRemove = abandonedShipCard.getLostAstronauts();
    }

    /**
     * Activates the effect for this state.
     * The player must select a cabin tile from which a crew member is removed.
     * After all required removals:
     * <ul>
     *   <li>The player receives cosmic credits</li>
     *   <li>The game resets the current card and updates the game state</li>
     * </ul>
     *
     * @param inputCommand the coordinates of the selected tile
     * @throws IllegalArgumentException if the selected tile is not a cabin or is invalid
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        Shipboard shipboard = game.getShipboards().get(game.getCurrPlayer());
        Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol());

        if (ct.isEmpty() || !ct.get().isCabin()) {
            throw new IllegalArgumentException("Cannot remove a crew member from a tile that is not a cabin");
        }

        ct.get().removeCrewMember();
        membersStillToRemove--;

        abandonedShipCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), shipboard);

        if (membersStillToRemove == 0) {
            shipboard.addCosmicCredits(abandonedShipCard.getCredits());

            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);

            abandonedShipCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            abandonedShipCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            abandonedShipCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    /**
     * Returns the name of this state.
     *
     * @return the string "AbandonedShipState_2"
     */
    @Override
    public String getStateName() {
        return "AbandonedShipState_2";
    }

    /**
     * Returns the number of crew members still to be removed.
     *
     * @return the remaining number of crew members to remove
     */
    public int getMembersStillToRemove() {
        return membersStillToRemove;
    }
}

