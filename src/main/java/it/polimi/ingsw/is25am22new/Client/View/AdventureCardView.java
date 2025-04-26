package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
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
        System.out.println("=== PIANETI ===");
        System.out.println("Giorni di volo persi: " + card.getFlightDaysLost());
        for (int i = 0; i < card.getPlanets().size(); i++) {
            System.out.println("Pianeta" + i + ":");
            Map<GoodBlock, Integer> theoreticalGoodblocks = card.getPlanets().get(i).getTheoreticalGoodblocks();
            theoreticalGoodblocks.forEach((goodBlock, quantity) ->
                    System.out.println("Merce: " + goodBlock + ", Quantita': " + quantity)
            );
        }
    }

    public static void showAbandonedShipCard(AbandonedShipCard card) {
        System.out.println("=== NAVE ABBANDONATA ===");
        System.out.println("Giorni di volo persi: " + card.getFlightDaysLost());
        System.out.println("Crediti: " + card.getCredits());
        System.out.println("Astronauti persi: " + card.getLostAstronauts());
    }

    public static void showAbandonedStationCard(AbandonedStationCard card) {
        System.out.println("=== STAZIONE ABBANDONATA ===");
        System.out.println("Giorni di volo persi: " + card.getFlightDaysLost());
        System.out.println("Numero di astronauti: " + card.getAstronautsNumber());
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Merce: " + goodBlock + ", Quantita': " + quantity)
        );
    }

    public static void showCombatZoneCard(CombatZoneCard card) {
        System.out.println("=== ZONA DI GUERRA ===");
        System.out.println("Giorni di volo persi: " + card.getFlightDaysLost());
        System.out.println("Numero di astronauti: " + card.getAstronautsToLose());
        System.out.println("Merci perse: " + card.getLostGoods());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    public static void showEpidemicCard(EpidemicCard card) {
        System.out.println("=== EPIDEMIA ===");
        //Self-explanatory
    }

    public static void showMeteorSwarmCard(MeteorSwarmCard card) {
        System.out.println("=== PIOGGIA DI METEORITI ===");
        Map<Integer, Meteor> numberToMeteor = card.getNumberToMeteor();
        for (Map.Entry<Integer, Meteor> entry : numberToMeteor.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Meteor: " + entry.getValue());
        }
    }

    public static void showOpenSpaceCard(OpenSpaceCard card) {
        System.out.println("=== SPAZIO APERTO ===");
        //Self-explanatory
    }

    public static void showPiratesCard(PiratesCard card) {
        System.out.println("=== PIRATI ===");
        System.out.println("Giorni di volo persi: " + card.getFlightDaysLost());
        System.out.println("Crediti: " + card.getCredits());
        System.out.println("Potenza cannoni: " + card.getCannonStrength());
        Map<Integer, Shot> numberToShot = card.getNumberToShot();
        for (Map.Entry<Integer, Shot> entry : numberToShot.entrySet()) {
            System.out.println("Number: " + entry.getKey() + ", Shot: " + entry.getValue());
        }
    }

    public static void showSlaversCard(SlaversCard card) {
        System.out.println("=== SCHIAVISTI ===");
        System.out.println("Giorni di volo persi: " + card.getFlightDaysLost());
        System.out.println("Crediti: " + card.getCredits());
        System.out.println("Astronauti persi: " + card.getAstronautsToLose());
        System.out.println("Potenza cannoni: " + card.getCannonStrength());
    }

    public static void showSmugglersCard(SmugglersCard card) {
        System.out.println("=== CONTRABBANDIERI ===");
        System.out.println("Giorni di volo persi: " + card.getFlightDaysLost());
        System.out.println("Merci perse: " + card.getLostGoods());
        System.out.println("Potenza cannoni: " + card.getCannonStrength());
        Map<GoodBlock, Integer> theoreticalGoodBlocks = card.getTheoreticalGoodBlocks();
        theoreticalGoodBlocks.forEach((goodBlock, quantity) ->
                System.out.println("Merce: " + goodBlock + ", Quantita': " + quantity)
        );
    }

    public static void showStardustCard(StardustCard card) {
        System.out.println("=== POLVERE STELLARE ===");
        //Self-explanatory
    }

}
