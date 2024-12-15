package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a card in the Uno game.
 */
public class Card {
    private String url;
    private String value;
    private String color;
    private Image image;
    private ImageView cardImageView;
    private String cardType;

    /**
     * Constructs a Card with the specified image URL and name.
     *
     * @param url the URL of the card image
     * @param value of the card
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

    public String getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }

    public String getCardType() {return cardType; }

    public boolean canPlayOn(Card topCard) {
        if (this.cardType.equals("WILD") || this.cardType.equals("WILD_DRAW_FOUR")) {
            return true;
        }

        return this.color.equals(topCard.getColor()) ||
                (this.value != null && this.value.equals(topCard.getValue())) ||
                this.cardType.equals(topCard.getCardType());
    }

    public static class CardValidator {
        public static boolean canPlayCard(Card cardToPlay, Card topCard) {
            if (topCard == null) {
                return true;
            }

            if (cardToPlay.getCardType() != null &&
                    (cardToPlay.getCardType().equals("WILD") ||
                            cardToPlay.getCardType().equals("WILD_DRAW_FOUR"))) {
                return true;
            }
            return (cardToPlay.getValue().equals(topCard.getValue()) || cardToPlay.getColor().equals(topCard.getColor()));
        }
    }
}
