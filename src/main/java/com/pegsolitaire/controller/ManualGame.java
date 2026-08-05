// note: code written by me
package com.pegsolitaire.controller;

import com.pegsolitaire.model.SolitaireGame;

// represents a manual game (user clicks)
public class ManualGame extends Game {

    public ManualGame(SolitaireGame game) {
        super(game);
    }

    @Override
    public void makeMove() {
        // moves are handled by GUI clicks
        // so nothing needed here
    }
}
