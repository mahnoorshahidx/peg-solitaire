// note: code written by chatgpt
package com.pegsolitaire.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.pegsolitaire.model.SolitaireGame;

// handles replaying a game from a file
public class GameReplayer {

    private SolitaireGame game;

    public GameReplayer(SolitaireGame game) {
        this.game = game;
    }

    // replays the game from file
    public void replay(String fileName, Runnable updateUI) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    String[] parts = line.split(",");

                    if (parts[0].equals("MOVE")) {
                        int fromRow = Integer.parseInt(parts[1]);
                        int fromCol = Integer.parseInt(parts[2]);
                        int toRow = Integer.parseInt(parts[3]);
                        int toCol = Integer.parseInt(parts[4]);

                        game.makeMove(fromRow, fromCol, toRow, toCol);
                    }

                    else if (parts[0].equals("RANDOMIZE")) {
                        String[] lines = new String[game.getBoardSize()];

                        for (int i = 0; i < game.getBoardSize(); i++) {
                            String rowLine = reader.readLine();
                            String[] rowParts = rowLine.split(",", 2);
                            lines[i] = rowParts[1];
                        }

                        game.setBoardStateLines(lines);
                    }

                    // update UI after each action
                    updateUI.run();

                    // delay so replay can be seen
                    Thread.sleep(400);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
