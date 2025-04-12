package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;

import java.util.ArrayList;
import java.util.List;

public abstract class ObservableModelView {
    List<ClientModelObserver> listeners = new ArrayList<>();
    //remember to add listeners to the list
    public void addListener(ClientModelObserver ld) {
        listeners.add(ld);
    }

    public void removeListener(ClientModelObserver ld) {
        listeners.remove(ld);
    }

    protected void notifyObservers(ClientModel model) {
        for (ClientModelObserver ld : listeners) {
            ld.modelChanged(model);
        }
    }
}
