package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2.CombatZoneCard2;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard.OpenSpaceCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard.PiratesCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard.SmugglersCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard.StardustCard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.util.Map;

//Methods used to show the cards

public class AdventureCardView {

    public static void showPlanetsCard(PlanetsCard card) {
        System.out.println("=== PLANETS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        for (int i = 0; i < card.getPlanets().size(); i++) {
            System.out.print("Planet" + i + ":");
            Map<GoodBlock, Integer> theoreticalGoodblocks = card.getPlanets().get(i).getTheoreticalGoodblocks();
            Map<GoodBlock, Integer> actualGoodblocks = card.getPlanets().get(i).getActualGoodblocks();
            theoreticalGoodblocks.forEach((goodBlock, quantity) ->
                    System.out.println("Good: " + goodBlock + ", Theoretical quantity: " + quantity)
            );
            actualGoodblocks.forEach((goodBlock, quantity) ->
                    System.out.println("Good: " + goodBlock + ", Actual quantity: " + quantity)
            );
        }
    }

    public static void showAbandonedShipCard(AbandonedShipCard card) {
        System.out.println("=== ABANDONED SHIP ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Astronauts lost: " + card.getLostAstronauts());
    }

    public static void showAbandonedStationCard(AbandonedStationCard card) {
        System.out.println("=== ABANDONED STATION ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Astronauts: " + card.getAstronautsNumber());
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        Map<GoodBlock, Integer> actualGoodblocks = card.getTheoreticalGoodBlocks();
        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Theoretical quantity: " + quantity)
        );
        actualGoodblocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Actual quantity: " + quantity)
        );
    }

    public static void showCombatZoneCard(CombatZoneCard card) {
        System.out.println("=== COMBAT ZONE ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Lost astronauts: " + card.getAstronautsToLose());
        System.out.println("Dice1: " + card.getDice1());
        System.out.println("Dice2: " + card.getDice2());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    public static void showCombatZoneCard2(CombatZoneCard2 card) {
        System.out.println("=== COMBAT ZONE ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Lost goods: " + card.getLostGoods());
        System.out.println("Dice1: " + card.getDice1());
        System.out.println("Dice2: " + card.getDice2());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    public static void showEpidemicCard(EpidemicCard card) {
        System.out.println("=== EPIDEMIC ===");
        //Self-explanatory
    }

    public static void showMeteorSwarmCard(MeteorSwarmCard card) {
        System.out.println("=== METEOR SWARM ===");
        System.out.println("Dice1: " + card.getDice1());
        System.out.println("Dice2: " + card.getDice2());
        System.out.println("Incoming meteor number: " + card.getIndexOfIncomingMeteor());
        Map<Integer, Meteor> numberToMeteor = card.getNumberToMeteor();
        for (Map.Entry<Integer, Meteor> entry : numberToMeteor.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Meteor: " + entry.getValue());
        }
    }

    public static void showOpenSpaceCard(OpenSpaceCard card) {
        System.out.println("=== OPEN SPACE ===");
        //Self-explanatory
    }

    public static void showPiratesCard(PiratesCard card) {
        System.out.println("=== PIRATES ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Cannon's strength: " + card.getCannonStrength());
        System.out.println("Dice1: " + card.getDice1());
        System.out.println("Dice2: " + card.getDice2());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    public static void showSlaversCard(SlaversCard card) {
        System.out.println("=== SLAVERS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Astronauts to lose: " + card.getAstronautsToLose());
        System.out.println("Cannon's strength: " + card.getCannonStrength());
    }

    public static void showSmugglersCard(SmugglersCard card) {
        System.out.println("=== SMUGGLERS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Lost goods: " + card.getLostGoods());
        System.out.println("Cannon's strength: " + card.getCannonStrength());
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        Map<GoodBlock, Integer> actualGoodBlocks = card.getActualGoodBlocks();

        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Theoretical quantity: " + quantity)
        );
        actualGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Actual quantity: " + quantity)
        );
    }

    public static void showStardustCard(StardustCard card) {
        System.out.println("=== STARDUST ===");
        //Self-explanatory
    }

}
