package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public class AbandonedShipState_2 extends AbandonedShipState {

    private int membersStillToRemove;

    public AbandonedShipState_2(AbandonedShipCard abandonedShipCard) {
        super(abandonedShipCard);
        this.membersStillToRemove = abandonedShipCard.getLostAstronauts();
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        Shipboard shipboard = game.getShipboards().get(game.getCurrPlayer());
        shipboard.getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get().removeCrewMember();
        membersStillToRemove--;
        if(membersStillToRemove == 0) {
            shipboard.addCosmicCredits(abandonedShipCard.getCredits());
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
        }
    }
}
