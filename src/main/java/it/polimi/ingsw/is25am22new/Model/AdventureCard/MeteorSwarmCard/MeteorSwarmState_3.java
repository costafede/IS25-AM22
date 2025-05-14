package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

public class MeteorSwarmState_3 extends MeteorSwarmState implements Serializable {
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
        meteorSwarmCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);

        if(game.getCurrPlayer().equals(game.getLastPlayer())) {
            meteorSwarmCard.setNextIndexOfMeteor();
            if(meteorSwarmCard.thereAreStillMeteors()) {
                game.setCurrPlayerToLeader();
                meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new MeteorSwarmState_1(meteorSwarmCard));
            }
            else {
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
                meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                meteorSwarmCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                meteorSwarmCard.getObservableModel().updateAllShipboardList(game.getShipboards());
            }
        }
        else {
            game.setCurrPlayerToNext();
            meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            transition(new MeteorSwarmState_1(meteorSwarmCard));
        }
    }

    @Override
    public String getStateName() {
        return "MeteorSwarmState_3";
    }
}
