package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Flightboards.TutorialFlightBoard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;

import java.util.*;

public class TutorialGame extends Game {
    public TutorialGame(List<String> nicknames, List<ObserverModel> observers) {
        super(nicknames, observers);
        this.flightboard = new TutorialFlightBoard(this);
    }

    @Override
    public void initGame() {
        super.initGame();
        this.initDeck();
    }

    public Map<String, Integer> endGame() {
        Map<String, Integer> scores = new HashMap<>();

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

        scores = sortDesc(scores);

        return scores;
    }

    protected Map<String, Integer> sortDesc(Map<String, Integer> scores) {
        scores = scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        LinkedHashMap::putAll
                );
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
