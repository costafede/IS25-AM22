package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands.*;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CorrectingShipPhaseCommands.DestroyTileCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.EndPhaseCommands.QuitCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.EndPhaseCommands.ShowLeaderboardCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands.*;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.PlaceCrewMembersPhaseCommands.PlaceAstronautCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.PlaceCrewMembersPhaseCommands.PlaceBrownAlienCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.PlaceCrewMembersPhaseCommands.PlacePurpleAlienCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GodModeCommands.EnterGodModeCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands.*;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all available commands in the game and filters commands based on their applicability
 * to the current game state. The class provides functionality to initialize and maintain the list
 * of commands, as well as retrieve commands available for a specific game context.
 */
public class CommandManager {
    private final List<Command> allCommands;

    public CommandManager() {
        this.allCommands = new ArrayList<>();
    }

    public void initializeCommandManagerGUI(VirtualServer virtualServer) {
        this.allCommands.add(new AcceptCreditsCommand(virtualServer, null));
        this.allCommands.add(new ActivateDoubleCannonCommand(virtualServer, null));
        this.allCommands.add(new ActivateDoubleEngineCommand(virtualServer, null));
        this.allCommands.add(new ActivateShieldCommand(virtualServer, null));
        this.allCommands.add(new ChooseShipWreckCommand(virtualServer, null));
        this.allCommands.add(new DecideToRemoveCrewMembersCommand(virtualServer, null));
        this.allCommands.add(new GetBlockCommand(virtualServer, null));
        this.allCommands.add(new LandOnAbandonedStationCommand(virtualServer, null));
        this.allCommands.add(new LandOnPlanetCommand(virtualServer, null));
        this.allCommands.add(new MoveGoodBlockCommand(virtualServer, null));
        this.allCommands.add(new PickCardCommand(virtualServer, null));
        this.allCommands.add(new RemoveCrewMemberCommand(virtualServer, null));
        this.allCommands.add(new RemoveGoodBlockCommand(virtualServer, null));
        this.allCommands.add(new ResolveEffectCommand(virtualServer, null));
        this.allCommands.add(new SwitchGoodBlocksCommand(virtualServer, null));
    }

    public void initializeCommandManager(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        this.allCommands.add(new DiscardComponentTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new FinishBuildingCommand(virtualServer, viewAdapter));
        this.allCommands.add(new FlipHourglassCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PickCoveredTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PickStandByComponentTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PickUncoveredTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowPileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowRemainingSecondsCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowStandByComponentTilesCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowUncoveredComponentTiles(virtualServer, viewAdapter));
        this.allCommands.add(new StandByComponentTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new WeldComponentTileCommand(virtualServer, viewAdapter));

        this.allCommands.add(new ShowAvailableCommandsCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowBankCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowCurrCardCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowCurrPhaseCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowFlightboardCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowShipCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowAllShipsCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowTileInHandCommand(virtualServer, viewAdapter));
        this.allCommands.add(new AbandonGameCommand(virtualServer, viewAdapter));
        this.allCommands.add(new DisconnectCommand(virtualServer, viewAdapter));

        this.allCommands.add(new DestroyTileCommand(virtualServer, viewAdapter));

        this.allCommands.add(new QuitCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowLeaderboardCommand(virtualServer, viewAdapter));

        this.allCommands.add(new PlaceAstronautCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PlaceBrownAlienCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PlacePurpleAlienCommand(virtualServer, viewAdapter));

        this.allCommands.add(new AcceptCreditsCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ActivateDoubleCannonCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ActivateDoubleEngineCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ActivateShieldCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ChooseShipWreckCommand(virtualServer, viewAdapter));
        this.allCommands.add(new DecideToRemoveCrewMembersCommand(virtualServer, viewAdapter));
        this.allCommands.add(new GetBlockCommand(virtualServer, viewAdapter));
        this.allCommands.add(new LandOnAbandonedStationCommand(virtualServer, viewAdapter));
        this.allCommands.add(new LandOnPlanetCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ManageGoodBlocksCommand(virtualServer, viewAdapter));
        this.allCommands.add(new MoveGoodBlockCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PickCardCommand(virtualServer, viewAdapter));
        this.allCommands.add(new RemoveCrewMemberCommand(virtualServer, viewAdapter));
        this.allCommands.add(new RemoveGoodBlockCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ResolveEffectCommand(virtualServer, viewAdapter));
        this.allCommands.add(new SwitchGoodBlocksCommand(virtualServer, viewAdapter));

        this.allCommands.add(new EnterGodModeCommand(virtualServer, viewAdapter));
    }

    public List<Command> getAvailableCommandTypes(ClientModel model) {
        List<Command> availableCommands = new ArrayList<>();

        for (Command cmd : allCommands) {
            if (cmd.isApplicable(model)) {
                availableCommands.add(cmd);
            }
        }

        return availableCommands;
    }

    public List<Command> getAllCommandTypes() {
        return allCommands;
    }

}
