package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands.*;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyStarsEffect;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableComponentTile;
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

/**
 * Controls the logic and rendering operations during a specific card-based phase
 * of the Galaxy Trucker application. Extending the {@link FXMLController} class,
 * this controller integrates with the GUI framework and manages functionalities
 * related to visual and logical game state.
 */
public class CardPhaseController extends FXMLController {
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

    private GalaxyStarsEffect animatedBackground;
    private Map<String, GridPane> playerToShip;
    private List<ImageView> playersImage;
    private final Map<String, Image> colorToRocketImage = new HashMap<>();
    private final Map<Integer, Image> diceToImage = new HashMap<>();
    private final Map<String, EventHandler<ActionEvent>> buttonToMethod = new HashMap<>();

    private int batteryXcoord = -1;
    private int batteryYcoord = -1;
    private int doubleCannonXcoord = -1;
    private int doubleCannonYcoord = -1;
    private int doubleEngineXcoord = -1;
    private int doubleEngineYcoord = -1;
    private int shieldXCoord = -1;
    private int shieldYCoord = -1;

    private CommandManager commandManager;

    @FXML
    private void initialize() {
        commandManager = new CommandManager();
        commandManager.initializeCommandManagerGUI(GalaxyTruckerGUI.getVirtualServer());
        setup(null, GalaxyTruckerGUI.getClientModel(), GalaxyTruckerGUI.getPrimaryStage(), GalaxyTruckerGUI.getVirtualServer());
        initializePlayerToShip();
        initializeButtonToMethod();
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

        // Aggiungi un cursore mano per indicare che la carta è cliccabile
        cardImage.setStyle("-fx-cursor: hand;");

        animatedBackground = new GalaxyStarsEffect(1280, 720);

        if (background.getParent() instanceof Pane pane) {
            animatedBackground.setWidth(background.getFitWidth());
            animatedBackground.setHeight(background.getFitHeight());

            // Inserisce l'animazione come primo elemento del pane (dietro a tutti gli altri elementi)
            pane.getChildren().add(0, animatedBackground);

            // Assicura che lo sfondo sia dietro tutti gli elementi
            animatedBackground.toBack();
            background.toBack();
        }
        // Carica lo stato iniziale della scena
        drawScene();
        updateButtonsState();
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
        // Impostata la visibilità delle navi in base al numero di giocatori
        setShipboardsVisibility(playersNumber);

        // Assegnate le immagini solo alle navi che saranno visibili
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
        // impostata la visibilità delle navi in base al numero di giocatori
        setShipboardsVisibility(playersNumber);

        // Assegnate le immagini solo alle navi che saranno visibili
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
                // Per il caso con un solo giocatore o errore, non visualizziamo navi aggiuntive
                break;
        }

        // Imposta i nomi dei giocatori in base all'elenco effettivo
        updatePlayerNames();
    }

    /**
     * Updates the player name labels based on the current players in the game
     */
    private void updatePlayerNames() {
        // Ottiene i nomi dei giocatori dall'elenco dei giocatori escluso il giocatore corrente
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
     * Disegna la nave del giocatore
     */
    public void drawShips() {
        for (Shipboard s : model.getShipboards().values()) {
            drawShipInCardPhase(s);
        }
    }

    /**
     * Aggiorna tutti gli elementi della scena in base allo stato attuale del gioco
     */
    public void drawScene() {
        try {
            // Aggiorna le visualizzazioni delle navi
            drawShips();
            // Aggiorna la flightboard
            drawFlightboardInCardPhase(model.getFlightboard());
            drawDices();
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento della scena: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Aggiorna lo stato dei pulsanti in base al turno corrente
     * Disabilita i pulsanti quando non è il turno del giocatore
     */
    public void updateButtonsState() {
        boolean isPlayerTurn = model.getPlayerName().equals(model.getCurrPlayer());

        // Il pulsante pickCard è attivo solo se è il turno del giocatore e lo metto disabilitato e grigio
        pickCardButton.setDisable(!isPlayerTurn);
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
            e.printStackTrace();
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
        } catch (Exception e) {
            System.err.println("Errore durante la visualizzazione della carta: " + e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
                try {
                    virtualServer.playerAbandons(model.getPlayerName());
                    System.out.println("Richiesta di abbandono partita inviata al server");
                } catch (IOException e) {
                    System.err.println("Errore durante l'abbandono della partita: " + e.getMessage());
                    e.printStackTrace();
                }
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
                    virtualServer.disconnect();
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
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Torna alla fase di costruzione della nave
     *
     * @param event l'evento di click sul bottone buildingShipButton
     */
    @FXML
    public void switchToBuildingShip(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/BuildingShip.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Ferma l'animazione dello sfondo quando si cambia scena
            if (animatedBackground != null) {
                animatedBackground.stopAnimation();
            }
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della scena BuildingShip.fxml: " + e.getMessage());
            e.printStackTrace();
        }
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
            popupStage.setX(event.getScreenX());
            popupStage.setY(event.getScreenY());

            popupStage.show();

        } catch (Exception e) {
            System.err.println("Errore durante la visualizzazione dei comandi applicabili: " + e.getMessage());
            e.printStackTrace();
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
            //draw grid
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
        } else {
            GridPane playerGrid = playerToShip.get(shipboard.getNickname());
            for (Node child : playerGrid.getChildren()) {
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
            for (String player : flightboard.getPositions().keySet()) {
                Image rocket = colorToRocketImage.get(model.getShipboard(player).getColor());
                for (Node child : tutorialFlightboardPane.getChildren()) {
                    String position = "t" + flightboard.getPositions().get(player);
                    if (child.getId().equals(position)) {
                        ((ImageView) child).setImage(rocket);
                    }
                }
            }
        } else {
            for (String player : flightboard.getPositions().keySet()) {
                Image rocket = colorToRocketImage.get(model.getShipboard(player).getColor());
                for (Node child : level2FlightboardPane.getChildren()) {
                    String position = "l" + flightboard.getPositions().get(player);
                    if (child.getId().equals(position)) {
                        ((ImageView) child).setImage(rocket);
                    }
                }
            }
        }
    }

    public void drawBankInCardPhase(Bank bank) {
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
                if(((ShieldGenerator)tile).isActive()) {
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

        // Set the tooltip directly on the imageView
        imageView.setOnMouseEntered(event -> {
            Tooltip.install(imageView, tooltip);
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
            tooltip.hide();
            Tooltip.uninstall(imageView, tooltip);
            // Mantengo l'effetto anche quando il mouse esce
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
        if(cmd.isApplicable(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "You are not allowed to execute this command");
    }

    private void activateDoubleCannonCommand(ActionEvent event) {
        Shipboard thisPlayerShip = model.getShipboard(model.getPlayerName());
        for(int i = 0; i < 5 ; i++) {
            for(int j = 0; j < 7 ; j++) {
                if(thisPlayerShip.getComponentTileFromGrid(i, j).isPresent()) {
                    if(thisPlayerShip.getComponentTileFromGrid(i, j).get().isBattery()){
                        for (Node node : myShip.getChildren()) {
                            Integer columnIndex = GridPane.getColumnIndex(node);
                            Integer rowIndex = GridPane.getRowIndex(node);

                            int nodeCol = columnIndex == null ? 0 : columnIndex;
                            int nodeRow = rowIndex == null ? 0 : rowIndex;

                            if (nodeCol == i && nodeRow == j) {
                                node.setOnMouseClicked(this::handleBatteryCoordinates);
                            }
                        }
                    } else if (thisPlayerShip.getComponentTileFromGrid(i, j).get().isDoubleCannon()) {
                        for (Node node : myShip.getChildren()) {
                            Integer columnIndex = GridPane.getColumnIndex(node);
                            Integer rowIndex = GridPane.getRowIndex(node);

                            int nodeCol = columnIndex == null ? 0 : columnIndex;
                            int nodeRow = rowIndex == null ? 0 : rowIndex;

                            if (nodeCol == i && nodeRow == j) {
                                node.setOnMouseClicked(this::handleDoubleCannonCoordinates);
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleBatteryCoordinates(MouseEvent event) {
        Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
        Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
        batteryXcoord = rowIndex;
        batteryYcoord = columnIndex;
        System.out.println("Battery coordinates: " + batteryXcoord + ", " + batteryYcoord);
    }

    private void handleDoubleCannonCoordinates(MouseEvent event) {
        if(batteryXcoord == -1 || batteryYcoord == -1){
            showErrorAlert("WARNING", "Activate batteries first!");
            return;
        }
        Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
        Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
        doubleCannonXcoord = rowIndex;
        doubleCannonYcoord = columnIndex;

        Command cmd = new ActivateDoubleCannonCommand(virtualServer, null);
        sendActivateComponentCommand(cmd, doubleCannonXcoord, doubleCannonYcoord);
        System.out.println("Double cannon coordinates: " + doubleCannonXcoord + ", " + doubleCannonYcoord);
    }

    private void activateDoubleEngineCommand(ActionEvent event) {
        Shipboard thisPlayerShip = model.getShipboard(model.getPlayerName());
        for(int i = 0; i < 5 ; i++) {
            for(int j = 0; j < 7 ; j++) {
                if(thisPlayerShip.getComponentTileFromGrid(i, j).isPresent()) {
                    if(thisPlayerShip.getComponentTileFromGrid(i, j).get().isBattery()){
                        for (Node node : myShip.getChildren()) {
                            Integer columnIndex = GridPane.getColumnIndex(node);
                            Integer rowIndex = GridPane.getRowIndex(node);

                            int nodeCol = columnIndex == null ? 0 : columnIndex;
                            int nodeRow = rowIndex == null ? 0 : rowIndex;

                            if (nodeCol == i && nodeRow == j) {
                                node.setOnMouseClicked(this::handleBatteryCoordinates);
                            }
                        }
                    } else if (thisPlayerShip.getComponentTileFromGrid(i, j).get().isDoubleEngine()) {
                        for (Node node : myShip.getChildren()) {
                            Integer columnIndex = GridPane.getColumnIndex(node);
                            Integer rowIndex = GridPane.getRowIndex(node);

                            int nodeCol = columnIndex == null ? 0 : columnIndex;
                            int nodeRow = rowIndex == null ? 0 : rowIndex;

                            if (nodeCol == i && nodeRow == j) {
                                node.setOnMouseClicked(this::handleDoubleEngineCoordinates);
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleDoubleEngineCoordinates(MouseEvent event) {
        if(batteryXcoord == -1 || batteryYcoord == -1){
            showErrorAlert("WARNING", "Activate batteries first!");
            return;
        }
        Integer rowIndex = GridPane.getRowIndex((Node) event.getSource());
        Integer columnIndex = GridPane.getColumnIndex((Node) event.getSource());
        doubleEngineXcoord = rowIndex;
        doubleEngineYcoord = columnIndex;

        Command cmd = new ActivateDoubleEngineCommand(virtualServer, null);
        sendActivateComponentCommand(cmd, doubleEngineXcoord, doubleEngineYcoord);
    }

    private void sendActivateComponentCommand(Command cmd, int xCoord, int yCoord) {
        List<String> input = new ArrayList<>();
        input.addLast(String.valueOf(batteryXcoord+5));
        input.addLast(String.valueOf(batteryYcoord+4));
        input.addLast(String.valueOf(xCoord+5));
        input.addLast(String.valueOf(yCoord+4));
        cmd.setInput(input);
        if(cmd.isApplicable(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "You are not allowed to execute this command");
    }

    private void activateShieldCommand(ActionEvent event) {

    }

    private void chooseShipWreckCommand(ActionEvent event) {

    }

    private void decideToRemoveCrewMembersCommand(ActionEvent event) {

    }

    private void getBlockCommand(ActionEvent event) {

    }

    private void landOnAbandonedStationCommand(ActionEvent event) {
        Command cmd = new LandOnAbandonedStationCommand(virtualServer, null);
        if(cmd.isApplicable(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "You are not allowed to execute this command");
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

        popupStage.setX(primaryStage.getX());
        popupStage.setY(primaryStage.getY());

        popupStage.show();
    }

    private Button setLandingButtons(int i, Stage popupStage) {
        Button cmdButton = new Button("Land on planet " + i);
        cmdButton.setPrefWidth(250);
        int landingPlanet = i;
        cmdButton.setOnAction(e -> {
            Command cmd = new LandOnPlanetCommand(virtualServer, null);
            cmd.setInput(Collections.singletonList(String.valueOf(landingPlanet)));
            if(cmd.isInputValid(model))
                new Thread(() -> cmd.execute(model)).start();
            else
                showErrorAlert("WARNING", "You are not allowed to execute this command");
            popupStage.close();
        });
        return cmdButton;
    }

    private void moveGoodBlockCommand(ActionEvent event) {

    }

    private void removeCrewMemberCommand(ActionEvent event) {

    }

    private void removeGoodBlockCommand(ActionEvent event) {

    }

    private void resolveEffectCommand(ActionEvent event) {
        Command cmd = new ResolveEffectCommand(virtualServer, null);
        if(cmd.isInputValid(model))
            new Thread(() -> cmd.execute(model)).start();
        else
            showErrorAlert("WARNING", "You are not allowed to execute this command");
    }

    private void switchGoodBlocksCommand(ActionEvent event) {

    }
}


