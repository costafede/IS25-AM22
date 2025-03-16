package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.List;

public class AbandonedStationCard extends AdventureCard{

    private int flightDaysLost;
    private int astronautsNumber;
    private List<GoodBlock> goodBlocks;

    public AbandonedStationCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int astronautsNumber, List<GoodBlock> goodBlocks) {
        super(pngName, name, game, level, tutorial);
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

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getAstronautsNumber() {
        return astronautsNumber;
    }

    public List<GoodBlock> getGoodBlocks() {
        return goodBlocks;
    }
}
