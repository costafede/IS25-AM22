package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

public class SlaversState_3 extends SlaversState implements Serializable {
    public SlaversState_3(SlaversCard slaversCard) {
        super(slaversCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        // choose to keep credits and lose flight days or not
        if(inputCommand.getChoice()) {
            game.getFlightboard().shiftRocket(currentPlayer, slaversCard.getFlightDaysLost());
            shipboard.addCosmicCredits(slaversCard.getCredits());
            slaversCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }

        // deactivates all components
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
            }
        }
        slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());

        // ends the card effect
        game.manageInvalidPlayers();
        game.setCurrPlayerToLeader();
        game.setCurrCard(null);
        slaversCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
        slaversCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
        slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());
    }

    @Override
    public String getStateName() {
        return "SlaversState_3";
    }
}
