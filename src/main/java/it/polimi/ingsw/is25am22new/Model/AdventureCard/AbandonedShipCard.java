package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.MockClasses.InputCommand;

import java.util.List;

public class AbandonedShipCard extends AdventureCard{

    private int flightdaysLost;
    private int credits;
    private int lostAstronauts;
    private int removedAstronauts;

    public int getFlightdaysLost() {
        return flightdaysLost;
    }

    public int getCredits() {
        return credits;
    }

    public int getLostAstronauts() {
        return lostAstronauts;
    }

    public AbandonedShipCard(String pngName, String name, Game game, int level, boolean tutorial, int flightdaysLost, int credits, int lostAstronauts) {
        super(pngName, name, game, level, tutorial);
        this.credits = credits;
        this.flightdaysLost = flightdaysLost;
        this.lostAstronauts = lostAstronauts;
        this.removedAstronauts = 0;
    }

    @Override
    public boolean checkActivationConditions(String nickname) {
        return game.getShipboards().get(nickname).getCrewNumber() >= lostAstronauts;
    }

    @Override
    public boolean activateCardPhase(String nickname, InputCommand inputCommand) {
        return inputCommand.getChoice();
    }

    @Override
    public boolean receiveInputPhase(String nickname, InputCommand inputCommand) {
        int x = inputCommand.getXCoordGrid();
        int y = inputCommand.getYCoordGrid();

        game.getShipboards().get(nickname).getComponentTileFromGrid(x, y).removeCrewMember();
        removedAstronauts++;
        // goes on until the player has removed all the astronauts the card requires
        return removedAstronauts < lostAstronauts;
    }

    @Override
    public void resolveCardEffectPhase(String nickname) {
        game.getShipboards().get(nickname).addCosmicCredits(credits);
        game.getFlightboard().shiftRocket(game.getShipboards(), nickname, flightdaysLost);
    }
}
