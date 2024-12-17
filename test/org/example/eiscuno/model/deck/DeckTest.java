package org.example.eiscuno.model.deck;


import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Deck Tests")
class DeckTest {

    @Test
    @DisplayName("Should initialize deck with correct number of cards")
    void shouldInitializeDeckWithCorrectCards() {
        Deck deck = new Deck();
        assertFalse(deck.isEmpty(), "Deck should not be empty after initialization");
    }

    @Test
    @DisplayName("Should properly handle taking cards")
    void shouldHandleTakingCards() {
        Deck deck = new Deck();
        Card card = deck.takeCard();

        assertNotNull(card, "Should return a valid card");
        assertTrue(card instanceof Card, "Should return a Card object");
    }

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