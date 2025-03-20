package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

public abstract class SlaversState {
    protected SlaversCard slaversCard;
    protected Game game;

    public SlaversState(SlaversCard slaversCard) {
        this.slaversCard = slaversCard;
        this.game = slaversCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);
    protected void transition(SlaversState slaversState) {
        slaversCard.setSlaversState(slaversState);
    }
}
