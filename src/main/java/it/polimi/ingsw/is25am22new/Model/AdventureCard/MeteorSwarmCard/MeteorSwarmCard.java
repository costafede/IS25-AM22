package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.Map;

public class MeteorSwarmCard extends AdventureCard {

    private Map<Integer, Meteor> NumberToMeteor;

    public MeteorSwarmCard(String pngName, String name, Game game, int level, boolean tutorial, Map<Integer, Meteor> NumberToMeteor) {
        super(pngName, name, game, level, tutorial);
        this.NumberToMeteor = NumberToMeteor;
    }

    @Override
    public boolean activateCardPhase(String nickname, InputCommand inputCommand) {
        return true;
    }

    @Override
    public boolean checkActivationConditions(String nickname) {
        return true;
    }

    @Override
    public boolean receiveInputPhase(String nickname, InputCommand inputCommand) {
        return true;
    }

    @Override
    public void resolveCardEffectPhase(String nickname) {
        return;
    }

    public Map<Integer, Meteor> getNumberToMeteor() {
        return NumberToMeteor;
    }
}
