package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

public class SlaversState_4 extends SlaversState implements Serializable {
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
                slaversCard.increaseSelectedMembers();
                slaversCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
            }
        }

        // deactivates all components
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
            }
        }
        slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());

        if (slaversCard.getSelectedMembers() == slaversCard.getAstronautsToLose() ||
                 !shipboard.thereIsStillCrew()) {

            if(game.getCurrPlayer().equals(game.getLastPlayer())){
                // last player lost (everyplayer lost)
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
                slaversCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                slaversCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());
            }
            else {
                game.setCurrPlayerToNext();
                slaversCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new SlaversState_1(slaversCard));
            }
        }
        else if(slaversCard.getSelectedMembers() < slaversCard.getAstronautsToLose()) {
            transition(new SlaversState_4(slaversCard));
        }
    }

    @Override
    public String getStateName() {
        return "SlaversState_4";
    }
}
