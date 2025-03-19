package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.Map;

public class Planet {
    private Map<GoodBlock, Integer> goodblocks;

    private String player;

    public Map<GoodBlock, Integer> getGoodblocks() {
        return goodblocks;
    }

    public void setGoodblocks(GoodBlock goodblock, int qt) {
        goodblocks.put(goodblock, qt);
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public boolean playerPresent(){
        return player != null;
    }
}
