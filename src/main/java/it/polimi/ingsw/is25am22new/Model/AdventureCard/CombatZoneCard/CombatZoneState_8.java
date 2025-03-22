package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public class CombatZoneState_8 extends CombatZoneState {
    public CombatZoneState_8(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String playerLowestCannon = game.getCurrPlayer();
        double lowestCannon = combatZoneCard.getPlayerToStrength().get(playerLowestCannon);
        for(String player : combatZoneCard.getPlayerToStrength().keySet()) {
            if(combatZoneCard.getPlayerToStrength().get(player) < lowestCannon) {
                playerLowestCannon = player;
            } else if (combatZoneCard.getPlayerToStrength().get(player) == lowestCannon) {
                playerLowestCannon =
                        // who is ahead receives penalty
                        game.getShipboards().get(player).getDaysOnFlight() >
                                game.getShipboards().get(playerLowestCannon).getDaysOnFlight() ?
                                player : playerLowestCannon;
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

        game.setCurrPlayer(playerLowestCannon);
        transition(new CombatZoneState_9(combatZoneCard));
    }
}
