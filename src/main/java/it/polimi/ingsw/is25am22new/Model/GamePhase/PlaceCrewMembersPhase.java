package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

/**
 * Represents the "Place Crew Members" phase of the game.
 * During this phase, players place their crew members on their shipboards to populate the cabins.
 * The phase will transition to the next phase only after all players have populated all cabins on their respective shipboards.
 *
 * The transition to the next phase follows these rules:
 * - The phase will not transition unless every player's shipboard has all cabins populated.
 * - Once all cabins are populated, the game will move to the {@code CardPhase}.
 */
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
