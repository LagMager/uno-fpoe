package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.card.Card;

import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of Uno cards.
 */
public class Deck {
    private Stack<Card> deckOfCards;

    /**
     * Constructs a new deck of Uno cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            if (cardEnum.isPlayableCard()) {
                Card card = new Card(
                        cardEnum.getFilePath(),
                        getCardValue(cardEnum.name()),
                        getCardColor(cardEnum.name()),
                        cardEnum.getCardType()
                );
                deckOfCards.push(card);
            }
        }
        Collections.shuffle(deckOfCards);
    }

    /**
     * Retrieves the card value based on its name.
     * <p>
     * This method analyzes the provided card name string and extracts its value.
     * It checks for numeric card values (0-9) and special card types such as "REVERSE",
     * "SKIP", "WILD", "TWO_WILD_DRAW", and "FOUR_WILD_DRAW".
     *
     * @param name The name of the card as a string. It is expected to follow specific naming conventions
     *             where numeric cards end with a digit, and special cards contain keywords like "REVERSE",
     *             "SKIP", or "WILD".
     * @return A string representing the card's value. Possible values include:
     *         <ul>
     *             <li>"0" through "9" for numeric cards</li>
     *             <li>"REVERSE" for reverse cards</li>
     *             <li>"SKIP" for skip cards</li>
     *             <li>"TWO_WILD_DRAW" for +2 wild draw cards</li>
     *             <li>"FOUR_WILD_DRAW" for +4 wild draw cards</li>
     *             <li>"WILD" for wild cards</li>
     *         </ul>
     *         Returns {@code null} if the card name does not match any known value.
     */
    private String getCardValue(String name) {
        if (name.endsWith("0")){
            return "0";
        } else if (name.endsWith("1")){
            return "1";
        } else if (name.endsWith("2")){
            return "2";
        } else if (name.endsWith("3")){
            return "3";
        } else if (name.endsWith("4")){
            return "4";
        } else if (name.endsWith("5")){
            return "5";
        } else if (name.endsWith("6")){
            return "6";
        } else if (name.endsWith("7")){
            return "7";
        } else if (name.endsWith("8")){
            return "8";
        } else if (name.endsWith("9")){
            return "9";
        } else if (name.startsWith("TWO_WILD_DRAW_")){
            return "TWO_WILD_DRAW";
        } else if (name.startsWith("REVERSE")){
            return "REVERSE";
        } else if (name.startsWith("SKIP")){
            return "SKIP";
        } else if (name.equals("FOUR_WILD_DRAW")){
            return "FOUR_WILD_DRAW";
        } else if (name.equals("WILD")){
            return "WILD";
        } else {
            return null;}

    }

    /**
     * Retrieves the card color based on its name.
     * <p>
     * This method analyzes the provided card name string and extracts its color.
     * It checks for standard card colors ("GREEN", "YELLOW", "BLUE", "RED") that appear
     * at the beginning or end of the card name. It also handles special cards like "WILD"
     * and "FOUR_WILD_DRAW", which do not have a specific color.
     *
     * @param name The name of the card as a string. It is expected to follow specific naming conventions
     *             where colors like "GREEN", "YELLOW", "BLUE", or "RED" appear at the start or end
     *             of the card name.
     * @return A string representing the card's color. Possible values include:
     *         <ul>
     *             <li>"GREEN" for green cards</li>
     *             <li>"YELLOW" for yellow cards</li>
     *             <li>"BLUE" for blue cards</li>
     *             <li>"RED" for red cards</li>
     *             <li>"FOUR_WILD_DRAW" for +4 wild draw cards</li>
     *             <li>"WILD" for wild cards</li>
     *         </ul>
     *         Returns {@code null} if the card name does not match any known color.
     */
    private String getCardColor(String name){
        if(name.startsWith("GREEN")){
            return "GREEN";
        } else if(name.startsWith("YELLOW")){
            return "YELLOW";
        } else if(name.startsWith("BLUE")){
            return "BLUE";
        } else if(name.startsWith("RED")){
            return "RED";
        } else if (name.endsWith("GREEN")) {
            return "GREEN";
        } else if (name.endsWith("YELLOW")) {
            return "YELLOW";
        } else if (name.endsWith("RED")) {
            return "RED";
        } else if (name.endsWith("BLUE")) {
            return "BLUE";
        }else if (name.equals("FOUR_WILD_DRAW")){
            return "FOUR_WILD_DRAW";
        } else if (name.equals("WILD")) {
            return "WILD";
        } else {
            return null;
        }
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {
        if (deckOfCards.isEmpty()) {
            throw new IllegalStateException("No hay más cartas en el mazo.");
        }
        return deckOfCards.pop();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }
}
