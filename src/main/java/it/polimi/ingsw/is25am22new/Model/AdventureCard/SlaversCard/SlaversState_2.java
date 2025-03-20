package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState_1;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public class SlaversState_2 extends SlaversState{
    public SlaversState_2(SlaversCard slaversCard) {
        super(slaversCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        if(inputCommand.getChoice()) {
            int x = inputCommand.getRow();
            int y = inputCommand.getCol();
            if(slaversCard.isBatteryUsed()) {
                // activates the component
                shipboard.getComponentTileFromGrid(x, y).ifPresent(ComponentTile::activateComponent);
            }
        }

        if(shipboard.getCannonStrength() > slaversCard.getCannonStrength()) { // win case
            transition(new SlaversState_3(slaversCard)); // decide to lose daysOnFlight and take credits or not
        }
        else if (shipboard.getCannonStrength() < slaversCard.getCannonStrength()) { // lose case
            transition(new SlaversState_4(slaversCard));
        }
        else {// in case of tie nothing happens and the slavers attack next player
            //what if the player before the last becomes the last ??
            if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
            }
            else {
                game.setCurrPlayerToNext();
                transition(new MeteorSwarmState_1(meteorSwarmCard));
            }

        }
    }
}
