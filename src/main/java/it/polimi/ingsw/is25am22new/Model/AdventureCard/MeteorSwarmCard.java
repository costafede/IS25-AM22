package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Game;

import java.util.List;
import java.util.Map;

public class MeteorSwarmCard extends AdventureCard{

    private Map<Meteor, Map<Integer, Orientation>> meteorTypeToMeteorToOrientattion;

    public MeteorSwarmCard(String name, Game game, Map<Meteor, Map<Integer, Orientation>> meteorTypeToMeteorToOrientattion) {
        super(name, game);
        this.meteorTypeToMeteorToOrientattion = meteorTypeToMeteorToOrientattion;
    }

    @Override
    public void activateCard(List<String> orderedPlayers) {
        return;
    }

    @Override
    public void activateCard(String player) {
        return;
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields, List<String> activatingCannon) {
        //to be implemented
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields) {
        return;
    }
}
