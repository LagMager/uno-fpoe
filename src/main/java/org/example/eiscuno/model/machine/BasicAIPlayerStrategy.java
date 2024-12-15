package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class BasicAIPlayerStrategy implements AIPlayerStrategy {
    private GameUno gameUno; // Reference to GameUno

    @Override
    public void setGameUno(GameUno gameUno) {
        this.gameUno = gameUno; // Set GameUno reference
    }

    @Override
    public GameUno getGameUno() {
        return gameUno;
    }

    @Override
    public void playTurn(Player machinePlayer, Table table, Deck deck, ImageView tableImageView) {
        Card topCard;
        try {
            topCard = table.getCurrentCardOnTheTable();
        } catch (IndexOutOfBoundsException e) {
            putRandomCard(machinePlayer, table, tableImageView);
            return;
        }

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

    private void putRandomCard(Player machinePlayer, Table table, ImageView tableImageView) {
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);
        gameUno.playCard(card);
    }
}

