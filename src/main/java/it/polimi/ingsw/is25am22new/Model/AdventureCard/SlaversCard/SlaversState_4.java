package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * The SlaversState_4 class represents a specific state in the state pattern
 * for handling interactions with the SlaversCard in the game. This state is
 * responsible for managing the activation of the SlaversCard's effect during
 * a certain step of its lifecycle.
 *
 * Key behavior of this class includes:
 * - Managing the removal of crew members from the player's shipboard in the
 *   context of the SlaversCard's requirements.
 * - Updating the game's observable model to reflect changes in the shipboard
 *   and other gameplay elements.
 * - Transitioning to other states or resolving the SlaversCard event based
 *   on the game's rules and conditions, such as checking if the required
 *   number of crew members have been removed or if the current player is
 *   the last in turn order.
 *
 * This class is a part of the SlaversState hierarchy and overrides the
 * activateEffect and getStateName methods to implement its unique behavior.
 */
public class SlaversState_4 extends SlaversState implements Serializable {

    /**
     * Initializes a new SlaversState_4 object with the given SlaversCard.
     * @param slaversCard
     */
    public SlaversState_4(SlaversCard slaversCard) {
        super(slaversCard);
    }

    /**
     * Activates the effect of the current state in the game by interacting with the shipboard and updating the game state
     * based on the input provided by the player.
     *
     * @param inputCommand The object containing player input, such as coordinates or choices,
     *                     used to trigger specific behaviors or states.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(ctOptional.isPresent() && ctOptional.get().isCabin()) {
            if(ctOptional.get().getCrewNumber() > 0) {
                ctOptional.get().removeCrewMember();
                slaversCard.increaseSelectedMembers();
                slaversCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
            }
        }

        // deactivates all components
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
            }
        }
        slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());

        if (slaversCard.getSelectedMembers() == slaversCard.getAstronautsToLose() ||
                 !shipboard.thereIsStillCrew()) {

            if(game.getCurrPlayer().equals(game.getLastPlayer())){
                // last player lost (everyplayer lost)
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
                slaversCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                slaversCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());
            }
            else {
                game.setCurrPlayerToNext();
                slaversCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new SlaversState_1(slaversCard));
            }
        }
        else if(slaversCard.getSelectedMembers() < slaversCard.getAstronautsToLose()) {
            transition(new SlaversState_4(slaversCard));
        }
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    @Override
    public String getStateName() {
        return "SlaversState_4";
    }
}
