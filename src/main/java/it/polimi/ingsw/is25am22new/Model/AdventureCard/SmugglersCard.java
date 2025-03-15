package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.List;

public class SmugglersCard extends AdventureCard {

    private int flightDaysLost;
    private int CannonStrength;
    private int lostGoods;
    private List<GoodBlock> goodBlocks;

    public SmugglersCard(String name, Game game) {
        super(name, game);
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
