package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversState;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class PiratesState implements Serializable {
    protected PiratesCard piratesCard;
    protected Game game;

    public PiratesState(PiratesCard piratesCard) {
        this.piratesCard = piratesCard;
        this.game = piratesCard.getGame();
    }
    public abstract void activateEffect(InputCommand inputCommand);
    protected void transition(PiratesState piratesState) {
        piratesCard.setPiratesState(piratesState);
    }
}
