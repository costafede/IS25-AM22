package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class SlaversState implements Serializable {
    protected SlaversCard slaversCard;
    protected Game game;

    /**
     * Constructs a new SlaversState for the given SlaversCard.
     * @param slaversCard
     */
    public SlaversState(SlaversCard slaversCard) {
        this.slaversCard = slaversCard;
        this.game = slaversCard.getGame();
    }

    /**
     * Activates the effect of the current SlaversState.
     * @param inputCommand
     */
    public abstract void activateEffect(InputCommand inputCommand);

    protected void transition(SlaversState slaversState) {
        slaversCard.setSlaversState(slaversState);
    }

    /**
     * Returns the name of the current SlaversState.
     * @return
     */
    public abstract String getStateName();
}
