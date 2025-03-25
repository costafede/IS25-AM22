package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.*;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanetsCard extends AdventureCard {
    private List<Planet> planets;
    private int flightDaysLost;
    private PlanetsState planetsState;
    private List<String> playersWhoLanded;

    public PlanetsCard(String pngName, String name, Game game, int level, boolean tutorial, List<Planet> planets, int flightDaysLost) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.planets = planets;
        this.planetsState = new PlanetsState_1(this);
        this.playersWhoLanded = new ArrayList<String>();
    }

    public void activateEffect(InputCommand inputCommand){
        planetsState.activateEffect(inputCommand);
    }

    public int getFlightDaysLost(){
        return flightDaysLost;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanetsState(PlanetsState planetsState) {
        this.planetsState = planetsState;
    }

    public boolean planetsFull(){
        for(Planet planet : planets){
            if(!planet.playerPresent())
                return false;
        }
        return true;
    }

    public void loadPlanet(String nickname){    // loads the planet where the player "nickname" has landed on with the good blocks from the bank
        Planet planet = getPlanet(nickname);

        for(GoodBlock gb : GoodBlock.values()){
            for(int goodBlocksToLoad = planet.getTheoreticalGoodblocks().get(gb); goodBlocksToLoad > 0 && game.getBank().withdrawGoodBlock(gb); goodBlocksToLoad--){
                planet.setActualGoodblocks(gb, planet.getActualGoodblocks().get(gb) + 1);
            }
        }
    }

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

    public Planet getPlanet(String nickname){
        for(Planet p : planets){
            if(p.getPlayer() != null && p.getPlayer().equals(nickname)){
                return p;
            }
        }
        return null;
    }

    public List<String> getPlayersWhoLanded() {
        return playersWhoLanded;
    }

}
