package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.CommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.ShipBuildingPhaseCommandTypes.*;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ShipBuildingPhaseCommands.WeldComponentTileCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<CommandType> allCommands;

    public CommandManager(List<CommandType> allCommands, VirtualServer virtualServer) {
        //Devo inizializzarlo con tutti i tipi di comando passati da costruttore  TO DO
        //Per ora metto solo quelli che ho scritto
        this.allCommands = new ArrayList<>();
        this.allCommands.add(new DiscardComponentTileCommandType(virtualServer));
        this.allCommands.add(new FinishBuildingCommandType(virtualServer));
        this.allCommands.add(new FlipHourglassCommandType(virtualServer));
        this.allCommands.add(new PickCoveredTileCommandType(virtualServer));
        this.allCommands.add(new PickStandByComponentTileCommandType(virtualServer));
        this.allCommands.add(new PickUncoveredTileCommandType(virtualServer));
        this.allCommands.add(new ShowPileCommandType(virtualServer));
        this.allCommands.add(new StandByComponentTileCommandType(virtualServer));
        this.allCommands.add(new WeldComponentTileCommandType(virtualServer));
    }

    public List<CommandType> getAvailableCommandTypes(ClientModel model) {
        List<CommandType> availableCommands = new ArrayList<>();

        for (CommandType cmd : allCommands) {
            if (cmd.isApplicable(model)) {
                availableCommands.add(cmd);
            }
        }

        return availableCommands;
    }

    public ParametrizedCommand createCommand(ClientModel clientModel, CommandType commandType, List<Integer> input, ViewAdapter viewAdapter) {
        return commandType.createWithInput(clientModel, input, viewAdapter);
    }

}
