package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneState;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneState_0;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2.CombatZoneCard2;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard.OpenSpaceCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard.PiratesCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard.SmugglersCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard.StardustCard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.util.Map;

/**
 * Methods used to show the cards in the TUI
 * the simple show method displays the info of the card as it is
 * the InGame version displays the information necessary for the player to play the card
 */

public class AdventureCardView {

    public static void showPlanetsCard(PlanetsCard card) {
        System.out.println("=== PLANETS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println();
        for (int i = 0; i < card.getPlanets().size(); i++) {
            System.out.println("Planet" + i + ": ");
            Map<GoodBlock, Integer> theoreticalGoodblocks = card.getPlanets().get(i).getTheoreticalGoodblocks();
            Map<GoodBlock, Integer> actualGoodblocks = card.getPlanets().get(i).getActualGoodblocks();
            theoreticalGoodblocks.forEach((goodBlock, quantity) ->
                    System.out.println("Good: " + goodBlock + ", Theoretical quantity: " + quantity)
            );
            System.out.println();
            actualGoodblocks.forEach((goodBlock, quantity) ->
                    System.out.println("Good: " + goodBlock + ", Actual quantity: " + quantity)
            );
            System.out.println();
        }
    }

    /**
     * shows the planets and if they're free, and also the respective goodBlocks available on the planet
     */
    public static void showPlanetsCardInGame(PlanetsCard card) {
        System.out.println("=== PLANETS ===");
        for (int i = 0; i < card.getPlanets().size(); i++) {
            System.out.print("Planet" + i + ": ");
            if(card.getPlanets().get(i).playerPresent()){
                System.out.println("A player is present. You can't land mate");
            }
            else{
                System.out.println("No player is present. You can land, enjoy!");
                Map<GoodBlock, Integer> actualGoodblocks = card.getPlanets().get(i).getActualGoodblocks();
                actualGoodblocks.forEach((goodBlock, quantity) ->
                        System.out.println("Good: " + goodBlock + ", Actual quantity: " + quantity)
                );
            }
            System.out.println();
        }
    }

    public static void showAbandonedShipCard(AbandonedShipCard card) {
        System.out.println("=== ABANDONED SHIP ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Astronauts lost: " + card.getLostAstronauts());
    }

    public static void showAbandonedShipCardInGame(AbandonedShipCard card) {
        System.out.println("=== ABANDONED SHIP ===");
        System.out.println("Is it a good idea to take the ship?");
    }

    public static void showAbandonedStationCard(AbandonedStationCard card) {
        System.out.println("=== ABANDONED STATION ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Astronauts: " + card.getAstronautsNumber());
        System.out.println();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        Map<GoodBlock, Integer> actualGoodBlocks = card.getTheoreticalGoodBlocks();
        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Theoretical quantity: " + quantity)
        );
        System.out.println();
        actualGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Actual quantity: " + quantity)
        );
    }

    public static void showAbandonedStationCardInGame(AbandonedStationCard card) {
        System.out.println("=== ABANDONED STATION ===");
        System.out.println("Is it a good idea to land on the station?");
        Map<GoodBlock, Integer> actualGoodblocks = card.getTheoreticalGoodBlocks();
        actualGoodblocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Actual quantity: " + quantity)
        );

    }

    public static void showCombatZoneCard(CombatZoneCard card) {
        System.out.println("=== COMBAT ZONE ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Lost astronauts: " + card.getAstronautsToLose());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    public static void showCombatZoneCardInGame(CombatZoneCard card) {
        System.out.println("=== COMBAT ZONE ===");
        String state = card.getStateName();
        if (state.equals("CombatZoneState_0")){
            System.out.println("The player with the least crewmembers loses " + card.getFlightDaysLost() + " days on flight");
        }
        else if (state.equals("CombatZoneState_1") || state.equals("CombatZoneState_3") || state.equals("CombatZoneState_5")){
            System.out.println("The player with the least engine strength loses " + card.getAstronautsToLose() + " crewmembers");
        }
        else if (state.equals("CombatZoneState_6") || state.equals("CombatZoneState_7") || state.equals("CombatZoneState_8") || state.equals("CombatZoneState_9") || state.equals("CombatZoneState_10")){
            System.out.println("The player with the least cannon strength will be shot from this projectile:");
            Map<Integer, Shot> numberToShot = card.getNumberToShot();
            int shotIndex = card.getIndexOfIncomingShot();
            System.out.println("Number of incoming shot: " + shotIndex + ", Shot: " + numberToShot.get(shotIndex));
            if (numberToShot.get(shotIndex).getOrientation().equals(Orientation.TOP) || numberToShot.get(shotIndex).getOrientation().equals(Orientation.BOTTOM)) {
                System.out.println("Dice1: " + card.getDice1());
                System.out.println("Dice2: " + card.getDice2());
                int column = card.getDice1() + card.getDice2();
                System.out.println("Shot incoming in column: " + column);
            }
            else{
                System.out.println("Dice1: " + card.getDice1());
                System.out.println("Dice2: " + card.getDice2());
                int row = card.getDice1() + card.getDice2();
                System.out.println("Shot incoming in row: " + row);
            }
        }
    }

    public static void showCombatZoneCard2(CombatZoneCard2 card) {
        System.out.println("=== COMBAT ZONE ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Lost goods: " + card.getLostGoods());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    public static void showCombatZoneCard2InGame(CombatZoneCard2 card) {
        System.out.println("=== COMBAT ZONE ===");
        String state = card.getStateName();
        if (state.equals("CombatZoneState2_1") || state.equals("CombatZoneState2_2")){
            System.out.println("The player with the least cannon strength loses " + card.getFlightDaysLost() + " days on flight");
        }
        else if (state.equals("CombatZoneState2_3") || state.equals("CombatZoneState2_4")){
            System.out.println("The player with the least engine strength loses " + card.getLostGoods() + " goods");
        }
        else if (state.equals("CombatZoneState_6") || state.equals("CombatZoneState_7") || state.equals("CombatZoneState_8") || state.equals("CombatZoneState_9")){
            System.out.println("The player with the least crewmembers will be shot from this projectile:");
            Map<Integer, Shot> numberToShot = card.getNumberToShot();
            int shotIndex = card.getIndexOfIncomingShot();
            System.out.println("Number of incoming shot: " + shotIndex + ", Shot: " + numberToShot.get(shotIndex));
            if (numberToShot.get(shotIndex).getOrientation().equals(Orientation.TOP) || numberToShot.get(shotIndex).getOrientation().equals(Orientation.BOTTOM)) {
                System.out.println("Dice1: " + card.getDice1());
                System.out.println("Dice2: " + card.getDice2());
                int column = card.getDice1() + card.getDice2();
                System.out.println("Shot incoming in column: " + column);
            }
            else{
                System.out.println("Dice1: " + card.getDice1());
                System.out.println("Dice2: " + card.getDice2());
                int row = card.getDice1() + card.getDice2();
                System.out.println("Shot incoming in row: " + row);
            }
        }
    }

    public static void showEpidemicCard(EpidemicCard card) {
        System.out.println("=== EPIDEMIC ===");
        //Self-explanatory
    }

    public static void showEpidemicCardInGame(EpidemicCard card) {
        System.out.println("=== EPIDEMIC ===");
        System.out.println("Oh no, a virus broke out in the ship. Wash carefully your hands and put on a mask");
    }

    public static void showMeteorSwarmCard(MeteorSwarmCard card) {
        System.out.println("=== METEOR SWARM ===");
        Map<Integer, Meteor> numberToMeteor = card.getNumberToMeteor();
        for (Map.Entry<Integer, Meteor> entry : numberToMeteor.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Meteor: " + entry.getValue());
        }
    }

    public static void showMeteorSwarmCardInGame(MeteorSwarmCard card) {
        System.out.println("=== METEOR SWARM ===");
        int meteorIndex = card.getIndexOfIncomingMeteor();
        Map<Integer, Meteor> numberToMeteor = card.getNumberToMeteor();
        System.out.println("Number of incoming meteor: " + meteorIndex + ", Meteor: " + numberToMeteor.get(meteorIndex));
        if (numberToMeteor.get(meteorIndex).getOrientation().equals(Orientation.TOP) || numberToMeteor.get(meteorIndex).getOrientation().equals(Orientation.BOTTOM)) {
            System.out.println("Dice1: " + card.getDice1());
            System.out.println("Dice2: " + card.getDice2());
            int column = card.getDice1() + card.getDice2();
            System.out.println("Meteor incoming in column: " + column);
        }
        else{
            System.out.println("Dice1: " + card.getDice1());
            System.out.println("Dice2: " + card.getDice2());
            int row = card.getDice1() + card.getDice2();
            System.out.println("Meteor incoming in row: " + row);
        }
    }

    public static void showOpenSpaceCard(OpenSpaceCard card) {
        System.out.println("=== OPEN SPACE ===");
        //Self-explanatory
    }

    public static void showOpenSpaceCardInGame(OpenSpaceCard card) {
        System.out.println("=== OPEN SPACE ===");
        System.out.println("Who's the quickest? Go full throttle and overtake the competition!");
    }

    public static void showPiratesCard(PiratesCard card) {
        System.out.println("=== PIRATES ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Cannon's strength: " + card.getCannonStrength());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    public static void showPiratesCardInGame(PiratesCard card) {
        System.out.println("=== PIRATES ===");
        System.out.println("ARRRR! Hide the bottles of Rum and load the cannons!");
        System.out.println("The reward if you eliminate the threat: " + card.getCredits() + " credits");
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        int shotIndex = card.getIndexOfIncomingShot();
        System.out.println("Number of incoming shot: " + shotIndex + ", Shot: " + numberToShot.get(shotIndex));
        if (numberToShot.get(shotIndex).getOrientation().equals(Orientation.TOP) || numberToShot.get(shotIndex).getOrientation().equals(Orientation.BOTTOM)) {
            System.out.println("Dice1: " + card.getDice1());
            System.out.println("Dice2: " + card.getDice2());
            int column = card.getDice1() + card.getDice2();
            System.out.println("Shot incoming in column: " + column);
        }

    }

    public static void showSlaversCard(SlaversCard card) {
        System.out.println("=== SLAVERS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Astronauts to lose: " + card.getAstronautsToLose());
        System.out.println("Cannon's strength: " + card.getCannonStrength());
    }

    public static void showSlaversCardInGame(SlaversCard card) {
        System.out.println("=== SLAVERS ===");
        System.out.println("We're back in colonialism... Prepare the cannons and watch out for the slavers!");
        System.out.println("Slavers's cannon strength: " + card.getCannonStrength());
        System.out.println("The reward if you eliminate the threat: " + card.getCredits() + " credits");
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

    public static void showSmugglersCardInGame(SmugglersCard card) {
        System.out.println("=== SMUGGLERS ===");
        System.out.println("Better watch out! If you're weak you will lose valuable stuff");
        System.out.println("Smuggler's cannon strength: " + card.getCannonStrength());
        System.out.println("The reward if you eliminate the threat:");
        Map<GoodBlock, Integer> actualGoodBlocks = card.getActualGoodBlocks();
        actualGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Actual quantity: " + quantity)
        );

    }

    public static void showStardustCard(StardustCard card) {
        System.out.println("=== STARDUST ===");
        //Self-explanatory
    }

    public static void showStardustCardInGame(StardustCard card) {
        System.out.println("=== STARDUST ===");
        System.out.println("Oh no, the exposed connectors of the ship will slow you down");
    }

}
