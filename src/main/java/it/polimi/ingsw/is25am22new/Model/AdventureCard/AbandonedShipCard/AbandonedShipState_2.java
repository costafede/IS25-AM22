package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

public class AbandonedShipState_2 extends AbandonedShipState implements Serializable {

    private int membersStillToRemove;

    public AbandonedShipState_2(AbandonedShipCard abandonedShipCard) {
        super(abandonedShipCard);
        this.membersStillToRemove = abandonedShipCard.getLostAstronauts();
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        Shipboard shipboard = game.getShipboards().get(game.getCurrPlayer());
        Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol());
        if (ct.isEmpty() || !ct.get().isCabin()) {
            throw new IllegalArgumentException("Cannot remove a crew member from a tile that is not a cabin");
        }
        ct.get().removeCrewMember();
        membersStillToRemove--;
        if(membersStillToRemove == 0) {
            shipboard.addCosmicCredits(abandonedShipCard.getCredits());
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
        }
    }

    @Override
    public String getStateName() {
        return "AbandonedShipState_2";
    }

    public int getMembersStillToRemove() {
        return membersStillToRemove;
    }
}
