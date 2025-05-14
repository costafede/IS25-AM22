package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.io.Serializable;
import java.util.Optional;

public class SmugglersState_1 extends SmugglersState implements Serializable {
    public SmugglersState_1(SmugglersCard smugglersCard) {
        super(smugglersCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){   //curr player has decided to activate a double cannon so he entered the coordinates of the battery component where we wants to remove the battery token
            ComponentTile batteryComponent = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get();
            batteryComponent.removeBatteryToken();
            smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            transition(new SmugglersState_2(smugglersCard)); //go to state where player has to enter the input to activate cannon
        }
        else{ //curr player has decided not to activate any cannon or he didn't have any battery token left -> resolve the card effect
            double playerCannonStrength = game.getShipboards().get(game.getCurrPlayer()).getCannonStrength();
            game.getShipboards().get(game.getCurrPlayer()).deactivateAllComponent();
            smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            if(smugglersCard.getCannonStrength() < playerCannonStrength){   //player wins
                smugglersCard.loadSmugglers();
                smugglersCard.getObservableModel().updateAllBanks(game.getBank());
                transition(new SmugglersState_3(smugglersCard));    //player goes to the state in which he decides if he wants to accept the reward
            }
            else{   //player doesn't defeat the smugglers
                if(smugglersCard.getCannonStrength() > playerCannonStrength) { //player gets defeated
                    game.getShipboards().get(game.getCurrPlayer()).removeMostValuableGoodBlocks(smugglersCard.getLostGoods());
                    smugglersCard.getObservableModel().updateAllBanks(game.getBank());
                    smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
                }
                //if there is a draw nothing happens (I didn't put an if statement with an empty body)
                if(!game.getCurrPlayer().equals(game.getLastPlayer())) { //smuggler goes to next player
                    game.setCurrPlayerToNext();
                    smugglersCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                }
                else{
                    game.manageInvalidPlayers();
                    game.setCurrPlayerToLeader();   //smuggler card effect has ended because the enemy has defeated all players
                    game.setCurrCard(null);
                    smugglersCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    smugglersCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                    smugglersCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                }
            }
        }
    }

    @Override
    public String getStateName() {
        return "SmugglersState_1";
    }

}
