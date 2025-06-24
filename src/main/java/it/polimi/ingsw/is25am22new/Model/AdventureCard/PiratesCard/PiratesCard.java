package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Client.View.TUI.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Client.View.GUI.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;


import java.io.Serializable;
import java.util.*;

/**
 * Represents the PiratesCard which extends the AdventureCard class and implements
 * Serializable and ViewableCard. This card includes functionality specific to pirates'
 * interactions and effects in the game.
 *
 * A PiratesCard has attributes related to shots, flight days lost, cannon strength,
 * credits, state, dice values, and tracking defeated players. Additionally, it works
 * within game logic by transitioning between different states and handling game-related
 * effects.
 */
public class PiratesCard extends AdventureCard implements Serializable, ViewableCard {

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

    /**
     * Constructor for the PiratesCard class.
     * @param pngName
     * @param name
     * @param game
     * @param level
     * @param tutorial
     * @param numberToShot
     * @param flightDaysLost
     * @param cannonStrength
     * @param credits
     */
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
        getObservableModel().updateAllDices(game.getDices());
    }

    /**
     * Getter for the dice1 attribute.
     * @return
     */
    public int getDice1() {
        return dice1;
    }

    /**
     * Getter for the dice2 attribute.
     * @return
     */
    public int getDice2() {
        return dice2;
    }

    /**
     * Setter for the dice1 attribute.
     * @param dice1
     */
    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    /**
     * Setter for the dice2 attribute.
     * @param dice2
     */
    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    /**
     * Setter for the game attribute.
     */
    public void setNewDices() {
        game.getDices().rollDices();
        setDice1(game.getDices().getDice1());
        setDice2(game.getDices().getDice2());
        getObservableModel().updateAllDices(game.getDices());
    }

    /**
     * Getter for the piratesState attribute.
     * @return
     */
    public int getIndexOfIncomingShot(){
        return indexOfIncomingShot;
    }

    /**
     * Setter for the indexOfIncomingShot attribute.
     */
    public void setNextIndexOfShot() {
        indexOfIncomingShot++;
    }

    /**
     * Setter for the piratesState attribute.
     * @param piratesState
     */
    public void setPiratesState(PiratesState piratesState) {
        this.piratesState = piratesState;
    }

    /**
     * Getter for the numberToShot attribute.
     * @return
     */
    public Map<Integer, Shot> getNumberToShot() {
        return numberToShot;
    }

    /**
     * Getter for the flightDaysLost attribute.
     * @return
     */
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    /**
     * Getter for the cannonStrength attribute.
     * @return
     */
    public int getCannonStrength() {
        return cannonStrength;
    }

    /**
     * Getter for the credits attribute.
     * @return
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Setter for the credits attribute.
     * @return
     */
    public boolean isBatteryUsed() {
        return batteryUsed;
    }

    /**
     * Getter for the defeatedPlayers attribute.
     * @return
     */
    public List<String> getDefeatedPlayers() {
        return defeatedPlayers;
    }

    /**
     * Adds a nickname to the defeatedPlayers list.
     * @param nickname
     */
    public void addDefeatedPlayer(String nickname) {
        this.defeatedPlayers.add(nickname);
    }

    /**
     * Setter for the batteryUsed attribute.
     * @param usedBattery
     */
    public void setBatteryUsed(boolean usedBattery) {this.batteryUsed= usedBattery;}

    /**
     * Activates the effect of the current state of the PiratesCard.
     * @param inputCommand the input provided by the player or controller
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        piratesState.activateEffect(inputCommand);
    }

    /**
     * Getter for the current state of the PiratesCard.
     * @return
     */
    @Override
    public String getStateName() {
        return piratesState.getStateName();
    }

    /**
     * Getter for the current nickname of the player who has been defeated.
     * @return
     */
    public String getCurrDefeatedPlayer() {
        return currDefeatedPlayer;
    }

    /**
     * Getter for the last nickname of the player who has been defeated.
     * @return
     */
    public String getLastDefeatedPlayer() {
        return defeatedPlayers.getLast();
    }

    /**
     * Setter for the current nickname of the player who has been defeated.
     */
    public void setCurrDefeatedPlayerToFirst() {
        this.currDefeatedPlayer = defeatedPlayers.getFirst();
    }

    /**
     * Returns true if there are still shots to be fired, false otherwise.
     * @return
     */
    public boolean thereAreStillShots() {
        return indexOfIncomingShot < numberToShot.size();
    }

    /**
     * Setter for the current nickname of the player who has been defeated.
     */
    public void setCurrDefeatedPlayerToNext() {
        this.currDefeatedPlayer = defeatedPlayers.get(defeatedPlayers.indexOf(currDefeatedPlayer) + 1);
    }

    /**
     * Setter for the current nickname of the player who has been defeated.
     * @param view
     * @param model
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showPiratesCardInGame(this);
        }
        else{
            view.showPiratesCard(this);
        }
    }
}
