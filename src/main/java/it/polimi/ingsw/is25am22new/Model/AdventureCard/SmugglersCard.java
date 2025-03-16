package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.List;

public class SmugglersCard extends AdventureCard {

    private int flightDaysLost;
    private int CannonStrength;
    private int lostGoods;
    private List<GoodBlock> goodBlocks;

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getCannonStrength() {
        return CannonStrength;
    }

    public int getLostGoods() {
        return lostGoods;
    }

    public List<GoodBlock> getGoodBlocks() {
        return goodBlocks;
    }

    public SmugglersCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int cannonStrength, int lostGoods, List<GoodBlock> goodBlocks) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.CannonStrength = cannonStrength;
        this.lostGoods = lostGoods;
        this.goodBlocks = goodBlocks;
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
