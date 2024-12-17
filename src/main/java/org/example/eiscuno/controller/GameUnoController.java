package org.example.eiscuno.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.BasicAIPlayerStrategy;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController implements GameUno.GameEventListener {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private AnchorPane bottonMenu;
    @FXML
    private ImageView tableImageView;

    private Player humanPlayer;
    private Player machinePlayer;
    private BasicAIPlayerStrategy strategy = new BasicAIPlayerStrategy();
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        bottonMenu.setVisible(false);
        initVariables();
        this.gameUno.startGame();
        tableImageView.setImage(table.getCurrentCardOnTheTable().getImage());
        printCardsHumanPlayer();
        System.out.println("GAME START!");
        createUnoMachineThread();
    }

    /**
     * Creates and starts the threads for handling the human player's UNO card checking and the machine player's turn.
     * <p>
     * This method initializes two threads:
     * <ul>
     *     <li>A thread that monitors the human player's cards to check if they have only one card left ("UNO").</li>
     *     <li>A thread that controls the machine player's actions during the game, including playing cards.</li>
     * </ul>
     * The threads are started immediately after creation to handle their respective tasks concurrently.
     */
    private void createUnoMachineThread() {
        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), this.gameUno);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.deck,
                this.strategy, this, this.gameUno);
        threadPlayMachine.start();
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
        this.gameUno.setGameEventListener(this);
    }


    /**
     * Updates and displays the cards of the human player on the screen.
     * <p>
     * This method clears the current card grid, fetches the visible cards for the human player,
     * retrieves the current card on the table, and then renders the player's cards on the grid.
     * It ensures that the human player's cards are displayed correctly and that the current game state is reflected.
     */
    public void printCardsHumanPlayer() {
        clearCardGrid();
        Card[] visibleCards = fetchVisibleCardsForHumanPlayer();
        Card currentTableCard = fetchCurrentTableCard();

        renderPlayerCards(visibleCards, currentTableCard);
    }

    /**
     * Clears all the cards from the player's card grid.
     * <p>
     * This method removes all the card image views from the grid, effectively clearing the player's displayed hand.
     * It is typically used when the player's cards need to be updated or reset.
     */
    private void clearCardGrid() {
        this.gridPaneCardsPlayer.getChildren().clear();
    }

    /**
     * Fetches the visible cards for the human player based on the current game state.
     * <p>
     * This method retrieves the cards that are visible to the human player, starting from a specified position.
     * The cards are fetched using the current game state and the position from which to start showing the cards.
     *
     * @return an array of cards that are visible to the human player
     */
    private Card[] fetchVisibleCardsForHumanPlayer() {
        return this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);
    }

    /**
     * Fetches the current card on the table.
     * <p>
     * This method retrieves the card that is currently placed on the table, which is used to determine valid plays
     * for the player.
     *
     * @return the current card on the table, or null if no card is present
     */
    private Card fetchCurrentTableCard() {
        return this.table.getCurrentCardOnTheTable();
    }

    /**
     * Renders the player's cards on the grid and attaches click handlers to each card.
     * <p>
     * This method iterates over the provided array of cards, creating an image view for each card, attaching a click handler,
     * and adding the card's image view to the player's card grid at the appropriate position.
     *
     * @param cards an array of cards to be rendered for the player
     * @param currentTableCard the card currently on the table, used to check if the player can play the cards
     */
    private void renderPlayerCards(Card[] cards, Card currentTableCard) {
        for (int i = 0; i < cards.length; i++) {
            Card card = cards[i];
            ImageView cardImageView = card.getCard();
            attachCardClickHandler(cardImageView, card, currentTableCard);
            addCardToGrid(cardImageView, i);
        }
    }

    /**
     * Attaches a click handler to the specified card image view.
     * <p>
     * This method sets up an event listener for mouse clicks on the given card's image view. When the card is clicked,
     * it triggers the {@link #handleCardClick(Card, Card)} method to check if the card can be played based on the current game state.
     *
     * @param cardImageView the image view representing the card to which the click handler will be attached
     * @param card the card associated with the image view
     * @param currentTableCard the card currently on the table, used to check if the clicked card can be played
     */
    private void attachCardClickHandler(ImageView cardImageView, Card card, Card currentTableCard) {
        cardImageView.setOnMouseClicked(event -> handleCardClick(card, currentTableCard));
    }

    /**
     * Handles the click event of a card by the human player.
     * <p>
     * This method checks if the clicked card is valid to play based on the current game state. If the card is valid,
     * it processes the card play; otherwise, it prints a message indicating that the card is not valid.
     *
     * @param card the card that was clicked by the human player
     * @param currentTableCard the card currently on the table, used to check if the clicked card can be played
     */
    private void handleCardClick(Card card, Card currentTableCard) {
        if(gameUno.canPlayCard(card)) {
            processValidCardPlay(card);

        }
        else {
            System.out.println("Card is not valid");
        }
    }


    /**
     * Processes a valid card play made by the human player.
     * <p>
     * This method performs the necessary actions when the human player plays a valid card:
     * <ul>
     *     <li>The card is played in the game using the {@link GameUno#playCard(Card)} method.</li>
     *     <li>The card's image is updated on the table.</li>
     *     <li>The card is removed from the human player's hand.</li>
     *     <li>The game over condition is checked.</li>
     *     <li>If the current player is the machine player, it signals that the AI player has played.</li>
     *     <li>The human player's cards are printed again to reflect the updated hand.</li>
     * </ul>
     *
     * @param card the valid card to be played
     */
    private void processValidCardPlay(Card card) {
        gameUno.playCard(card);
        tableImageView.setImage(card.getImage());
        humanPlayer.removeCard(findPosCardsHumanPlayer(card));

        checkGameOver();
        if (gameUno.getCurrentPlayer().equals(machinePlayer)) {
            threadPlayMachine.setHasPlayerPlayed(true);
        }
        printCardsHumanPlayer();
    }

    /**
     * Adds a card image view to the player's card grid at the specified position.
     * <p>
     * This method places the given card image view into the grid at the specified position (column) in the first row.
     *
     * @param cardImageView the image view representing the card to be added to the grid
     * @param position the column position in the grid where the card image view should be placed
     */
    private void addCardToGrid(ImageView cardImageView, int position) {
        this.gridPaneCardsPlayer.add(cardImageView, position, 0);
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        if (threadPlayMachine.getHasPlayerPlayed()) {
            if (!deck.isEmpty()) {
                Card playerNewCard = deck.takeCard();
                humanPlayer.addCard(playerNewCard);
                System.out.println("Added Player Card!: " + playerNewCard.getColor() + "/" + playerNewCard.getValue());
                System.out.println("-----------------------");
                printCardsHumanPlayer();
            }
            else {
                System.out.println("Deck is empty!");

                checkGameOver();

            }
            gameUno.cardTaken();
            threadPlayMachine.setHasPlayerPlayed(true);
        }
        else {
            System.out.println("Not Player's Turn");
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
    gameUno.haveSungOne("HUMAN_PLAYER");
    }

    /**
     * Checks if the game is over and determines the winner.
     * <p>
     * This method evaluates the current state of the game to check if either the human player or the
     * machine player has won, or if the game is a tie. It considers the following conditions:
     * <ul>
     *     <li>If the human player has no cards left, the human player wins.</li>
     *     <li>If the machine player has no cards left, the machine player wins.</li>
     *     <li>If the deck is empty, the player with fewer cards wins. If both players have the same number of cards, the game is a tie.</li>
     * </ul>
     * If a winner is determined, it triggers the display of the game over dialog with the appropriate message.
     */
    public void checkGameOver(){
        String winner = null;
        if(humanPlayer.getCardsPlayer().isEmpty()){
            winner = "Felicidades! Has ganado la partida";
        } else if (machinePlayer.getCardsPlayer().isEmpty()) {
            winner = "La máquina a ganado la partida.";
            
        } else if (deck.isEmpty()) {
            if(humanPlayer.getCardsPlayer().size() < machinePlayer.getCardsPlayer().size()){
                winner = "Felicidades! Has ganado la partida";
            } else if (machinePlayer.getCardsPlayer().size() < humanPlayer.getCardsPlayer().size()) {
                winner = "La máquina a ganado la partida.";
                
            } else {
                winner = "Empate! Tienen el mismo numéro de cartas.";
            }

        }
        if(winner != null){
            showGameOverDialog(winner);

        }
    }

    /**
     * Displays a game over dialog with options to restart the game or exit.
     * <p>
     * This method is called when the game ends. It shows a confirmation dialog with a message and two
     * buttons: one to restart the game and another to exit the game. The behavior is determined by the
     * player's selection.
     *
     * @param message the message to be displayed in the game over dialog
     */
    private void showGameOverDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Fin del Juego");
            alert.setHeaderText(message);
            alert.setContentText("¿Qué deseas hacer?");

            ButtonType restartButton = new ButtonType("Reiniciar Juego");
            ButtonType exitButton = new ButtonType("Salir");

            alert.getButtonTypes().setAll(restartButton, exitButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == restartButton) {
                    restartGame();
                } else {
                    Platform.exit();
                }
            });
        });
    }

    /**
     * Restarts the game by resetting necessary variables and starting a new game.
     * <p>
     * This method is called when the player chooses to restart the game from the game over dialog.
     * It stops any ongoing threads, reinitializes variables, and starts a new game session.
     */
    private void restartGame() {
        if (threadPlayMachine != null) {
            threadPlayMachine.interrupt();
        }
        if (threadSingUNOMachine != null) {
            Thread.currentThread().interrupt();
        }

        initVariables();
        this.gameUno.startGame();
        tableImageView.setImage(table.getCurrentCardOnTheTable().getImage());
        printCardsHumanPlayer();

        createUnoMachineThread();
    }

    /**
     * Called when a wild card is played in the game.
     * <p>
     * This method makes the button menu visible when a wild card is played during the game.
     */
    @Override
    public void onWildCardPlayed() {
        bottonMenu.setVisible(true);
    }


    /**
     * Sets the game color to red and hides the button menu.
     * <p>
     * This method is called when the player selects the "RED" color. It sets the current game color to red
     * and hides the button menu.
     *
     * @param actionEvent the event triggered by the player's action (e.g., clicking a button)
     */
    public void setColorRed(ActionEvent actionEvent) {
        gameUno.setGameColor("RED");
        bottonMenu.setVisible(false);
    }

    /**
     * Sets the game color to blue and hides the button menu.
     * <p>
     * This method is called when the player selects the "BLUE" color. It sets the current game color to blue
     * and hides the button menu.
     *
     * @param actionEvent the event triggered by the player's action (e.g., clicking a button)
     */
    public void setColorBlue(ActionEvent actionEvent) {
        gameUno.setGameColor("BLUE");
        bottonMenu.setVisible(false);
    }

    /**
     * Sets the game color to green and hides the button menu.
     * <p>
     * This method is called when the player selects the "GREEN" color. It sets the current game color to green
     * and hides the button menu.
     *
     * @param actionEvent the event triggered by the player's action (e.g., clicking a button)
     */
    public void setColorGreen(ActionEvent actionEvent) {
        gameUno.setGameColor("GREEN");
        bottonMenu.setVisible(false);
    }

    /**
     * Sets the game color to yellow and hides the button menu.
     * <p>
     * This method is called when the player selects the "YELLOW" color. It sets the current game color to yellow
     * and hides the button menu.
     *
     * @param actionEvent the event triggered by the player's action (e.g., clicking a button)
     */
    public void setColorYellow(ActionEvent actionEvent) {
        gameUno.setGameColor("YELLOW");
        bottonMenu.setVisible(false);
    }

}
