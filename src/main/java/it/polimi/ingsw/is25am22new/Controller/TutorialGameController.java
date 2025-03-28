package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;

import java.util.ArrayList;
import java.util.List;

public class TutorialGameController {

    private final Game game;

    public TutorialGameController(List<String> playerList) {
        this.game = new TutorialGame(new ArrayList<>(playerList));
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

    public void finishBuilding(String nickname) {
        game.finishBuilding(nickname);
    }

    public void flipHourglass() {
        game.flipHourglass(() -> {

        });
    }

    public void pickCard() {
        game.pickCard();
    }

    public void playerAbadons(String nickname) {
        game.playerAbandons(nickname);
    }

    public void destroyTile(String nickname, int i, int j) {
        game.destroyTile(nickname, i, j);
    }

    public void endGame() {
        game.endGame();
    }

}
