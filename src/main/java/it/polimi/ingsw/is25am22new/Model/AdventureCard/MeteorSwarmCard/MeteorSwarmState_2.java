package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;

public class MeteorSwarmState_2 extends MeteorSwarmState {
    public MeteorSwarmState_2(MeteorSwarmCard meteorSwarmCard) { super(meteorSwarmCard); }

    @Override
    public void activateEffect(InputCommand inputCommand) {

    }

    @Override
    public void transition(MeteorSwarmState meteorSwarmState) {

    }

    Meteor incomingMeteor = meteorSwarmCard.getNumberToMeteor().get(meteorSwarmCard.getIndexOfIncomingMeteor());
}
