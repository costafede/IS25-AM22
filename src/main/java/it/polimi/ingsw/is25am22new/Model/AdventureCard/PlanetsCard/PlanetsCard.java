package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Client.View.TUI.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Client.View.GUI.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specialized type of adventure card involving multiple planets.
 * The card supports interactions related to planetary exploration, resources, and player activities.
 * It maintains details about the planets, their states, and actions related to them.
 */
public class PlanetsCard extends AdventureCard implements Serializable, ViewableCard {
    private List<Planet> planets;
    private int flightDaysLost;
    private PlanetsState planetsState;
    private List<String> playersWhoLanded;

    /**
     * Initializes a new instance of the PlanetsCard class.
     * @param pngName
     * @param name
     * @param game
     * @param level
     * @param tutorial
     * @param planets
     * @param flightDaysLost
     */
    public PlanetsCard(String pngName, String name, Game game, int level, boolean tutorial, List<Planet> planets, int flightDaysLost) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.planets = planets;
        this.planetsState = new PlanetsState_1(this);
        this.playersWhoLanded = new ArrayList<String>();
    }

    /**
     * Applies the effects of the card to the game model and observable elements.
     * @param inputCommand the input provided by the player or controller
     */
    public void activateEffect(InputCommand inputCommand){
        planetsState.activateEffect(inputCommand);
    }

    /**
     * Returns the name of the current state of the card.
     * @return
     */
    @Override
    public String getStateName() {
        return planetsState.getStateName();
    }

    /**
     * Returns the number of flight days lost by the player.
     * @return
     */
    public int getFlightDaysLost(){
        return flightDaysLost;
    }

    /**
     * Returns the list of planets involved in the card.
     * @return
     */
    public List<Planet> getPlanets() {
        return planets;
    }

    /**
     * Sets the list of planets involved in the card.
     * @param planetsState
     */
    public void setPlanetsState(PlanetsState planetsState) {
        this.planetsState = planetsState;
    }

    /**
     * Returns true if all planets have been explored by the player.
     * @return
     */
    public boolean planetsFull(){
        for(Planet planet : planets){
            if(!planet.playerPresent())
                return false;
        }
        return true;
    }

    /**
     * Loads the planet where the player "nickname" has landed on with the good blocks from the bank.
     * @param nickname
     */
    public void loadPlanet(String nickname){    // loads the planet where the player "nickname" has landed on with the good blocks from the bank
        Planet planet = getPlanet(nickname);

        for(GoodBlock gb : GoodBlock.values()){
            for(int goodBlocksToLoad = planet.getTheoreticalGoodblocks().get(gb); goodBlocksToLoad > 0 && game.getBank().withdrawGoodBlock(gb); goodBlocksToLoad--){
                planet.setActualGoodblocks(gb, planet.getActualGoodblocks().get(gb) + 1);
            }
        }
    }

    /**
     * Unloads the planet where the player "nickname" has landed on with the good blocks currently present in the planet.
     * @param nickname
     */
    public void unloadPlanet(String nickname){
        Planet planet = getPlanet(nickname);

        for(GoodBlock gb : GoodBlock.values()){
            for(int goodBlocksToUnload = planet.getActualGoodblocks().get(gb); goodBlocksToUnload > 0; goodBlocksToUnload--){
                game.getBank().depositGoodBlock(gb);
            }
        }
    }

    public boolean playerHasLanded(String nickname){
        return getPlanet(nickname) != null;
    }

    /**
     * Returns the planet where the player "nickname" has landed on.
     * @param nickname
     * @return
     */
    public Planet getPlanet(String nickname){
        for(Planet p : planets){
            if(p.getPlayer() != null && p.getPlayer().equals(nickname)){
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the list of players who have landed on the planets.
     * @return
     */
    public List<String> getPlayersWhoLanded() {
        return playersWhoLanded;
    }

    /**
     * Adds a player to the list of players who have landed on the planets.
     * @param view
     * @param model
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showPlanetsCardInGame(this);
        }
        else{
            view.showPlanetsCard(this);
        }
    }

}
