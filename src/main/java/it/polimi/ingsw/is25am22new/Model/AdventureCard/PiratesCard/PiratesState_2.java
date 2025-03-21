package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;

public class PiratesState_2 extends PiratesState {
    public PiratesState_2(PiratesCard piratesCard) {
        super(piratesCard);
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
        if(piratesCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleCannon()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
        }

        if(inputCommand.getChoice()) { // choose to stop using the batteries
            if(shipboard.getCannonStrength() > piratesCard.getCannonStrength()) { // win case
                transition(new PiratesState_3(piratesCard)); // decide to lose daysOnFlight and take credits or not
            }
            else if (shipboard.getCannonStrength() <= piratesCard.getCannonStrength()) { // lose or tie case
                if(shipboard.getCannonStrength() < piratesCard.getCannonStrength()) {
                    piratesCard.addDefeatedPlayer(currentPlayer);
                }
                if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                    transition(new PiratesState_4(piratesCard)); // all defeated players get shot
                }
                else {
                    piratesCard.setBatteryUsed(false);

                    // deactivates all components
                    for(int i = 0; i < 5; i++){
                        for(int j = 0; j < 7; j++){
                            game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                        }
                    }

                    game.setCurrPlayerToNext();
                    transition(new PiratesState_1(piratesCard));
                }
            }
        }
        else { // choose to continue using the batteries
            transition(new PiratesState_1(piratesCard));
        }
    }
}
