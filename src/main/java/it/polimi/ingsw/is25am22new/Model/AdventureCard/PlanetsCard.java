package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.List;
import java.util.Map;

public class PlanetsCard extends AdventureCard{
    private Map<Integer, List<GoodBlock>> planetToGoodBlocks;
    private int flightDaysLost;

    public PlanetsCard(String pngName, String name, Game game, int level, boolean tutorial, Map<Integer, List<GoodBlock>> planetToGoodBlocks, int flightDaysLost) {
        super(pngName, name, game, level, tutorial);
        this.planetToGoodBlocks = planetToGoodBlocks;
        this.flightDaysLost = flightDaysLost;
        // When reading the json file
        // Read numOfPlanets first, then based on that read which blocks are stored
        // example: 2 planets: firstPlanetGoods, secondPlanetGoods
    }

    @Override
    public void activateCard(List<String> orderedPlayers) {
        for(String player : orderedPlayers) {
            handlePlayerLanding(player);
        }
    }

    private void handlePlayerLanding(String player) {
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

    public Map<Integer, List<GoodBlock>> getPlanetToGoodBlocks() {
        return planetToGoodBlocks;
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }
}
