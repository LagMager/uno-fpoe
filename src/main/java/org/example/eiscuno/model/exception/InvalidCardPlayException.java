package org.example.eiscuno.model.exception;


/**
 * Exception thrown when a player attempts to play an invalid card in the Uno game.
 * This exception is used to handle cases where a card cannot be played according to Uno rules.
 */
public class InvalidCardPlayException extends RuntimeException {

    /**
     * Constructs a new InvalidCardPlayException with the specified detail message.
     *
     * @param message the detail message explaining why the card play was invalid
     */
    public InvalidCardPlayException(String message) {
        super(message);
    }
}