package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.Commands.Command;
import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Client.Commands.ConditionVerifier;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyStarsEffect;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Controls the logic and rendering operations during a specific card-based phase
 * of the Galaxy Trucker application. Extending the {@link FXMLController} class,
 * this controller integrates with the GUI framework and manages functionalities
 * related to visual and logical game state.
 */
public class CardPhaseController extends FXMLController {
    @FXML private GridPane myShip;
    @FXML private GridPane player1Ship; // Changed from player1Grid
    @FXML private GridPane player2Ship; // Changed from player2Grid
    @FXML private GridPane player3Ship; // Changed from player3Grid
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
    @FXML private Button resolveEffectButton;

    private GalaxyStarsEffect animatedBackground;
    private Map<String, GridPane> playerToShip;
    private List<ImageView> playersImage;

    private CommandManager commandManager;

    @FXML
    private void initialize() {
        commandManager = new CommandManager();
        commandManager.initializeCommandManagerGUI(GalaxyTruckerGUI.getVirtualServer());
        setup(null, GalaxyTruckerGUI.getClientModel(), GalaxyTruckerGUI.getPrimaryStage() ,GalaxyTruckerGUI.getVirtualServer());
        initializePlayerToShip();
        playersImage = List.of(myShipImage, player1ShipImage, player2ShipImage, player3ShipImage);
        if (model.getGametype() == GameType.TUTORIAL) {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BlueBackground.png")).toString()));
            setShipboardImagesTutorial(playerToShip.size()+1);
            tutorialFlightboardPane.setVisible(true);
            level2FlightboardPane.setVisible(false);

        } else {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/PurpleBackground.png")).toString()));
            setShipboardImagesLevel2(playerToShip.size()+1);
            tutorialFlightboardPane.setVisible(false);
            level2FlightboardPane.setVisible(true);
        }

        // Aggiungi gestore eventi per clic sulla carta per mostrare comandi applicabili
        cardImage.setOnMouseClicked(this::showApplicableCommands);

        // Aggiungi un cursore mano per indicare che la carta è cliccabile
        cardImage.setStyle("-fx-cursor: hand;");

        animatedBackground = new GalaxyStarsEffect(1280, 720);

        if (background.getParent() instanceof Pane pane) {
            animatedBackground.setWidth(background.getFitWidth());
            animatedBackground.setHeight(background.getFitHeight());

            // Inserisce l'animazione come primo elemento del pane (dietro a tutti gli altri elementi)
            pane.getChildren().add(0, animatedBackground);

            // Assicurati che lo sfondo sia dietro tutti gli elementi
            animatedBackground.toBack();
            background.toBack();
        }
        // Carica lo stato iniziale della scena
        drawScene();
    }

    private void initializePlayerToShip() {
        playerToShip = new HashMap<>();
        List<GridPane> shipViews = List.of(myShip, player1Ship, player2Ship, player3Ship);
        for (int i = 0; i < model.getShipboards().size(); i++) {
            playerToShip.put(model.getShipboards().keySet().stream().toList().get(i), shipViews.get(i));
        }
    }

    private void setShipboardImagesLevel2(int playersNumber) {
        // Prima imposta la visibilità delle navi in base al numero di giocatori
        setShipboardsVisibility(playersNumber);

        // Assegna le immagini solo alle navi che saranno visibili
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
        // Prima imposta la visibilità delle navi in base al numero di giocatori
        setShipboardsVisibility(playersNumber);

        // Assegna le immagini solo alle navi che saranno visibili
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

        // Now show only what we need based on player count
        switch(numberOfPlayers) {
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

        if (!otherPlayers.isEmpty() && otherPlayers.size() >= 1) {
            player1Name.setText(otherPlayers.get(0));
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
        for(Shipboard s : model.getShipboards().values()) {
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
        } catch(Exception e) {
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

        // Il pulsante pickCard è attivo solo se è il turno del giocatore
        pickCardButton.setDisable(!isPlayerTurn);

        // Si potrebbe fare lo stesso con gli altri pulsanti di azione che richiedono il turno del giocatore
        resolveEffectButton.setDisable(!isPlayerTurn);
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
                // Se non c'è una carta corrente, visualizza un messaggio o un'immagine predefinita
                System.out.println("Nessuna carta corrente da visualizzare");
                // Opzionale: impostare un'immagine predefinita per il dorso della carta
                // cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/GraficheGioco/cards/back.jpg")).toString()));
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

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Goodblocks Bank");
            alert.setHeaderText("Gestione dei Goodblocks");
            alert.setContentText("Qui puoi gestire i tuoi Goodblocks");
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
            // Chiama il server per pescare una carta
            virtualServer.pickCard();
            System.out.println("Richiesta di pesca carta inviata al server per il giocatore: " + model.getPlayerName());

            // Il server dovrebbe rispondere chiamando updateCurrCard in GalaxyTruckerGUI
            // che a sua volta chiamerà drawCard in questa classe
        } catch (IOException e) {
            System.err.println("Errore durante la comunicazione con il server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce la risoluzione dell'effetto della carta corrente
     * @param event l'evento di click sul bottone resolveEffect
     */
    @FXML
    public void resolveEffect(ActionEvent event) {
        System.out.println("Tentativo di risolvere l'effetto della carta");

        // Verifica che sia il turno del giocatore
        if (!model.getPlayerName().equals(model.getCurrPlayer())) {
            System.out.println("Non è il tuo turno!");
            return;
        }

        // Verifica che ci sia una carta da risolvere
        if (model.getCurrCard() == null) {
            System.out.println("Nessuna carta corrente da risolvere!");
            return;
        }

        try {
            // Chiama il server per risolvere l'effetto della carta
            InputCommand inputCommand = new InputCommand();
            inputCommand.setChoice(false);
            virtualServer.activateCard(inputCommand);
            System.out.println("Richiesta di risoluzione carta inviata al server");
        } catch (IOException e) {
            System.err.println("Errore durante la comunicazione con il server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'abbandono della partita
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

                    // Torna alla schermata principale
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                    // Ferma l'animazione dello sfondo
                    if (animatedBackground != null) {
                        animatedBackground.stopAnimation();
                    }
                } catch (IOException e) {
                    System.err.println("Errore durante l'abbandono della partita: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Gestisce la disconnessione dal server
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

                    // Ferma l'animazione dello sfondo
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
     * Mostra i tiles disponibili
     * @param event l'evento di click sul bottone showTilesButton
     */
    @FXML
    public void showTiles(ActionEvent event) {
        System.out.println("Mostra tessere disponibili");

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Tessere disponibili");
        alert.setHeaderText("Elenco delle tessere disponibili");

        if (model.getCoveredComponentTiles() != null && !model.getCoveredComponentTiles().isEmpty()) {
            alert.setContentText("Numero di tessere coperte: " + model.getCoveredComponentTiles().size());
        } else {
            alert.setContentText("Nessuna tessera disponibile.");
        }

        alert.showAndWait();
    }

    /**
     * Torna alla fase di costruzione della nave
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
                    try {
                        command.execute(model);
                        System.out.println("Comando eseguito: " + command.getName());
                        popupStage.close();
                    } catch (Exception ex) {
                        System.err.println("Errore durante l'esecuzione del comando: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
                commandsBox.getChildren().add(cmdButton);
            }

            // Aggiungi un pulsante Annulla
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
        if(shipboard.getNickname().equals(model.getPlayerName())) {
            //draw grid
            for(Node child : myShip.getChildren()) {
                int i = GridPane.getRowIndex(child) != null ? GridPane.getRowIndex(child) : 0;
                int j = GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0;
                Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(i, j);
                if (ct.isPresent() && ConditionVerifier.gridCoordinatesAreNotOutOfBound(i, j, model)) {
                    drawComponentTileImageForGrid((ImageView) child, ct.get().getPngName(), ct.get().getNumOfRotations());
                }
                else {
                    ((ImageView) child).setImage(null);
                }
            }
        }
        else {
            GridPane playerGrid = playerToShip.get(shipboard.getNickname());
            for(Node child : playerGrid.getChildren()) {
                int i = GridPane.getRowIndex(child) != null ? GridPane.getRowIndex(child) : 0;
                int j = GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0;
                Optional<ComponentTile> ct = shipboard.getComponentTileFromGrid(i, j);
                if (ct.isPresent() && ConditionVerifier.gridCoordinatesAreNotOutOfBound(i, j, model)) {
                    drawComponentTileImageForGrid((ImageView) child, ct.get().getPngName(), ct.get().getNumOfRotations());
                }
                else {
                    ((ImageView) child).setImage(null);
                }
            }
        }
    }

    private void drawComponentTileImageForGrid(ImageView imageView, String pngName, int numOfRotations) {
        Image image = new Image(getClass().getResource("/GraficheGioco/tiles/" + pngName).toExternalForm());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setRotate(90 * numOfRotations);
        imageView.setImage(image);
    }

    public void drawFlightboardInCardPhase(Flightboard flightboard) {

    }

    public void drawBankInCardPhase(Bank bank) {
    }
}
