package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.CardPile;
import it.polimi.ingsw.is25am22new.Model.Games.*;

import java.util.ArrayList;
import java.util.List;

public class Level2Controller {
    private final Game game;

    public Level2Controller(Game game) {
        this.game = game;
    }

    public Level2Controller(List<String> playerList) {
        this.game = new Level2Game(new ArrayList<>(playerList));
        this.initGame();
    }

    public void initGame() {
        game.initGame();
    }

    public void pickCoveredTile(String player) {
        game.pickCoveredTile(player);
    }

    public void pickUncoveredTile(String player, int index) {
        game.pickUncoveredTile(player, index);
    }

    public void rotateClockwise(String player) {
        game.rotateClockwise(player);
    }

    public void rotateCounterClockwise(String player) {
        game.rotateCounterClockwise(player);
    }

    public void weldComponentTile(String player, int i, int j) {
        game.weldComponentTile(player, i, j);
    }

    public void standbyComponentTile(String player) {
        game.standbyComponentTile(player);
    }

    public void pickStandByComponentTile(String player, int index) {
        game.pickStandByComponentTile(player, index);
    }

    public void discardComponentTile(String player) {
        game.discardComponentTile(player);
    }

    public boolean finishBuilding(String player) {
        return game.finishBuilding(player);
    }

    public boolean finishBuilding(String player, int pos) {
        return game.finishBuilding(player, pos);
    }

    public boolean finishedAllShipboards() {
        return game.finishedAllShipboards();
    }

    public void flipHourglass() {
        game.flipHourglass(() -> {});
    }

    public void pickCard() {
        game.pickCard();
    }

    public void playerAbandons(String player) {
        game.playerAbandons(player);
    }

    public void destroyTile(String player, int i, int j) {
        game.destroyTile(player, i, j);
    }

    public void setCurrPlayer(String player) {
        game.setCurrPlayer(player);
    }

    public void setCurrPlayerToLeader() {
        game.setCurrPlayerToLeader();
    }

    public Game getGame() {
        return game;
    }

    public AdventureCard getCurrCard() {
        return game.getCurrCard();
    }

    public String getLastPlayer() {
        return game.getLastPlayer();
    }

    public List<CardPile> getCardPiles() {
        return ((Level2Game)game).getCardPiles();
    }

    public List<AdventureCard> getDeck() {
        return game.getDeck();
    }

    public void endGame() {
        game.endGame();
    }
}
