// note: code written by chatgpt
package com.pegsolitaire.model;

// this class contains the main game logic for Peg Solitaire
public class SolitaireGame {

    // 2D array representing the board
    // P = peg, E = empty, I = invalid position
    private char[][] board;

    // selected board size and type
    private int boardSize;
    private BoardType boardType;

    // constructor with size and type
    public SolitaireGame(int boardSize, BoardType boardType) {
        this.boardSize = boardSize;
        this.boardType = boardType;
        initializeBoard();
    }

    // default constructor
    public SolitaireGame() {
        this.boardSize = 7;
        this.boardType = BoardType.ENGLISH;
        initializeBoard();
    }

    // initializes the board based on the selected type
    public void initializeBoard() {
        board = new char[boardSize][boardSize];

        if (boardType == BoardType.ENGLISH) {
            initializeEnglishBoard();
        }
        else if (boardType == BoardType.HEXAGON) {
            initializeHexagonBoard();
        }
        else if (boardType == BoardType.DIAMOND) {
            initializeDiamondBoard();
        }
    }

    // creates the English board
    private void initializeEnglishBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                // invalid corners for English board
                if ((row < 2 || row > boardSize - 3) &&
                    (col < 2 || col > boardSize - 3)) {
                    board[row][col] = 'I';
                } else {
                    board[row][col] = 'P';
                }
            }
        }

        // center starts empty
        int mid = boardSize / 2;
        board[mid][mid] = 'E';
    }

    // creates the Hexagon board
    private void initializeHexagonBoard() {
        int mid = boardSize / 2;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                // cells closer to the center are valid
                if (Math.abs(row - mid) + Math.abs(col - mid) <= mid + 1) {
                    board[row][col] = 'P';
                } else {
                    board[row][col] = 'I';
                }
            }
        }

        // center starts empty
        board[mid][mid] = 'E';
    }

    // creates the Diamond board
    private void initializeDiamondBoard() {
        int mid = boardSize / 2;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                // diamond shape based on distance from center
                if (Math.abs(row - mid) + Math.abs(col - mid) <= mid) {
                    board[row][col] = 'P';
                } else {
                    board[row][col] = 'I';
                }
            }
        }

        // center starts empty
        board[mid][mid] = 'E';
    }

    // returns the current board
    public char[][] getBoard() {
        return board;
    }

    // returns the current board size
    public int getBoardSize() {
        return boardSize;
    }

    // returns the current board type
    public BoardType getBoardType() {
        return boardType;
    }

    // checks if a move is valid
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {

        // positions must be inside the board
        if (!isInsideBoard(fromRow, fromCol) || !isInsideBoard(toRow, toCol)) {
            return false;
        }

        // start must contain a peg
        if (board[fromRow][fromCol] != 'P') {
            return false;
        }

        // destination must be empty
        if (board[toRow][toCol] != 'E') {
            return false;
        }

        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        // horizontal jump
        if (rowDiff == 0 && Math.abs(colDiff) == 2) {
            int middleCol = (fromCol + toCol) / 2;
            return board[fromRow][middleCol] == 'P';
        }

        // vertical jump
        if (colDiff == 0 && Math.abs(rowDiff) == 2) {
            int middleRow = (fromRow + toRow) / 2;
            return board[middleRow][fromCol] == 'P';
        }

        return false;
    }

    // performs a move if it is valid
    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {

        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        int middleRow = (fromRow + toRow) / 2;
        int middleCol = (fromCol + toCol) / 2;

        // update board after move
        board[fromRow][fromCol] = 'E';
        board[middleRow][middleCol] = 'E';
        board[toRow][toCol] = 'P';

        return true;
    }

    // randomly changes the board state
    public void randomizeBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                // skip invalid cells
                if (board[row][col] != 'I') {
                    if (Math.random() > 0.5) {
                        board[row][col] = 'P';
                    } else {
                        board[row][col] = 'E';
                    }
                }
            }
        }
    }

    // returns the board as an array of strings for recording
    public String[] getBoardStateLines() {
        String[] lines = new String[boardSize];

        for (int row = 0; row < boardSize; row++) {
            lines[row] = new String(board[row]);
        }

        return lines;
    }

    // loads a recorded board state back into the board
    public void setBoardStateLines(String[] lines) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = lines[row].charAt(col);
            }
        }
    }

    // checks if the game is over
    public boolean isGameOver() {

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                if (board[row][col] == 'P') {
                    if (canMove(row, col)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // checks if one peg has any valid move
    private boolean canMove(int row, int col) {

        int[][] directions = {
            {-2, 0}, {2, 0}, {0, -2}, {0, 2}
        };

        for (int[] d : directions) {
            int newRow = row + d[0];
            int newCol = col + d[1];

            if (isInsideBoard(newRow, newCol)) {
                if (isValidMove(row, col, newRow, newCol)) {
                    return true;
                }
            }
        }

        return false;
    }

    // helper method to check if a position is inside the board
    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }
}
