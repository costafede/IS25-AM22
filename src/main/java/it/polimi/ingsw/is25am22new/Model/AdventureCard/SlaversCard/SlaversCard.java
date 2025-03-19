package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class SlaversCard extends AdventureCard {

    private int flightDaysLost;
    private int cannonStrength;
    private int lostAstronauts;
    private int credits;

    public SlaversCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int cannonStrength, int lostAstronauts, int credits) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.cannonStrength = cannonStrength;
        this.lostAstronauts = lostAstronauts;
        this.credits = credits;
    }

    @Override
    public boolean activateCardPhase(String nickname, InputCommand inputCommand) {
        return true;
    }

    @Override
    public boolean checkActivationConditions(String nickname) {
        return true;
    }

    @Override
    public boolean receiveInputPhase(String nickname, InputCommand inputCommand) {
        return true;
    }

    @Override
    public void resolveCardEffectPhase(String nickname) {
        return;
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getCannonStrength() {
        return cannonStrength;
    }

    public int getLostAstronauts() {
        return lostAstronauts;
    }

    public int getCredits() {
        return credits;
    }
}
