package it.polimi.ingsw.is25am22new.Model.Miscellaneous;

import java.io.Serializable;
import java.util.Random;

/**
 * The Dices class simulates the behavior of two six-sided dice. It provides
 * the ability to roll the dice, retrieve their current values, and initializes
 * them with random values upon object creation.
 */
public class Dices implements Serializable {
    int dice1;
    int dice2;

    /**
     * Constructs a new instance of the Dices class. The Dices instance simulates
     * two six-sided dice, each initialized with a random value between 1 and 6.
     * This constructor ensures that both dice have randomized values upon creation.
     */
    public Dices() {
        this.dice1 = new Random().nextInt(6) + 1;
        this.dice2 = new Random().nextInt(6) + 1;
    }

    /**
     * Simulates the rolling of two six-sided dice. Updates the values of dice1 and dice2
     * with new random values between 1 and 6 (inclusive), representing the results
     * of the dice rolls. The method overwrites the previous values of the dice.
     */
    public void rollDices() {
        dice1 = new Random().nextInt(6) + 1;
        dice2 = new Random().nextInt(6) + 1;
    }

    /**
     * Retrieves the current value of the first dice (dice1).
     *
     * @return the integer value of dice1
     */
    public int getDice1() {
        return dice1;
    }

    /**
     * Retrieves the current value of the second dice.
     *
     * @return the value of dice2, representing the outcome of the second dice roll.
     */
    public int getDice2() {
        return dice2;
    }

    /**
     * Sets the value of the first dice.
     *
     * @param dice1 the value to set, representing the outcome of the first dice (1 to 6)
     */
    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    /**
     * Sets the value of the second dice.
     *
     * @param dice2 the value to set for the second dice, typically between 1 and 6 inclusive
     */
    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }
}
