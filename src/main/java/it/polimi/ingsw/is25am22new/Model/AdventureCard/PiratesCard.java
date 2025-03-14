package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Game;

import java.util.List;
import java.util.Map;

public class PiratesCard extends AdventureCard{

    private Map<Integer, Shot> numberToShot;
    private int flightDaysLost;
    private int cannonStrength;
    private int credits;

    public PiratesCard(String name, Game game, Map<Integer, Shot> numberToShot, int flightDaysLost, int cannonStrength, int credits) {
        super(name, game);
        this.numberToShot = numberToShot;
        this.flightDaysLost = flightDaysLost;
        this.cannonStrength = cannonStrength;
        this.credits = credits;
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
}
