package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPlayMachine extends Thread {
    private final Table table;
    private final Player machinePlayer;
    private final Deck deck;
    private final ImageView tableImageView;
    private final AtomicBoolean hasPlayerPlayed;
    private final AIPlayerStrategy aiPlayerStrategy;
    private final GameUnoController gameUnoController;
    private final GameUno gameUno;

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
                System.out.println("------------------");
                System.out.println("Player played set to: " + hasPlayerPlayed.get());
                System.out.println("------------------");

                Platform.runLater(gameUnoController::printCardsHumanPlayer);
            }
        }
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed.set(hasPlayerPlayed);
    }

    public boolean getHasPlayerPlayed() {
        return !hasPlayerPlayed.get();
    }
}
