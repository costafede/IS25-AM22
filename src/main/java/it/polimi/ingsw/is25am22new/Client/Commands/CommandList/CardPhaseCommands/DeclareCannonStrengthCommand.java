//package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;
//
//import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
//import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
//import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
//import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
//import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
//import it.polimi.ingsw.is25am22new.Network.VirtualServer;
//
//public class DeclareCannonStrengthCommand extends AbstractCommand {
//    public DeclareCannonStrengthCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
//        super(virtualServer, viewAdapter);
//    }
//
//    @Override
//    public String getName() {
//        return "DeclareCannonStrength";
//    }
//
//    @Override
//    public boolean isApplicable(ClientModel model) {
//        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
//                model.getCurrCard() != null &&
//                model.getCurrPlayer().equals(model.getPlayerName()) &&
//                (model.getCurrCard().getStateName().equals("CombatZoneState_6") ||
//                model.getCurrCard().getStateName().equals("SmugglersState_1") ||
//                model.getCurrCard().getStateName().equals("MeteorSwarmState_1") ||
//                model.getCurrCard().getStateName().equals("SlaversState_1") ||
//                model.getCurrCard().getStateName().equals("PiratesState_1"));
//    }
//
//    @Override
//    public void execute(ClientModel model) {
//        InputCommand inputCommand = new InputCommand();
//        inputCommand.setChoice(false);
//        activateCard(inputCommand);
//    }
//}
