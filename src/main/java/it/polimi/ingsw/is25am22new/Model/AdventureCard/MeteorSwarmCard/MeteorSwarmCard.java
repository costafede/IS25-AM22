package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.Map;
import java.util.Random;

public class MeteorSwarmCard extends AdventureCard {

    private Map<Integer, Meteor> numberToMeteor;
    private MeteorSwarmState meteorSwarmState;
    private int indexOfIncomingMeteor;
    private boolean batteryUsed;
    private int dice1;
    private int dice2;

    public MeteorSwarmCard(String pngName, String name, Game game, int level, boolean tutorial, Map<Integer, Meteor> numberToMeteor) {
        super(pngName, name, game, level, tutorial);
        this.numberToMeteor = numberToMeteor;
        this.meteorSwarmState = new MeteorSwarmState_1(this);
        this.indexOfIncomingMeteor = 0;
        this.batteryUsed = false;
        this.dice1 = new Random().nextInt(6) + 1;
        this.dice2 = new Random().nextInt(6) + 1;
    }

    @Override
    public void activateEffect(InputCommand command) {
        meteorSwarmState.activateEffect(command);
    }

    public Map<Integer, Meteor> getNumberToMeteor() {
        return numberToMeteor;
    }

    public int getDice1(){
        return dice1;
    }

    public int getDice2(){
        return dice2;
    }

    public void setDice1(int dice1){
        this.dice1 = dice1;
    }

    public void setDice2(int dice2){
        this.dice2 = dice2;
    }

    public boolean thereAreStillMeteors() {
        return indexOfIncomingMeteor < numberToMeteor.size();
    }

    public int getIndexOfIncomingMeteor() {
        return indexOfIncomingMeteor;
    }

    public void setNextIndexOfMeteor() {
        indexOfIncomingMeteor++;
    }

    public void setMeteorSwarmState(MeteorSwarmState meteorSwarmState) {
        this.meteorSwarmState = meteorSwarmState;
    }

    public void setBatteryUsed(boolean batteryUsed) {
        this.batteryUsed = batteryUsed;
    }

    public boolean isBatteryUsed() {
        return batteryUsed;
    }
}
