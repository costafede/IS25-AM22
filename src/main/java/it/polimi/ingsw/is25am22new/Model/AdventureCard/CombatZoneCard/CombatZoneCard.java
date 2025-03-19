package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.Map;

public class CombatZoneCard extends AdventureCard {

    private int flightDaysLost;
    private int lostAstronauts;
    private int lostGoods;
    private Map<Integer, Shot> numberToShot;

    public CombatZoneCard(String pngName, String name, Game game, int level, boolean tutorial, int flightDaysLost, int lostAstronauts, int lostGoods, Map<Integer, Shot> numberToShot) {
        super(pngName, name, game, level, tutorial);
        this.flightDaysLost = flightDaysLost;
        this.lostAstronauts = lostAstronauts;
        this.numberToShot = numberToShot;
        this.lostGoods = lostGoods;
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

    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getLostAstronauts() {
        return lostAstronauts;
    }

    public int getLostGoods() {
        return lostGoods;
    }

    public Map<Integer, Shot> getNumberToShot() {
        return numberToShot;
    }
}
