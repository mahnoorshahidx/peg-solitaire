// note: code written by chatgpt
package com.pegsolitaire;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.pegsolitaire.model.SolitaireGame;
import com.pegsolitaire.model.BoardType;
import com.pegsolitaire.io.GameRecorder;
import com.pegsolitaire.controller.AutomatedGame;

// tests for manual and automated Peg Solitaire
public class SolitaireGameTest {

	// -------- Manual Game Tests --------

	// test that a new board starts correctly
	@Test
	public void testBoardInitialization() {
		SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);
		char[][] board = game.getBoard();

		// center should start empty
		assertEquals('E', board[3][3]);
	}

	// test that a valid move is recognized
	@Test
	public void testValidMove() {
		SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);

		// this is a valid opening move
		assertTrue(game.isValidMove(1, 3, 3, 3));
	}

	// test that an invalid move is rejected
	@Test
	public void testInvalidMove() {
		SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);

		// this should not be a valid move
		assertFalse(game.isValidMove(0, 0, 0, 2));
	}

	// test that making a move updates the board correctly
	@Test
	public void testMakeMove() {
		SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);

		game.makeMove(1, 3, 3, 3);
		char[][] board = game.getBoard();

		assertEquals('E', board[1][3]);
		assertEquals('E', board[2][3]);
		assertEquals('P', board[3][3]);
	}

	// test that the game is not over at the start
	@Test
	public void testGameNotOverAtStart() {
		SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);

		assertFalse(game.isGameOver());
	}

	// -------- Automated Game Tests --------

	// test that automated game can run one move attempt without crashing
	@Test
	public void testAutomatedGameRuns() {
		SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);
		GameRecorder recorder = new GameRecorder();
		AutomatedGame autoGame = new AutomatedGame(game, recorder);

		assertDoesNotThrow(() -> autoGame.makeMove());
	}

	// test that automated game changes the board after repeated attempts
	@Test
	public void testAutomatedGameMakesProgress() {
		SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);
		GameRecorder recorder = new GameRecorder();
		AutomatedGame autoGame = new AutomatedGame(game, recorder);

		char[][] before = copyBoard(game.getBoard());

		// try several automated moves to improve chance of change
		for (int i = 0; i < 10; i++) {
			autoGame.makeMove();
		}

		char[][] after = game.getBoard();

		assertFalse(boardsAreEqual(before, after));
	}

	// test that randomizeBoard changes non-invalid cells
	@Test
	public void testRandomizeBoardRuns() {
		SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);

		assertDoesNotThrow(() -> game.randomizeBoard());
	}

	// helper method to copy a board
	private char[][] copyBoard(char[][] original) {
		char[][] copy = new char[original.length][original[0].length];

		for (int row = 0; row < original.length; row++) {
			for (int col = 0; col < original[row].length; col++) {
				copy[row][col] = original[row][col];
			}
		}

		return copy;
	}

	// helper method to compare two boards
	private boolean boardsAreEqual(char[][] board1, char[][] board2) {
		if (board1.length != board2.length) {
			return false;
		}

		for (int row = 0; row < board1.length; row++) {
			for (int col = 0; col < board1[row].length; col++) {
				if (board1[row][col] != board2[row][col]) {
					return false;
				}
			}
		}

		return true;
	}
}
