package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class OpenSpaceState implements Serializable {
    protected OpenSpaceCard openSpaceCard;
    protected Game game;

    public OpenSpaceState(OpenSpaceCard openSpaceCard) {
        this.openSpaceCard = openSpaceCard;
        game = openSpaceCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);

    public void transition(OpenSpaceState openSpaceState) {
        openSpaceCard.setOpenSpaceState(openSpaceState);
    }
}
