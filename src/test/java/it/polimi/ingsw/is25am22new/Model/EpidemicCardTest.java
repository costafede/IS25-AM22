package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class EpidemicCardTest {

    @Test
    void RemovesCrewFromConnectedCabins(){
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Federico");
        nicknames.add("Tommaso");
        nicknames.add("Emanuele");
        nicknames.add("Anatoly");

        Level2Game l2g = new Level2Game(nicknames);

        EpidemicCard ec = new EpidemicCard( "x", "Epidemia", l2g, 2, false);


    }
}