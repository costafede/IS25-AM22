package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.List;

public class AbandonedStationCard extends AdventureCard{

    private int flightDaysLost;
    private int astronautsNumber;
    private List<GoodBlock> goodBlocks;

    public AbandonedStationCard(String name, Game game, int flightDaysLost, int astronautsNumber, List<GoodBlock> goodBlocks) {
        super(name, game);
        this.flightDaysLost = flightDaysLost;
        this.astronautsNumber = astronautsNumber;
        this.goodBlocks = goodBlocks;
    }


    @Override
    public void activateCard(List<String> orderedPlayers) {
        return;
    }

    @Override
    public void activateCard(String player) {
        //to be implemented
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
