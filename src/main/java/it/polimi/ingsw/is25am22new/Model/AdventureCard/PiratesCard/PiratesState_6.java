package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public class PiratesState_6 extends PiratesState {
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

        if(piratesCard.getCurrDefeatedPlayer().equals(piratesCard.getLastDefeatedPlayer())) {
            piratesCard.setNextIndexOfShot();
            piratesCard.setCurrDefeatedPlayerToFirst();
            game.setCurrPlayer(piratesCard.getCurrDefeatedPlayer());
            if(piratesCard.thereAreStillShots()) {
                transition(new PiratesState_4(piratesCard));
            }
            else {
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
            }
        }
        else {
            piratesCard.setCurrDefeatedPlayerToNext();
            game.setCurrPlayer(piratesCard.getCurrDefeatedPlayer());
            transition(new PiratesState_4(piratesCard));
        }
    }
}
