package it.polimi.ingsw.is25am22new.Model.Games;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The initlevel2deckandpilestest class contains unit tests designed to verify the proper
 * initialization of the Level 2 game's deck and card piles. The tests ensure compliance
 * with game rules and constraints during the setup phase of the game.
 */
class initlevel2deckandpilestest {

    @Test
    void level2_deck_should_contains_12_AdventureCards() {
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Federico");
        nicknames.add("Giovanni");
        nicknames.add("Alessandro");
        nicknames.add("Davide");
        Level2Game l2g = new Level2Game(nicknames, null);

        l2g.initGame();
        assertEquals(12, l2g.getDeck().size());
    }

    @Test
    void level2_piles_should_be_4_and_should_contains_2_level2_card_and_1_level1_card(){
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Federico");
        nicknames.add("Giovanni");
        nicknames.add("Alessandro");
        nicknames.add("Davide");
        Level2Game l2g = new Level2Game(nicknames, null);

        l2g.initGame();
        assertEquals(4, l2g.getCardPiles().size());
        for (int i = 0; i < 4; i++) {
            assertEquals(3, l2g.getCardPiles().get(i).getCards().size());
            assertEquals(1, l2g.getCardPiles().get(i).getCards().get(0).getLevel());
            assertEquals(2, l2g.getCardPiles().get(i).getCards().get(1).getLevel());
        }
    }

}