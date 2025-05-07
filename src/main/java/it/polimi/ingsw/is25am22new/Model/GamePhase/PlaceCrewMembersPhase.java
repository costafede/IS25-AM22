package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public class PlaceCrewMembersPhase extends GamePhase {
    public PlaceCrewMembersPhase(Game game) {
        super(game);
        phaseType = PhaseType.PLACECREWMEMBERS;
    }

    @Override
    public void trySwitchToNextPhase() {
        for(String player : game.getPlayerList()) {
            Shipboard ship = game.getShipboards().get(player);
            if(!ship.allCabinsArePopulated())
                return;
        }
        transition(new CardPhase(game));
    }
}
