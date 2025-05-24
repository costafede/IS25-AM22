package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.io.Serializable;

/**
 * Represents the second state of an Abandoned Station card in the game.
 * This state handles the management of good blocks between the station and player's shipboard.
 * Implements Serializable to support game state persistence.
 */
public class AbandonedStationState_2 extends AbandonedStationState implements Serializable {

    /**
     * Constructs a new AbandonedStationState_2 instance.
     *
     * @param abandonedStationCard The abandoned station card this state belongs to
     */
    public AbandonedStationState_2(AbandonedStationCard abandonedStationCard){
        super(abandonedStationCard);
    }

    /**
     * Activates the effect of this state based on the provided input command.
     * Handles various operations with good blocks including:
     * - Adding good blocks from the station to player's storage
     * - Removing good blocks from player's storage to bank
     * - Switching good blocks between different storage compartments
     * - Ending the card effect when player chooses to
     *
     * @param inputCommand Contains all necessary parameters for the action (good blocks, coordinates, player choices)
     * @throws IllegalArgumentException if attempting to retrieve a good block type that is no longer available at the station
     */
    public void activateEffect(InputCommand inputCommand) {
        GoodBlock gb = inputCommand.getGoodBlock();
        if(inputCommand.getChoice()){   //choice must be set true if the player wants to keep managing his good blocks
            ComponentTile storageCompartment = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get();
            if(inputCommand.isAddingGoodBlock()){ //player is retrieving good blocks from the station
                storageCompartment.addGoodBlock(gb);
                if(abandonedStationCard.getActualGoodBlocks().get(gb) == 0)
                    throw new IllegalArgumentException("There is no such block to pick on the Station");
                abandonedStationCard.getActualGoodBlocks().put(gb, abandonedStationCard.getActualGoodBlocks().get(gb) - 1); //remove the good block taken from the station (so I take it from the actualGoodblocks)
                abandonedStationCard.getObservableModel().updateAllBanks(game.getBank());
                abandonedStationCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
            else if(inputCommand.isRemovingGoodBlock()){ //player decides to discard good block from his shipboard
                game.getBank().depositGoodBlock(gb);
                storageCompartment.removeGoodBlock(gb);
                abandonedStationCard.getObservableModel().updateAllBanks(game.getBank());
                abandonedStationCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
            else if(inputCommand.isSwitchingGoodBlock()){ //player decides to switch a good block between two storage compartments
                GoodBlock gb_1 = inputCommand.getGoodBlock_1();//row_1,row_2 are the coordinates of the destination of gb
                ComponentTile storageCompartment_1 = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow_1(), inputCommand.getCol_1()).get();
                storageCompartment.removeGoodBlock(gb);
                if(gb_1 != null) {
                    storageCompartment_1.removeGoodBlock(gb_1);
                    storageCompartment.addGoodBlock(gb_1);
                }
                storageCompartment_1.addGoodBlock(gb);
                abandonedStationCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
        }
        else{    //if choice is false the card effect ends if the player is the last one
            abandonedStationCard.unloadStation();
            abandonedStationCard.getObservableModel().updateAllBanks(game.getBank());
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null); //card effect has ended
            abandonedStationCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            abandonedStationCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            abandonedStationCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }   //as the card is implemented, even if there are no more moves available for the curr player, he still has to send the message with choice set on false to end the card effect or pass the turn to the next one
    }

    /**
     * Returns the name of this state.
     *
     * @return The string "AbandonedStationState_2"
     */
    @Override
    public String getStateName() {
        return "AbandonedStationState_2";
    }
}