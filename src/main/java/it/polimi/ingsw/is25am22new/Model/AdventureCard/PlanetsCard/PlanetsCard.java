package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.*;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.List;
import java.util.Map;

public class PlanetsCard extends AdventureCard {
    private List<Planet> planets;
    private int flightDaysLost;
    private PlanetsState planetsState;

    public PlanetsCard(String pngName, String name, Game game, int level, boolean tutorial, List<Planet> planets, int flightDaysLost) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.planets = planets;
        planetsState = new PlanetsState_1(this);
    }

    public void activateEffect(InputCommand inputCommand){
        planetsState.activateEffect(inputCommand);
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
}
