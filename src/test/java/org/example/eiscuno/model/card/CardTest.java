package org.example.eiscuno.model.card;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit tests for the {@link Card} class.
 * This class tests various functionalities of a card in the card game, including card creation,
 * validation of card properties, and checking if cards match based on game rules.
 */
@DisplayName("Card Tests")
class CardTest {

    /**
     * Test to ensure that a card is created with the correct properties.
     * This test checks that the card's value, color, type, image, and card view are correctly set.
     */
    @Test
    @DisplayName("Should create card with correct properties")
    void shouldCreateCardWithCorrectProperties() {
        String url = "/org/example/eiscuno/cards-uno/0_red.png";
        String value = "0";
        String color = "RED";
        String cardType = "NUMBER";

        Card card = new Card(url, value, color, cardType);

        assertEquals(value, card.getValue(), "Card should have correct value");
        assertEquals(color, card.getColor(), "Card should have correct color");
        assertEquals(cardType, card.getCardType(), "Card should have correct type");
        assertNotNull(card.getCard(), "Card should have an ImageView");
        assertNotNull(card.getImage(), "Card should have an Image");
    }

    /**
     * Test to validate that card matching works correctly.
     * This test checks if the game rules are respected, including matching by value or color,
     * and if wild cards can be played regardless of the current card.
     */
    @Test
    @DisplayName("Should validate card matching correctly")
    void shouldValidateCardMatchingCorrectly() {
        Platform.startup(()->{});
        Card card1 = new Card("/org/example/eiscuno/cards-uno/0_red.png", "0", "RED", "NUMBER");
        Card card2 = new Card("/org/example/eiscuno/cards-uno/0_blue.png", "0", "BLUE", "NUMBER");
        Card card3 = new Card("/org/example/eiscuno/cards-uno/1_red.png", "1", "RED", "NUMBER");
        Card wildCard = new Card("/org/example/eiscuno/cards-uno/wild.png", "WILD", "WILD", "WILD");

        // Same number, different color
        assertTrue(Card.CardValidator.canPlayCard(card2, card1, null),
                "Should allow playing card with same number");

        // Same color, different number
        assertTrue(Card.CardValidator.canPlayCard(card3, card1, null),
                "Should allow playing card with same color");

        // Wild card should always be playable
        assertTrue(Card.CardValidator.canPlayCard(wildCard, card1, null),
                "Should always allow playing wild card");
    }
}
