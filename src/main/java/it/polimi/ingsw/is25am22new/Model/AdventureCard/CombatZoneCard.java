package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;
import java.util.Map;

public class CombatZoneCard extends AdventureCard{

    private int flightDaysLost;
    private int lostAstronauts;
    private int lostGoods;
    private Map<Integer, Shot> numberToShot;

    public CombatZoneCard(String name, Game game, int flightDaysLost, int lostAstronauts, Map<Integer, Shot> numberToShot) {
        super(name, game);
        this.flightDaysLost = flightDaysLost;
        this.lostAstronauts = lostAstronauts;
        this.numberToShot = numberToShot;
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
}
