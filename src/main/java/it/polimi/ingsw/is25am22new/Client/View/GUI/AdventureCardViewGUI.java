package it.polimi.ingsw.is25am22new.Client.View.GUI;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2.CombatZoneCard2;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
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

/**
 * Methods used to show the cards in the TUI
 * the simple show method displays the info of the card as it is
 * the InGame version displays the information necessary for the player to play the card
 */

public class AdventureCardViewGUI {
    /**
     * shows the information of the planets card
     * @param card
     */
    public String showPlanetsCard(PlanetsCard card) {
        String output = "";

        for (int i = 0; i < card.getPlanets().size(); i++) {
            output = output.concat("Planet" + (i+1) + ": ");
            if (card.getPlanets().get(i).playerPresent()) {
                output = output.concat("A player is present\n");
            } else {
                output = output.concat("No player is present. You can land, enjoy!\n");
            }
        }

        return "Planets Card\n" + output;
    }

    /**
     * shows the information of the abandoned ship card
     * @param card
     */
    public String showAbandonedShipCard(AbandonedShipCard card) {
        return "Abandoned Ship Card";
    }

    /**
     * shows the information of the abandoned station card,
     * where the player can choose to land on the station or not,
     * and the theoretical goodblocks available on the station, and the actual ones available on the ship.
     * @param card
     */
    public String showAbandonedStationCard(AbandonedStationCard card) {
        return "Abandoned Station Card";
    }

    /**
     * shows the information of the combat zone card
     * @param card
     */
    public String showCombatZoneCard(CombatZoneCard card) {
        String output = "\n";
        if(card.getStateName().equals("CombatZoneState_8") || card.getStateName().equals("CombatZoneState_9") || card.getStateName().equals("CombatZoneState_10")) {
            output = output.concat("Incoming shot: " + (card.getIndexOfIncomingShot()+1)) + " - Look at the dices\n";
        } else {
            output = output.concat("No incoming shot");
        }
        return "Combat Zone Card" + output;
    }

    /**
     * shows the combat zone card2 info in game, with the specific shot incoming
     * @param card
     */
    public String showCombatZoneCard2InGame(CombatZoneCard2 card) {
        String output = "\n";
        String state = card.getStateName();
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        if (state.equals("CombatZoneState_6") || state.equals("CombatZoneState_7") || state.equals("CombatZoneState_8") || state.equals("CombatZoneState_9")){
            int shotIndex = card.getIndexOfIncomingShot();
            output = output.concat("Incoming shot: " + (shotIndex+1) + " - Look at the dices\n");
        } else {
            output = output.concat("No incoming shot");
        }
        return "Combat Zone Card" + output;
    }

    /**
     * shows the information of the epidemic card in game
     * @param card
     */
    public String showEpidemicCardInGame(EpidemicCard card) {
        return "Epidemic Card\nOh no, a virus broke out in the ship. Wash carefully your hands and put on a mask";
    }

    /**
     * shows the information of the meteor swarm card in game, with the specific meteor incoming
     * @param card
     */
    public String showMeteorSwarmCardInGame(MeteorSwarmCard card) {
        String output = "\n";

        int meteorIndex = card.getIndexOfIncomingMeteor();
        output = output.concat("Number of incoming meteor: " + (meteorIndex+1) + " - Look at the dices\n");

        return "Meteor Swarm Card" + output;
    }

    /**
     * shows the information of the open space card in game
     * @param card
     */
    public String showOpenSpaceCardInGame(OpenSpaceCard card) {
        return "OpenSpace\nWho's the quickest? Go full throttle and overtake the competition!";
    }

    /**
     * shows the information of the pirates card in game, with the specific shot incoming
     * @param card
     */
    public String showPiratesCardInGame(PiratesCard card) {

        String output = "\n";

        int shotIndex = card.getIndexOfIncomingShot();
        output = output.concat("Number of incoming shot: " + (shotIndex+1) + " - Look at the dices\n");


        return "Pirates Card" + output;

    }

    /**
     * shows the information of the slavers card in game
     * @param card
     */
    public String showSlaversCardInGame(SlaversCard card) {
        return "Slavers Card\nOh no, the slavers are coming! You have to be careful and protect your crew";
    }

    /**
     * shows the information of the smugglers card in game
     * @param card
     */
    public String showSmugglersCardInGame(SmugglersCard card) {
        String output = "\n";
        card.loadSmugglers();
        Map<GoodBlock, Integer> actualGoodBlocks = card.getActualGoodBlocks();
        output = output.concat("Actual good blocks on the planet: \n");

        for(GoodBlock goodBlock : actualGoodBlocks.keySet()) {
            output = output.concat("Good: " + goodBlock + ", Available in the bank: " + actualGoodBlocks.get(goodBlock) + "\n");
        }

        return "Smugglers Card" + output;
    }

    /**
     * shows the information of the stardust card in game
     * @param card
     */
    public String showStardustCardInGame(StardustCard card) {
        return "Stardust Card\nOh no, the exposed connectors of the ship will slow you down";
    }

}
