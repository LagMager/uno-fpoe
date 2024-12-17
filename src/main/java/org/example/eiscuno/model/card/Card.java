package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a card in the Uno game.
 * <p>
 * Each card has a value, color, and card type. It also has an image and an ImageView for display purposes.
 * The card's properties are used to determine its validity in gameplay.
 */
public class Card {
    private String url;
    private String value;
    private String color;
    private Image image;
    private ImageView cardImageView;
    private String cardType;

    /**
     * Constructs a Card with the specified image URL, value, color, and card type.
     * <p>
     * This constructor initializes the card with the provided image URL, value, color, and card type.
     * It also creates an Image and an ImageView for the card.
     *
     * @param url the URL of the card image
     * @param value the value of the card (e.g., "1", "WILD")
     * @param color the color of the card (e.g., "RED", "GREEN")
     * @param cardType the type of the card (e.g., "WILD", "DRAW_TWO")
     */
    public Card(String url, String value, String color, String cardType) {
        this.url = url;
        this.value = value;
        this.color = color;
        this.cardType = cardType;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
    }

    /**
     * Creates and configures the ImageView for the card.
     * <p>
     * This method creates an ImageView to display the card's image, setting its size and position.
     *
     * @return the configured ImageView of the card
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }

    /**
     * Gets the ImageView representation of the card.
     *
     * @return the ImageView of the card
     */
    public ImageView getCard() {
        return cardImageView;
    }

    /**
     * Gets the image of the card.
     *
     * @return the Image of the card
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the value of the card.
     * <p>
     * This method returns the value of the card (e.g., "1", "WILD").
     *
     * @return the value of the card
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the color of the card.
     * <p>
     * This method returns the color of the card (e.g., "RED", "GREEN").
     *
     * @return the color of the card
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the type of the card.
     * <p>
     * This method returns the type of the card (e.g., "WILD", "DRAW_TWO").
     *
     * @return the type of the card
     */
    public String getCardType() {return cardType; }

    /**
     * Utility class for validating card playability.
     * <p>
     * This class provides methods to check whether a card can be played based on game rules.
     */
    public static class CardValidator {

        /**
         * Checks if a card can be played on top of another card.
         * <p>
         * This method validates whether the given card can be played on top of the
         * current top card, based on the game's rules. It considers the card's value, color,
         * and special types like "WILD" or "WILD_DRAW_FOUR".
         *
         * @param cardToPlay The card the player wants to play.
         * @param topCard    The current top card on the table.
         * @param gameColor  The current game color (used for matching with wild cards).
         * @return {@code true} if the card can be played, {@code false} otherwise.
         */
        public static boolean canPlayCard(Card cardToPlay, Card topCard, String gameColor) {
            if (topCard == null) {
                return true;
            }

            if (cardToPlay.getCardType() != null &&
                    (cardToPlay.getCardType().equals("WILD") ||
                            cardToPlay.getCardType().equals("WILD_DRAW_FOUR"))) {
                return true;
            }
            return (cardToPlay.getValue().equals(topCard.getValue()) || (cardToPlay.getColor().equals(topCard.getColor()) || cardToPlay.getColor().equals(gameColor)));
        }
    }
}
