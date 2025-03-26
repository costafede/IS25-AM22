package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SlaversState_4 extends SlaversState {
    public SlaversState_4(SlaversCard slaversCard) {
        super(slaversCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(ctOptional.isPresent() && ctOptional.get().isCabin()) {
            if(ctOptional.get().getCrewNumber() > 0) {
                ctOptional.get().removeCrewMember();
                slaversCard.increaseSelectedAstronauts();
            }
        }

        // deactivates all components
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
            }
        }

        if (slaversCard.getSelectedAstronauts() == slaversCard.getAstronautsToLose() ||
                 !shipboard.thereIsStillCrew()) {

            if(game.getCurrPlayer().equals(game.getLastPlayer())){
                // last player lost (everyplayer lost)
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
            }
            else {
                game.setCurrPlayerToNext();
                transition(new SlaversState_1(slaversCard));
            }
        }
        else if(slaversCard.getSelectedAstronauts() < slaversCard.getAstronautsToLose()) {
            transition(new SlaversState_4(slaversCard));
        }
    }
}
