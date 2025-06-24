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
    int seed;
    Random rand;

    public Dices() {
        this.seed = new Random().nextInt();
        this.rand= new Random(seed);
        this.dice1 = rand.nextInt(6) + 1;
        this.dice2 = rand.nextInt(6) + 1;
    }

    public Dices(int seed) {
        this.seed = seed;
        this.rand= new Random(seed);
        this.dice1 = rand.nextInt(6) + 1;
        this.dice2 = rand.nextInt(6) + 1;
    }

    public void rollDices() {
        dice1 = rand.nextInt(6) + 1;
        dice2 = rand.nextInt(6) + 1;
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    public int getRandomSeed() {
        return seed;
    }
}
