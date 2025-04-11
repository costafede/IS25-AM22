package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class CombatZoneState implements Serializable {
    protected CombatZoneCard combatZoneCard;
    protected Game game;

    public CombatZoneState(CombatZoneCard combatZoneCard) {
        this.combatZoneCard = combatZoneCard;
        this.game = combatZoneCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);
    protected void transition(CombatZoneState combatZoneState) {
        combatZoneCard.setCombatZoneState(combatZoneState);
    }
}
