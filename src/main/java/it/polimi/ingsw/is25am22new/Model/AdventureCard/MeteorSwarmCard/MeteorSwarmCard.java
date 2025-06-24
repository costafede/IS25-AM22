package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Client.View.TUI.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Client.View.GUI.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;
import java.util.Map;

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

    /**
     * Constructor for the MeteorSwarmCard class.
     * @param pngName
     * @param name
     * @param game
     * @param level
     * @param tutorial
     * @param numberToMeteor
     */
    public MeteorSwarmCard(String pngName, String name, Game game, int level, boolean tutorial, Map<Integer, Meteor> numberToMeteor) {
        super(pngName, name, game, level, tutorial);
        this.numberToMeteor = numberToMeteor;
        this.meteorSwarmState = new MeteorSwarmState_1(this);
        this.indexOfIncomingMeteor = 0;
        this.batteryUsed = false;
        game.getDices().rollDices();
        this.dice1 = game.getDices().getDice1();
        this.dice2 = game.getDices().getDice2();
        getObservableModel().updateAllDices(game.getDices());
    }

    /**
     * Sets the new dice values for the meteor swarm card.
     * @param command the input provided by the player or controller
     */
    @Override
    public void activateEffect(InputCommand command) {
        meteorSwarmState.activateEffect(command);
    }

    /**
     * Returns the current state name of the meteor swarm card.
     * @return
     */
    @Override
    public String getStateName() {
        return meteorSwarmState.getStateName();
    }

    /**
     * Returns the meteor swarm card's meteor map.
     * @return
     */
    public Map<Integer, Meteor> getNumberToMeteor() {
        return numberToMeteor;
    }

    /**
     * Returns the meteor swarm card's current meteor state.
     * @return
     */
    public int getDice1(){
        return dice1;
    }

    /**
     * Returns the meteor swarm card's current meteor state.
     * @return
     */
    public int getDice2(){
        return dice2;
    }

    /**
     * Sets the meteor swarm card's current meteor state.
     * @param dice1
     */
    public void setDice1(int dice1){
        this.dice1 = dice1;
    }

    /**
     * Sets the meteor swarm card's current meteor state.
     * @param dice2
     */
    public void setDice2(int dice2){
        this.dice2 = dice2;
    }

    /**
     * Sets the new dice values for the meteor swarm card.
     * @return
     */
    public boolean thereAreStillMeteors() {
        return indexOfIncomingMeteor < numberToMeteor.size();
    }


    /**
     * Returns the index of the next meteor to be activated.
     * @return
     */
    public int getIndexOfIncomingMeteor() {
        return indexOfIncomingMeteor;
    }

    /**
     * Sets the index of the next meteor to be activated.
     */
    public void setNextIndexOfMeteor() {
        indexOfIncomingMeteor++;
    }

    /**
     * Sets the meteor swarm card's current meteor state.
     * @param meteorSwarmState
     */
    public void setMeteorSwarmState(MeteorSwarmState meteorSwarmState) {
        this.meteorSwarmState = meteorSwarmState;
    }

    /**
     * Sets the meteor swarm card's current meteor state.
     * @param batteryUsed
     */
    public void setBatteryUsed(boolean batteryUsed) {
        this.batteryUsed = batteryUsed;
    }

    /**
     * Returns the meteor swarm card's current meteor state.
     * @return
     */
    public boolean isBatteryUsed() {
        return batteryUsed;
    }

    /**
     * Shows the meteor swarm card in the game's GUI or TUI.
     * @param view
     * @param model
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showMeteorSwarmCardInGame(this);
        }
        else{
            view.showMeteorSwarmCard(this);
        }
    }
}
