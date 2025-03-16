package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;
import java.util.Map;

public class CombatZoneCard extends AdventureCard{

    private int flightDaysLost;
    private int lostAstronauts;
    private int lostGoods;
    private Map<Integer, Shot> numberToShot;

    public CombatZoneCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int lostAstronauts, int lostGoods, Map<Integer, Shot> numberToShot) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.lostAstronauts = lostAstronauts;
        this.numberToShot = numberToShot;
        this.lostGoods = lostGoods;
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
        //to be implemented
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields) {
        return;
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getLostAstronauts() {
        return lostAstronauts;
    }

    public int getLostGoods() {
        return lostGoods;
    }

    public Map<Integer, Shot> getNumberToShot() {
        return numberToShot;
    }
}
