package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

/**
 * The PiratesState_3 class represents a specific state in the PiratesCard state machine.
 * It extends the abstract PiratesState class and implements behavior unique to the
 * corresponding game phase where pirates' effects are resolved.
 *
 * The class contains game logic to handle subsequent actions such as the player's choice
 * of keeping cosmic credits at the cost of losing flight days, deactivating ship
 * components, and managing defeated player transitions or end-of-effect conditions.
 */
public class PiratesState_3 extends PiratesState implements Serializable {

    /**
     * Initializes the PiratesState_3 object with the given PiratesCard.
     * @param piratesCard
     */
    public PiratesState_3(PiratesCard piratesCard){
        super(piratesCard);
    }

    /**
     * Activates the effect of the PiratesState_3. Handles the player's decision to either
     * retain cosmic credits at the cost of losing flight days or avoid loss. It also
     * manages the deactivation of ship components and transitions to the next game state
     * depending on the current player's status or any defeated players.
     *
     * @param inputCommand an InputCommand object that contains the player's choices and
     *                      actions, such as opting whether to keep cosmic credits or not.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        // choose to keep credits and lose flight days or not
        if(inputCommand.getChoice()) {
            game.getFlightboard().shiftRocket(currentPlayer, piratesCard.getFlightDaysLost());
            piratesCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            shipboard.addCosmicCredits(piratesCard.getCredits());
            piratesCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }

        // deactivates all components
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
            }
        }
        piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());

        if(!piratesCard.getDefeatedPlayers().isEmpty()){
            piratesCard.setCurrDefeatedPlayerToFirst();
            String defeatedPlayer = piratesCard.getCurrDefeatedPlayer();
            game.setCurrPlayer(defeatedPlayer);
            piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            transition(new PiratesState_4(piratesCard));
        }
        else {
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
            piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            piratesCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    @Override
    public String getStateName() {
        return "PiratesState_3";
    }
}
