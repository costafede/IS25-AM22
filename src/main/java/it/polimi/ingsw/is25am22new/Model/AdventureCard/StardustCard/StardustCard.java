package it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard;

import it.polimi.ingsw.is25am22new.Client.View.TUI.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Client.View.GUI.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.List;

/**
 * The StardustCard class represents a specific type of adventure card in the game.
 * This card implements functionality that enables it to interact with game components
 * and updates its state or effects accordingly.
 *
 * StardustCard extends the abstract class AdventureCard and implements the ViewableCard interface.
 */
public class StardustCard extends AdventureCard implements ViewableCard {

    public StardustCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
    }

    /**
     * Activates the effect of the StardustCard by modifying the game state. It executes the following operations:
     * - Retrieves the rockets in their ordered sequence on the flightboard.
     * - For each player in reverse order of their rockets on the flightboard, calculates steps backward based on exposed connectors on their shipboard.
     * - Shifts the player's rocket backward on the flightboard by the calculated steps.
     * - Updates the flightboard to manage players who can no longer play.
     * - Sets the current player to the leader.
     * - Clears the current adventure card being played.
     * - Notifies observers about the updates to the current player, the flightboard, and the shipboards.
     *
     * @param command The input command object used to manage card activation settings. While this specific implementation does
     *                not directly use the command parameter, it allows for flexibility in handling input configurations in games.
     */
    @Override
    public void activateEffect(InputCommand command) {
        List<String> orderedPlayers = game.getFlightboard().getOrderedRockets();
        for (int i = orderedPlayers.size() - 1; i >= 0; i--) {
            String nickname = orderedPlayers.get(i);
            int stepsBackwards = game.getShipboards().get(nickname).countExposedConnectors();
            game.getFlightboard().shiftRocket(nickname, stepsBackwards);
        }
        game.manageInvalidPlayers();
        game.setCurrPlayerToLeader();
        game.setCurrCard(null);
        observableModel.updateAllCurrPlayer(game.getCurrPlayer());
        observableModel.updateAllFlightboard(game.getFlightboard());
        observableModel.updateAllShipboardList(game.getShipboards());
    }

    @Override
    public String getStateName() {
        return "";
    }

    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showStardustCardInGame(this);
        }
        else{
            view.showStardustCard(this);
        }
    }
}
