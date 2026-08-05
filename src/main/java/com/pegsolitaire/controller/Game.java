// note: code written by me
package com.pegsolitaire.controller;

import com.pegsolitaire.model.SolitaireGame;

// parent class for different game modes
public abstract class Game {

    // reference to the board logic
    protected SolitaireGame game;

    // constructor
    public Game(SolitaireGame game) {
        this.game = game;
    }

    // each type of game must define how a move happens
    public abstract void makeMove();
}
