package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TutorialGame extends Game {
    public TutorialGame(List<String> nicknames) {
        super(nicknames);
    }

    @Override
    public void initGame() {
        super.initGame();
        this.initDeck();
    }

    public Map<String, Integer> endGame() {
        Map<String, Integer> scores = new HashMap<>();
        String nickname = super.betterShipboard();

        //Calculate the partial scores : Score = Sold Goods - Discard Tiles
        for(String player : playerList) {
            scores.put(player, shipboards.get(player).getScore());
        }

        //Adding scores for position in the flightboard
        List<String> orderedRockets = flightboard.getOrderedRockets();
        for(int i = 0; i < orderedRockets.size(); i++) {
            scores.put(orderedRockets.get(i), scores.get(orderedRockets.get(i)) + (4 - orderedRockets.indexOf(orderedRockets.get(i))));
        }

        //Adding scores for the best shipboard
        scores.put(betterShipboard(), scores.get(betterShipboard()) + 2);

        return scores;
    }

    @Override
    public void initDeck() {
        // Reads 8 cards from json file and adds them to the deck
        List<AdventureCard> tutorialCards = new java.util.ArrayList<>(cardArchive.stream()
                .filter(AdventureCard::isTutorial)
                .toList());

        Collections.shuffle(tutorialCards);

        deck.addAll(tutorialCards);
    }
}
