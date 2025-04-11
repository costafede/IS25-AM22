package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SmugglersCard extends AdventureCard implements Serializable {

    private int flightDaysLost;
    private int CannonStrength;
    private int lostGoods;
    private Map<GoodBlock, Integer> theoreticalGoodBlocks;
    private SmugglersState smugglersState;
    protected Map<GoodBlock, Integer> actualGoodBlocks;

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getCannonStrength() {
        return CannonStrength;
    }

    public int getLostGoods() {
        return lostGoods;
    }

    public Map<GoodBlock, Integer> getTheoreticalGoodBlocks() {
        return theoreticalGoodBlocks;
    }

    public SmugglersCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int cannonStrength, int lostGoods, Map<GoodBlock, Integer> theoreticalGoodBlocks) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.CannonStrength = cannonStrength;
        this.lostGoods = lostGoods;
        this.theoreticalGoodBlocks = theoreticalGoodBlocks;
        this.smugglersState = new SmugglersState_1(this);
        this.actualGoodBlocks = new HashMap<GoodBlock, Integer>();
        actualGoodBlocks.put(GoodBlock.BLUEBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.YELLOWBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.GREENBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.REDBLOCK, 0);
    }

    public void activateEffect(InputCommand inputCommand){
        smugglersState.activateEffect(inputCommand);
    }

    public void setSmugglersState(SmugglersState smugglersState){
        this.smugglersState = smugglersState;
    }


    public void loadSmugglers(){
        for(GoodBlock gb : GoodBlock.values()){
            for(int goodBlocksToLoad = theoreticalGoodBlocks.get(gb); goodBlocksToLoad > 0 && game.getBank().withdrawGoodBlock(gb); goodBlocksToLoad--){
                actualGoodBlocks.put(gb, actualGoodBlocks.get(gb) + 1);
            }
        }
    }

    public void unloadSmugglers(){
        for(GoodBlock gb : GoodBlock.values()){
            for(int goodBlocksToUnload = actualGoodBlocks.get(gb); goodBlocksToUnload > 0; goodBlocksToUnload--){
                game.getBank().depositGoodBlock(gb);
            }
        }
    }

    public Map<GoodBlock, Integer> getActualGoodBlocks() {
        return actualGoodBlocks;
    }
}
