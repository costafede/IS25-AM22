package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Game;

import java.util.List;

public class SlavesCard extends AdventureCard {

    private int flightDaysLost;
    private int cannonStrength;
    private int lostAstronauts;
    private int credits;

    public SlavesCard(String name, Game game, int flightDaysLost, int cannonStrength, int lostAstronauts, int credits) {
        super(name, game);
        this.flightDaysLost = flightDaysLost;
        this.cannonStrength = cannonStrength;
        this.lostAstronauts = lostAstronauts;
        this.credits = credits;
    }

    @Override
    public void activateCard(List<String> orderedPlayers) {
        //to be implemented
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
        return;
    }
}
