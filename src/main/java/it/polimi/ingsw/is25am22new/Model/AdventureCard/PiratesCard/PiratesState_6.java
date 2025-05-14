package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

public class PiratesState_6 extends PiratesState implements Serializable {
    public PiratesState_6(PiratesCard card) {
        super(card);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        shipboard.chooseShipWreck(x, y);
        piratesCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);

        if(piratesCard.getCurrDefeatedPlayer().equals(piratesCard.getLastDefeatedPlayer())) {
            piratesCard.setNextIndexOfShot();
            piratesCard.setCurrDefeatedPlayerToFirst();
            game.setCurrPlayer(piratesCard.getCurrDefeatedPlayer());
            piratesCard.getObservableModel().updateAllCurrPlayer(piratesCard.getCurrDefeatedPlayer());
            if(piratesCard.thereAreStillShots()) {
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
        else {
            piratesCard.setCurrDefeatedPlayerToNext();
            game.setCurrPlayer(piratesCard.getCurrDefeatedPlayer());
            transition(new PiratesState_4(piratesCard));
        }
    }

    @Override
    public String getStateName() {
        return "PiratesState_6";
    }
}
