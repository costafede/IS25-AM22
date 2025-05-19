package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for verifying the initialization of the tutorial deck in a tutorial game.
 *
 * This class contains a test to ensure that the tutorial deck is correctly initialized
 * with the expected number of adventure cards and that each card in the deck belongs
 * to the tutorial mode.
 */
class inittutorialdecktest {

    @Test
    void tutorial_deck_should_contains_8_AdventureCards_and_isTutorial_has_to_be_true() {
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Federico");
        nicknames.add("Giovanni");
        nicknames.add("Alessandro");
        nicknames.add("Davide");
        TutorialGame tg = new TutorialGame(nicknames, null);

        tg.initGame();
        assertEquals(8, tg.getDeck().size());
        assertTrue(tg.getDeck().stream().allMatch(AdventureCard::isTutorial));
    }
}