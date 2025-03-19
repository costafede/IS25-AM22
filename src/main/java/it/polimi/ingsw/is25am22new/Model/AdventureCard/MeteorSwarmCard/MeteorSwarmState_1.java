package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsState;

public class MeteorSwarmState_1 extends MeteorSwarmState{
    public MeteorSwarmState_1(MeteorSwarmCard meteorSwarmCard) { super(meteorSwarmCard); }

    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        if(meteorSwarmCard.thereAreStillMeteors()) {
            Meteor incomingMeteor = meteorSwarmCard.getNumberToMeteor().get(meteorSwarmCard.getIndexOfIncomingMeteor());
            if(incomingMeteor.getOrientation() == Orientation.TOP ||
                incomingMeteor.getOrientation() == Orientation.BOTTOM) {
                game.getDices().rollDices();
                int col = game.getDices().getDice1() + game.getDices().getDice2(); // gets the right column


            }
            else if (incomingMeteor.getOrientation() == Orientation.LEFT ||
                    incomingMeteor.getOrientation() == Orientation.RIGHT) {

            }
        }
    }

    public void transition(PlanetsState planetsState) {
        // transition made after all players have taken the same meteor
    }
}
