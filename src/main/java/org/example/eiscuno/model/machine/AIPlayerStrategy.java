package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public interface AIPlayerStrategy {
    void setGameUno(GameUno gameUno);

    void playTurn(Player machinePlayer, Table table, Deck deck, ImageView tableImageView);
    GameUno getGameUno();
}

