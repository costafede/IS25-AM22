package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Game;

import java.util.List;
import java.util.Map;

public class PiratesCard extends AdventureCard{

    private Map<Shot, Map<Integer, Orientation>> shotTypeToShotToOrientation;
    private int flightDaysLost;
    private int cannonStrength;
    private int credits;

    public PiratesCard(String name, Game game, Map<Shot, Map<Integer, Orientation>> shotTypeToShotToOrientation, int flightDaysLost, int cannonStrength, int credits) {
        super(name, game);
        this.shotTypeToShotToOrientation = shotTypeToShotToOrientation;
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
