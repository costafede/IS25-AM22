package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public class PiratesState_3 extends PiratesState{

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
            shipboard.addCosmicCredits(piratesCard.getCredits());
        }

        transition(new PiratesState_4(piratesCard));
    }
}
