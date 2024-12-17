package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

/**
 * A basic implementation of the {@link AIPlayerStrategy} interface for the Uno game.
 * <p>
 * This class provides a simple strategy for the AI player to make decisions during
 * its turn. The strategy is designed to play valid cards from the AI player's hand
 * based on basic game rules, without advanced decision-making or prioritization.
 * It interacts with the {@link GameUno} instance to check for valid plays and update
 * the game state accordingly.
 */
public class BasicAIPlayerStrategy implements AIPlayerStrategy {
    private GameUno gameUno; // Reference to GameUno


    /**
     * Sets the {@link GameUno} instance for the current game.
     * <p>
     * This method assigns a {@code GameUno} object, which represents the overall
     * game state, to the current object. It allows the game state to be updated or
     * replaced as needed.
     *
     * @param gameUno The {@link GameUno} instance to set, representing the current game state.
     */
    @Override
    public void setGameUno(GameUno gameUno) {
        this.gameUno = gameUno;
    }

    /**
     * Retrieves the current instance of the {@link GameUno} class.
     * <p>
     * This method returns the {@code GameUno} object, which represents the overall
     * game state and provides access to various game-related operations.
     *
     * @return The {@link GameUno} instance representing the current game state.
     */
    @Override
    public GameUno getGameUno() {
        return gameUno;
    }

    /**
     * Executes the AI player's turn in the Uno game.
     * <p>
     * This method iterates through the AI player's cards to find the best valid card
     * that can be played. It prioritizes special cards like "WILD_DRAW_FOUR" or "DRAW_TWO"
     * and plays the first valid card found. If no valid card can be played, the AI draws a card
     * from the deck and adds it to its hand. After the card is played or drawn, the method updates
     * the game state and the visual representation of the table.
     *
     * @param machinePlayer    The {@link Player} representing the AI-controlled machine player.
     * @param table            The {@link Table} where the cards are played.
     * @param deck             The {@link Deck} from which the AI can draw a card if needed.
     * @param tableImageView   The {@link ImageView} that displays the current card on the table.
     */
    @Override
    public void playTurn(Player machinePlayer, Table table, Deck deck, ImageView tableImageView) {

        Card bestCard = null;
        int bestCardIndex = -1;

        // Iterate through the player's cards to find the best valid card
        for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
            Card card = machinePlayer.getCard(i);

            // Check if the card can be played
            if (gameUno.canPlayCard(card)) {
                // Prioritize WILD_DRAW_FOUR or DRAW_TWO
                if (card.getCardType() != null &&
                        (card.getCardType().equals("WILD_DRAW_FOUR") || card.getCardType().equals("DRAW_TWO"))) {
                    bestCard = card;
                    bestCardIndex = i;
                    break; // Highest priority, play immediately
                }

                // Otherwise, set the first valid card as the best card if none found yet
                if (bestCard == null) {
                    bestCard = card;
                    bestCardIndex = i;
                }
            }
        }

        // Play the best card if found
        if (bestCard != null) {
            gameUno.playCard(bestCard); // Use GameUno to handle the play
            machinePlayer.removeCard(bestCardIndex); // Remove the card from the AI's hand
            tableImageView.setImage(bestCard.getImage());
            return;
        }

        // Draw a card if no valid play
        Card newCard = deck.takeCard();
        machinePlayer.addCard(newCard);
        System.out.println("Added AI Card!: " + newCard.getColor() + "/" + newCard.getValue());
        System.out.println("-----------------------");
        gameUno.cardTaken();

    }
}

