package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

public abstract class AbandonedShipState {
    protected AbandonedShipCard abandonedShipCard;
    protected Game game;

    public AbandonedShipState(AbandonedShipCard abandonedShipCard) {
        this.abandonedShipCard = abandonedShipCard;
        this.game = abandonedShipCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);
    public void transition(AbandonedShipState abandonedShipState) {
        abandonedShipCard.setAbandonedShipState(abandonedShipState);
    }

}
