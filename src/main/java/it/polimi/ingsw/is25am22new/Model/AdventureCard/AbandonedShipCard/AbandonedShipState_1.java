package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

public class AbandonedShipState_1 extends AbandonedShipState implements Serializable {
    public AbandonedShipState_1(AbandonedShipCard abandonedShipCard) {
        super(abandonedShipCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){
            if(game.getShipboards().get(game.getCurrPlayer()).getCrewNumber() < abandonedShipCard.getLostAstronauts())
                throw new IllegalStateException("Not Enough Astronauts");
            game.getFlightboard().shiftRocket(game.getCurrPlayer(), abandonedShipCard.getFlightDaysLost());
            transition(new AbandonedShipState_2(abandonedShipCard));
        }
        else if(!game.getCurrPlayer().equals(game.getLastPlayer()))
            game.setCurrPlayerToNext();
        else{
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
        }
    }
}
