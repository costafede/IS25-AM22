package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ResolveEffectCommand extends AbstractCommand{
    public ResolveEffectCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ResolveEffect";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrCard() != null &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                (model.getCurrCard().getStateName().equals("CombatZoneState_6") ||
                model.getCurrCard().getStateName().equals("SmugglersState_1") ||
                model.getCurrCard().getStateName().equals("SmugglersState_3") ||
                model.getCurrCard().getStateName().equals("SmugglersState_4") ||
                model.getCurrCard().getStateName().equals("MeteorSwarmState_1") ||
                model.getCurrCard().getStateName().equals("SlaversState_1") ||
                model.getCurrCard().getStateName().equals("PiratesState_1") ||
                model.getCurrCard().getStateName().equals("OpenSpaceState_1") ||
                model.getCurrCard().getStateName().equals("CombatZoneState2_6") ||
                model.getCurrCard().getStateName().equals("CombatZoneState2_1") ||
                model.getCurrCard().getStateName().equals("CombatZoneState2_3") ||
                model.getCurrCard().getStateName().equals("CombatZoneState2_7") ||
                model.getCurrCard().getStateName().equals("CombatZoneState_9") ||
                model.getCurrCard().getStateName().equals("CombatZoneState_6") ||
                model.getCurrCard().getStateName().equals("CombatZoneState_1") ||
                model.getCurrCard().getStateName().equals("CombatZoneState_0") ||
                model.getCurrCard().getStateName().equals("SlaversState_3") ||
                model.getCurrCard().getStateName().equals("PiratesState_3") ||
                model.getCurrCard().getStateName().equals("PiratesState_4") ||
                model.getCurrCard().getStateName().equals("PlanetsState_1") ||
                model.getCurrCard().getStateName().equals("PlanetsState_2") ||
                model.getCurrCard().getStateName().equals("AbandonedShipState_1") ||
                model.getCurrCard().getStateName().equals("AbandonedStationState_1") ||
                model.getCurrCard().getStateName().equals("AbandonedStationState_2") ||
                model.getCurrCard().getStateName().equals("OpenSpaceState_1") ||
                model.getCurrCard().getName().equals("Stardust") ||
                model.getCurrCard().getName().equals("Epidemic"));
    }

    @Override
    public void execute(ClientModel model) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(false);
        activateCard(inputCommand);
    }
}
