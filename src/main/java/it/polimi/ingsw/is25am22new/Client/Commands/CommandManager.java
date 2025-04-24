package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.ShipBuildingPhaseCommands.*;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> allCommands;

    public CommandManager(List<Command> allCommands, VirtualServer virtualServer) {
        //Devo inizializzarlo con tutti i tipi di comando passati da costruttore  TO DO
        //Per ora metto solo quelli che ho scritto
        this.allCommands = new ArrayList<>();
    }

    public void initializeCommandManager(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        this.allCommands.add(new DiscardComponentTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new FinishBuildingCommand(virtualServer, viewAdapter));
        this.allCommands.add(new FlipHourglassCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PickCoveredTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PickStandByComponentTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new PickUncoveredTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new ShowPileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new StandByComponentTileCommand(virtualServer, viewAdapter));
        this.allCommands.add(new WeldComponentTileCommand(virtualServer, viewAdapter));
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
