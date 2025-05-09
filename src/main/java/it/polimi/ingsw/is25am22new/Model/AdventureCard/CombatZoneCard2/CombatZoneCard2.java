package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CombatZoneCard2 extends AdventureCard implements Serializable {

    private int flightDaysLost;
    private int lostGoods;
    private Map<Integer, Shot> numberToShot;
    private CombatZoneState2 combatZoneState2;
    private boolean batteryUsed;
    private Map<String, Double> playerToStrength;
    private int removedGoods;
    private int indexOfIncomingShot;
    private int dice1;
    private int dice2;

    public CombatZoneCard2(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int lostGoods, Map<Integer, Shot> numberToShot) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.numberToShot = numberToShot;
        this.lostGoods = lostGoods;
        this.combatZoneState2 = new CombatZoneState2_1(this);
        this.batteryUsed = false;
        this.playerToStrength = new HashMap<>();
        this.removedGoods= 0;
        this.dice1 = new Random().nextInt(6) + 1;
        this.dice2 = new Random().nextInt(6) + 1;
    }

    public void setNewDices() {
        game.getDices().rollDices();
        setDice1(game.getDices().getDice1());
        setDice2(game.getDices().getDice2());
    }

    public int getDice1() {
        return dice1;
    }

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    public int getIndexOfIncomingShot() {
        return indexOfIncomingShot;
    }

    public void setNextIndexOfShot() {
        indexOfIncomingShot++;
    }

    public int getRemovedGoods() {
        return removedGoods;
    }

    public boolean thereAreStillShots() {
        return numberToShot.size() > indexOfIncomingShot;
    }

    public void increaseRemovedGoods() {
        removedGoods++;
    }

    public Map<String, Double> getPlayerToStrength() {
        return playerToStrength;
    }

    public void setCombatZoneState(CombatZoneState2 combatZoneState2) {
        this.combatZoneState2 = combatZoneState2;
    }

    public void setBatteryUsed(boolean batteryUsed) {
        this.batteryUsed = batteryUsed;
    }

    public boolean isBatteryUsed(){
        return batteryUsed;
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getLostGoods() {
        return lostGoods;
    }

    public Map<Integer, Shot> getNumberToShot() {
        return numberToShot;
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        combatZoneState2.activateEffect(inputCommand);
    }

    @Override
    public String getStateName() {
        return combatZoneState2.getStateName();
    }
}
