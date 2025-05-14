package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Client.View.AdventureCardView;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

public class AbandonedShipCard extends AdventureCard implements Serializable, ViewableCard {

    private int flightdaysLost;
    private int credits;
    private int lostAstronauts;
    private AbandonedShipState abandonedShipState;

    public int getFlightDaysLost() {
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
        this.abandonedShipState = new AbandonedShipState_1(this);
    }


    @Override
    public void activateEffect(InputCommand inputCommand) {
        abandonedShipState.activateEffect(inputCommand);
    }

    @Override
    public String getStateName() {
        return abandonedShipState.getStateName();
    }

    public void setAbandonedShipState(AbandonedShipState abandonedShipState) {
        this.abandonedShipState = abandonedShipState;
    }

    public AbandonedShipState getAbandonedShipState() {
        return abandonedShipState;
    }

    @Override
    public void show(AdventureCardView view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showAbandonedShipCardInGame(this);
        }
        else{
            view.showAbandonedShipCard(this);
        }
    }

}
