// note: code written by chatgpt
package com.pegsolitaire.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.pegsolitaire.model.SolitaireGame;

// handles recording a game into a text file
public class GameRecorder {

    private PrintWriter writer;
    private boolean recording;

    // starts recording to a file
    public void startRecording(String fileName) {
        try {
            writer = new PrintWriter(new FileWriter(fileName));
            recording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // records one move
    public void recordMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (recording && writer != null) {
            writer.println("MOVE," + fromRow + "," + fromCol + "," + toRow + "," + toCol);
            writer.flush();
        }
    }

    // records a randomize action and the exact board state after randomize
    public void recordRandomize(SolitaireGame game) {
        if (recording && writer != null) {
            writer.println("RANDOMIZE");

            String[] lines = game.getBoardStateLines();
            for (String line : lines) {
                writer.println("ROW," + line);
            }

            writer.flush();
        }
    }

    // stops recording
    public void stopRecording() {
        if (writer != null) {
            writer.close();
        }
        recording = false;
    }

    // returns whether recording is active
    public boolean isRecording() {
        return recording;
    }
}
