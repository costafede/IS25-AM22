package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * The LandOnPlanetCommand class represents the command for a player to land on a planet
 * during the "CARD" phase of the game. This command is applicable when the player has
 * a specific card in the "PlanetsState_1" state and it is their turn to act.
 *
 * This class extends the AbstractCommand class, inheriting the basic command structure,
 * such as input validation and interaction with the VirtualServer and ViewAdapter.
 */
public class LandOnPlanetCommand extends AbstractCommand {
    public LandOnPlanetCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "LandOnPlanet";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                model.getCurrCard().getStateName().equals("PlanetsState_1");
    }
    //input is the index of the planet
    public int getInputLength() {
        return 1;
    }

    @Override
    public boolean isInputValid(ClientModel model) {
        if(!super.isInputValid(model))
            return false;
        int index;
        try {
            index = Integer.parseInt(input.getFirst());
        }
        catch(NumberFormatException e) {
            return false;
        }
        if(index < 0 || index >= ((PlanetsCard) model.getCurrCard()).getPlanets().size() ||
            ((PlanetsCard) model.getCurrCard()).getPlanets().get(index).playerPresent()) {// if a player has already landed on a planet
            return false;
        }
        return true;
    }

    @Override
    public void execute(ClientModel model) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setIndexChosen(Integer.parseInt(input.getFirst()));
        activateCard(inputCommand);
    }
}
