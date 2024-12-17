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
    private String gameColor;
    private GameEventListener gameEventListener;
    private static final String NUMBER = "NUMBER";
    private static final String SKIP = "SKIP";
    private static final String REVERSE = "REVERSE";
    private static final String DRAW_TWO = "DRAW_TWO";
    private static final String WILD = "WILD";
    private static final String WILD_DRAW_FOUR = "WILD_DRAW_FOUR";

    /**
     * Internal interface for handling game events in the {@link GameUno} class.
     * <p>
     * This interface defines methods that respond to specific game events, such as
     * when a wild card is played. It is intended to be implemented by classes that
     * need to react to game-specific actions within the {@link GameUno} context.
     */
    public interface GameEventListener{

        /**
         * Called when a wild card is played in the game.
         * <p>
         * This method is triggered whenever a wild card is played, and implementing
         * classes should define the appropriate action to take when this event occurs.
         */
        void onWildCardPlayed();
    }

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
        Card firstCard = this.deck.takeCard();
        table.addCardOnTheTable(firstCard);
        setGameColor(firstCard.getColor());
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

        setGameColor(card.getColor());
        table.addCardOnTheTable(card);
        handleCards(card);
        System.out.println("Played card: " + card.getColor() + "/" + card.getValue());
    }

    /**
     * Determines if a given card can be played based on the current card on the table.
     * <p>
     * This method checks if the card can be played according to the rules of Uno. It handles the case where the table
     * has no current card (e.g., at the start of the game).
     * </p>
     *
     * @param card The card that the player wants to play.
     * @return true if the card can be played, false otherwise.
     */
    public boolean canPlayCard(Card card) {
        Card topCard;
        try {
            topCard = table.getCurrentCardOnTheTable();
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
        return Card.CardValidator.canPlayCard(card, topCard, gameColor);
    }

    /**
     * Switches to the next player after a card has been taken.
     * <p>
     * This method is called when a player takes a card, and it triggers the switching
     * of players to ensure the game progresses. The player turn is updated accordingly.
     */
    public void cardTaken(){
        switchPlayers();
    }

    /**
     * Handles the effects of the special cards when they are played.
     * <p>
     * This method executes the special actions associated with each type of card. These actions include skipping the
     * next player, reversing the play order, drawing cards, and switching players.
     * </p>
     *
     * @param card The card that was played.
     */
    private void handleCards(Card card) {
        String cardType = card.getCardType();
        if (cardType == null) {
            return;
        }

        switch (cardType) {
            case NUMBER:
                switchPlayers();
                break;
            case SKIP:
                System.out.println("SKIPPED " + nextPlayer.getTypePlayer());
                break;
            case REVERSE:
                isReversed = !isReversed;
                Player temp = nextPlayer;
                nextPlayer = currentPlayer;
                currentPlayer = temp;
                System.out.println(currentPlayer.getTypePlayer() + "'s Turn");
                break;
            case DRAW_TWO:
                eatCard(nextPlayer, 2);
                System.out.println(nextPlayer.getTypePlayer() + " draws 2 cards!!");
                switchPlayers();
                break;
            case WILD_DRAW_FOUR:
                System.out.println(nextPlayer.getCardsPlayer().size());
                eatCard(nextPlayer, 4);
                System.out.println("Now you draw 4!");
                if (gameEventListener != null) {
                    gameEventListener.onWildCardPlayed();
                }
                System.out.println(nextPlayer.getCardsPlayer().size());
                break;
            case WILD:
                if (gameEventListener != null) {
                    gameEventListener.onWildCardPlayed();
                }
                break;
        }
    }

    /**
     * Switches the current player and the next player.
     * <p>
     * This method is called when a player's turn is over. It updates the current player and the next player, ensuring
     * that the game proceeds to the next player in the sequence.
     * </p>
     */
    private void switchPlayers() {
        Player temp = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = temp;
        System.out.println(currentPlayer.getTypePlayer() + "'s Turn");
    }


    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER") && (machinePlayer.getCardsPlayer().size() == 1)) {
            machinePlayer.addCard(this.deck.takeCard());
        } else if (playerWhoSang.equals("MACHINE_PLAYER") && (humanPlayer.getCardsPlayer().size() == 1)) {
            humanPlayer.addCard(this.deck.takeCard());
        }

    } //needs to be implemented

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


    /**
     * Retrieves the current player in the game.
     * <p>
     * This method returns the {@link Player} object representing the player who is
     * currently taking their turn in the game.
     *
     * @return The {@link Player} representing the current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Retrieves the next player in the game.
     * <p>
     * This method returns the {@link Player} object representing the player who will
     * take their turn after the current player.
     *
     * @return The {@link Player} representing the next player.
     */
    public Player getNextPlayer() {return nextPlayer;}

    /**
     * Sets the game color.
     * <p>
     * This method updates the {@code gameColor} variable to the specified color,
     * which represents the current color used in the game (e.g., for card colors).
     *
     * @param color The {@code String} representing the new game color.
     *              This value is expected to be a valid color name, such as "RED", "GREEN", etc.
     */
    public void setGameColor(String color) {
        gameColor = color;
    }

    /**
     * Sets the game event listener.
     * <p>
     * This method assigns a {@link GameEventListener} to handle game-related events.
     * The listener will be used to respond to various game actions or changes.
     *
     * @param gameEventListener The {@link GameEventListener} that will handle game events.
     *                           It is expected to implement the appropriate methods for handling events.
     */
    public void setGameEventListener(GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
    }
}
