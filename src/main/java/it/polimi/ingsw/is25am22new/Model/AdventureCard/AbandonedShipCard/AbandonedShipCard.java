package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class AbandonedShipCard extends AdventureCard {

    private int flightdaysLost;
    private int credits;
    private int lostAstronauts;
    private AbandonedShipState abandonedShipState;

    public int getFlightDaysLost() {
        return flightdaysLost;
    }

    public int getCredits() {
        return credits;
    }

    public int getLostAstronauts() {
        return lostAstronauts;
    }

    public AbandonedShipCard(String pngName, String name, Game game, int level, boolean tutorial, int flightdaysLost, int credits, int lostAstronauts) {
        super(pngName, name, game, level, tutorial);
        this.credits = credits;
        this.flightdaysLost = flightdaysLost;
        this.lostAstronauts = lostAstronauts;
        this.abandonedShipState = new AbandonedShipState_1(this);
    }


    @Override
    public void activateEffect(InputCommand inputCommand) {
        abandonedShipState.activateEffect(inputCommand);
    }

    public void setAbandonedShipState(AbandonedShipState abandonedShipState) {
        this.abandonedShipState = abandonedShipState;
    }
}
