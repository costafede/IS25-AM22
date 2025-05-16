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

    public Dices() {
        this.dice1 = new Random().nextInt(6) + 1;
        this.dice2 = new Random().nextInt(6) + 1;
    }

    public void rollDices() {
        dice1 = new Random().nextInt(6) + 1;
        dice2 = new Random().nextInt(6) + 1;
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }
}
