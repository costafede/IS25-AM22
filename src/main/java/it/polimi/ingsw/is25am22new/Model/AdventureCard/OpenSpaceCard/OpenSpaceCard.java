package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Client.View.TUI.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Client.View.GUI.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an OpenSpaceCard, a type of adventure card with specific behaviors and states in the game.
 * Extends the functionality of AdventureCard and implements the ViewableCard interface.
 * The card includes logic for managing its state and interacting with players and the game.
 */
public class OpenSpaceCard extends AdventureCard implements Serializable, ViewableCard {

    private OpenSpaceState openSpaceState;
    private List<String> orderedPlayersBeforeEffect;
    private List<String> playersWithNoEngineStrength;

    /**
     * Initializes the OpenSpaceCard instance with the given parameters.
     * @param pngName
     * @param name
     * @param game
     * @param level
     * @param tutorial
     */
    public OpenSpaceCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
        openSpaceState = new OpenSpaceState_1(this);
        orderedPlayersBeforeEffect = new ArrayList<>();
        playersWithNoEngineStrength = new ArrayList<>();
    }

    /**
     * Activates the card's effect by delegating to the current state.
     * @param inputCommand the input provided by the player or controller
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(orderedPlayersBeforeEffect.isEmpty())
            orderedPlayersBeforeEffect = new ArrayList<>(game.getFlightboard().getOrderedRockets());
        openSpaceState.activateEffect(inputCommand);
    }

    /**
     * Returns the name of the current state of the card.
     * @return
     */
    @Override
    public String getStateName() {
        return openSpaceState.getStateName();
    }

    /**
     * Sets the current state of the card.
     * @param openSpaceState
     */
    public void setOpenSpaceState(OpenSpaceState openSpaceState) {
        this.openSpaceState = openSpaceState;
    }

    /**
     * Returns the current state of the card.
     * @return
     */
    public List<String> getOrderedPlayersBeforeEffect() {
        return orderedPlayersBeforeEffect;
    }

    /**
     * Returns the list of players who have no engine strength.
     * @return
     */
    public List<String> getPlayersWithNoEngineStrength() {
        return playersWithNoEngineStrength;
    }

    /**
     * Displays the card in the user interface depending on the current game phase.
     * @param view
     * @param model
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showOpenSpaceCardInGame(this);
        }
        else{
            view.showOpenSpaceCard(this);
        }
    }
}
