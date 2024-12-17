package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A thread that handles the gameplay logic for the AI-controlled machine player in the Uno game.
 * <p>
 * This class extends {@link Thread} and is responsible for executing the AI player's turn logic
 * in a separate thread. It interacts with various game components such as the game table, deck,
 * machine player, and AI strategy. The thread simulates the AI's decision-making process and
 * updates the game state accordingly. It also checks for game over conditions and updates the UI.
 * <p>
 * The class uses an {@link AtomicBoolean} to track whether the AI player has played its turn,
 * ensuring thread-safe operations.
 */
public class ThreadPlayMachine extends Thread {
    private final Table table;
    private final Player machinePlayer;
    private final Deck deck;
    private final ImageView tableImageView;
    private final AtomicBoolean hasPlayerPlayed;
    private final AIPlayerStrategy aiPlayerStrategy;
    private final GameUnoController gameUnoController;
    private final GameUno gameUno;

    /**
     * Constructs a new {@code ThreadPlayMachine} instance.
     * <p>
     * This constructor initializes the AI player's thread for managing its turn logic
     * during the game. It sets up the required game components, including the table,
     * machine player, game deck, AI strategy, and game controller. Additionally, it
     * initializes the {@code hasPlayerPlayed} flag and associates the AI strategy
     * with the current game instance.
     *
     * @param table            The {@link Table} representing the game table where cards are played.
     * @param machinePlayer    The {@link Player} representing the AI-controlled machine player.
     * @param tableImageView   The {@link ImageView} displaying the current state of the table.
     * @param gameDeck         The {@link Deck} representing the deck of cards used in the game.
     * @param aiPlayerStrategy The {@link AIPlayerStrategy} that defines the AI's gameplay logic.
     * @param gameUnoController The {@link GameUnoController} managing game flow and user interactions.
     * @param gameUno          The {@link GameUno} instance representing the overall game state.
     */
    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, Deck gameDeck, AIPlayerStrategy aiPlayerStrategy, GameUnoController gameUnoController, GameUno gameUno) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.deck = gameDeck;
        this.hasPlayerPlayed = new AtomicBoolean(false);
        this.aiPlayerStrategy = aiPlayerStrategy;
        this.gameUnoController =  gameUnoController;
        this.gameUno = gameUno;
        this.aiPlayerStrategy.setGameUno(gameUno);
    }

    /**
     * Executes the AI player's turn logic in a separate thread.
     * <p>
     * This method runs in a loop until the current thread is interrupted. It checks if the player
     * (AI) has played their turn, simulates a delay to mimic AI thinking, and then executes the AI's
     * turn strategy. After the AI makes its move, it updates the play status and checks if the game
     * is over. UI updates are handled on the JavaFX Application Thread using {@code Platform.runLater}.
     * <p>
     * The method ensures proper thread interruption handling by catching {@code InterruptedException}.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (hasPlayerPlayed.get()) {
                try {
                    Thread.sleep(2000); // Simulate delay for AI thinking
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                aiPlayerStrategy.playTurn(machinePlayer, table, deck, tableImageView);
                hasPlayerPlayed.set(aiPlayerStrategy.getGameUno().getCurrentPlayer().equals(machinePlayer));


                Platform.runLater(() -> {
                    if (machinePlayer.getCardsPlayer().isEmpty() || deck.isEmpty()){
                        gameUnoController.checkGameOver();
                    }
                });

                Platform.runLater(gameUnoController::printCardsHumanPlayer);
            }
        }
    }

    /**
     * Sets the player's play status.
     * <p>
     * This method updates the {@code hasPlayerPlayed} flag to indicate whether
     * the player has played their turn or not.
     *
     * @param hasPlayerPlayed A boolean value where {@code true} indicates the player has played,
     *                        and {@code false} indicates they have not.
     */
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed.set(hasPlayerPlayed);
    }

    /**
     * Retrieves the player's play status.
     * <p>
     * This method returns the negated value of {@code hasPlayerPlayed}. If the
     * {@code hasPlayerPlayed} flag is {@code true}, it will return {@code false},
     * and vice versa.
     *
     * @return A boolean value where {@code true} indicates the player has NOT played,
     *         and {@code false} indicates the player HAS played.
     */
    public boolean getHasPlayerPlayed() {
        return !hasPlayerPlayed.get();
    }
}
