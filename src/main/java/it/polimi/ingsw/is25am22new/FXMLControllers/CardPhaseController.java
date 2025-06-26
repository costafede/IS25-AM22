package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands.*;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands.AbandonGameCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.GeneralCommands.DisconnectCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.GUI.AdventureCardViewGUI;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyStarsEffect;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2.CombatZoneCard2;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard.OpenSpaceCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard.PiratesCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard.SmugglersCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard.StardustCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

/**
 * Controls the logic and rendering operations during a specific card-based phase
 * of the Galaxy Trucker application. Extending the {@link FXMLController} class,
 * this controller integrates with the GUI framework and manages functionalities
 * related to visual and logical game state.
 */
public class CardPhaseController extends FXMLController {
    @FXML private TextArea infoArea;
    @FXML private Label CurrPlayerLabel;
    @FXML private GridPane myShip;
    @FXML private GridPane player1Ship;
    @FXML private GridPane player2Ship;
    @FXML private GridPane player3Ship;
    @FXML private ImageView myShipImage;
    @FXML private ImageView player1ShipImage;
    @FXML private ImageView player2ShipImage;
    @FXML private ImageView player3ShipImage;
    @FXML private Label player1Name;
    @FXML private Label player2Name;
    @FXML private Label player3Name;
    @FXML private AnchorPane tutorialFlightboardPane; // Layout x = 375 Layout y = 15 positions fx:id t[num]
    @FXML private AnchorPane level2FlightboardPane; // positions fx:id l[num]
    @FXML private ImageView background;
    @FXML private ImageView cardImage;
    @FXML private Button pickCardButton;
    @FXML private ImageView dice1;
    @FXML private ImageView dice2;
    @FXML private Button abandonGameButton;

    private GalaxyStarsEffect animatedBackground;
    private Map<String, GridPane> playerToShip;
    private List<ImageView> playersImage;
    private final Map<String, Image> colorToRocketImage = new HashMap<>();
    private final Map<Integer, Image> diceToImage = new HashMap<>();
    private final Map<String, EventHandler<ActionEvent>> buttonToMethod = new HashMap<>();
    private final Map<Integer, String> numToGoodBlock = new HashMap<>();

    private final Map<String, Supplier<String>> cardNameToInfoText = new HashMap<>();

    private int batteryXcoord = -1;
    private int batteryYcoord = -1;
    private int doubleCannonXcoord = -1;
    private int doubleCannonYcoord = -1;
    private int doubleEngineXcoord = -1;
    private int doubleEngineYcoord = -1;
    private int shieldXCoord = -1;
    private int shieldYCoord = -1;
    private int gbFromXCoord = -1;
    private int gbFromYCoord = -1;
    private int gbToXCoord = -1;
    private int gbToYCoord = -1;
    private String selectedGoodBlock1 = null;
    private String selectedGoodBlock2 = null;

    private CommandManager commandManager;

    @FXML
    private void initialize() {
        commandManager = new CommandManager();
        commandManager.initializeCommandManagerGUI(GalaxyTruckerGUI.getVirtualServer());
        setup(null, GalaxyTruckerGUI.getClientModel(), GalaxyTruckerGUI.getPrimaryStage(), GalaxyTruckerGUI.getVirtualServer());
        initializePlayerToShip();
        initializeButtonToMethod();
        initializeNumToGoodBlock();
        initializeCardNameToInfoText();
        playersImage = List.of(myShipImage, player1ShipImage, player2ShipImage, player3ShipImage);
        if (model.getGametype() == GameType.TUTORIAL) {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BlueBackground.png")).toString()));
            setShipboardImagesTutorial(playerToShip.size());
            tutorialFlightboardPane.setVisible(true);
            level2FlightboardPane.setVisible(false);
            // Set default card image for tutorial mode
            cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cards/GT-cards_I_IT_0121.jpg")).toString()));
        } else {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/PurpleBackground.png")).toString()));
            setShipboardImagesLevel2(playerToShip.size());
            tutorialFlightboardPane.setVisible(false);
            level2FlightboardPane.setVisible(true);
            // Set default card image for level 2 mode
            cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cards/RetroCartaDefault.png")).toString()));
        }

        fillGridPane(myShip, 63);
        fillGridPane(player1Ship, 38);
        fillGridPane(player2Ship, 38);
        fillGridPane(player3Ship, 38);
        initializeRocketColorMap();
        initializeDiceToImage();


        cardImage.setOnMouseClicked(this::showApplicableCommands);

        cardImage.setStyle("-fx-cursor: hand;");

        animatedBackground = new GalaxyStarsEffect(1280, 720);

        if (background.getParent() instanceof Pane pane) {
            animatedBackground.setWidth(background.getFitWidth());
            animatedBackground.setHeight(background.getFitHeight());

            pane.getChildren().addFirst(animatedBackground);

            animatedBackground.toBack();
            background.toBack();
        }
        drawScene();
        if(model.isGameLoaded())
            drawCard();

        updatePickCardButton();
        CurrPlayerLabel.setText("It's the turn of " + model.getCurrPlayer());
    }

    private void initializeCardNameToInfoText() {
        cardNameToInfoText.put("MeteorSwarm", () -> new AdventureCardViewGUI().showMeteorSwarmCardInGame((MeteorSwarmCard) model.getCurrCard()));
        cardNameToInfoText.put("Planets", () -> new AdventureCardViewGUI().showPlanetsCard((PlanetsCard) model.getCurrCard()));
        cardNameToInfoText.put("Slavers", () -> new AdventureCardViewGUI().showSlaversCardInGame((SlaversCard) model.getCurrCard()));
        cardNameToInfoText.put("Smugglers", () -> new AdventureCardViewGUI().showSmugglersCardInGame((SmugglersCard) model.getCurrCard()));
        cardNameToInfoText.put("Pirates", () -> new AdventureCardViewGUI().showPiratesCardInGame((PiratesCard) model.getCurrCard()));
        cardNameToInfoText.put("OpenSpace", () -> new AdventureCardViewGUI().showOpenSpaceCardInGame((OpenSpaceCard) model.getCurrCard()));
        cardNameToInfoText.put("Epidemic", () -> new AdventureCardViewGUI().showEpidemicCardInGame((EpidemicCard) model.getCurrCard()));
        cardNameToInfoText.put("AbandonedShip", () -> new AdventureCardViewGUI().showAbandonedShipCard((AbandonedShipCard) model.getCurrCard()));
        cardNameToInfoText.put("AbandonedStation", () -> new AdventureCardViewGUI().showAbandonedStationCard((AbandonedStationCard) model.getCurrCard()));
        cardNameToInfoText.put("Stardust", () -> new AdventureCardViewGUI().showStardustCardInGame((StardustCard) model.getCurrCard()));
        cardNameToInfoText.put("CombatZone", () -> {
            if(model.getCurrCard().getLevel() == 1) {
                return new AdventureCardViewGUI().showCombatZoneCard((CombatZoneCard) model.getCurrCard());
            } else {
                return new AdventureCardViewGUI().showCombatZoneCard2InGame((CombatZoneCard2) model.getCurrCard());
            }
        });
    }

    private void initializeNumToGoodBlock() {
        numToGoodBlock.put(0, "redblock");
        numToGoodBlock.put(1, "yellowblock");
        numToGoodBlock.put(2, "greenblock");
        numToGoodBlock.put(3, "blueblock");
    }

    private void initializePlayerToShip() {
        playerToShip = new HashMap<>();
        List<GridPane> shipViews = new ArrayList<>();
        shipViews.addFirst(player3Ship);
        shipViews.addFirst(player2Ship);
        shipViews.addFirst(player1Ship);
        for (int i = 0; i < model.getShipboards().size(); i++) {
            if (model.getShipboards().keySet().stream().toList().get(i).equals(model.getPlayerName())) {
                playerToShip.put(model.getPlayerName(), myShip);
            } else {
                playerToShip.put(model.getShipboards().keySet().stream().toList().get(i), shipViews.getFirst());
                shipViews.removeFirst();
            }
        }
    }

    private void setShipboardImagesLevel2(int playersNumber) {
        setShipboardsVisibility(playersNumber);

        int count = 0;
        for (int i = 0; i < playersNumber && i < model.getShipboards().size(); i++) {
            ImageView shipImage = playersImage.get(i);
            if (shipImage.isVisible()) {
                shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cardboard/cardboard-1b.jpg")).toString()));
                count++;
            }
        }
        System.out.println("Impostato " + count + " navi per la modalità Level2");
    }

    private void setShipboardImagesTutorial(int playersNumber) {
        setShipboardsVisibility(playersNumber);

        int count = 0;
        for (int i = 0; i < playersNumber && i < model.getShipboards().size(); i++) {
            ImageView shipImage = playersImage.get(i);
            if (shipImage.isVisible()) {
                shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cardboard/cardboard-1.jpg")).toString()));
                count++;
            }
        }
        System.out.println("Impostato " + count + " navi per la modalità Tutorial");
    }

    /**
     * Sets the visibility of player shipboards based on number of players
     *
     * @param numberOfPlayers the number of players in the game
     */
    private void setShipboardsVisibility(int numberOfPlayers) {
        // First hide all player boards
        player1ShipImage.setVisible(false);
        player2ShipImage.setVisible(false);
        player3ShipImage.setVisible(false);
        player1Name.setVisible(false);
        player2Name.setVisible(false);
        player3Name.setVisible(false);
        player1Ship.setVisible(false);
        player2Ship.setVisible(false);
        player3Ship.setVisible(false);

        switch (numberOfPlayers) {
            case 4:
                player3ShipImage.setVisible(true);
                player3Name.setVisible(true);
                player3Ship.setVisible(true);
                // Fall through to show other boards
            case 3:
                player2ShipImage.setVisible(true);
                player2Name.setVisible(true);
                player2Ship.setVisible(true);
                // Fall through to show other boards
            case 2:
                player1ShipImage.setVisible(true);
                player1Name.setVisible(true);
                player1Ship.setVisible(true);
                break;
            default:
                break;
        }

        updatePlayerNames();
    }

    /**
     * Updates the player name labels based on the current players in the game
     */
    private void updatePlayerNames() {
        List<String> otherPlayers = playerToShip.keySet().stream()
                .filter(name -> !name.equals(model.getPlayerName()))
                .toList();

        if (!otherPlayers.isEmpty()) {
            player1Name.setText(otherPlayers.getFirst());
        }

        if (otherPlayers.size() >= 2) {
            player2Name.setText(otherPlayers.get(1));
        }

        if (otherPlayers.size() >= 3) {
            player3Name.setText(otherPlayers.get(2));
        }
    }

    /**
     * Draws the player's ship
     */
    public void drawShips() {
        for (Shipboard s : model.getShipboards().values()) {
            drawShipInCardPhase(s);
        }
        // Reset visual effects for all tiles that are no longer active
        resetAllTileEffects();
        if(model.getCurrCard() != null)
            updateInfoArea();
    }

    /**
     * Resets visual effects and borders for all tiles that are no longer active
     */
    private void resetAllTileEffects() {
        resetTileEffectsForShip(model.getPlayerName(), myShip);

        for (Map.Entry<String, GridPane> entry : playerToShip.entrySet()) {
            if (!entry.getKey().equals(model.getPlayerName())) {
                resetTileEffectsForShip(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Resets visual effects for a specific player's ship
     *
     * @param playerName the name of the player whose ship to reset
     * @param shipGridPane the GridPane containing the ship's tiles
     */
    private void resetTileEffectsForShip(String playerName, GridPane shipGridPane) {
        Shipboard shipboard = model.getShipboard(playerName);
        if (shipboard == null) return;

        for (Node child : shipGridPane.getChildren()) {
            if (!(child instanceof ImageView)) continue;

            int i = GridPane.getRowIndex(child) != null ? GridPane.getRowIndex(child) : 0;
            int j = GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0;

            Optional<ComponentTile> tileOpt = shipboard.getComponentTileFromGrid(i, j);
            if (tileOpt.isEmpty()) continue;

            ComponentTile tile = tileOpt.get();

            // Check if tile should have effects or not
            boolean shouldHaveEffect = false;

            if (tile.isDoubleCannon() && tile.getCannonStrength() > 0) {
                shouldHaveEffect = true;
            } else if (tile.isDoubleEngine() && tile.getEngineStrength() > 0) {
                shouldHaveEffect = true;
            } else if (tile.isShieldGenerator() && tile.isActivated()) {
                shouldHaveEffect = true;
            }

            // If tile shouldn't have an effect but has one, remove it
            if (!shouldHaveEffect) {
                child.setEffect(null);
                child.setStyle("");
            }
        }
    }

    /**
     * Aggiorna tutti gli elementi della scena in base allo stato attuale del gioco
     */
    public void drawScene() {
        try {
            drawShips();
            drawFlightboardInCardPhase(model.getFlightboard());
            drawDices();
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento della scena: " + e.getMessage());
        }
    }

    public void updateCurrPlayerInfo(){
        updatePickCardButton();
        updateCurrPlayerLabel();
        if(model.getCurrCard() != null)
            updateInfoArea();
    }

    @FXML
    public void showInfo(){
        infoArea.setText(getStats());
    }

    private String getStats() {
        return ("=== " + model.getPlayerName() + "'s SHIP ===\n" +
            "Days on flight: " + model.getShipboard(model.getPlayerName()).getDaysOnFlight() + "\n" +
            "Credits: " + model.getShipboard(model.getPlayerName()).getCosmicCredits() + "\n" +
            "Flight crew: " + model.getShipboard(model.getPlayerName()).getCrewNumber() + "\n" +
            "Astronauts: " + model.getShipboard(model.getPlayerName()).getOnlyHumanNumber() + "\n" +
            "Cannon strength: " + model.getShipboard(model.getPlayerName()).getCannonStrength() + "\n" +
            "Engine strength: " + model.getShipboard(model.getPlayerName()).getEngineStrength() + "\n" +
            "Discarded tiles: " + model.getShipboard(model.getPlayerName()).getDiscardedTiles());
    }

    public void updateInfoArea() {
        if (model.getCurrCard() != null) {
            infoArea.setText(cardNameToInfoText.get(model.getCurrCard().getName()).get());
        } else {
            infoArea.setText("");
        }
    }

    /**
     * Aggiorna lo stato dei pulsanti in base al turno corrente
     * Disabilita i pulsanti quando non è il turno del giocatore
     */
    public void updatePickCardButton() {
        // Il pulsante pickCard è attivo solo se è il turno del giocatore e non è presente una carta
        // e lo metto disabilitato e grigio
        pickCardButton.setDisable(!model.getPlayerName().equals(model.getCurrPlayer()) || model.getCurrCard() != null);
    }

    public void updateCurrPlayerLabel(){
        CurrPlayerLabel.setText("It's the turn of " + model.getCurrPlayer());
    }

    /**
     * Aggiorna le informazioni dei giocatori nella UI
     */
    private void updatePlayerInfo() {
        try {
            System.out.println("Aggiornamento delle informazioni per il giocatore: " + model.getPlayerName());
            // Implementazione per aggiornare le informazioni dei giocatori
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento delle informazioni del giocatore: " + e.getMessage());
        }
    }

    /**
     * Disegna la carta corrente nell'ImageView designato
     * Viene chiamato dopo aver pescato una carta per aggiornare l'interfaccia
     */
    public void drawCard() {
        try {
            if (model.getCurrCard() != null) {
                // Ottieni il nome del file dell'immagine dalla carta
                String pngName = model.getCurrCard().getPngName();
                // Costruisci il percorso dell'immagine
                String imagePath = "/GraficheGioco/cards/" + pngName;/* + ".jpg";*/

                // Carica e visualizza l'immagine della carta
                Image cardImg = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toString());
                cardImage.setImage(cardImg);

                System.out.println("Visualizzazione carta: " + model.getCurrCard().getName());
            } else {
                // Se non c'è una carta corrente, visualizza un'immagine predefinita
                System.out.println("Nessuna carta corrente da visualizzare");
                if (model.getFlightboard().getFlightBoardLength() == 24)
                    cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cards/RetroCartaDefault.png")).toString()));
                else
                    cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cards/GT-cards_I_IT_0121.jpg")).toString()));
            }
            updatePickCardButton();
            updateInfoArea();
        } catch (Exception e) {
            System.err.println("Errore durante la visualizzazione della carta: " + e.getMessage());
        }
    }

    /**
     * Gestisce l'evento quando il giocatore apre la banca dei goodblocks
     */
    @FXML
    public void openGoodBlocksBank(ActionEvent event) {
        try {
            System.out.println("Apertura della banca dei goodblocks");

            Bank bank = model.getBank();
            if (bank == null) {
                System.err.println("La banca non è disponibile");
                return;
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Banca dei Goodblocks");
            alert.setHeaderText("Disponibilità dei blocchi nella banca");

            GridPane gridPane = new GridPane();
            gridPane.setHgap(15);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 20, 20, 20));

            Label typeLabel = new Label("Tipo");
            Label countLabel = new Label("Quantità");
            typeLabel.setStyle("-fx-font-weight: bold;");
            countLabel.setStyle("-fx-font-weight: bold;");
            gridPane.add(typeLabel, 0, 0);
            gridPane.add(countLabel, 2, 0);

            String redStyle = "-fx-background-color: #FF5555; -fx-padding: 5px 10px; -fx-border-radius: 3;";
            String yellowStyle = "-fx-background-color: #FFFF55; -fx-padding: 5px 10px; -fx-border-radius: 3;";
            String greenStyle = "-fx-background-color: #55FF55; -fx-padding: 5px 10px; -fx-border-radius: 3;";
            String blueStyle = "-fx-background-color: #5555FF; -fx-padding: 5px 10px; -fx-border-radius: 3; -fx-text-fill: white;";

            Label redBlock = new Label("Rossi");
            redBlock.setStyle(redStyle);
            Label redCount = new Label(String.valueOf(bank.getNumGoodBlock(GoodBlock.REDBLOCK)));
            gridPane.add(redBlock, 0, 1);
            gridPane.add(redCount, 2, 1);

            Label yellowBlock = new Label("Gialli");
            yellowBlock.setStyle(yellowStyle);
            Label yellowCount = new Label(String.valueOf(bank.getNumGoodBlock(GoodBlock.YELLOWBLOCK)));
            gridPane.add(yellowBlock, 0, 2);
            gridPane.add(yellowCount, 2, 2);

            Label greenBlock = new Label("Verdi");
            greenBlock.setStyle(greenStyle);
            Label greenCount = new Label(String.valueOf(bank.getNumGoodBlock(GoodBlock.GREENBLOCK)));
            gridPane.add(greenBlock, 0, 3);
            gridPane.add(greenCount, 2, 3);

            Label blueBlock = new Label("Blu");
            blueBlock.setStyle(blueStyle);
            Label blueCount = new Label(String.valueOf(bank.getNumGoodBlock(GoodBlock.BLUEBLOCK)));
            gridPane.add(blueBlock, 0, 4);
            gridPane.add(blueCount, 2, 4);

            alert.getDialogPane().setContent(gridPane);

            // Mostra l'alert
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Errore durante l'apertura della banca dei goodblocks: " + e.getMessage());
            showErrorAlert("Errore", "Impossibile aprire la banca dei goodblocks.");
        }
    }

    /**
     * Gestisce l'evento quando il giocatore lancia i dadi
     */
    @FXML
    public void pickCard(ActionEvent event) {
        System.out.println("Tentativo di pescare una carta");

        // Verifica che sia il turno del giocatore
        if (!model.getPlayerName().equals(model.getCurrPlayer())) {
            System.out.println("Non è il tuo turno!");
            return;
        }

        try {
            virtualServer.pickCard();
            System.out.println("Richiesta di pesca carta inviata al server per il giocatore: " + model.getPlayerName());

            //Dopo aver pescato la carta, il giocatore non può fare la pickCard di nuovo. Ritornerà disponibile solo quando la currCard sarà nuovamente null
            pickCardButton.setDisable(true);

            // Il server dovrebbe rispondere chiamando updateCurrCard in GalaxyTruckerGUI
            // che a sua volta chiamerà drawCard
        } catch (IOException e) {
            System.err.println("Errore durante la comunicazione con il server: " + e.getMessage());
        }
    }

    /**
     * Gestisce l'abbandono della partita
     *
     * @param event l'evento di click sul bottone abandonGameButton
     */
    @FXML
    public void abandonGame(ActionEvent event) {
        System.out.println("Tentativo di abbandonare la partita");

        // Chiedi conferma all'utente
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Abbandonare la partita?");
        confirmDialog.setHeaderText("Sei sicuro di voler abbandonare la partita?");
        confirmDialog.setContentText("Questa azione non può essere annullata.");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                Command cmd = new AbandonGameCommand(virtualServer, null);
                if(cmd.isApplicable(model)) {
                    new Thread(() -> {
                        cmd.execute(model);
                    }).start();
                } else {
                    showErrorAlert("WARNING", "You can't abandon the game right now!");
                }
                System.out.println("Richiesta di abbandono partita inviata al server");
            }
        });
    }

    /**
     * Gestisce la disconnessione dal server
     *
     * @param event l'evento di click sul bottone disconnectButton
     */
    @FXML
    public void disconnect(ActionEvent event) {
        System.out.println("Tentativo di disconnessione dal server");

        // Chiedi conferma all'utente
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Disconnettersi dal server?");
        confirmDialog.setHeaderText("Sei sicuro di volerti disconnettere dal server?");
        confirmDialog.setContentText("Questa azione causerà l'abbandono della partita corrente.");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    Command cmd = new DisconnectCommand(virtualServer, null);
                    if(cmd.isApplicable(model)) {
                        new Thread(() -> {
                            try {
                                cmd.execute(model);
                            } catch (Exception e){
                                ///  TODO
                            }
                        }).start();
                    } else {
                        showErrorAlert("WARNING", "You can't disconnect right now!");
                    }
                    System.out.println("Richiesta di disconnessione inviata al server");

                    // Torna alla schermata principale
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                    if (animatedBackground != null) {
                        animatedBackground.stopAnimation();
                    }
                } catch (IOException e) {
                    System.err.println("Errore durante la disconnessione: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Shows a popup window with buttons for applicable commands when the card is clicked
     */
    @FXML
    public void showApplicableCommands(MouseEvent event) {
        System.out.println("Mostra comandi applicabili per la carta corrente");

        // Verifica che ci sia una carta da visualizzare
        if (model.getCurrCard() == null) {
            System.out.println("Nessuna carta corrente disponibile");
            return;
        }

        // Verifica che sia il turno del giocatore
        if (!model.getPlayerName().equals(model.getCurrPlayer())) {
            System.out.println("Non è il tuo turno, non puoi eseguire comandi");
            return;
        }

        try {

            // Ottiene i comandi applicabili in base al modello corrente
            List<Command> applicableCommands = commandManager.getAvailableCommandTypes(model);
            if (applicableCommands.isEmpty()) {
                System.out.println("Nessun comando applicabile disponibile");
                return;
            }

            // Crea una finestra pop-up per mostrare i comandi applicabili
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Comandi Applicabili - " + model.getCurrCard().getName());

            // Contenitore verticale per i pulsanti dei comandi
            VBox commandsBox = new VBox(10);
            commandsBox.setAlignment(Pos.CENTER);
            commandsBox.setPadding(new Insets(20));

            // Aggiungi un'etichetta di intestazione
            Label titleLabel = new Label("Seleziona un comando da eseguire:");
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            commandsBox.getChildren().add(titleLabel);

            // Crea un pulsante per ogni comando applicabile
            for (Command command : applicableCommands) {
                Button cmdButton = new Button(command.getName());
                cmdButton.setPrefWidth(250);
                cmdButton.setOnAction(e -> {
                    resetShip();
                    buttonToMethod.get(command.getName()).handle(e);
                    popupStage.close();
                });
                commandsBox.getChildren().add(cmdButton);
            }

            // Aggiunge un pulsante Annulla
            Button cancelButton = new Button("Annulla");
            cancelButton.setOnAction(e -> popupStage.close());
            commandsBox.getChildren().add(cancelButton);

            // Crea uno ScrollPane nel caso ci siano molti comandi
            ScrollPane scrollPane = new ScrollPane(commandsBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefViewportHeight(400);

            Scene popupScene = new Scene(scrollPane, 300, 400);
            popupStage.setScene(popupScene);

            // Posiziona la finestra vicino al punto di click
            popupStage.centerOnScreen();

            popupStage.show();

        } catch (Exception e) {
            System.err.println("Errore durante la visualizzazione dei comandi applicabili: " + e.getMessage());
        }
    }

    private void resetShip() {
        for (Node node : myShip.getChildren()) {
            if (node instanceof ImageView imageView) {
                imageView.setOnMouseClicked(null);
            }
        }
        batteryXcoord = -1;
        batteryYcoord = -1;
        doubleCannonXcoord = -1;
        doubleCannonYcoord = -1;
        doubleEngineXcoord = -1;
        doubleEngineYcoord = -1;
        shieldXCoord = -1;
        shieldYCoord = -1;
        gbFromXCoord = -1;
        gbFromYCoord = -1;
        gbToXCoord = -1;
        gbToYCoord = -1;
        selectedGoodBlock1 = null;
        selectedGoodBlock2 = null;
    }

    /**
     * Mostra un messaggio di errore all'utente
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Errore");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Pulisce le risorse utilizzate dal controller
     */
    public void cleanup() {
        if (animatedBackground != null) {
            animatedBackground.stopAnimation();
            animatedBackground = null;
        }
        System.out.println("Pulizia delle risorse del CardPhaseController");
    }

    public void drawShipInCardPhase(Shipboard shipboard) {
        if (shipboard.getNickname().equals(model.getPlayerName())) {
            drawShipboardOnGrid(shipboard, myShip);
        } else {
            GridPane playerGrid = playerToShip.get(shipboard.getNickname());
            drawShipboardOnGrid(shipboard, playerGrid);
        }
    }

    private void drawShipboardOnGrid(Shipboard shipboard, GridPane myShip) {
        for (Node child : myShip.getChildren()) {
            int i = GridPane.getRowIndex(child) != null ? GridPane.getRowIndex(child) : 0;
            int j = GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0;
            Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(i, j);
            if (ct.isPresent() && ConditionVerifier.gridCoordinatesAreNotOutOfBound(i, j, model)) {
                drawComponentTileImageForGrid((ImageView) child, ct.get().getPngName(), ct.get().getNumOfRotations());
                setupTileTooltip((ImageView) child, ct.get());
            } else {
                ((ImageView) child).setImage(null);
            }
        }
    }

    private void drawComponentTileImageForGrid(ImageView imageView, String pngName, int numOfRotations) {
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/tiles/" + pngName)).toExternalForm());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setRotate(90 * numOfRotations);
        imageView.setImage(image);
    }

    public void drawFlightboardInCardPhase(Flightboard flightboard) {
        if (model.getGametype().equals(GameType.TUTORIAL)) {
            // Prima pulisci tutte le immagini dei razzi nella flightboard tutorial
            for (Node child : tutorialFlightboardPane.getChildren()) {
                if (child instanceof ImageView && child.getId() != null && child.getId().startsWith("t")) {
                    ((ImageView) child).setImage(null);
                }
            }

            // Poi disegna i razzi nelle posizioni correnti
            for (String player : flightboard.getPositions().keySet()) {
                Image rocket = colorToRocketImage.get(model.getShipboard(player).getColor());
                for (Node child : tutorialFlightboardPane.getChildren()) {
                    String position = "t" + flightboard.getPositions().get(player);
                    if (child.getId() != null && child.getId().equals(position)) {
                        ((ImageView) child).setImage(rocket);
                    }
                }
            }
        } else {
            // Prima pulisci tutte le immagini dei razzi nella flightboard level2
            for (Node child : level2FlightboardPane.getChildren()) {
                if (child instanceof ImageView && child.getId() != null && child.getId().startsWith("l")) {
                    ((ImageView) child).setImage(null);
                }
            }

            // Poi disegna i razzi nelle posizioni correnti
            for (String player : flightboard.getPositions().keySet()) {
                Image rocket = colorToRocketImage.get(model.getShipboard(player).getColor());
                for (Node child : level2FlightboardPane.getChildren()) {
                    String position = "l" + flightboard.getPositions().get(player);
                    if (child.getId() != null && child.getId().equals(position)) {
                        ((ImageView) child).setImage(rocket);
                    }
                }
            }
        }
    }

    public void fillGridPane(GridPane gridPane, int dimension) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                ImageView imageView = new ImageView();

                //Set the size
                imageView.setFitWidth(dimension);
                imageView.setFitHeight(dimension);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                gridPane.add(imageView, j, i);
            }
        }
    }

    private void initializeRocketColorMap() {
        colorToRocketImage.put("yellow", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/yellowRocket.png")).toString()));
        colorToRocketImage.put("blue", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/blueRocket.png")).toString()));
        colorToRocketImage.put("green", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/greenRocket.png")).toString()));
        colorToRocketImage.put("red", new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/rockets/redRocket.png")).toString()));
    }

    public void drawDices() {
        if (model.getDices() != null) {
            dice1.setImage(diceToImage.get(model.getDices().getDice1()));
            dice2.setImage(diceToImage.get(model.getDices().getDice2()));
        }
    }

    private void initializeDiceToImage() {
        diceToImage.put(1, new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/dices/1.png")).toString()));
        diceToImage.put(2, new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/dices/2.png")).toString()));
        diceToImage.put(3, new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/dices/3.png")).toString()));
        diceToImage.put(4, new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/dices/4.png")).toString()));
        diceToImage.put(5, new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/dices/5.png")).toString()));
        diceToImage.put(6, new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/dices/6.png")).toString()));
    }

    private void setupTileTooltip(ImageView imageView, ComponentTile tile) {
        // Create a tooltip to display information about the tile
        Tooltip tooltip = new Tooltip();
        StringBuilder tooltipText = new StringBuilder();

        DrawableComponentTile drawableComponentTile = tile.getDrawable();
        if(drawableComponentTile.isActivable()){
            // Resetta qualsiasi stile precedente
            imageView.setStyle("");

            // Creo un effetto di evidenziazione
            InnerShadow innerShadow = new javafx.scene.effect.InnerShadow();

            if(tile.isDoubleCannon()){
                if (tile.getCannonStrength() > 0) {
                    // Applico sia lo stile del bordo che un effetto visivo
                    innerShadow.setColor(javafx.scene.paint.Color.MAGENTA);
                    innerShadow.setRadius(10);
                    innerShadow.setChoke(0.5);
                    imageView.setEffect(innerShadow);
                    imageView.setStyle("-fx-border-color: #FF00FF; -fx-border-width: 4; -fx-border-style: solid;");
                    System.out.println("Setting magenta border for cannon at strength: " + tile.getCannonStrength());
                }
            }
            else if(tile.isDoubleEngine()){
                if (tile.getEngineStrength() > 0) {
                    innerShadow.setColor(javafx.scene.paint.Color.SADDLEBROWN);
                    innerShadow.setRadius(10);
                    innerShadow.setChoke(0.5);
                    imageView.setEffect(innerShadow);
                    imageView.setStyle("-fx-border-color: #8B4513; -fx-border-width: 4; -fx-border-style: solid;");
                    System.out.println("Setting brown border for engine at strength: " + tile.getEngineStrength());
                }
            }
            else if(tile.isShieldGenerator()){
                if(tile.isActivated()) {
                    innerShadow.setColor(javafx.scene.paint.Color.GREEN);
                    innerShadow.setRadius(10);
                    innerShadow.setChoke(0.5);
                    imageView.setEffect(innerShadow);
                    imageView.setStyle("-fx-border-color: #00FF00; -fx-border-width: 4; -fx-border-style: solid;");
                    System.out.println("Setting green border for shield generator");
                }
            }
        }

        if(!drawableComponentTile.draw().equals("null"))
            tooltipText.append(drawableComponentTile.draw());

        // Set the tooltip text and install it on the image view
        tooltip.setText(tooltipText.toString());

        // Make sure the tooltip appears faster and stays visible longer
        tooltip.setShowDelay(new javafx.util.Duration(200));
        tooltip.setHideDelay(new javafx.util.Duration(5000));

        // Rimuovi eventuali eventi precedenti per evitare duplicazioni
        imageView.setOnMouseEntered(null);
        imageView.setOnMouseExited(null);
        imageView.setOnMouseClicked(null);

        // Set the tooltip directly on the imageView
        imageView.setOnMouseEntered(event -> {
            // Disinstallo eventuali tooltip precedenti
            Tooltip.uninstall(imageView, tooltip);
            // Installo il nuovo tooltip
            Tooltip.install(imageView, tooltip);
            // Mostro il tooltip con un offset rispetto al cursore
            tooltip.show(imageView, event.getScreenX() + 15, event.getScreenY() + 15);

            // Riapplico lo stile quando il mouse entra
            if(drawableComponentTile.isActivable()) {
                if(tile.isDoubleCannon() && tile.getCannonStrength() == 0) {
                    imageView.setStyle("-fx-border-color: #FF00FF; -fx-border-width: 4; -fx-border-style: solid;");
                }
                else if(tile.isDoubleEngine() && tile.getEngineStrength() == 0) {
                    imageView.setStyle("-fx-border-color: #8B4513; -fx-border-width: 4; -fx-border-style: solid;");
                }
                else if(tile.isShieldGenerator()) {
                    imageView.setStyle("-fx-border-color: #00FF00; -fx-border-width: 4; -fx-border-style: solid;");
                }
            }
        });

        imageView.setOnMouseExited(event -> {
            // Prima nascondo il tooltip
            tooltip.hide();
            // Poi lo disinstallo
            Tooltip.uninstall(imageView, tooltip);

            // Mantengo l'effetto anche quando il mouse esce, ma solo se necessario
            // Altrimenti, se non è un componente attivabile o non ha uno stato attivo,
            // rimuovo lo stile applicato sull'hover
            if (!drawableComponentTile.isActivable() ||
                (tile.isDoubleCannon() && tile.getCannonStrength() == 0) ||
                (tile.isDoubleEngine() && tile.getEngineStrength() == 0) ||
                (tile.isShieldGenerator() && !tile.isActivated())) {
                // Rimuovo solo lo stile di hover, mantenendo eventuali altri stili
                imageView.setStyle("");
            }
        });

        // Aggiungi anche un handler per il click che nasconde il tooltip
        imageView.setOnMouseClicked(event -> {
            tooltip.hide();
            Tooltip.uninstall(imageView, tooltip);
        });

        // Assicurati che il tooltip sia rimosso quando l'immagine viene aggiornata o rimossa
        imageView.imageProperty().addListener((obs, oldImage, newImage) -> {
            tooltip.hide();
            Tooltip.uninstall(imageView, tooltip);
        });
    }

    public void initializeButtonToMethod() {
        buttonToMethod.put("AcceptCredits", this::acceptCreditsCommand);
        buttonToMethod.put("ActivateDoubleCannon", this::activateDoubleCannonCommand);
        buttonToMethod.put("ActivateDoubleEngine", this::activateDoubleEngineCommand);
        buttonToMethod.put("ActivateShield", this::activateShieldCommand);
        buttonToMethod.put("ChooseShipWreck", this::chooseShipWreckCommand);
        buttonToMethod.put("DecideToRemoveCrewMembers", this::decideToRemoveCrewMembersCommand);
        buttonToMethod.put("GetBlock", this::getBlockCommand);
        buttonToMethod.put("LandOnAbandonedStation", this::landOnAbandonedStationCommand);
        buttonToMethod.put("LandOnPlanet", this::landOnPlanetCommand);
        buttonToMethod.put("MoveGoodBlock", this::moveGoodBlockCommand);
        buttonToMethod.put("RemoveCrewMember", this::removeCrewMemberCommand);
        buttonToMethod.put("RemoveGoodBlock", this::removeGoodBlockCommand);
        buttonToMethod.put("ResolveEffect", this::resolveEffectCommand);
        buttonToMethod.put("SwitchGoodBlocks", this::switchGoodBlocksCommand);
    }

    private void acceptCreditsCommand(ActionEvent event) {
        Command cmd = new AcceptCreditsCommand(virtualServer, null);
        if(cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "Invalid inputs!");
        resetShip();
    }

    private void activateDoubleCannonCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleDoubleCannonCoordinates);
    }

    private void handleDoubleCannonCoordinates(MouseEvent event) {
        if(batteryXcoord == -1 || batteryYcoord == -1){
            Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
            Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
            batteryXcoord = rowIndex;
            batteryYcoord = columnIndex;
        } else {
            Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
            Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
            doubleCannonXcoord = rowIndex;
            doubleCannonYcoord = columnIndex;

            Command cmd = new ActivateDoubleCannonCommand(virtualServer, null);
            sendActivateComponentCommand(cmd, doubleCannonXcoord, doubleCannonYcoord);
        }
    }

    private void activateDoubleEngineCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleDoubleEngineCoordinates);
    }

    private void handleDoubleEngineCoordinates(MouseEvent event) {
        if(batteryXcoord == -1 || batteryYcoord == -1){
            Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
            Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
            batteryXcoord = rowIndex;
            batteryYcoord = columnIndex;
        } else {
            Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
            Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
            doubleEngineXcoord = rowIndex;
            doubleEngineYcoord = columnIndex;

            Command cmd = new ActivateDoubleEngineCommand(virtualServer, null);
            sendActivateComponentCommand(cmd, doubleEngineXcoord, doubleEngineYcoord);
        }
    }

    private void sendActivateComponentCommand(Command cmd, int xCoord, int yCoord) {
        List<String> input = new ArrayList<>();
        input.addLast(String.valueOf(batteryXcoord+5));
        input.addLast(String.valueOf(batteryYcoord+4));
        input.addLast(String.valueOf(xCoord+5));
        input.addLast(String.valueOf(yCoord+4));
        cmd.setInput(input);
        if(cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "Invalid inputs!");
        resetShip();
    }

    private void activateShieldCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleShieldGeneratorCoordinates);
    }

    private void handleShieldGeneratorCoordinates(MouseEvent event) {
        if(batteryXcoord == -1 || batteryYcoord == -1){
            Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
            Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
            batteryXcoord = rowIndex;
            batteryYcoord = columnIndex;
        } else {
            Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
            Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
            shieldXCoord = rowIndex;
            shieldYCoord = columnIndex;
            Command cmd = new ActivateShieldCommand(virtualServer, null);
            sendActivateComponentCommand(cmd, shieldXCoord, shieldYCoord);
        }
    }

    private void chooseShipWreckCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleChooseShipWreckCoordinates);
    }

    private void handleChooseShipWreckCoordinates(MouseEvent mouseEvent) {
        Integer rowIndex = GridPane.getRowIndex((Node) mouseEvent.getSource());
        Integer columnIndex = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        Command cmd = new ChooseShipWreckCommand(virtualServer, null);
        List<String> input = new ArrayList<>();
        input.addLast(String.valueOf(rowIndex+5));
        input.addLast(String.valueOf(columnIndex+4));
        cmd.setInput(input);
        if(cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "Invalid inputs!");
        resetShip();
    }

    private void decideToRemoveCrewMembersCommand(ActionEvent event) {
        Command cmd = new DecideToRemoveCrewMembersCommand(virtualServer, null);
        new Thread(() -> cmd.execute(model)).start();
    }

    private void getBlockCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleGetBlockCoordinates);
    }

    private void handleGetBlockCoordinates(MouseEvent mouseEvent) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("Choose the block to get");

        VBox commandsBox = new VBox(10);
        commandsBox.setAlignment(Pos.CENTER);
        commandsBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Choose the block to get");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        commandsBox.getChildren().add(titleLabel);

        for (int i = 0; i < 4; i++) {
            Button cmdButton = new Button(numToGoodBlock.get(i));
            cmdButton.setPrefWidth(250);
            int gbType = i;
            cmdButton.setOnAction(e -> {
                Integer rowIndex = GridPane.getRowIndex((Node) mouseEvent.getSource());
                Integer columnIndex = GridPane.getColumnIndex((Node) mouseEvent.getSource());

                Command cmd = new GetBlockCommand(virtualServer, null);
                List<String> input = new ArrayList<>();
                input.addLast(numToGoodBlock.get(gbType));
                input.addLast(String.valueOf(rowIndex+5));
                input.addLast(String.valueOf(columnIndex+4));
                cmd.setInput(input);

                if(cmd.isInputValid(model))
                    new Thread(() -> cmd.execute(model)).start();
                else
                    showErrorAlert("WARNING", "Invalid inputs!");
                resetShip();
                popupStage.close();
            });
            commandsBox.getChildren().add(cmdButton);
        }

        ScrollPane scrollPane = new ScrollPane(commandsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(400);

        Scene popupScene = new Scene(scrollPane, 300, 400);
        popupStage.setScene(popupScene);

        popupStage.centerOnScreen();

        popupStage.show();
    }

    private void landOnAbandonedStationCommand(ActionEvent event) {
        Command cmd = new LandOnAbandonedStationCommand(virtualServer, null);
        new Thread(() -> cmd.execute(model)).start();
    }

    private void landOnPlanetCommand(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("Choose the planet");

        VBox commandsBox = new VBox(10);
        commandsBox.setAlignment(Pos.CENTER);
        commandsBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Choose the planet");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        commandsBox.getChildren().add(titleLabel);

        for (int i = 0; i < ((PlanetsCard) (model.getCurrCard())).getPlanets().size(); i++) {
            Button cmdButton = setLandingButtons(i, popupStage);
            commandsBox.getChildren().add(cmdButton);
        }

        ScrollPane scrollPane = new ScrollPane(commandsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(400);

        Scene popupScene = new Scene(scrollPane, 300, 400);
        popupStage.setScene(popupScene);

        popupStage.centerOnScreen();

        popupStage.show();
    }

    private Button setLandingButtons(int i, Stage popupStage) {
        Button cmdButton = new Button("Land on planet " + (i+1));
        cmdButton.setPrefWidth(250);
        cmdButton.setOnAction(e -> {
            Command cmd = new LandOnPlanetCommand(virtualServer, null);
            cmd.setInput(Collections.singletonList(String.valueOf(i)));
            if(cmd.isInputValid(model))
                new Thread(() -> cmd.execute(model)).start();
            else
                showErrorAlert("WARNING", "Invalid inputs!");
            resetShip();
            popupStage.close();
        });
        return cmdButton;
    }

    private void moveGoodBlockCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleMoveBlockCoordinates);
    }

    private void handleMoveBlockCoordinates(MouseEvent mouseEvent) {
        Integer rowIndex = GridPane.getRowIndex((Node) mouseEvent.getSource());
        Integer columnIndex = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        if(gbFromXCoord == -1 || gbFromYCoord == -1){
            gbFromXCoord = rowIndex;
            gbFromYCoord = columnIndex;

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Choose the block");

            VBox commandsBox = new VBox(10);
            commandsBox.setAlignment(Pos.CENTER);
            commandsBox.setPadding(new Insets(20));

            Label titleLabel = new Label("Choose the block");
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            commandsBox.getChildren().add(titleLabel);

            for (int i = 0; i < 4; i++) {
                Button cmdButton = new Button(numToGoodBlock.get(i));
                cmdButton.setPrefWidth(250);
                int gbType = i;
                cmdButton.setOnAction(e -> {
                    selectedGoodBlock1 = numToGoodBlock.get(gbType);
                    popupStage.close();
                });
                commandsBox.getChildren().add(cmdButton);
            }

            ScrollPane scrollPane = new ScrollPane(commandsBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefViewportHeight(400);

            Scene popupScene = new Scene(scrollPane, 300, 400);
            popupStage.setScene(popupScene);

            popupStage.centerOnScreen();

            popupStage.show();
        } else {
            gbToXCoord = rowIndex;
            gbToYCoord = columnIndex;

            sendMoveBlockCommand();
        }
    }

    private void sendMoveBlockCommand() {
        Command cmd = new MoveGoodBlockCommand(virtualServer, null);
        List<String> input = new ArrayList<>();
        input.addLast(selectedGoodBlock1);
        input.addLast(String.valueOf(gbFromXCoord+5));
        input.addLast(String.valueOf(gbFromYCoord+4));
        input.addLast(String.valueOf(gbToXCoord+5));
        input.addLast(String.valueOf(gbToYCoord+4));
        cmd.setInput(input);
        if(cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "Invalid inputs!");
        resetShip();
    }

    private void removeCrewMemberCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleRemoveCrewCoordinates);
    }

    private void handleRemoveCrewCoordinates(MouseEvent mouseEvent) {
        Integer rowIndex = GridPane.getRowIndex((Node) mouseEvent.getSource());
        Integer columnIndex = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        Command cmd = new RemoveCrewMemberCommand(virtualServer, null);
        List<String> input = new ArrayList<>();
        input.addLast(String.valueOf(rowIndex+5));
        input.addLast(String.valueOf(columnIndex+4));
        cmd.setInput(input);
        if(cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "Invalid inputs!");
        resetShip();
    }

    private void removeGoodBlockCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleRemoveGoodBlockCoordinates);
    }

    private void insertMethodsInTiles(EventHandler<MouseEvent> event) {
        Shipboard thisPlayerShip = model.getShipboard(model.getPlayerName());
        for(int i = 0; i < 5 ; i++) {
            for(int j = 0; j < 7 ; j++) {
                if(thisPlayerShip.getComponentTileFromGrid(i, j).isPresent()) {
                    for (Node node : myShip.getChildren()) {
                        Integer columnIndex = GridPane.getColumnIndex(node);
                        Integer rowIndex = GridPane.getRowIndex(node);

                        int nodeCol = columnIndex == null ? 0 : columnIndex;
                        int nodeRow = rowIndex == null ? 0 : rowIndex;

                        if (nodeCol == j && nodeRow == i) {
                            node.setOnMouseClicked(event);
                        }
                    }
                }
            }
        }
    }

    private void handleRemoveGoodBlockCoordinates(MouseEvent mouseEvent) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("Choose the block");

        VBox commandsBox = new VBox(10);
        commandsBox.setAlignment(Pos.CENTER);
        commandsBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Choose the block");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        commandsBox.getChildren().add(titleLabel);

        for (int i = 0; i < 4; i++) {
            Button cmdButton = new Button(numToGoodBlock.get(i));
            cmdButton.setPrefWidth(250);
            int gbType = i;
            cmdButton.setOnAction(e -> {
                sendRemoveBlockMessage(mouseEvent, gbType, popupStage);
            });
            commandsBox.getChildren().add(cmdButton);
        }

        ScrollPane scrollPane = new ScrollPane(commandsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(400);

        Scene popupScene = new Scene(scrollPane, 300, 400);
        popupStage.setScene(popupScene);

        popupStage.centerOnScreen();

        popupStage.show();
    }

    private void sendRemoveBlockMessage(MouseEvent mouseEvent, int gbType, Stage popupStage) {
        Integer rowIndex = GridPane.getRowIndex((Node) mouseEvent.getSource());
        Integer columnIndex = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        Command cmd = new RemoveGoodBlockCommand(virtualServer, null);
        List<String> input = new ArrayList<>();
        input.addLast(numToGoodBlock.get(gbType));
        input.addLast(String.valueOf(rowIndex+5));
        input.addLast(String.valueOf(columnIndex+4));
        cmd.setInput(input);
        if(cmd.isInputValid(model)) {
            // Nascondo tutti i tooltip attivi prima di eseguire il comando
            for (Node node : myShip.getChildren()) {
                Tooltip.uninstall(node, null);
            }
            // Eseguo il comando in un thread separato
            new Thread(() -> {
                cmd.execute(model);
                // Aggiorno la scena dopo l'esecuzione del comando
                javafx.application.Platform.runLater(this::drawScene);
            }).start();
        }
        else
            showErrorAlert("WARNING", "Invalid inputs!");
        resetShip();
        popupStage.close();
    }

    private void resolveEffectCommand(ActionEvent event) {
        Command cmd = new ResolveEffectCommand(virtualServer, null);
        new Thread(() -> cmd.execute(model)).start();
    }

    private void switchGoodBlocksCommand(ActionEvent event) {
        insertMethodsInTiles(this::handleSwitchGoodBlockCoordinates);
    }

    private void handleSwitchGoodBlockCoordinates(MouseEvent mouseEvent) {
        Integer rowIndex = GridPane.getRowIndex((Node) mouseEvent.getSource());
        Integer columnIndex = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        if(gbFromXCoord == -1 || gbFromYCoord == -1){
            gbFromXCoord = rowIndex;
            gbFromYCoord = columnIndex;

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Choose the block");

            VBox commandsBox = new VBox(10);
            commandsBox.setAlignment(Pos.CENTER);
            commandsBox.setPadding(new Insets(20));

            Label titleLabel = new Label("Choose the block");
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            commandsBox.getChildren().add(titleLabel);

            for (int i = 0; i < 4; i++) {
                Button cmdButton = new Button(numToGoodBlock.get(i));
                cmdButton.setPrefWidth(250);
                int gbType = i;
                cmdButton.setOnAction(e -> {
                    selectedGoodBlock1 = numToGoodBlock.get(gbType);
                    popupStage.close();
                });
                commandsBox.getChildren().add(cmdButton);
            }

            ScrollPane scrollPane = new ScrollPane(commandsBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefViewportHeight(400);

            Scene popupScene = new Scene(scrollPane, 300, 400);
            popupStage.setScene(popupScene);

            popupStage.centerOnScreen();

            popupStage.show();
        } else {
            gbToXCoord = rowIndex;
            gbToYCoord = columnIndex;

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Choose the block");

            VBox commandsBox = new VBox(10);
            commandsBox.setAlignment(Pos.CENTER);
            commandsBox.setPadding(new Insets(20));

            Label titleLabel = new Label("Choose the block");
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            commandsBox.getChildren().add(titleLabel);

            for (int i = 0; i < 4; i++) {
                Button cmdButton = new Button(numToGoodBlock.get(i));
                cmdButton.setPrefWidth(250);
                int gbType = i;
                cmdButton.setOnAction(e -> {
                    selectedGoodBlock2 = numToGoodBlock.get(gbType);
                    sendSwitchCommand();
                    popupStage.close();
                });
                commandsBox.getChildren().add(cmdButton);
            }

            ScrollPane scrollPane = new ScrollPane(commandsBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefViewportHeight(400);

            Scene popupScene = new Scene(scrollPane, 300, 400);
            popupStage.setScene(popupScene);

            popupStage.centerOnScreen();

            popupStage.show();
        }
    }

    private void sendSwitchCommand() {
        Command cmd = new SwitchGoodBlocksCommand(virtualServer, null);
        List<String> input = new ArrayList<>();
        input.addLast(selectedGoodBlock1);
        input.addLast(String.valueOf(gbFromXCoord+5));
        input.addLast(String.valueOf(gbFromYCoord+4));
        input.addLast(selectedGoodBlock2);
        input.addLast(String.valueOf(gbToXCoord+5));
        input.addLast(String.valueOf(gbToYCoord+4));
        cmd.setInput(input);
        if(cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "Invalid inputs!");
        resetShip();
    }
}

