package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Client.View.TUI.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Client.View.GUI.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

/**
 * The SlaversCard class represents an AdventureCard specifically modeled for
 * interactions involving slavers in the game. This class implements the
 * ViewableCard interface, which includes the ability to render the card using
 * a graphical view.
 *
 * A SlaversCard includes attributes to define its specific effects such as
 * the number of flight days lost, cannon strength needed to overcome the event,
 * astronauts to lose, and credits gained. It also manages its internal state
 * via the SlaversState pattern to handle the progression of its functionality.
 */
public class SlaversCard extends AdventureCard implements Serializable, ViewableCard {

    private int flightDaysLost;
    private int cannonStrength;
    private int astronautsToLose;
    private int credits;
    private boolean batteryUsed;
    private SlaversState slaversState;
    private int selectedMembers;

    /**
     * Constructor for the SlaversCard class.
     * @param pngName
     * @param name
     * @param game
     * @param level
     * @param tutorial
     * @param flightDaysLost
     * @param cannonStrength
     * @param astronautsToLose
     * @param credits
     */
    public SlaversCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int cannonStrength, int astronautsToLose, int credits) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.cannonStrength = cannonStrength;
        this.astronautsToLose = astronautsToLose;
        this.credits = credits;
        this.slaversState = new SlaversState_1(this);
        this.selectedMembers = 0;
    }

    /**
     * Activates the SlaversCard's state-specific effect based on the provided input.
     * @param inputCommand the input provided by the player or controller
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        slaversState.activateEffect(inputCommand);
    }

    /**
     * Returns the name of the current state of the SlaversCard.
     * @return
     */
    @Override
    public String getStateName() {
        return slaversState.getStateName();
    }

    /**
     * Returns the number of flight days lost by the SlaversCard.
     * @return
     */
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    /**
     * Returns the cannon strength needed to overcome the event.
     * @return
     */
    public int getCannonStrength() {
        return cannonStrength;
    }

    /**
     * Returns the number of astronauts to lose in the event.
     * @return
     */
    public int getAstronautsToLose() {
        return astronautsToLose;
    }

    /**
     * Returns the number of credits gained by the SlaversCard.
     * @return
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the current state of the SlaversCard to the provided state.
     * @param slaversState
     */
    public void setSlaversState(SlaversState slaversState) {
        this.slaversState = slaversState;
    }

    /**
     * Returns whether the SlaversCard's battery has been used or not.
     * @return
     */
    public boolean isBatteryUsed() {
        return batteryUsed;
    }

    /**
     * Sets whether the SlaversCard's battery has been used or not.
     * @param batteryUsed
     */
    public void setBatteryUsed(boolean batteryUsed) {
        this.batteryUsed = batteryUsed;
    }

    /**
     * Resets the number of selected members to 0.
     */
    public void resetSelectedMembers() {
        selectedMembers = 0;
    }

    /**
     * Returns the number of selected members.
     * @return
     */
    public int getSelectedMembers() {
        return selectedMembers;
    }

    /**
     * Increases the number of selected members by 1.
     */
    public void increaseSelectedMembers () {
        selectedMembers++;
    }

    /**
     * Decreases the number of selected members by 1.
     * @param view
     * @param model
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showSlaversCardInGame(this);
        }
        else{
            view.showSlaversCard(this);
        }
    }
}
