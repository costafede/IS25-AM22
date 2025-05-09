package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class CombatZoneState2 implements Serializable {
    protected CombatZoneCard2 combatZoneCard2;
    protected Game game;

    public CombatZoneState2(CombatZoneCard2 combatZoneCard2) {
        this.combatZoneCard2 = combatZoneCard2;
        this.game = combatZoneCard2.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);
    protected void transition(CombatZoneState2 combatZoneState2) {
        combatZoneCard2.setCombatZoneState(combatZoneState2);
    }

    public abstract String getStateName();
}
