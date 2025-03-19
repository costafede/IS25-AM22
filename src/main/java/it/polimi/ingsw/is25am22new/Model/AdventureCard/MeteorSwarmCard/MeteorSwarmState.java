package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsState;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

public abstract class MeteorSwarmState {
    protected MeteorSwarmCard meteorSwarmCard;
    protected Game game;

    public MeteorSwarmState(MeteorSwarmCard meteorSwarmCard) {
        this.meteorSwarmCard = meteorSwarmCard;
        this.game = meteorSwarmCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);
    public abstract void transition(MeteorSwarmState meteorSwarmState);
}
