package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.List;
import java.util.Map;

public class PlanetsCard extends AdventureCard {
    private Map<Integer, List<GoodBlock>> planetToGoodBlocks;
    private int flightDaysLost;

    public PlanetsCard(String pngName, String name, Game game, int level, boolean tutorial, Map<Integer, List<GoodBlock>> planetToGoodBlocks, int flightDaysLost) {
        super(pngName, name, game, level, tutorial);
        this.planetToGoodBlocks = planetToGoodBlocks;
        this.flightDaysLost = flightDaysLost;
    }

    @Override
    public boolean activateCardPhase(String nickname, InputCommand inputCommand) {
        return true;
    }

    @Override
    public boolean checkActivationConditions(String nickname) {
        return true;
    }

    @Override
    public boolean receiveInputPhase(String nickname, InputCommand inputCommand) {
        return true;
    }

    @Override
    public void resolveCardEffectPhase(String nickname) {
        return;
    }

    public Map<Integer, List<GoodBlock>> getPlanetToGoodBlocks() {
        return planetToGoodBlocks;
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }
}
