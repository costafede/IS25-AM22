package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

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
 * Represents a Combat Zone Card in the game, extending the AdventureCard class.
 * This card introduces specific effects and states during gameplay and tracks
 * properties such as flight days lost, goods lost, shots, and battery usage.
 * The card operates within a specific state determined by the CombatZoneState2
 * instance and provides functionality for handling dice rolls, player strengths,
 * and shot-related operations.
 *
 * Basically it's the same as the CombatZoneCard, but with different outcomes in the conditions to verify in the states
 */
public class CombatZoneCard2 extends AdventureCard implements Serializable, ViewableCard {

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

    /**
     * Constructor for the CombatZoneCard2 class.
     * @param pngName
     * @param name
     * @param game
     * @param level
     * @param tutorial
     * @param flightDaysLost
     * @param lostGoods
     * @param numberToShot
     */
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
     * Getter for the removedGoods attribute.
     * @return
     */
    public int getRemovedGoods() {
        return removedGoods;
    }

    /**
     * Getter for the numberToShot attribute.
     * @return
     */
    public boolean thereAreStillShots() {
        return numberToShot.size() > indexOfIncomingShot;
    }

    /**
     * Increases the removedGoods attribute by 1.
     */
    public void increaseRemovedGoods() {
        removedGoods++;
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
     * @param combatZoneState2
     */
    public void setCombatZoneState(CombatZoneState2 combatZoneState2) {
        this.combatZoneState2 = combatZoneState2;
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
     * Getter for the lostGoods attribute.
     * @return
     */
    public int getLostGoods() {
        return lostGoods;
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
        combatZoneState2.activateEffect(inputCommand);
    }

    /**
     * Getter for the combatZoneState2 attribute.
     * @return
     */
    @Override
    public String getStateName() {
        return combatZoneState2.getStateName();
    }

    /**
     * Shows the card in the game or in the TUI.
     * @param view
     * @param model
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showCombatZoneCard2InGame(this);
        }
        else{
            view.showCombatZoneCard2(this);
        }
    }
}
