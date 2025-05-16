//package it.polimi.ingsw.is25am22new.Client.View;
//
//import it.polimi.ingsw.is25am22new.Client.Commands.Command;
//import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
//import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
//import it.polimi.ingsw.is25am22new.Network.RMI.Client.RmiClient;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GUI extends Application implements ViewAdapter, ClientModelObserver{
//
//    private static ClientModel clientModel;
//    private static CommandManager commandManager;
//    private static Stage primaryStage;
//    private static RmiClient rmiClient;
//    private static boolean isInitialized = false;
//    private List<Command> allCommands;
//    private static GameViewController gameViewController;
//
//    public GUI(){}
//
//    public GUI(ClientModel clientModel, CommandManager commandManager) {
//        GUI.commandManager = commandManager;
//        GUI.clientModel = clientModel;
//        this.allCommands = commandManager.getAllCommandTypes();
//        clientModel.addListener(this);
//
//        if(!isInitialized){
//            new Thread(() -> Application.launch(GUI.class)).start();
//            isInitialized = true;
//        }
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        try {
//            primaryStage = stage;
//            stage.setTitle("Galaxy Trucker");
//
//            // Load the initial scene (should be ConnectToServer.fxml)
//            Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml"));
//            Scene scene = new Scene(root);
//            scene.getStylesheets().add(getClass().getResource("/it/polimi/ingsw/is25am22new/styles.css").toExternalForm());
//
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            System.err.println("Failed to load initial scene: " + e.getMessage());
//        }
//    }
//
//    public void setRmiClient(RmiClient rmiClient){
//        GUI.rmiClient = rmiClient;
//    }
//
//    @Override
//    public synchronized void modelChanged(ClientModel model) {
//        Platform.runLater(() -> {
//            GUI.clientModel = model;
//
//            if (model.isGameStartMessageReceived() && gameViewController != null) {
//                updateGameUI();
//            }
//        })
//    }
//
//    @Override
//    public void showCardPile(int idx, ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showShipboardGrid(String player, ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showShipboardStandByComponents(String player, ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showFlightboard(ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showCard(AdventureCard card, ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showUncoveredComponentTiles(ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showLeaderboard(ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showAvailableCommands(ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showTileInHand(String player, ClientModel clientModel) {
//
//    }
//
//    @Override
//    public void showRemainingSeconds(ClientModel model) {
//
//    }
//
//    @Override
//    public void showCurrPhase(ClientModel model) {
//
//    }
//
//    @Override
//    public void showBank(ClientModel model) {
//
//    }
//
//    @Override
//    public void quit() {
//
//    }
//
//    @Override
//    public void abandonGame(String player) {
//
//    }
//
//}
