package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class SlaversCard extends AdventureCard {

    private int flightDaysLost;
    private int cannonStrength;
    private int astronautsToLose;
    private int credits;
    private boolean batteryUsed;
    private SlaversState slaversState;
    private int selectedAstronauts;


    public SlaversCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int cannonStrength, int astronautsToLose, int credits) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.cannonStrength = cannonStrength;
        this.astronautsToLose = astronautsToLose;
        this.credits = credits;
        this.slaversState = new SlaversState_1(this);
        this.selectedAstronauts = 0;
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        slaversState.activateEffect(inputCommand);
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

    public int getSelectedAstronauts() {
        return selectedAstronauts;
    }

    public void increaseSelectedAstronauts () {
        selectedAstronauts++;
    }
}
