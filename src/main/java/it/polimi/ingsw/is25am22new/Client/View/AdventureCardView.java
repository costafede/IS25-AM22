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
    /**
     * shows the information of the planets card
     * @param card
     */
    public void showPlanetsCard(PlanetsCard card) {
        System.out.println("=== PLANETS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println();
        for (int i = 0; i < card.getPlanets().size(); i++) {
            System.out.println("Planet" + i + ": ");
            Map<GoodBlock, Integer> theoreticalGoodblocks = card.getPlanets().get(i).getTheoreticalGoodblocks();
            theoreticalGoodblocks.forEach((goodBlock, quantity) ->
                    System.out.println(goodBlock + ", Quantity: " + quantity)
            );
            System.out.println();
        }
    }

    /**
     * shows the planets and if they're free, and also the respective goodBlocks available on the planet
     * @param card
     */
    public void showPlanetsCardInGame(PlanetsCard card) {
        System.out.println("=== PLANETS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        for (int i = 0; i < card.getPlanets().size(); i++) {
            System.out.print("Planet" + i + ": ");
            if(card.getPlanets().get(i).playerPresent()){
                System.out.println("A player is present");
            }
            else{
                System.out.println("No player is present. You can land, enjoy!");
            }
            System.out.println();
            Map<GoodBlock, Integer> theoreticalGoodblocks = card.getPlanets().get(i).getTheoreticalGoodblocks();
            theoreticalGoodblocks.forEach((goodBlock, quantity) ->
                    System.out.println(goodBlock + ", Quantity: " + quantity)
            );
            System.out.println();
            Map<GoodBlock, Integer> actualGoodblocks = card.getPlanets().get(i).getActualGoodblocks();
            actualGoodblocks.forEach((goodBlock, quantity) ->
                    System.out.println(goodBlock + ", Available: " + quantity)
            );
            System.out.println();
        }
    }

    /**
     * shows the information of the abandoned ship card
     * @param card
     */
    public void showAbandonedShipCard(AbandonedShipCard card) {
        System.out.println("=== ABANDONED SHIP ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Astronauts lost: " + card.getLostAstronauts());
    }

    /**
     * shows the information of the abandoned ship card in the game, where the player can choose to take the ship or not
     * @param card
     */
    public void showAbandonedShipCardInGame(AbandonedShipCard card) {
        System.out.println("=== ABANDONED SHIP ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Astronauts lost: " + card.getLostAstronauts());
        System.out.println("Is it a good idea to take the ship?");
    }

    /**
     * shows the information of the abandoned station card,
     * where the player can choose to land on the station or not,
     * and the theoretical goodblocks available on the station, and the actual ones available on the ship.
     * @param card
     */
    public void showAbandonedStationCard(AbandonedStationCard card) {
        System.out.println("=== ABANDONED STATION ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Astronauts: " + card.getAstronautsNumber());
        System.out.println();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println(goodBlock + ", Quantity: " + quantity)
        );
    }

    /**
     * shows the information of the abandoned station card in the game,
     * where the player can choose to land on the station or not,
     * @param card
     */
    public void showAbandonedStationCardInGame(AbandonedStationCard card) {
        System.out.println("=== ABANDONED STATION ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Astronauts: " + card.getAstronautsNumber());
        System.out.println();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println(goodBlock + ", Quantity: " + quantity)
        );
        System.out.println();
        Map<GoodBlock, Integer> actualGoodblocks = card.getActualGoodBlocks();
        actualGoodblocks.forEach((goodBlock, quantity) ->
                System.out.println(goodBlock + ", Available: " + quantity)
        );
        System.out.println();
        System.out.println("Is it a good idea to land on the station?");
    }

    /**
     * shows the information of the combat zone card
     * @param card
     */
    public void showCombatZoneCard(CombatZoneCard card) {
        System.out.println("=== COMBAT ZONE ===");
        System.out.println("The player with the least crewmembers loses " + card.getFlightDaysLost() + " days on flight");
        System.out.println("The player with the least engine strength loses " + card.getAstronautsToLose() + " crewmembers");
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    /**
     * shows the combat zone card info in game, with the specific shot incoming
     * @param card
     */
    public void showCombatZoneCardInGame(CombatZoneCard card) {
        System.out.println("=== COMBAT ZONE ===");
        String state = card.getStateName();
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        System.out.println("The player with the least crewmembers loses " + card.getFlightDaysLost() + " days on flight");
        System.out.println("The player with the least engine strength loses " + card.getAstronautsToLose() + " crewmembers");
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
        if (state.equals("CombatZoneState_6") || state.equals("CombatZoneState_7") || state.equals("CombatZoneState_8") || state.equals("CombatZoneState_9") || state.equals("CombatZoneState_10")){
            System.out.println("The player with the least cannon strength will be shot from this projectile:");
            int shotIndex = card.getIndexOfIncomingShot();
            System.out.println("Number of incoming shot: " + shotIndex);
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

    /**
     * shows the information of the combat zone card2
     * @param card
     */
    public void showCombatZoneCard2(CombatZoneCard2 card) {
        System.out.println("=== COMBAT ZONE ===");
        System.out.println("The player with the least cannon strength loses " + card.getFlightDaysLost() + " days on flight");
        System.out.println("The player with the least engine strength loses " + card.getLostGoods() + " goods");
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    /**
     * shows the combat zone card2 info in game, with the specific shot incoming
     * @param card
     */
    public void showCombatZoneCard2InGame(CombatZoneCard2 card) {
        System.out.println("=== COMBAT ZONE ===");
        String state = card.getStateName();
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        System.out.println("The player with the least cannon strength loses " + card.getFlightDaysLost() + " days on flight");
        System.out.println("The player with the least engine strength loses " + card.getLostGoods() + " goods");
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
        if (state.equals("CombatZoneState_6") || state.equals("CombatZoneState_7") || state.equals("CombatZoneState_8") || state.equals("CombatZoneState_9")){
            System.out.println("The player with the least crewmembers will be shot from this projectile:");
            int shotIndex = card.getIndexOfIncomingShot();
            System.out.println("Number of incoming shot: " + shotIndex);
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

    /**
     * shows the information of the epidemic card
     * @param card
     */
    public void showEpidemicCard(EpidemicCard card) {
        System.out.println("=== EPIDEMIC ===");
        //Self-explanatory
    }

    /**
     * shows the information of the epidemic card in game
     * @param card
     */
    public void showEpidemicCardInGame(EpidemicCard card) {
        System.out.println("=== EPIDEMIC ===");
        System.out.println("Oh no, a virus broke out in the ship. Wash carefully your hands and put on a mask");
    }

    /**
     * shows the information of the meteor swarm card
     * @param card
     */
    public void showMeteorSwarmCard(MeteorSwarmCard card) {
        System.out.println("=== METEOR SWARM ===");
        Map<Integer, Meteor> numberToMeteor = card.getNumberToMeteor();
        for (Map.Entry<Integer, Meteor> entry : numberToMeteor.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Meteor: " + entry.getValue());
        }
    }

    /**
     * shows the information of the meteor swarm card in game, with the specific meteor incoming
     * @param card
     */
    public void showMeteorSwarmCardInGame(MeteorSwarmCard card) {
        System.out.println("=== METEOR SWARM ===");
        int meteorIndex = card.getIndexOfIncomingMeteor();
        Map<Integer, Meteor> numberToMeteor = card.getNumberToMeteor();
        for (Map.Entry<Integer, Meteor> entry : numberToMeteor.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Meteor: " + entry.getValue());
        }
        System.out.println("Number of incoming meteor: " + meteorIndex);
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

    /**
     * shows the information of the open space card
     * @param card
     */
    public void showOpenSpaceCard(OpenSpaceCard card) {
        System.out.println("=== OPEN SPACE ===");
        //Self-explanatory
    }

    /**
     * shows the information of the open space card in game
     * @param card
     */
    public void showOpenSpaceCardInGame(OpenSpaceCard card) {
        System.out.println("=== OPEN SPACE ===");
        System.out.println("Who's the quickest? Go full throttle and overtake the competition!");
    }

    /**
     * shows the information of the pirates card
     * @param card
     */
    public void showPiratesCard(PiratesCard card) {
        System.out.println("=== PIRATES ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Cannon's strength: " + card.getCannonStrength());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    /**
     * shows the information of the pirates card in game, with the specific shot incoming
     * @param card
     */
    public void showPiratesCardInGame(PiratesCard card) {
        System.out.println("=== PIRATES ===");
        System.out.println("ARRRR! Hide the bottles of Rum and load the cannons!");
        System.out.println("The reward if you eliminate the threat: " + card.getCredits() + " credits");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Pirate's cannons strength: " + card.getCannonStrength());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        int shotIndex = card.getIndexOfIncomingShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
        System.out.println("Number of incoming shot: " + shotIndex);
        if (numberToShot.get(shotIndex).getOrientation().equals(Orientation.TOP) || numberToShot.get(shotIndex).getOrientation().equals(Orientation.BOTTOM)) {
            System.out.println("Dice1: " + card.getDice1());
            System.out.println("Dice2: " + card.getDice2());
            int column = card.getDice1() + card.getDice2();
            System.out.println("Shot incoming in column: " + column);
        }

    }

    /**
     * shows the information of the slavers card
     * @param card
     */
    public void showSlaversCard(SlaversCard card) {
        System.out.println("=== SLAVERS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Credits: " + card.getCredits());
        System.out.println("Astronauts to lose: " + card.getAstronautsToLose());
        System.out.println("Cannon's strength: " + card.getCannonStrength());
    }

    /**
     * shows the information of the slavers card in game
     * @param card
     */
    public void showSlaversCardInGame(SlaversCard card) {
        System.out.println("=== SLAVERS ===");
        System.out.println("We're back in colonialism... Prepare the cannons and watch out for the slavers!");
        System.out.println("Slavers's cannon strength: " + card.getCannonStrength());
        System.out.println("The reward if you eliminate the threat: " + card.getCredits() + " credits");
        System.out.println("If you're weak you will lose " + card.getAstronautsToLose() + " crewmembers");
    }

    /**
     * shows the information of the smugglers card
     * @param card
     */
    public void showSmugglersCard(SmugglersCard card) {
        System.out.println("=== SMUGGLERS ===");
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("Lost goods: " + card.getLostGoods());
        System.out.println("Cannon's strength: " + card.getCannonStrength());
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println(goodBlock + ", Quantity: " + quantity)
        );
    }

    /**
     * shows the information of the smugglers card in game
     * @param card
     */
    public void showSmugglersCardInGame(SmugglersCard card) {
        System.out.println("=== SMUGGLERS ===");
        System.out.println("Better watch out! If you're weak you will lose valuable stuff");
        System.out.println("Smuggler's cannon strength: " + card.getCannonStrength());
        System.out.println("Days on flight lost: " + card.getFlightDaysLost());
        System.out.println("The reward if you eliminate the threat:");
        Map<GoodBlock, Integer> actualGoodBlocks = card.getActualGoodBlocks();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Quantity: " + quantity)
        );
        System.out.println();
        actualGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Good: " + goodBlock + ", Available: " + quantity)
        );
        System.out.println("If you're weak you will lose " + card.getLostGoods() + " goods");
    }

    /**
     * shows the information of the stardust card
     * @param card
     */
    public void showStardustCard(StardustCard card) {
        System.out.println("=== STARDUST ===");
        //Self-explanatory
    }

    /**
     * shows the information of the stardust card in game
     * @param card
     */
    public void showStardustCardInGame(StardustCard card) {
        System.out.println("=== STARDUST ===");
        System.out.println("Oh no, the exposed connectors of the ship will slow you down");
    }

}
