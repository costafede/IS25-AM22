package it.polimi.ingsw.is25am22new.Model.GamePhase;

import java.io.Serializable;

/**
 * Enum representing the various phases in the lifecycle of the game.
 *
 * Each constant corresponds to a distinct phase, encapsulating the
 * specific logic and behaviors associated with that stage of gameplay.
 *
 * The phases are:
 * - SETUP: The initial preparation phase for setting up the game.
 * - BUILDING: The phase where players construct their shipboards.
 * - CORRECTINGSHIP: The phase for validating and correcting ship configurations.
 * - PLACECREWMEMBERS: The phase where players assign crew members to their ships.
 * - CARD: The phase focused on card evaluations and transitions.
 * - END: The final phase marking the conclusion of the game.
 */
public enum PhaseType implements Serializable {
    SETUP,
    BUILDING,
    CORRECTINGSHIP,
    PLACECREWMEMBERS,
    CARD,
    END
}
