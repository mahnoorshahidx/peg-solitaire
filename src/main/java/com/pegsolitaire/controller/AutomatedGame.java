// note: code written by chatgpt
package com.pegsolitaire.controller;

import java.util.Random;

import com.pegsolitaire.model.SolitaireGame;
import com.pegsolitaire.io.GameRecorder;

// represents an automated game
public class AutomatedGame extends Game {

    private Random rand = new Random();

    // recorder used to save automated moves
    private GameRecorder recorder;

    public AutomatedGame(SolitaireGame game, GameRecorder recorder) {
        super(game);
        this.recorder = recorder;
    }

    @Override
    public void makeMove() {
        for (int i = 0; i < 100; i++) {
            int size = game.getBoardSize();

            int r1 = rand.nextInt(size);
            int c1 = rand.nextInt(size);
            int r2 = rand.nextInt(size);
            int c2 = rand.nextInt(size);

            if (game.makeMove(r1, c1, r2, c2)) {
                // record the successful automated move
                if (recorder != null) {
                    recorder.recordMove(r1, c1, r2, c2);
                }
                break;
            }
        }
    }
}
