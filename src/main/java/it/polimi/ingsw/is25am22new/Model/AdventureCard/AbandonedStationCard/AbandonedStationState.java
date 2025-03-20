package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.Map;

public abstract class AbandonedStationState {
    protected AbandonedStationCard abandonedStationCard;
    protected Game game;
    protected Map<GoodBlock, Integer> actualGoodBlocks;

    public AbandonedStationState(AbandonedStationCard abandonedStationCard) {
        this.abandonedStationCard = abandonedStationCard;
        game = abandonedStationCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);

    public abstract void transition(AbandonedStationState abandonedStationState);

    public void loadStation(){
        Map<GoodBlock, Integer> theoreticalGoodBlocks = abandonedStationCard.getTheoreticalGoodBlocks();
        for(GoodBlock gb : GoodBlock.values()){
            for(int goodBlocksToLoad = theoreticalGoodBlocks.get(gb); goodBlocksToLoad > 0; goodBlocksToLoad--){
                game.getBank().withdrawGoodBlock(gb);
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
}
