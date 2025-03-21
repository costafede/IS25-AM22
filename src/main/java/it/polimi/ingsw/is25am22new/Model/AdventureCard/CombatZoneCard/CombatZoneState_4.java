package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.util.Optional;

public class CombatZoneState_4 extends CombatZoneState {
    public CombatZoneState_4(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String playerLowestEngine = game.getCurrPlayer();
        int lowestEngine = combatZoneCard.getPlayerToStrength().get(playerLowestEngine);
        for(String player : combatZoneCard.getPlayerToStrength().keySet()) {
            if(combatZoneCard.getPlayerToStrength().get(player) < lowestEngine) {
                playerLowestEngine = player;
            }
        }

        // deactivates all components for all players
        for(String player : combatZoneCard.getPlayerToStrength().keySet()) {
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 7; j++){
                    game.getShipboards().get(player).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                }
            }
        }


        game.setCurrPlayer(playerLowestEngine);
        transition(new CombatZoneState_5(combatZoneCard));
    }
}
