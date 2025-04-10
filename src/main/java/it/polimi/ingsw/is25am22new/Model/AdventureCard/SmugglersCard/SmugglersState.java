package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class SmugglersState implements Serializable {
    protected SmugglersCard smugglersCard;
    protected Game game;

    public SmugglersState(SmugglersCard smugglersCard) {
        this.smugglersCard = smugglersCard;
        this.game = smugglersCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);
    public void transition(SmugglersState smugglersState){
        smugglersCard.setSmugglersState(smugglersState);
    }

}
