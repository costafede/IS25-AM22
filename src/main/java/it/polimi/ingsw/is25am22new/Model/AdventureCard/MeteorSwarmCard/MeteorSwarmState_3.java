package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public class MeteorSwarmState_3 extends MeteorSwarmState {
    public MeteorSwarmState_3(MeteorSwarmCard meteorSwarmCard) {
       super(meteorSwarmCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        shipboard.chooseShipWreck(x, y);

        if(game.getCurrPlayer().equals(game.getLastPlayer())) {
            meteorSwarmCard.setNextIndexOfMeteor();
            if(meteorSwarmCard.thereAreStillMeteors()) {
                game.setCurrPlayerToLeader();
                transition(new MeteorSwarmState_1(meteorSwarmCard));
            }
            else {
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
            }
        }
        else {
            game.setCurrPlayerToNext();
            transition(new MeteorSwarmState_1(meteorSwarmCard));
        }
    }
}
