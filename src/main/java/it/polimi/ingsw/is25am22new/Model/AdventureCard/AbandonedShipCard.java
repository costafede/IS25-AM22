package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;

public class AbandonedShipCard extends AdventureCard{

    private int flightdaysLost;
    private int credits;
    private int lostAstronauts;

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
    }
    @Override
    public void activateCard(List<String> orderedPlayers) {
        return;
    }

    @Override
    public void activateCard(String player) {
        //to be implemented
        /*game.getShipboards(player).addCredits(credits);
        game.getShipboards(player).addFlightDays(-flightdaysLost);
        game.getShipboards(player).addAstronauts(-lostAstronauts);*/
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields, List<String> activatingCannon) {
        return;
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields) {
        return;
    }
}
