package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private Deck deck;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, Deck gameDeck) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.deck = gameDeck;
    }

    public void run() {
        while (true){
            if(hasPlayerPlayed){
                System.out.println("MACHINE'S TURN");
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable() {
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);

        boolean isValid = false;
        for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
            card = machinePlayer.getCard(i);
            isValid = validCard(card);
            if(isValid){
                System.out.println("Machine Card: " + card.getColor() + "/" + card.getValue());
                table.addCardOnTheTable(card);
                tableImageView.setImage(card.getImage());
                machinePlayer.getCardsPlayer().remove(card);
                break;
            }
        }
        if (!isValid){
            Card newCard = deck.takeCard();
            System.out.println("I dont have any cards");
            machinePlayer.addCard(newCard);
            System.out.println("Added AI Card!: " + newCard.getColor() + "/" + newCard.getValue());
        }
        System.out.println("DEBUGGING MACHINE TURN");
        System.out.println("Table Card: " + table.getCurrentCardOnTheTable().getColor() + "/" + table.getCurrentCardOnTheTable().getValue());
    }


    public boolean getHasPlayerPlayed() { return hasPlayerPlayed; }


    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    private boolean validCard(Card machineCard) {
        Card TableCard = table.getCurrentCardOnTheTable();
        if (TableCard != null) {
            // Change this later to also take into account special cards.
            return machineCard.getValue().equals(TableCard.getValue()) || machineCard.getColor().equals(TableCard.getColor());
        }
        return false;
    }

}

