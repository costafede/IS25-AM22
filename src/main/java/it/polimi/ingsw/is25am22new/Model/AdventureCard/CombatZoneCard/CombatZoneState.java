package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class CombatZoneState implements Serializable {
    protected CombatZoneCard combatZoneCard;
    protected Game game;

    /**
     * Constructs a new CombatZoneState for the given CombatZoneCard.
     * @param combatZoneCard
     */
    public CombatZoneState(CombatZoneCard combatZoneCard) {
        this.combatZoneCard = combatZoneCard;
        this.game = combatZoneCard.getGame();
    }

    /**
     * Activates the effect associated with this state.
     * @param inputCommand
     */
    public abstract void activateEffect(InputCommand inputCommand);

    /**
     * Transitions the card to a new state.
     * @param combatZoneState
     */
    protected void transition(CombatZoneState combatZoneState) {
        combatZoneCard.setCombatZoneState(combatZoneState);
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    public abstract String getStateName();

}
