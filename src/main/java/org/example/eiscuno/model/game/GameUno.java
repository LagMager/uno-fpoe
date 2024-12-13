package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

/**
 * Represents a game of Uno.
 * This class manages the game logic and interactions between players, deck, and the table.
 */
public class GameUno implements IGameUno {

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private boolean isReversed;
    private Player currentPlayer;
    private Player nextPlayer;

    private static final String SKIP = "SKIP";
    private static final String REVERSE = "REVERSE";
    private static final String DRAW_TWO = "DRAW_TWO";
    private static final String WILD = "WILD";
    private static final String WILD_DRAW_FOUR = "WILD_DRAW_FOUR";

    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        this.isReversed = false;
        this.currentPlayer = humanPlayer;
        this.nextPlayer = machinePlayer;
    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }
    }

    /**
     * Allows a player to draw a specified number of cards from the deck.
     *
     * @param player        The player who will draw cards.
     * @param numberOfCards The number of cards to draw.
     */
    @Override
    public void eatCard(Player player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }
    }

    /**
     * Places a card on the table during the game.
     *
     * @param card The card to be placed on the table.
     */
    @Override
    public void playCard(Card card) {
        if(!canPlayCard(card)) {
            return;
        }

        table.addCardOnTheTable(card);
        handleSpecialCard(card);
        switchPlayers();
    }

    public boolean canPlayCard(Card card) {
        try {
            Card topCard = table.getCurrentCardOnTheTable();
            return card.canPlayOn(topCard);
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private void handleSpecialCard(Card card) {
        String cardType = card.getCardType();
        if(cardType == null) return;

        switch (cardType){
            case SKIP:
                switchPlayers();
                break;
            case REVERSE:
                isReversed = !isReversed;
                Player temp = nextPlayer;
                nextPlayer = currentPlayer;
                currentPlayer = temp;
                break;
            case DRAW_TWO:
                eatCard(nextPlayer, 2);
                switchPlayers();
                break;
        }
    }

    private void switchPlayers() {
        Player temp = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = temp;
    }

    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            machinePlayer.addCard(this.deck.takeCard());
        } else {
            humanPlayer.addCard(this.deck.takeCard());
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the deck is empty, indicating the game is over; otherwise, false.
     */
    @Override
    public Boolean isGameOver() {
        return humanPlayer.getCardsPlayer().isEmpty() ||
                machinePlayer.getCardsPlayer().isEmpty() ||
                deck.isEmpty();
    }

    public Player getCurrentPlayer() {return currentPlayer;}

    public Player getNextPlayer() {return nextPlayer;}

}
