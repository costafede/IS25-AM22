package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

public class PiratesState_3 extends PiratesState implements Serializable {

    public PiratesState_3(PiratesCard piratesCard){
        super(piratesCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        // choose to keep credits and lose flight days or not
        if(inputCommand.getChoice()) {
            game.getFlightboard().shiftRocket(currentPlayer, piratesCard.getFlightDaysLost());
            piratesCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            shipboard.addCosmicCredits(piratesCard.getCredits());
            piratesCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }

        // deactivates all components
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
            }
        }
        piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());

        if(!piratesCard.getDefeatedPlayers().isEmpty()){
            piratesCard.setCurrDefeatedPlayerToFirst();
            String defeatedPlayer = piratesCard.getCurrDefeatedPlayer();
            game.setCurrPlayer(defeatedPlayer);
            piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            transition(new PiratesState_4(piratesCard));
        }
        else {
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
            piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            piratesCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    @Override
    public String getStateName() {
        return "PiratesState_3";
    }
}
