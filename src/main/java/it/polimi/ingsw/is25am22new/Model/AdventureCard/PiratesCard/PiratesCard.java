package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.Map;

public class PiratesCard extends AdventureCard {

    private Map<Integer, Shot> numberToShot;
    private int flightDaysLost;
    private int cannonStrength;
    private int credits;

    public PiratesCard(String pngName, String name, Game game, int level, boolean tutorial, Map<Integer, Shot> numberToShot, int flightDaysLost, int cannonStrength, int credits) {
        super(pngName, name, game, level, tutorial);
        this.numberToShot = numberToShot;
        this.flightDaysLost = flightDaysLost;
        this.cannonStrength = cannonStrength;
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

    public Map<Integer, Shot> getNumberToShot() {
        return numberToShot;
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getCannonStrength() {
        return cannonStrength;
    }

    public int getCredits() {
        return credits;
    }
}
