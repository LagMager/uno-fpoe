package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

/**
 * An abstract adapter class that implements the {@link AIPlayerStrategy} interface.
 * This adapter provides default (empty or null) implementations for all methods in the interface,
 * allowing subclasses to override only the methods they need.
 */
public abstract class AIPlayerStrategyAdapter implements AIPlayerStrategy {

    /**
     * Sets the GameUno instance for the AI player strategy.
     * <p>
     * Default implementation does nothing. Subclasses may override this method
     * to provide specific behavior.
     *
     * @param gameUno The current GameUno instance.
     */
    @Override
    public void setGameUno(GameUno gameUno) {
        // Default implementation: do nothing
    }

    /**
     * Retrieves the GameUno instance associated with the AI player strategy.
     * <p>
     * Default implementation returns {@code null}. Subclasses may override this method
     * to return a specific GameUno instance.
     *
     * @return The current GameUno instance, or {@code null} if not implemented.
     */
    @Override
    public GameUno getGameUno() {
        return null; // Default implementation
    }

    /**
     * Executes the AI player's turn during the game.
     * <p>
     * Default implementation does nothing. Subclasses should override this method
     * to define the AI player's behavior when playing a turn.
     *
     * @param machinePlayer   The AI player executing the turn.
     * @param table           The game table where the cards are played.
     * @param deck            The deck of cards available in the game.
     * @param tableImageView  The visual representation of the table (UI component).
     */
    @Override
    public void playTurn(Player machinePlayer, Table table, Deck deck, ImageView tableImageView) {
        // Default implementation: do nothing
    }
}
