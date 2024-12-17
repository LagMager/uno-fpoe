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



@DisplayName("GameUno Tests")
class GameUnoTest {
    private GameUno gameUno;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;

    @BeforeEach
    void setUp() {
        humanPlayer = new Player("HUMAN_PLAYER");
        machinePlayer = new Player("MACHINE_PLAYER");
        deck = new Deck();
        table = new Table();
        gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);
    }

    @Nested
    @DisplayName("Game Initialization Tests")
    class GameInitializationTests {
        @Test
        @DisplayName("Should initialize game with correct number of cards")
        void shouldInitializeGameWithCorrectNumberOfCards() {
            gameUno.startGame();

            assertEquals(5, humanPlayer.getCardsPlayer().size(), "Human player should have 5 cards");
            assertEquals(5, machinePlayer.getCardsPlayer().size(), "Machine player should have 5 cards");
            assertNotNull(table.getCurrentCardOnTheTable(), "Table should have a card");
        }
    }

    @Nested
    @DisplayName("Card Playing Tests")
    class CardPlayingTests {
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

    @Nested
    @DisplayName("Game State Tests")
    class GameStateTests {
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