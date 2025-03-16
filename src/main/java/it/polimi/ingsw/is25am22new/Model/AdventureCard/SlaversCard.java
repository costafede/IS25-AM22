package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;

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
