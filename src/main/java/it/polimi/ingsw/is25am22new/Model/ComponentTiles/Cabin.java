package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

abstract class Cabin {

    private int numOfAstronauts;

    public abstract void putCrewMembersTile();

    public abstract void putAlienTile (String color);

    public abstract int getCrewNumberTile();

    public abstract boolean getBrownAlienPresenceTile();

    public abstract boolean getPurpleAlienPresenceTile();
}
