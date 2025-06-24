package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

/**
 * Represents the "Place Crew Members" phase of the game.
 * During this phase, players place their crew members on their shipboards to populate the cabins.
 * The phase will transition to the next phase only after all players have populated all cabins on their respective shipboards.
 *
 * The transition to the next phase follows these rules:
 * - The phase will not transition unless every player's shipboard has all cabins populated.
 * - Once all cabins are populated, the game will move to the {@code CardPhase}.
 */
public class PlaceCrewMembersPhase extends GamePhase {
    /**
     * Initializes the "Place Crew Members" phase of the game.
     * This phase allows players to place their crew members on their shipboards
     * to populate the cabins. The phase ensures that gameplay flows properly
     * and prepares for the transition to the next phase after all players have
     * completed their placements.
     *
     * @param game The game instance that this phase is associated with, providing access
     *             to the game state, players, and shipboards necessary for the phase's logic.
     */
    public PlaceCrewMembersPhase(Game game) {
        super(game);
        phaseType = PhaseType.PLACECREWMEMBERS;
    }

    /**
     * Attempts to transition the game to the next phase.
     *
     * This method checks if all players have fully populated the cabins on their respective shipboards.
     * If any player's shipboard has unpopulated cabins, the game remains in the current phase. Once
     * all shipboards have their cabins populated, the phase transitions to the {@code CardPhase}.
     *
     * Transition Rules:
     * - Each player's shipboard is checked for fully populated cabins using the {@code allCabinsArePopulated} method.
     * - If any shipboard fails this check, the method exits without transitioning to the next phase.
     * - If the check is successful for all players, the {@code CardPhase} is triggered by invoking the {@code transition} method.
     */
    @Override
    public void trySwitchToNextPhase() {
        for(String player : game.getPlayerList()) {
            Shipboard ship = game.getShipboards().get(player);
            if(!ship.allCabinsArePopulated())
                return;
        }
        transition(new CardPhase(game));
    }
}
