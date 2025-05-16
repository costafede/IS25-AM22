package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the first state of the OpenSpace card during gameplay.
 * In this state, players decide how to activate their engine or manage
 * their component tiles depending on the card's effect and the player's choices.
 * This is a concrete implementation of the abstract OpenSpaceState class.
 */
public class OpenSpaceState_1 extends OpenSpaceState implements Serializable {
    public OpenSpaceState_1(OpenSpaceCard openSpaceCard) {
        super(openSpaceCard);
    }

    @Override
    public String getStateName() {
        return "OpenSpaceState_1";
    }

    /**
     * Activates the effect associated with the current OpenSpaceCard based on the player's input.
     * It handles two scenarios: activating an engine by removing a battery token
     * or resolving the card's effect by declaring engine strength.
     *
     * @param inputCommand the input command containing the player's choice and relevant
     *                     information such as the coordinates of a component tile
     *                     or other necessary details.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){   //player wants to activate a double engine, so he removes a battery token from the component tile with the coordinates given
            ComponentTile batteryComponent = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get();
            batteryComponent.removeBatteryToken();
            transition(new OpenSpaceState_2(openSpaceCard)); //go to state where player has to enter the input to activate the engine
            openSpaceCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
        }
        else{   //player declares his engine power and resolves the card's effect(so choice must be set to false)
            int engineStrength = game.getShipboards().get(game.getCurrPlayer()).getEngineStrength();
            game.getShipboards().get(game.getCurrPlayer()).deactivateAllComponent();
            openSpaceCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            if(engineStrength == 0){    //player is forced to leave if his engine strength is zero
                openSpaceCard.getPlayersWithNoEngineStrength().add(game.getCurrPlayer());
            }
            else {
                game.getFlightboard().shiftRocket(game.getCurrPlayer(), -engineStrength);
                openSpaceCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
                openSpaceCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            }
            List<String> playersBeforeEffect = openSpaceCard.getOrderedPlayersBeforeEffect();
            if(!game.getCurrPlayer().equals(playersBeforeEffect.getLast())) {   //if curr player isn't the last one, the turn is passed to the next player
                game.setCurrPlayer(playersBeforeEffect.get(playersBeforeEffect.indexOf(game.getCurrPlayer()) + 1));
                openSpaceCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            }
            else{   //the card effect finishes
                game.manageInvalidPlayers();    //players lapped
                for(String p : openSpaceCard.getPlayersWithNoEngineStrength()){ //players with no engine strength
                    game.playerAbandons(p);
                }
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
                openSpaceCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                openSpaceCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                openSpaceCard.getObservableModel().updateAllShipboardList(game.getShipboards());
            }
        }
    }
}
