package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a planet within the game, which holds information about resource blocks and player interactions.
 *
 * The planet is associated with theoretical (expected) and actual quantities of specific resources (GoodBlock).
 * Each planet can be occupied by a single player, which signifies a player landing on the planet.
 *
 * This class manages operations related to setting and retrieving the resource allocations and
 * the player who has landed on the planet.
 */
public class Planet implements Serializable {
    private Map<GoodBlock, Integer> theoreticalGoodblocks;

    private Map<GoodBlock, Integer> actualGoodblocks;

    private String player;

    public Planet(Map<GoodBlock, Integer> theoreticalGoodblocks) {
        this.theoreticalGoodblocks = theoreticalGoodblocks;
        this.actualGoodblocks = new HashMap<GoodBlock, Integer>();
        for(GoodBlock gb : GoodBlock.values()) {
            if(!theoreticalGoodblocks.containsKey(gb)) {
                theoreticalGoodblocks.put(gb, 0);
            }
        }
        actualGoodblocks.put(GoodBlock.BLUEBLOCK, 0);
        actualGoodblocks.put(GoodBlock.YELLOWBLOCK, 0);
        actualGoodblocks.put(GoodBlock.GREENBLOCK, 0);
        actualGoodblocks.put(GoodBlock.REDBLOCK, 0);
    }

    public Map<GoodBlock, Integer> getTheoreticalGoodblocks() {
        return theoreticalGoodblocks;
    }

    public void setTheoreticalGoodblocks(GoodBlock goodblock, int qt) {
        theoreticalGoodblocks.put(goodblock, qt);
    }

    public Map<GoodBlock, Integer> getActualGoodblocks() {
        return actualGoodblocks;
    }

    public void setActualGoodblocks(GoodBlock goodblock, int qt) {
        actualGoodblocks.put(goodblock, qt);
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        if(this.player != null)
            throw new IllegalArgumentException("A player already landed");
        this.player = player;
    }

    public boolean playerPresent(){
        return player != null;
    }

}
