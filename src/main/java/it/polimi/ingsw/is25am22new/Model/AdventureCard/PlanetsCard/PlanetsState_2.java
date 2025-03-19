package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class PlanetsState_2 extends PlanetsState {
    public PlanetsState_2(PlanetsCard planetsCard) {
        super(planetsCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){   //choice must be set true if the player wants to keep managing his goodblocks
            if(inputCommand.isAddingGoodBlock()){
                game.getBank().withdrawGoodBlock(inputCommand.getGoodBlock());
                game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).addGoodBlock(inputCommand.getGoodBlock());
            }
            else if(inputCommand.isRemovingGoodBlock()){
                game.getBank().depositGoodBlock(inputCommand.getGoodBlock());
                game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).removeGoodBlock(inputCommand.getGoodBlock());
            }
            else if(inputCommand.isSwitchingGoodBlock()){

            }
        }
        else{   //if choice is false the player decides to end its turn and pass it to the next one or end the card effect if the player is the last one

        }
    }

    @Override
    public void transition(PlanetsState planetsState) {
    }
}
