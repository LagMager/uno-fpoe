package org.example.eiscuno.model.deck;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit tests for the {@link Deck} class.
 * This class tests various functionalities of a deck of cards including initialization,
 * handling card removal, and throwing exceptions when the deck is empty.
 */
@DisplayName("Deck Tests")
class DeckTest {

    /**
     * Test to ensure the deck is properly initialized with the correct number of cards.
     * This test checks that the deck is not empty after initialization.
     */
    @Test
    @DisplayName("Should initialize deck with correct number of cards")
    void shouldInitializeDeckWithCorrectCards() {
        Platform.startup(()->{});

        Deck deck = new Deck();
        assertFalse(deck.isEmpty(), "Deck should not be empty after initialization");
    }

    /**
     * Test to check that the deck properly handles the action of taking a card.
     * This test verifies that a valid card is returned when a card is taken from the deck.
     */
    @Test
    @DisplayName("Should properly handle taking cards")
    void shouldHandleTakingCards() {
        Deck deck = new Deck();
        Card card = deck.takeCard();

        assertNotNull(card, "Should return a valid card");
        assertTrue(card instanceof Card, "Should return a Card object");
    }

    /**
     * Test to check that an exception is thrown when attempting to take a card
     * from an empty deck.
     * This test ensures that the {@link IllegalStateException} is thrown when the deck is empty.
     */
    @Test
    @DisplayName("Should throw exception when deck is empty")
    void shouldThrowExceptionWhenEmpty() {
        Deck deck = new Deck();

        // Empty the deck
        while (!deck.isEmpty()) {
            deck.takeCard();
        }

        assertThrows(IllegalStateException.class, deck::takeCard,
                "Should throw IllegalStateException when taking card from empty deck");
    }
}
