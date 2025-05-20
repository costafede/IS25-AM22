package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyStarsEffect;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Client.View.GameType;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    private List<String> players;
    private List<ImageView> playersShipImages;

    @FXML
    private void initialize() {
        setup(null, GalaxyTruckerGUI.getClientModel(), GalaxyTruckerGUI.getPrimaryStage() ,GalaxyTruckerGUI.getVirtualServer());
        players = model.getShipboards().keySet().stream().toList();
        playersShipImages = List.of(myShipImage, player1ShipImage, player2ShipImage, player3ShipImage);
        if (model.getGametype() == GameType.TUTORIAL) {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/BlueBackground.png")).toString()));
            setShipboardImagesTutorial(players.size());
            tutorialFlightboardPane.setVisible(true);
            level2FlightboardPane.setVisible(false);

        } else {
            background.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/PurpleBackground.png")).toString()));
            setShipboardImagesLevel2(players.size());
            tutorialFlightboardPane.setVisible(false);
            level2FlightboardPane.setVisible(true);
        }


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

    private void setShipboardImagesLevel2(int playersNumber) {
        // Prima imposta la visibilità delle navi in base al numero di giocatori
        setShipboardsVisibility(playersNumber);

        // Assegna le immagini solo alle navi che saranno visibili
        int count = 0;
        for (int i = 0; i < playersNumber && i < playersShipImages.size(); i++) {
            ImageView shipImage = playersShipImages.get(i);
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
        for (int i = 0; i < playersNumber && i < playersShipImages.size(); i++) {
            ImageView shipImage = playersShipImages.get(i);
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
        List<String> otherPlayers = players.stream()
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
    public void drawShip() {
        // Implementazione per disegnare la nave del giocatore
        try {
            // Aggiorna la visualizzazione della nave in base al modello
            System.out.println("Disegno della nave per il giocatore: " + model.getPlayerName());
        } catch (Exception e) {
            System.err.println("Errore durante il disegno della nave: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Aggiorna tutti gli elementi della scena in base allo stato attuale del gioco
     */
    public void drawScene() {
        try {
            // Aggiorna le visualizzazioni delle navi
            drawShip();

            // Aggiorna la carta visualizzata
            updateCardDisplay();

            // Aggiorna i tabelloni
            updateCardboards();

            // Aggiorna le informazioni dei giocatori
            updatePlayerInfo();

            // Aggiorna lo stato dei pulsanti in base al turno corrente
            updateButtonsState();
        } catch(Exception e) {
            System.err.println("Errore durante l'aggiornamento della scena: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Aggiorna lo stato dei pulsanti in base al turno corrente
     * Disabilita i pulsanti quando non è il turno del giocatore
     */
    private void updateButtonsState() {
        boolean isPlayerTurn = model.getPlayerName().equals(model.getCurrPlayer());

        // Il pulsante pickCard è attivo solo se è il turno del giocatore
        pickCardButton.setDisable(!isPlayerTurn);

        // Si potrebbe fare lo stesso con gli altri pulsanti di azione che richiedono il turno del giocatore
        resolveEffectButton.setDisable(!isPlayerTurn);
    }

    /**
     * Aggiorna la carta visualizzata
     */
    private void updateCardDisplay() {
        // Implementazione per aggiornare la carta visualizzata in base al modello
        System.out.println("Aggiornamento della carta visualizzata");
    }

    /**
     * Aggiorna i tabelloni visualizzati
     */
    private void updateCardboards() {
        // Implementazione per aggiornare i tabelloni in base allo stato del gioco
        System.out.println("Aggiornamento dei tabelloni di gioco");
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
     * Gestisce l'evento quando il giocatore pesca una carta
     */
    @FXML
    public void pickCard(ActionEvent event) {
        try {
            // Implementazione della logica per pescare una carta
            System.out.println("Tentativo di pescare una carta");
            virtualServer.pickCard();

            // Aggiorna la carta dopo averla pescata
            drawCard();
        } catch (IOException e) {
            showErrorAlert("Errore Pesca Carta", "Impossibile pescare la carta: " + e.getMessage());
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
            showErrorAlert("Errore Banca", "Impossibile aprire la banca: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento quando il giocatore lancia i dadi
     */
    @FXML
    public void rollDices(ActionEvent event) {
        try {
            System.out.println("Lancio dei dadi");

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Dices");
            alert.setHeaderText("Lancio dei dadi");
            alert.setContentText("Hai lanciato i dadi!");
            alert.showAndWait();
        } catch (Exception e) {
            showErrorAlert("Errore Dadi", "Impossibile lanciare i dadi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento quando il giocatore abbandona la partita
     */
    @FXML
    public void abandonGame(ActionEvent event) {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Abbandona Partita");
            alert.setHeaderText("Sei sicuro di voler abbandonare la partita?");
            alert.setContentText("Questa azione non può essere annullata.");

            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    try {
                        System.out.println("Abbandono della partita da parte del giocatore: " + model.getPlayerName());
                        virtualServer.playerAbandons(model.getPlayerName());

                        // Torna al menu principale
                        goToMainMenu(event);
                    } catch (IOException e) {
                        showErrorAlert("Errore Abbandono", "Impossibile abbandonare la partita: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            showErrorAlert("Errore", "Si è verificato un errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento quando il giocatore si disconnette
     */
    @FXML
    public void disconnect(ActionEvent event) {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Disconnessione");
            alert.setHeaderText("Sei sicuro di volerti disconnettere?");
            alert.setContentText("La connessione con il server verrà chiusa.");

            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    try {
                        System.out.println("Disconnessione del giocatore: " + model.getPlayerName());
                        virtualServer.disconnect();

                        // Esci dall'applicazione
                        System.exit(0);
                    } catch (IOException e) {
                        showErrorAlert("Errore Disconnessione", "Impossibile disconnettersi: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            showErrorAlert("Errore", "Si è verificato un errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento quando il giocatore risolve l'effetto di una carta
     */
    @FXML
    public void resolveEffect(ActionEvent event) {
        try {
            System.out.println("Risoluzione dell'effetto della carta");
            InputCommand command = new InputCommand();
            command.setChoice(false);
            virtualServer.activateCard(command);
            ///  TODO

            // Aggiorna la scena dopo aver risolto l'effetto
            drawScene();
        } catch (IOException e) {
            showErrorAlert("Errore Effetto", "Impossibile risolvere l'effetto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento quando il giocatore vuole vedere le tessere disponibili
     */
    @FXML
    public void showTiles(ActionEvent event) {
        try {
            System.out.println("Visualizzazione delle tessere disponibili");

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Tessere Disponibili");
            alert.setHeaderText("Visualizzazione Tessere");
            alert.setContentText("Qui puoi vedere le tessere disponibili");
            alert.showAndWait();
        } catch (Exception e) {
            showErrorAlert("Errore Visualizzazione", "Impossibile visualizzare le tessere: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento quando il giocatore capovolge la clessidra
     */
    @FXML
    public void flipHourglass(ActionEvent event) {
        try {
            System.out.println("Clessidra capovolta");

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Clessidra");
            alert.setHeaderText("Clessidra Capovolta");
            alert.setContentText("Il tempo è iniziato!");
            alert.showAndWait();
        } catch (Exception e) {
            showErrorAlert("Errore Clessidra", "Impossibile capovolgere la clessidra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Torna al menu principale
     */
    private void goToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/MainMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Ferma l'animazione dello sfondo prima di cambiare scena
            if (animatedBackground != null) {
                animatedBackground.stopAnimation();
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Errore Menu", "Impossibile tornare al menu principale: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Torna alla fase di costruzione della nave
     */
    @FXML
    public void switchToBuildingShip(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/BuildingShip.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Ferma l'animazione dello sfondo prima di cambiare scena
            if (animatedBackground != null) {
                animatedBackground.stopAnimation();
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Errore Cambio Fase", "Impossibile tornare alla fase di costruzione: " + e.getMessage());
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
}
