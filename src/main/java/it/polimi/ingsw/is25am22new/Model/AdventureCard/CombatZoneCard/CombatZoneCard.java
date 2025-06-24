package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Client.View.TUI.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Client.View.GUI.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents a Combat Zone card in the game, which is a type of Adventure Card that handles combat encounters.
 * This class manages combat-related mechanics including dice rolls, shots, flight days lost, and astronaut losses.
 *
 * The card maintains various states during combat and tracks:
 * - Flight days that will be lost due to combat
 * - Number of astronauts that can be lost in combat
 * - Combat shots and their mapping to dice roll numbers
 * - Current combat zone state
 * - Battery usage status
 * - Player strength values
 * - Number of removed crew members
 * - Combat dice rolls
 *
 * @see AdventureCard
 * @see CombatZoneState
 */
public class CombatZoneCard extends AdventureCard implements Serializable, ViewableCard {

    private int flightDaysLost;
    private int astronautsToLose;
    private Map<Integer, Shot> numberToShot;
    private CombatZoneState combatZoneState;
    private boolean batteryUsed;
    private Map<String, Double> playerToStrength;
    private int removedMembers;
    private int indexOfIncomingShot;
    private int dice1;
    private int dice2;

    /**
     * Constructor for the CombatZoneCard class.
     * @param pngName
     * @param name
     * @param game
     * @param level
     * @param tutorial
     * @param flightDaysLost
     * @param astronautsToLose
     * @param numberToShot
     */
    public CombatZoneCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int astronautsToLose, Map<Integer, Shot> numberToShot) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.astronautsToLose = astronautsToLose;
        this.numberToShot = numberToShot;
        this.combatZoneState = new CombatZoneState_0(this);
        this.batteryUsed = false;
        this.playerToStrength = new HashMap<>();
        this.removedMembers = 0;
        this.dice1 = new Random().nextInt(6) + 1;
        this.dice2 = new Random().nextInt(6) + 1;
    }

    /**
     * Getter for the dice1 attribute.
     */
    public void setNewDices() {
        game.getDices().rollDices();
        setDice1(game.getDices().getDice1());
        setDice2(game.getDices().getDice2());
        getObservableModel().updateAllDices(game.getDices());
    }

    /**
     * Getter for the dice2 attribute.
     * @return
     */
    public int getDice1() {
        return dice1;
    }

    /**
     * Setter for the dice1 attribute.
     * @param dice1
     */
    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    /**
     * Getter for the dice2 attribute.
     * @return
     */
    public int getDice2() {
        return dice2;
    }

    /**
     * Setter for the dice2 attribute.
     * @param dice2
     */
    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    /**
     * Getter for the indexOfIncomingShot attribute.
     * @return
     */
    public int getIndexOfIncomingShot() {
        return indexOfIncomingShot;
    }

    /**
     * Setter for the indexOfIncomingShot attribute.
     */
    public void setNextIndexOfShot() {
        indexOfIncomingShot++;
    }

    /**
     * Getter for the removedMembers attribute.
     * @return
     */
    public int getRemovedMembers() {
        return removedMembers;
    }

    /**
     * Increases the removedMembers attribute by 1.
     * @return
     */
    public boolean thereAreStillShots() {
        return numberToShot.size() > indexOfIncomingShot;
    }

    /**
     * Increases the removedMembers attribute by 1.
     */
    public void increaseRemovedMembers() {
        removedMembers++;
    }

    /**
     * Getter for the playerToStrength attribute.
     * @return
     */
    public Map<String, Double> getPlayerToStrength() {
        return playerToStrength;
    }

    /**
     * Setter for the playerToStrength attribute.
     * @param combatZoneState
     */
    public void setCombatZoneState(CombatZoneState combatZoneState) {
        this.combatZoneState = combatZoneState;
    }

    /**
     * Setter for the batteryUsed attribute.
     * @param batteryUsed
     */
    public void setBatteryUsed(boolean batteryUsed) {
        this.batteryUsed = batteryUsed;
    }

    /**
     * Getter for the batteryUsed attribute.
     * @return
     */
    public boolean isBatteryUsed(){
        return batteryUsed;
    }

    /**
     * Getter for the flightDaysLost attribute.
     * @return
     */
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    /**
     * Getter for the astronautsToLose attribute.
     * @return
     */
    public int getAstronautsToLose() {
        return astronautsToLose;
    }

    /**
     * Getter for the numberToShot attribute.
     * @return
     */
    public Map<Integer, Shot> getNumberToShot() {
        return numberToShot;
    }

    /**
     * Activates the effect of the current state.
     * @param inputCommand the input provided by the player or controller
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        combatZoneState.activateEffect(inputCommand);
    }

    /**
     * Getter for the combatZoneState attribute.
     * @return
     */
    @Override
    public String getStateName() {
        return combatZoneState.getStateName();
    }

    /**
     * Shows the card in the game or in the TUI.
     * @param view
     * @param model
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showCombatZoneCardInGame(this);
        }
        else{
            view.showCombatZoneCard(this);
        }
    }
}