package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.Games.Game;


import java.io.Serializable;
import java.util.*;

public class PiratesCard extends AdventureCard implements Serializable {

    private Map<Integer, Shot> numberToShot;
    private int flightDaysLost;
    private int cannonStrength;
    private int credits;
    private PiratesState piratesState;
    private boolean batteryUsed;
    private List<String> defeatedPlayers;
    private int indexOfIncomingShot;
    private String currDefeatedPlayer;
    private int dice1;
    private int dice2;

    public PiratesCard(String pngName, String name, Game game, int level, boolean tutorial, Map<Integer, Shot> numberToShot, int flightDaysLost, int cannonStrength, int credits) {
        super(pngName, name, game, level, tutorial);
        this.numberToShot = numberToShot;
        this.flightDaysLost = flightDaysLost;
        this.cannonStrength = cannonStrength;
        this.credits = credits;
        this.piratesState = new PiratesState_1(this);
        this.batteryUsed = false;
        this.defeatedPlayers = new ArrayList<>();
        this.indexOfIncomingShot = 0;
        game.getDices().rollDices();
        this.dice1 = game.getDices().getDice1();
        this.dice2 = game.getDices().getDice2();
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

    public void setNewDices() {
        game.getDices().rollDices();
        setDice1(game.getDices().getDice1());
        setDice2(game.getDices().getDice2());
    }

    public int getIndexOfIncomingShot(){
        return indexOfIncomingShot;
    }

    public void setNextIndexOfShot() {
        indexOfIncomingShot++;
    }

    public void setPiratesState(PiratesState piratesState) {
        this.piratesState = piratesState;
    }

    public Map<Integer, Shot> getNumberToShot() {
        return numberToShot;
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getCannonStrength() {
        return cannonStrength;
    }

    public int getCredits() {
        return credits;
    }

    public boolean isBatteryUsed() {
        return batteryUsed;
    }

    public List<String> getDefeatedPlayers() {
        return defeatedPlayers;
    }

    public void addDefeatedPlayer(String nickname) {
        this.defeatedPlayers.add(nickname);
    }

    public void setBatteryUsed(boolean usedBattery) {this.batteryUsed= usedBattery;}

    @Override
    public void activateEffect(InputCommand inputCommand) {
        piratesState.activateEffect(inputCommand);
    }

    @Override
    public String getStateName() {
        return piratesState.getStateName();
    }

    public String getCurrDefeatedPlayer() {
        return currDefeatedPlayer;
    }

    public String getLastDefeatedPlayer() {
        return defeatedPlayers.getLast();
    }

    public void setCurrDefeatedPlayerToFirst() {
        this.currDefeatedPlayer = defeatedPlayers.getFirst();
    }

    public boolean thereAreStillShots() {
        return indexOfIncomingShot < numberToShot.size();
    }

    public void setCurrDefeatedPlayerToNext() {
        this.currDefeatedPlayer = defeatedPlayers.get(defeatedPlayers.indexOf(currDefeatedPlayer) + 1);
    }
}
