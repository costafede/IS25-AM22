package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;
import java.util.Map;

public class PiratesCard extends AdventureCard{

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
        // When reading from json
        // true means Big shot, false means Small shot
        // shotSize is read from top to bottom
        // shots are always 3
    }

    @Override
    public void activateCard(List<String> orderedPlayers) {
        return;
    }

    @Override
    public void activateCard(String player) {
        return;
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields, List<String> activatingCannon) {
        return;
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields) {
        //to be implemented
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
