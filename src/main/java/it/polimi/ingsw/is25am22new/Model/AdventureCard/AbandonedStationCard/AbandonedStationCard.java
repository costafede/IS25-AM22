package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Client.View.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the "Abandoned Station" adventure card in the game.
 * <p>
 * This card allows players to retrieve good blocks from an abandoned station and lose flight days.
 * It follows a state-based mechanism to manage its effect using subclasses of {@link AbandonedStationState}.
 * </p>
 */
public class AbandonedStationCard extends AdventureCard implements Serializable, ViewableCard {

    /** The number of flight days lost when interacting with this card. */
    private int flightDaysLost;

    /** The number of astronauts involved in the effect. */
    private int astronautsNumber;

    /** The current state of the abandoned station card. */
    private AbandonedStationState abandonedStationState;

    /** The expected amount of each type of good block available at the station. */
    private Map<GoodBlock, Integer> theoreticalGoodBlocks;

    /** The actual good blocks retrieved from the station. */
    private Map<GoodBlock, Integer> actualGoodBlocks;

    /**
     * Constructs an Abandoned Station card.
     *
     * @param pngName               the image filename associated with this card
     * @param name                  the name of the card
     * @param game                  the game instance
     * @param level                 the level of the card
     * @param tutorial              true if this card is used in tutorial mode
     * @param flightDaysLost        the number of flight days lost
     * @param astronautsNumber      the number of astronauts involved
     * @param theoreticalGoodBlocks the expected distribution of good blocks
     */
    public AbandonedStationCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int astronautsNumber, Map<GoodBlock, Integer> theoreticalGoodBlocks) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.astronautsNumber = astronautsNumber;
        this.theoreticalGoodBlocks = theoreticalGoodBlocks;

        // Ensure all GoodBlock types are present in the map
        for (GoodBlock gb : GoodBlock.values()) {
            if (!theoreticalGoodBlocks.containsKey(gb)) {
                theoreticalGoodBlocks.put(gb, 0);
            }
        }

        this.abandonedStationState = new AbandonedStationState_1(this);
        this.actualGoodBlocks = new HashMap<GoodBlock, Integer>();
        actualGoodBlocks.put(GoodBlock.BLUEBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.YELLOWBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.GREENBLOCK, 0);
        actualGoodBlocks.put(GoodBlock.REDBLOCK, 0);
    }

    /**
     * Activates the effect of this card by delegating to its current state.
     *
     * @param inputCommand the player's input command
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        abandonedStationState.activateEffect(inputCommand);
    }

    /**
     * Returns the name of the current state of this card.
     *
     * @return the name of the current state
     */
    @Override
    public String getStateName() {
        return abandonedStationState.getStateName();
    }

    /** @return the number of flight days lost */
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    /** @return the number of astronauts involved */
    public int getAstronautsNumber() {
        return astronautsNumber;
    }

    /** @return the theoretical distribution of good blocks */
    public Map<GoodBlock, Integer> getTheoreticalGoodBlocks() {
        return theoreticalGoodBlocks;
    }

    /**
     * Sets the current state of the card.
     *
     * @param abandonedStationState the new state to set
     */
    public void setAbandonedStationState(AbandonedStationState abandonedStationState) {
        this.abandonedStationState = abandonedStationState;
    }

    /**
     * Loads good blocks from the bank into the abandoned station based on the theoretical distribution.
     * If a block type is not available in the bank, it is skipped.
     */
    public void loadStation(){
        for(GoodBlock gb : GoodBlock.values()){
            for(int goodBlocksToLoad = theoreticalGoodBlocks.get(gb); goodBlocksToLoad > 0 && game.getBank().withdrawGoodBlock(gb); goodBlocksToLoad--){
                actualGoodBlocks.put(gb, actualGoodBlocks.get(gb) + 1);
            }
        }
    }

    /**
     * Unloads all actual good blocks from the station back into the bank.
     */
    public void unloadStation() {
        for (GoodBlock gb : GoodBlock.values()) {
            for (int goodBlocksToUnload = actualGoodBlocks.get(gb); goodBlocksToUnload > 0; goodBlocksToUnload--) {
                game.getBank().depositGoodBlock(gb);
            }
        }
    }

    /** @return the actual good blocks retrieved during the card's effect */
    public Map<GoodBlock, Integer> getActualGoodBlocks() {
        return actualGoodBlocks;
    }

    /**
     * Displays this card using the provided view, based on the current game phase.
     *
     * @param view  the view that renders the card
     * @param model the client-side model of the game
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model) {
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")) {
            view.showAbandonedStationCardInGame(this);
        } else {
            view.showAbandonedStationCard(this);
        }
    }
}

