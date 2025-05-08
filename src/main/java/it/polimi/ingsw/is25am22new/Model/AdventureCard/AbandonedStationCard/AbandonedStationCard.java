package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AbandonedStationCard extends AdventureCard implements Serializable {

    private int flightDaysLost;
    private int astronautsNumber;
    private AbandonedStationState abandonedStationState;
    private Map<GoodBlock, Integer> theoreticalGoodBlocks;
    private Map<GoodBlock, Integer> actualGoodBlocks;


    public AbandonedStationCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int astronautsNumber, Map<GoodBlock, Integer> theoreticalGoodBlocks) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.astronautsNumber = astronautsNumber;
        this.theoreticalGoodBlocks = theoreticalGoodBlocks;
        for(GoodBlock gb : GoodBlock.values()) {
            if(!theoreticalGoodBlocks.containsKey(gb)) {
                theoreticalGoodBlocks.put(gb, 0);
            }
        }
        this.abandonedStationState = new AbandonedStationState_1(this);
        this.actualGoodBlocks = new HashMap<GoodBlock, Integer>();
        actualGoodBlocks.put(GoodBlock.BLUEBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.YELLOWBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.GREENBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.REDBLOCK, 0);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        abandonedStationState.activateEffect(inputCommand);
    }

    @Override
    public String getStateName() {
        return abandonedStationState.getStateName();
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getAstronautsNumber() {
        return astronautsNumber;
    }

    public Map<GoodBlock, Integer> getTheoreticalGoodBlocks() {
        return theoreticalGoodBlocks;
    }

    public void setAbandonedStationState(AbandonedStationState abandonedStationState) {
        this.abandonedStationState = abandonedStationState;
    }

    public void loadStation(){
        for(GoodBlock gb : GoodBlock.values()){
            for(int goodBlocksToLoad = theoreticalGoodBlocks.get(gb); goodBlocksToLoad > 0 && game.getBank().withdrawGoodBlock(gb); goodBlocksToLoad--){
                actualGoodBlocks.put(gb, actualGoodBlocks.get(gb) + 1);
            }
        }
    }

    public void unloadStation(){
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
