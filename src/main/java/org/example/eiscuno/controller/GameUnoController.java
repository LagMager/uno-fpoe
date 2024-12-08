package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    private Player humanPlayer;
    private Player machinePlayer;
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
        initVariables();
        this.gameUno.startGame();
        tableImageView.setImage(table.getCurrentCardOnTheTable().getImage());
        printCardsHumanPlayer();
        System.out.println("player can play");
        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.deck);
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
    }


    // Entry point for updating the human player's visible cards
    private void printCardsHumanPlayer() {
        clearCardGrid();
        Card[] visibleCards = fetchVisibleCardsForHumanPlayer();
        Card currentTableCard = fetchCurrentTableCard();

        renderPlayerCards(visibleCards, currentTableCard);
    }

    // Clears the grid displaying the player's cards
    private void clearCardGrid() {
        this.gridPaneCardsPlayer.getChildren().clear();
    }

    // Fetches the currently visible cards for the human player
    private Card[] fetchVisibleCardsForHumanPlayer() {
        return this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);
    }

    // Fetches the current card on the table
    private Card fetchCurrentTableCard() {
        return this.table.getCurrentCardOnTheTable();
    }

    // Renders the player's visible cards and attaches click event handlers
    private void renderPlayerCards(Card[] cards, Card currentTableCard) {
        for (int i = 0; i < cards.length; i++) {
            Card card = cards[i];
            ImageView cardImageView = card.getCard();
            attachCardClickHandler(cardImageView, card, currentTableCard);
            addCardToGrid(cardImageView, i);
        }
    }

    // Attaches a click event handler to the card
    private void attachCardClickHandler(ImageView cardImageView, Card card, Card currentTableCard) {
        cardImageView.setOnMouseClicked(event -> handleCardClick(card, currentTableCard));
    }

    // Handles the logic for when a player clicks a card
    private void handleCardClick(Card card, Card currentTableCard) {
        if (this.gameUno.validCard(card, currentTableCard) && !threadPlayMachine.getHasPlayerPlayed()) {
            processValidCardPlay(card);
        }
    }

    // Processes the actions for playing a valid card
    private void processValidCardPlay(Card card) {
        gameUno.playCard(card);
        tableImageView.setImage(card.getImage());
        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
        threadPlayMachine.setHasPlayerPlayed(true);
        printCardsHumanPlayer(); // Refresh the view for the updated hand
    }

    // Adds a card to the player's grid at a specific position
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
        if (!threadPlayMachine.getHasPlayerPlayed()) {
            if (!deck.isEmpty()) {
                Card playerNewCard = deck.takeCard();
                humanPlayer.addCard(playerNewCard);
                System.out.println("Added Player Card!: " + playerNewCard.getColor() + "/" + playerNewCard.getValue());
                printCardsHumanPlayer();
            }
            else {
                System.out.println("Deck is empty!");
            }
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
        // Implement logic to handle Uno event here
    }
}