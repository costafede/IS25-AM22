package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState_1;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;

public class SlaversState_2 extends SlaversState{
    public SlaversState_2(SlaversCard slaversCard) {
        super(slaversCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = 0;
        int y = 0;

        x = inputCommand.getRow();
        y = inputCommand.getCol();
        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(slaversCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleCannon()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
        }

        if(!inputCommand.getChoice()) { // choose to continue to use batteries or to stop
            if(shipboard.getCannonStrength() > slaversCard.getCannonStrength()) { // win case
                transition(new SlaversState_3(slaversCard)); // decide to lose daysOnFlight and take credits or not
            }
            else if (shipboard.getCannonStrength() < slaversCard.getCannonStrength()) { // lose case
                transition(new SlaversState_4(slaversCard));
            }
            else {// in case of tie nothing happens and the slavers attack next player
                if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                    game.setCurrPlayerToLeader();
                    game.setCurrCard(null);
                }
                else {
                    slaversCard.setBatteryUsed(false);
                    // deactivates all components
                    for(int i = 0; i < 5; i++){
                        for(int j = 0; j < 7; j++){
                            game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                        }
                    }
                    game.setCurrPlayerToNext();
                    transition(new SlaversState_1(slaversCard));
                }
            }
        }
        else {
            transition(new SlaversState_1(slaversCard));
        }
    }
}
