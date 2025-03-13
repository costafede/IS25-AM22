package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class Game {
    private List<String> playerList;
    private Bank bank;
    private Map<String, Integer> cardArchive;
    private Set<ComponentTile> coveredComponentTiles;
    private List<ComponentTile> uncoveredComponentTiles;
    private Map<String, Shipboard> shipboards;
    private Flightboard flightboard;

    public Game() {
        // temporaneo
        playerList = new ArrayList<>();
        bank = new Bank();
        cardArchive = new HashMap<>();
        coveredComponentTiles = new HashSet<>();
        uncoveredComponentTiles = new ArrayList<>();
        shipboards = new HashMap<>();
        flightboard = new FlightBoard();
    }

    public Shipboard getShipboards(String player) {
        return shipboards.get(player);
    }

    public void initGame(){

    }
}
