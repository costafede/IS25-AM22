package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

/**
 * The Side enum represents the different types of sides a component tile in the game can have.
 * It is used to define the properties and configurations of various tiles based on their sides.
 *
 * - SMOOTH: Represents a smooth side with no pipe.
 * - ONEPIPE: Represents a side with a single pipe.
 * - TWOPIPES: Represents a side with two pipes.
 * - UNIVERSALPIPE: Represents a side with a universal connection.
 *
 * This enum is utilized in all components to manage their connectivity
 * and functionality specific to the game mechanics.
 */
public enum Side {
    SMOOTH, ONEPIPE, TWOPIPES, UNIVERSALPIPE
}
