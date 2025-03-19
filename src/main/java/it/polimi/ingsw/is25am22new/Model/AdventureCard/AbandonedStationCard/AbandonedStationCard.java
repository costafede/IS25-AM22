package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.List;

public class AbandonedStationCard extends AdventureCard {

    private int flightDaysLost;
    private int astronautsNumber;
    private List<GoodBlock> goodBlocks;

    public AbandonedStationCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int astronautsNumber, List<GoodBlock> goodBlocks) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.astronautsNumber = astronautsNumber;
        this.goodBlocks = goodBlocks;
    }

    @Override
    public boolean checkActivationConditions(String nickname) {
        return game.getShipboards().get(nickname).getCrewNumber() >= astronautsNumber;
    }

    @Override
    public boolean activateCardPhase(String nickname, InputCommand inputCommand) {
        return inputCommand.getChoice();
    }

    @Override
    public boolean receiveInputPhase(String nickname, InputCommand inputCommand) {
        return true;
    }

    @Override
    public void resolveCardEffectPhase(String nickname) {
        return;
    }

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getAstronautsNumber() {
        return astronautsNumber;
    }

    public List<GoodBlock> getGoodBlocks() {
        return goodBlocks;
    }
}
