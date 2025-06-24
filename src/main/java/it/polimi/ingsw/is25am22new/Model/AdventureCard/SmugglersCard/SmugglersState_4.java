package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.io.Serializable;

/**
 * Represents the fourth state in the SmugglersCard state machine. This state manages
 * advanced interactions with the smugglers' mechanics, allowing players to retrieve,
 * discard, or move good blocks across their shipboard's storage compartments. It also
 * governs the ending of the card's effect when players finalize their operations.
 *
 * This class extends the SmugglersState abstract class and implements additional
 * functionalities tailored to this specific phase in the smuggling operations, while handling
 * state transitions and game updates accordingly.
 */
public class SmugglersState_4 extends SmugglersState implements Serializable {

    /**
     * Initializes the state of the smuggler's card.
     * @param smugglersCard
     */
    public SmugglersState_4(SmugglersCard smugglersCard) {
        super(smugglersCard);
    }

    /**
     * Activates the effect of the smugglers card, allowing the current player to manage good blocks
     * or complete the card's effect based on the specified player input command.
     *
     * @param inputCommand the command containing all necessary details to process the activation,
     *                     including the choice to continue, type of operation (adding, removing,
     *                     or switching good blocks), and relevant coordinates and good block information.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        GoodBlock gb = inputCommand.getGoodBlock();
        if(inputCommand.getChoice()){   //choice must be set true if the player wants to keep managing his good blocks
            ComponentTile storageCompartment = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get();
            if(inputCommand.isAddingGoodBlock()){ //player is retrieving good blocks from the station
                if(smugglersCard.actualGoodBlocks.get(gb) == 0)
                    throw new IllegalArgumentException("There are no more blocks to retrieve from the smugglers");
                storageCompartment.addGoodBlock(gb);
                smugglersCard.actualGoodBlocks.put(gb, smugglersCard.actualGoodBlocks.get(gb) - 1); //remove the good block taken from the station (so I take it from the actualGoodblocks)
                smugglersCard.getObservableModel().updateAllBanks(game.getBank());
                smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
            else if(inputCommand.isRemovingGoodBlock()){ //player decides to discard good block from his shipboard
                game.getBank().depositGoodBlock(gb);
                storageCompartment.removeGoodBlock(gb);
                smugglersCard.getObservableModel().updateAllBanks(game.getBank());
                smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
            else if(inputCommand.isSwitchingGoodBlock()){ //player decides to switch a good block between two storage compartments
                GoodBlock gb_1 = inputCommand.getGoodBlock_1();
                ComponentTile storageCompartment_1 = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow_1(), inputCommand.getCol_1()).get();
                storageCompartment.removeGoodBlock(gb);
                if(gb_1 != null) {
                    storageCompartment_1.removeGoodBlock(gb_1);
                    storageCompartment.addGoodBlock(gb_1);
                }
                storageCompartment_1.addGoodBlock(gb);
                smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            }
        }
        else{    //if choice is false the card effect ends if the player is the last one
            smugglersCard.unloadSmugglers();
            smugglersCard.getObservableModel().updateAllBanks(game.getBank());
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null); //card effect has ended
            smugglersCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            smugglersCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            smugglersCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }   //as the card is implemented, even if there are no more moves available for the curr player, he still has to send the message with choice set on false to end the card effect or pass the turn to the next one

    }

    /**
     * Returns the name of the current state.
     * @return
     */
    @Override
    public String getStateName() {
        return "SmugglersState_4";
    }
}