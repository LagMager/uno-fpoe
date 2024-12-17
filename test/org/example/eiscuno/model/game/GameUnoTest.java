package org.example.eiscuno.model.game;

import static org.junit.jupiter.api.Assertions.*;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Unit tests for the {@link GameUno} class.
 * This class tests various functionalities of the UNO game logic, including game initialization,
 * card playing rules, game state (such as detecting when the game is over),
 * and handling special UNO declarations.
 */
@DisplayName("GameUno Tests")
class GameUnoTest {

    private GameUno gameUno;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;

    /**
     * Setup method to initialize the game, players, deck, and table before each test.
     * This ensures that each test starts with a fresh game state.
     */
    @BeforeEach
    void setUp() {
        humanPlayer = new Player("HUMAN_PLAYER");
        machinePlayer = new Player("MACHINE_PLAYER");
        deck = new Deck();
        table = new Table();
        gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);
    }

    /**
     * Nested test class for game initialization-related tests.
     * Verifies that the game starts correctly, with the right number of cards for each player
     * and a card on the table.
     */
    @Nested
    @DisplayName("Game Initialization Tests")
    class GameInitializationTests {

        /**
         * Test to ensure that the game initializes with the correct number of cards for each player.
         * Both the human player and the machine player should receive 5 cards.
         */
        @Test
        @DisplayName("Should initialize game with correct number of cards")
        void shouldInitializeGameWithCorrectNumberOfCards() {
            gameUno.startGame();

            assertEquals(5, humanPlayer.getCardsPlayer().size(), "Human player should have 5 cards");
            assertEquals(5, machinePlayer.getCardsPlayer().size(), "Machine player should have 5 cards");
            assertNotNull(table.getCurrentCardOnTheTable(), "Table should have a card");
        }
    }

    /**
     * Nested test class for card playing-related tests.
     * Verifies that cards can be played according to the rules, and checks whether invalid cards are rejected.
     */
    @Nested
    @DisplayName("Card Playing Tests")
    class CardPlayingTests {

        /**
         * Test to ensure that a valid card can be played by a player.
         * A valid card must either match the color or value of the current card on the table,
         * or be a special card such as a wild card.
         */
        @Test
        @DisplayName("Should allow playing a valid card")
        void shouldAllowPlayingValidCard() {
            gameUno.startGame();
            Card tableCard = table.getCurrentCardOnTheTable();
            Card validCard = null;

            // Find a valid card in human player's hand
            for (Card card : humanPlayer.getCardsPlayer()) {
                if (card.getColor().equals(tableCard.getColor()) ||
                        card.getValue().equals(tableCard.getValue()) ||
                        "WILD".equals(card.getCardType()) ||
                        "WILD_DRAW_FOUR".equals(card.getCardType())) {
                    validCard = card;
                    break;
                }
            }

            if (validCard != null) {
                assertTrue(gameUno.canPlayCard(validCard), "Should be able to play a matching card");
            }
        }

        /**
         * Test to ensure that an invalid card cannot be played.
         * An invalid card does not match the color or value of the current card on the table
         * and is not a wild card.
         */
        @Test
        @DisplayName("Should not allow playing an invalid card")
        void shouldNotAllowPlayingInvalidCard() {
            gameUno.startGame();
            Card tableCard = table.getCurrentCardOnTheTable();
            Card invalidCard = null;

            // Find an invalid card in human player's hand
            for (Card card : humanPlayer.getCardsPlayer()) {
                if (!card.getColor().equals(tableCard.getColor()) &&
                        !card.getValue().equals(tableCard.getValue()) &&
                        !"WILD".equals(card.getCardType()) &&
                        !"WILD_DRAW_FOUR".equals(card.getCardType())) {
                    invalidCard = card;
                    break;
                }
            }

            if (invalidCard != null) {
                assertFalse(gameUno.canPlayCard(invalidCard), "Should not be able to play a non-matching card");
            }
        }
    }

    /**
     * Nested test class for game state-related tests.
     * Verifies the game state, including game-over conditions and handling of UNO declarations.
     */
    @Nested
    @DisplayName("Game State Tests")
    class GameStateTests {

        /**
         * Test to verify that the game correctly identifies game over conditions.
         * The game should be over when a player has no cards left.
         */
        @Test
        @DisplayName("Should correctly identify game over conditions")
        void shouldIdentifyGameOverConditions() {
            gameUno.startGame();

            // Initially game should not be over
            assertFalse(gameUno.isGameOver(), "Game should not be over at start");

            // Empty player's hand to simulate win condition
            humanPlayer.getCardsPlayer().clear();
            assertTrue(gameUno.isGameOver(), "Game should be over when player has no cards");
        }

        /**
         * Test to verify that the game correctly handles UNO declarations.
         * If a player fails to declare UNO when they have only one card left, they should receive a penalty card.
         */
        @Test
        @DisplayName("Should handle UNO declarations correctly")
        void shouldHandleUnoDeclarations() {
            gameUno.startGame();

            // Leave only one card for human player
            while (humanPlayer.getCardsPlayer().size() > 1) {
                humanPlayer.getCardsPlayer().remove(0);
            }

            // Machine declares UNO
            gameUno.haveSungOne("MACHINE_PLAYER");

            // Human player should have received a penalty card
            assertTrue(humanPlayer.getCardsPlayer().size() > 1,
                    "Human player should receive a penalty card when not declaring UNO");
        }
    }
}
