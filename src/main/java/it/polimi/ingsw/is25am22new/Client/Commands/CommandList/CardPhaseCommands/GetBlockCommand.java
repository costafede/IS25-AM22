package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.Commands.StringConverter;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard.SmugglersCard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The GetBlockCommand class represents a specific command in the game allowing
 * the current player to retrieve a GoodBlock from the active card and place it
 * onto a specified position in their shipboard. The command ensures all necessary
 * validations for the conditions under which the action can be performed.
 * This class extends AbstractCommand, inheriting shared functionalities for
 * command execution and server interaction.
 *
 * Responsibilities:
 * - Validates whether the command is applicable and the input provided is valid.
 * - Ensures that the current game phase, card state, and player conditions
 *   align with the rules for placing a GoodBlock.
 * - Executes the action of retrieving a GoodBlock from the active card and
 *   placing it into the player's shipboard storage compartment grid.
 */
public class GetBlockCommand extends AbstractCommand {
    public GetBlockCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "GetBlock";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("PlanetsState_2") ||
                model.getCurrCard().getStateName().equals("SmugglersState_4") ||
                model.getCurrCard().getStateName().equals("AbandonedStationState_2"));
    }

    public int getInputLength() {
        return 3;
    }
    //input: gb, row_1, col_1. I get the gb from the card and place it in (row, col)
    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        GoodBlock gb;
        int row, col;
        try {
            row = StringConverter.stringToGridRow(input.get(1));
            col = StringConverter.stringToGridCol(input.get(2));
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(!ConditionVerifier.stringIsGoodBlock(input.getFirst()))
            return false;
        gb = StringConverter.stringToGoodBlock(input.getFirst());
        if(model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row, col).isEmpty() ||
            !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row, col).get().isStorageCompartment() ||
            !model.getShipboard(model.getPlayerName()).getComponentTileFromGrid(row, col).get().isBlockPlaceable(gb)) {
            return false;
        }
        switch (model.getCurrCard().getName()){ //I check if the block the player asked for is actually available in the card
            case "AbandonedStation" :
                if(((AbandonedStationCard) model.getCurrCard()).getActualGoodBlocks().get(gb) <= 0)
                    return false;
                break;
            case "Smugglers" :
                if(((SmugglersCard) model.getCurrCard()).getActualGoodBlocks().get(gb) <= 0)
                    return false;
                break;
            case "Planets" :
                if(((PlanetsCard) model.getCurrCard()).getPlanet(model.getPlayerName()).getActualGoodblocks().get(gb) <= 0)
                    return false;
                break;
        }

        return true;
    }

    @Override
    public void execute(ClientModel model) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        GoodBlock gb = StringConverter.stringToGoodBlock(input.getFirst());
        int row = StringConverter.stringToGridRow(input.get(1));
        int col = StringConverter.stringToGridCol(input.get(2));
        inputCommand.setRow(row);
        inputCommand.setCol(col);
        inputCommand.setGoodBlock(gb);
        inputCommand.flagIsAddingGoodBlock();
        activateCard(inputCommand);
    }
}
