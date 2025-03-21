package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.HashMap;
import java.util.Map;

public abstract class AbandonedStationState {
    protected AbandonedStationCard abandonedStationCard;
    protected Game game;

    public AbandonedStationState(AbandonedStationCard abandonedStationCard) {
        this.abandonedStationCard = abandonedStationCard;
        game = abandonedStationCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);

    public void transition(AbandonedStationState abandonedStationState) {
        abandonedStationCard.setAbandonedStationState(abandonedStationState);
    }
}
