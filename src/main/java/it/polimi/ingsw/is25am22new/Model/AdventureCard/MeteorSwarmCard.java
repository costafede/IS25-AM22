package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Game;
import java.util.List;
import java.util.Map;

public class MeteorSwarmCard extends AdventureCard{

    private Map<Integer, Meteor> NumberToMeteor;

    public MeteorSwarmCard(String name, Game game, int level, boolean tutorial, Map<Integer, Meteor> NumberToMeteor) {
        super(name, game, level, tutorial);
        this.NumberToMeteor = NumberToMeteor;
    }

    @Override
    public void activateCard(List<String> orderedPlayers) { return;}

    @Override
    public void activateCard(String player) {return;}

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields, List<String> activatingCannon) {
        //to be implemented
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields) {return;}
}
