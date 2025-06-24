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

    /**
     * Constructs a new Planet object with the specified theoretical resource blocks.
     * @param theoreticalGoodblocks
     */
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

    /**
     * Returns the theoretical resource blocks associated with this planet.
     * @return
     */
    public Map<GoodBlock, Integer> getTheoreticalGoodblocks() {
        return theoreticalGoodblocks;
    }

    /**
     * Sets the theoretical resource blocks associated with this planet.
     * @param goodblock
     * @param qt
     */
    public void setTheoreticalGoodblocks(GoodBlock goodblock, int qt) {
        theoreticalGoodblocks.put(goodblock, qt);
    }

    /**
     * Returns the actual resource blocks associated with this planet.
     * @return
     */
    public Map<GoodBlock, Integer> getActualGoodblocks() {
        return actualGoodblocks;
    }

    /**
     * Sets the actual resource blocks associated with this planet.
     * @param goodblock
     * @param qt
     */
    public void setActualGoodblocks(GoodBlock goodblock, int qt) {
        actualGoodblocks.put(goodblock, qt);
    }

    /**
     * Returns the player who has landed on this planet.
     * @return
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Sets the player who has landed on this planet.
     * @param player
     */
    public void setPlayer(String player) {
        if(this.player != null)
            throw new IllegalArgumentException("A player already landed");
        this.player = player;
    }

    /**
     * Returns true if the player associated with this planet is not null, false otherwise.
     * @return
     */
    public boolean playerPresent(){
        return player != null;
    }

}
