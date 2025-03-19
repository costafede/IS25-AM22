package it.polimi.ingsw.is25am22new.Model;

import java.util.Random;

public class Dices {
    int dice1;
    int dice2;

    public Dices(int dice1, int dice2) {
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
