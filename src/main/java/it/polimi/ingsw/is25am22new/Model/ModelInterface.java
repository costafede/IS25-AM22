package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.util.List;

public interface ModelInterface {
    public void initGame(List<String> nicknames);

    public ComponentTile pickCoveredTile();

    public ComponentTile pickUncoveredTile(int index);

    public void weldComponentTile(String nickname, ComponentTile ct, int x, int y);

    public void standbyComponentTile(String nickname, ComponentTile ct);

    public ComponentTile pickStandByComponentTile(String nickname, int index);

    public void discardComponentTile(ComponentTile ct);

    public void placeRocket(String nickname, int pos);

    public boolean checkShipboards();

    public void createDeck();

    public void isDeckEmpty();

    public AdventureCard pickCard();

    public List<String> checkInvalidPlayers();

    public void chooseToAbandon(String nickname);

    public Hashmap<String, Integer> calculateFinalScores();

    public void flipHourglass();

    public List<ComponentTile> getUncoveredTilesList();

    public CardPile getCardPile(int index);

    public Map<String, Shipboard> getShipboards();

    public Flightboard getFlightboard();

    public List<String> getPlayerList();

    public void addGoodBlock(String nickname, int x, int y, GoodBlock gb);

    public GoodBlock removeGoodBlock(String nickname, int x, int y, int index);

    public GoodBlock withdrawGoodBlock();

    public void depositGoodBlock(GoodBlock gb);

    public void shiftRocket(String nickname, int steps);

    public void putCrewMembers(String nickname, int x, int y);

    public boolean isAlienPlaceable(String nickname, int x, int y, String color);

    public void putAlien(String nickname, int x, int y, String color);

    public boolean isBlockPlaceable(String nickname, int x, int y, GoodBlock gb);

    public int getCrewNumber(String nickname);

    public void removeCrewMember(String nickname, int x, int y);

    public void addCosmicCredits(String nickname, int credit);

    public void removeMostValuableGoodBlocks(String nickname);

    public void removeBatteryToken(String nickname, int x, int y);

    public double getCannonStrength(String nickname);

    public int getEngineStrength(String nickname, int x, int y);

    public boolean rowEmpty(String nickname, int numOfRow);

    public boolean columnEmpty(String nickname, int numOfColumn);

    public boolean isLeftSideSmooth(String nickname, int x, int y);

    public boolean isRightSideSmooth(String nickname, int x, int y);

    public boolean isTopSideSmooth(String nickname, int x, int y);

    public boolean isBottomSideSmooth(String nickname, int x, int y);

    public void activateComponent(String nickname, int x, int y);

    public void deactivateComponent(String nickname, int x, int y);

    public void destroyTile(String nickname, int x, int y);

    public int[][] findShipWreck(String nickname);

    public boolean isBottomSideEngine(String nickname, int x, int y);

    public boolean isTopSideEngine(String nickname, int x, int y);

    public boolean isLeftSideEngine(String nickname, int x, int y);

    public boolean isRightSideEngine(String nickname, int x, int y);

    public boolean isLeftSideShielded(String nickname);

    public boolean isRightSideShielded(String nickname);

    public boolean isTopSideShielded(String nickname);

    public boolean isBottomSideShielded(String nickname);

    public boolean isLeftSideCannon(String nickname, int x, int y);

    public boolean isRightSideCannon(String nickname, int x, int y);

    public boolean isTopSideCannon(String nickname, int x, int y);

    public boolean isBottomSideCannon(String nickname, int x, int y);

    public int countExposedConnectors(String nickname);
}