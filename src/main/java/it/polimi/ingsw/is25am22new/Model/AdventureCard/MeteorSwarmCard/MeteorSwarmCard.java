package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.Map;

public class MeteorSwarmCard extends AdventureCard {

    private Map<Integer, Meteor> numberToMeteor;
    private MeteorSwarmState meteorSwarmState;
    private int indexOfIncomingMeteor;

    public MeteorSwarmCard(String pngName, String name, Game game, int level, boolean tutorial, Map<Integer, Meteor> numberToMeteor) {
        super(pngName, name, game, level, tutorial);
        this.numberToMeteor = numberToMeteor;
        this.meteorSwarmState = new MeteorSwarmState_1(this);
        this.indexOfIncomingMeteor = 0;
    }

    public void activateEffect(InputCommand command) {
        meteorSwarmState.activateEffect(command);
    }

    public Map<Integer, Meteor> getNumberToMeteor() {
        return numberToMeteor;
    }

    public boolean thereAreStillMeteors() {
        return indexOfIncomingMeteor < numberToMeteor.size();
    }

    public int getIndexOfIncomingMeteor() {
        return indexOfIncomingMeteor;
    }
}
