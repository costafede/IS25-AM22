package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.io.Serializable;

/**
 * Represents a specific state of the PlanetsCard game mechanic, enabling various actions related
 * to managing good blocks and player turns during interactions with planets.
 * This state extends PlanetsState and implements Serializable for persistence.
 */
public class PlanetsState_2 extends PlanetsState implements Serializable {
    public PlanetsState_2(PlanetsCard planetsCard) {
        super(planetsCard);
    }

    /**
     * Activates the effect of the current PlanetsState_2 card. Handles various actions related to
     * managing good blocks, including adding, removing, or switching good blocks on the player's
     * shipboard, and manages turn-ending behavior when the choice indicates the player is finished.
     *
     * @param inputCommand the input command containing the player's choices, including whether the
     *                      player wants to add, remove, or switch good blocks, or end their turn.
     *                      The command also specifies the good blocks to be acted upon and which
     *                      storage compartments or grid positions are involved.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        GoodBlock gb = inputCommand.getGoodBlock();
        if(inputCommand.getChoice()){   //choice must be set true if the player wants to keep managing his good blocks
            ComponentTile storageCompartment = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get();
            if(inputCommand.isAddingGoodBlock()){ //player is retrieving good blocks from planet
                if(planetsCard.getPlanet(game.getCurrPlayer()).getActualGoodblocks().get(gb) == 0)
                    throw new IllegalArgumentException("There are no more blocks to pick on this planet");
                storageCompartment.addGoodBlock(gb);
                planetsCard.getPlanet(game.getCurrPlayer()).setActualGoodblocks(gb, planetsCard.getPlanet(game.getCurrPlayer()).getActualGoodblocks().get(gb) - 1); //remove the good block taken from the planet (so I take it from the actualGoodblocks in the Planet class)
                planetsCard.getObservableModel().updateAllBanks(game.getBank());
                planetsCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
            else if(inputCommand.isRemovingGoodBlock()){ //player decides to discard good block from his shipboard
                game.getBank().depositGoodBlock(gb);
                storageCompartment.removeGoodBlock(gb);
                planetsCard.getObservableModel().updateAllBanks(game.getBank());
                planetsCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
            else if(inputCommand.isSwitchingGoodBlock()){ //player decides to switch a good block between two storage compartments, if gb_1 is set null there is only one block to switch
                GoodBlock gb_1 = inputCommand.getGoodBlock_1();
                ComponentTile storageCompartment_1 = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow_1(), inputCommand.getCol_1()).get();
                storageCompartment.removeGoodBlock(gb);
                if(gb_1 != null) {
                    storageCompartment_1.removeGoodBlock(gb_1);
                    storageCompartment.addGoodBlock(gb_1);
                }
                storageCompartment_1.addGoodBlock(gb);
                planetsCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
        }
        else if(!game.getCurrPlayer().equals(planetsCard.getPlayersWhoLanded().getLast())) { //if choice is false the player decides to end its turn and pass it to the next one
            planetsCard.unloadPlanet(game.getCurrPlayer());
            game.setCurrPlayer(planetsCard.getPlayersWhoLanded().get(planetsCard.getPlayersWhoLanded().indexOf(game.getCurrPlayer()) + 1)); //* ATTENZIONE IL NEXT PLAYER DEVE ESSERE QUELLO CHE E' EFFETTIVAMENTE ATTERRATO*//
            planetsCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            planetsCard.loadPlanet(game.getCurrPlayer());
            planetsCard.getObservableModel().updateAllBanks(game.getBank());
        }
        else{    //if choice is false the card effect ends if the player is the last one
            planetsCard.unloadPlanet(game.getCurrPlayer());
            planetsCard.getObservableModel().updateAllBanks(game.getBank());
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null); //card effect has ended
            planetsCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            planetsCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            planetsCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }   //as the card is implemented, even if there are no more moves available for the curr player, he still has to send the message with choice set on false to end the card effect or pass the turn to the next one
    }
    @Override
    public String getStateName() {
        return "PlanetsState_2";
    }
}
