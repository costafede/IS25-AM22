package it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class EpidemicCard extends AdventureCard {

    public EpidemicCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
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
}
