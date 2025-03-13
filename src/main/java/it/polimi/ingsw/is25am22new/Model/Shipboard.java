package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

public abstract class Shipboard {

    private boolean abbandoned;

    private int daysOnFlight;

    private String color;

    private String nickname;

    private ComponentTile[5][7] componentTilesGrid;

    private ComponentTile[2] standbyComponent;

    private int discardedTiles;

    private boolean finishedShipboard;


}
