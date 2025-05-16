package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Client.View.AdventureCardView;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

/**
 * Represents a meteor swarm card in the game, extending the functionalities of an AdventureCard.
 * This card manages the state and behavior associated with a swarm of meteors during gameplay.
 * It interacts with the game's dice mechanics and maintains state data related to meteors.
 */
public class MeteorSwarmCard extends AdventureCard implements Serializable, ViewableCard {

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
        game.getDices().rollDices();
        this.dice1 = game.getDices().getDice1();
        this.dice2 = game.getDices().getDice2();
    }

    @Override
    public void activateEffect(InputCommand command) {
        meteorSwarmState.activateEffect(command);
    }

    @Override
    public String getStateName() {
        return meteorSwarmState.getStateName();
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

    @Override
    public void show(AdventureCardView view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showMeteorSwarmCardInGame(this);
        }
        else{
            view.showMeteorSwarmCard(this);
        }
    }
}
