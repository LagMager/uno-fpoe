package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;

import java.util.ArrayList;

/**
 * Represents a thread that monitors the player's hand to check if they have only one card left.
 * <p>
 * This class implements the {@link Runnable} interface and periodically checks if the player has
 * exactly one card remaining in their hand. If the player has only one card, it prints "UNO".
 */
public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private GameUno game;

    /**
     * Constructs a new {@code ThreadSingUNOMachine} with the player's cards.
     * <p>
     * This constructor initializes the thread to monitor the player's hand and check if they have
     * only one card left.
     *
     * @param cardsPlayer the list of cards the player has
     */
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, GameUno game) {
        this.cardsPlayer = cardsPlayer;
        this.game = game;
    }

    /**
     * Runs the thread, periodically checking if the player has only one card left.
     * <p>
     * The thread sleeps for a random duration between 0 and 5000 milliseconds and then checks if
     * the player has exactly one card. If the player has one card, it prints "UNO" to the console.
     */
    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()) {  // Check for interruptions
            try {
                Thread.sleep((long) (Math.random() * 5000));  // Simulate random delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore interrupt status
                break;  // Exit the loop if interrupted
            }
            hasOneCardTheHumanPlayer();
        }
    }

    /**
     * Checks if the player has only one card left and prints "UNO" if true.
     * <p>
     * This method is called periodically by the {@code run()} method. If the player's hand
     * contains only one card, it prints "UNO" to the console to notify that the player is
     * about to win the game.
     */
    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){

            System.out.println("UNO");
            game.haveSungOne("MACHINE_PLAYER");
        }
    }
}