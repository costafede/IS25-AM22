package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import java.io.Serializable;

/**
 * Represents the four possible orientations in the game.
 * Orientation determines the direction, which can be one of the following:
 *
 * - TOP: The arrow points upwards.
 * - BOTTOM: The arrow points downwards.
 * - LEFT: The arrow points to the left.
 * - RIGHT: The arrow points to the right.
 *
 * Orientation is used in various parts of the game to define the direction
 * of objects such as shots and meteors.
 * It implements Serializable to allow its use in persistent or networked environments.
 */
public enum Orientation implements Serializable {
    TOP, //Top: arrow is pointing up
    BOTTOM, //Bottom: arrow is pointing down
    LEFT, //Left: arrow is pointing left
    RIGHT //Right: arrow is pointing right



}
