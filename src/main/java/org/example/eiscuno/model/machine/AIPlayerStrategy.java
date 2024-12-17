package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

/**
 * Interface for AI player strategies in the Uno game.
 * <p>
 * This interface defines the methods required for implementing different strategies
 * for the AI player in the Uno game. Implementing classes should define how the AI
 * plays its turn and how it interacts with the game state, such as choosing a card
 * to play or drawing a card when necessary.
 */
public interface AIPlayerStrategy {

    /**
     * Sets the {@link GameUno} instance for the AI player strategy.
     * <p>
     * This method allows the AI player strategy to access and interact with the game state,
     * including validating moves and updating the game as the AI plays its turn.
     *
     * @param gameUno The {@link GameUno} instance representing the current game state.
     */
    void setGameUno(GameUno gameUno);

    /**
     * Executes the AI player's turn in the Uno game.
     * <p>
     * This method defines the logic for the AI to decide which card to play or whether
     * to draw a card from the deck. It interacts with the game state to determine the
     * best course of action.
     *
     * @param machinePlayer    The {@link Player} representing the AI-controlled machine player.
     * @param table            The {@link Table} where the cards are played.
     * @param deck             The {@link Deck} from which the AI can draw a card if needed.
     * @param tableImageView   The {@link ImageView} that displays the current card on the table.
     */
    void playTurn(Player machinePlayer, Table table, Deck deck, ImageView tableImageView);

    /**
     * Retrieves the current instance of the {@link GameUno} class.
     * <p>
     * This method returns the {@code GameUno} object, which represents the overall
     * game state and provides access to various game-related operations.
     *
     * @return The {@link GameUno} instance representing the current game state.
     */
    GameUno getGameUno();
}

