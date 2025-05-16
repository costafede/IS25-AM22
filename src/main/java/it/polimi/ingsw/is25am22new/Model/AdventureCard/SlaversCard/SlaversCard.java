package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Client.View.AdventureCardView;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.ViewableCard;
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


    public SlaversCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int cannonStrength, int astronautsToLose, int credits) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.cannonStrength = cannonStrength;
        this.astronautsToLose = astronautsToLose;
        this.credits = credits;
        this.slaversState = new SlaversState_1(this);
        this.selectedMembers = 0;
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        slaversState.activateEffect(inputCommand);
    }

    @Override
    public String getStateName() {
        return slaversState.getStateName();
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getCannonStrength() {
        return cannonStrength;
    }

    public int getAstronautsToLose() {
        return astronautsToLose;
    }

    public int getCredits() {
        return credits;
    }

    public void setSlaversState(SlaversState slaversState) {
        this.slaversState = slaversState;
    }

    public boolean isBatteryUsed() {
        return batteryUsed;
    }

    public void setBatteryUsed(boolean batteryUsed) {
        this.batteryUsed = batteryUsed;
    }

    public void resetSelectedMembers() {
        selectedMembers = 0;
    }

    public int getSelectedMembers() {
        return selectedMembers;
    }

    public void increaseSelectedMembers () {
        selectedMembers++;
    }

    @Override
    public void show(AdventureCardView view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showSlaversCardInGame(this);
        }
        else{
            view.showSlaversCard(this);
        }
    }
}
