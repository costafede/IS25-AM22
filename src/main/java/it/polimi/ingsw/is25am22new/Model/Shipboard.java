package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

public abstract class Shipboard {

    private boolean abbandoned;

    private int daysOnFlight;

    private String color;

    private String nickname;

    private ComponentTile[][] componentTilesGrid;

    private ComponentTile[] standbyComponent;

    private int discardedTiles;

    private boolean finishedShipboard;

    private int CosmicCredits;

    public void weldComponentTileShip (ComponentTile ct, int x, int y){
        //bla bla
    }

    public void standbyComponentTileShip (ComponentTile ct){
        //bla bla
    }

    /*public ComponentTile pickStandByComponentTileShip (int index){
        //bla bla
    }*/

    public void destroyTileShip (int x, int y){
        //bla bla
    }

    public void bla(){
        //bla
    }

}
