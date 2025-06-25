package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Flightboards.TutorialFlightBoard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The TutorialGame class represents a specialized game implementation
 * that extends the generic functionality of the Game class and includes
 * tutorial-specific features and logic. This class is used to handle a
 * tutorial mode for guiding players with specific game mechanics.
 *
 * TutorialGame introduces a custom flight board and modifies the deck
 * initialization and scoring methods to support tutorial-specific
 * objectives and gameplay.
 *
 * This class implements the Serializable interface to allow instances of
 * TutorialGame to be serialized and deserialized.
 */
public class TutorialGame extends Game implements Serializable {


    /**
     * Constructs a new TutorialGame instance, initializing the game with the provided list of players
     * and observers. This constructor also creates a TutorialFlightBoard specific to the tutorial game mode.
     *
     * @param nicknames a list of player nicknames participating in the tutorial game
     * @param observers a list of observers to monitor and react to changes in the game state
     */
    public TutorialGame(List<String> nicknames, List<ObserverModel> observers) {
        super(nicknames, observers);
        this.flightboard = new TutorialFlightBoard(this);
    }

    public TutorialGame(List<String> playerList, List<ObserverModel> observers, List<String> coveredComponentTilesNames, List<String> deckCardsNames, int randomSeed) {
        super(playerList,observers);
        this.flightboard = new TutorialFlightBoard(this);
        this.dices = new Dices(randomSeed);
        initGame();
        Map<String, Integer> position = new HashMap<>();
        for(int i = 0; i < coveredComponentTilesNames.size(); i++) {
            position.put(coveredComponentTilesNames.get(i), i);
        }
        coveredComponentTiles.sort(Comparator.comparingInt(t -> position.get(t.getPngName())));

        position.clear();
        for(int i = 0; i < deckCardsNames.size(); i++) {
            position.put(deckCardsNames.get(i), i);
        }
        this.deck = this.getCardArchive().stream()
                .filter(t -> position.containsKey(t.getPngName()))
                .sorted(Comparator.comparingInt(t -> position.get(t.getPngName())))
                .collect(Collectors.toList());
    }

    /**
     * Initializes the tutorial game mode by setting up the necessary components
     * and configurations specific to tutorial gameplay.
     *
     * This method overrides the default game initialization to include tutorial-specific
     * setup tasks. It first calls the superclass's {@code initGame()} method to establish
     * the base game setup, including components such as the game board and phase transitions.
     *
     * As part of the tutorial initialization:
     * - The {@code initDeck()} method is invoked to load and shuffle tutorial-specific cards
     *   into the game deck.
     * - The {@code updateAllGame()} method is called to apply any corrections to ship boards
     *   and notify observers of the game's updated state.
     */
    @Override
    public void initGame() {
        super.initGame();
        this.initDeck();
        updateAllGame(this);
    }

    /**
     * Ends the game by calculating and aggregating the final scores for all players.
     *
     * This method computes player scores based on the following:
     * 1. Partial scores derived from each player's shipboard (sold goods and penalties for discarded tiles).
     * 2. Bonus points for positions on the flightboard.
     * 3. Additional points awarded to the player with the best shipboard.
     *
     * The resulting scores are sorted in descending order, and the leaderboard is updated
     * by notifying all observers.
     *
     * @return A map containing player nicknames as keys and their corresponding final scores as values,
     *         sorted in descending order of scores.
     */
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

        updateAllLeaderboard(scores);

        return scores;
    }

    /**
     * Sorts the provided map in descending order based on the values.
     *
     * The resulting map will maintain the sorted order of the entries.
     *
     * @param scores A map containing keys and their associated integer values to be sorted.
     * @return A new map containing entries sorted in descending order by value.
     */
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

    /**
     * Initializes the deck for the tutorial game mode by loading the tutorial cards.
     *
     * This method performs the following steps:
     * 1. Filters the adventure cards from the card archive to retrieve only those marked as tutorial cards.
     * 2. Shuffles the filtered list of tutorial cards to ensure randomization.
     * 3. Adds the shuffled tutorial cards to the game deck.
     */
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
