package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState_1;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * Represents the fifth state in the PiratesCard state machine. This state
 * handles specific game logic and transitions when the PiratesCard effect
 * is activated within the game.
 *
 * In this state:
 * - The current defeated player's shipboard can be interacted with.
 * - If the card's battery has been used and a shield generator component
 *   exists at the specified coordinates, the component is activated.
 * - The PiratesCard resets its battery usage status and transitions to
 *   the next state (PiratesState_4) upon completing its effect.
 *
 * This class extends the generic PiratesState class and overrides the
 * methods specific to state behavior and identity.
 */
public class PiratesState_5 extends PiratesState implements Serializable {
    public PiratesState_5(PiratesCard piratesCard) {
        super(piratesCard);
    }

    /**
     * Activates the effect associated with the current state of the PiratesCard. This method
     * processes input commands to interact with a defeated player's shipboard based on
     * the PiratesCard's current state and specific game logic. If certain conditions are met,
     * it activates a shield generator component and updates the game state accordingly.
     *
     * @param inputCommand The input command containing the row and column coordinates
     *                     of the targeted grid position on the defeated player's shipboard.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String defeatedPlayer = piratesCard.getCurrDefeatedPlayer();
        Shipboard shipboard = game.getShipboards().get(defeatedPlayer);
        int x = 0;
        int y = 0;

        x = inputCommand.getRow();
        y = inputCommand.getCol();
        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(piratesCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isShieldGenerator()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            piratesCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
        }

        piratesCard.setBatteryUsed(false);

        transition(new PiratesState_4(piratesCard));
    }

    @Override
    public String getStateName() {
        return "PiratesState_5";
    }
}
