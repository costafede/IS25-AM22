package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.List;
import java.util.Map;

public class AbandonedStationCard extends AdventureCard {

    private int flightDaysLost;
    private int astronautsNumber;
    private AbandonedStationState abandonedStationState;
    private Map<GoodBlock, Integer> theoreticalGoodBlocks;


    public AbandonedStationCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int astronautsNumber, Map<GoodBlock, Integer> theoreticalGoodBlocks) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.astronautsNumber = astronautsNumber;
        this.theoreticalGoodBlocks = theoreticalGoodBlocks;
        this.abandonedStationState = new AbandonedStationState_1(this);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        abandonedStationState.activateEffect(inputCommand);
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getAstronautsNumber() {
        return astronautsNumber;
    }

    public Map<GoodBlock, Integer> getTheoreticalGoodBlocks() {
        return theoreticalGoodBlocks;
    }

    public void setAbandonedStationState(AbandonedStationState abandonedStationState) {
        this.abandonedStationState = abandonedStationState;
    }
}
