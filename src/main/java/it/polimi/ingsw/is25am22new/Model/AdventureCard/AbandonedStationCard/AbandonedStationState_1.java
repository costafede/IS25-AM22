package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class AbandonedStationState_1 extends AbandonedStationState{

    public AbandonedStationState_1(AbandonedStationCard abandonedStationCard) {
        super(abandonedStationCard);
    }
    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){
            game.getFlightboard().shiftRocket(game.getCurrPlayer(), abandonedStationCard.getFlightDaysLost());
            abandonedStationCard.loadStation();
            transition(new AbandonedStationState_2(abandonedStationCard));
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
