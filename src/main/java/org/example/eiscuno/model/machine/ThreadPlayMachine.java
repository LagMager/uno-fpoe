package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
    }

    public void run() {
        while (true) {
            if(hasPlayerPlayed) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                playBestCard();
                hasPlayerPlayed = false;
            }
        }
    }

    private void playBestCard() {
        Card topCard = null;
        try {
            topCard = table.getCurrentCardOnTheTable();
        } catch (IndexOutOfBoundsException e) {
            putRandomCard();
            return;
        }
        for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
            Card card = machinePlayer.getCard(i);
            if (card.getCardType() != null &&
                    (card.getCardType().equals("WILD_DRAW_FOUR") ||
                            card.getCardType().equals("DRAW_TWO"))) {
                playCard(card, i);
                return;
            }
            if (card.getColor().equals(topCard.getColor()) ||
                    (card.getValue() != null && card.getValue().equals(topCard.getValue()))) {
                playCard(card, i);
                return;
            }
        }

        putRandomCard();
    }

    private void playCard(Card card, int index) {
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
        machinePlayer.removeCard(index);
    }

    private void putRandomCard() {
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);
        playCard(card, index);
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    private void putCardOnTheTable(){
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
    }
}