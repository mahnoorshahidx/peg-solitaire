//note: code written by chatgpt with some of my own modifications for design
package com.pegsolitaire.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;

import com.pegsolitaire.model.SolitaireGame;
import com.pegsolitaire.model.BoardType;
import com.pegsolitaire.model.GameMode;
import com.pegsolitaire.controller.Game;
import com.pegsolitaire.controller.ManualGame;
import com.pegsolitaire.controller.AutomatedGame;
import com.pegsolitaire.io.GameRecorder;
import com.pegsolitaire.io.GameReplayer;

// this class handles the GUI for the Peg Solitaire game
public class SolitaireGUI extends JFrame {

	// Reference to the game logic
	private SolitaireGame game;

	// current game mode object (manual or automated)
	private Game currentGame;

	// 2D array of buttons representing the board
	private JButton[][] buttons;

	// Stores the currently selected peg position
	private int selectedRow = -1;
	private int selectedCol = -1;

	// dropdowns for size, type, and mode
	private JComboBox<Integer> sizeBox;
	private JComboBox<BoardType> typeBox;
	private JComboBox<GameMode> modeBox;

	// panel that holds the board
	private JPanel boardPanel;

	// handles recording moves into a file
	private GameRecorder recorder = new GameRecorder();

	// checkbox to record game
	private JCheckBox recordCheckBox;

	// track if recording happened
	private boolean hasRecording = false;

	// Constructor that builds the GUI
	public SolitaireGUI() {
		setTitle("Peg Solitaire");
		setSize(700, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// top panel for controls
		JPanel topPanel = new JPanel();

		sizeBox = new JComboBox<>(new Integer[] { 5, 7, 9 });
		typeBox = new JComboBox<>(BoardType.values());
		modeBox = new JComboBox<>(GameMode.values());

		topPanel.add(new JLabel("Size:"));
		topPanel.add(sizeBox);
		topPanel.add(new JLabel("Type:"));
		topPanel.add(typeBox);
		topPanel.add(new JLabel("Mode:"));
		topPanel.add(modeBox);

		add(topPanel, BorderLayout.NORTH);

		// board panel
		boardPanel = new JPanel();
		add(boardPanel, BorderLayout.CENTER);

		// bottom panel for buttons
		JPanel bottomPanel = new JPanel();

		// button to start a new game
		JButton newGameButton = new JButton("New Game");
		newGameButton.addActionListener(e -> startNewGame());

		// button to randomize the board
		JButton randomizeButton = new JButton("Randomize");
		randomizeButton.addActionListener(e -> {
			if (game != null) {
				game.randomizeBoard();
				recorder.recordRandomize(game); // record randomize action
				updateBoard();
			}
		});

		// button to replay a recorded game
		JButton replayButton = new JButton("Replay");
		replayButton.addActionListener(e -> {

		    if (!hasRecording) {
		        JOptionPane.showMessageDialog(this, "No recording available");
		        return;
		    }

		    if (game == null) return;

		    game.initializeBoard();
		    updateBoard();

		    GameReplayer replayer = new GameReplayer(game);

		    replayer.replay("game_record.txt", () -> {
		        SwingUtilities.invokeLater(() -> updateBoard());
		    });
		});

		bottomPanel.add(newGameButton);
		bottomPanel.add(randomizeButton);
		bottomPanel.add(replayButton);
		recordCheckBox = new JCheckBox("Record Game");
		bottomPanel.add(recordCheckBox);

		// add bottom panel to frame
		add(bottomPanel, BorderLayout.SOUTH);

		// start initial game and show GUI
		startNewGame();
		setVisible(true);
	}

	// starts a new game with the selected size, type, and mode
	private void startNewGame() {
		int size = (Integer) sizeBox.getSelectedItem();
		BoardType type = (BoardType) typeBox.getSelectedItem();
		GameMode mode = (GameMode) modeBox.getSelectedItem();

		// create the board
		game = new SolitaireGame(size, type);

		// choose game mode
		if (mode == GameMode.MANUAL) {
			currentGame = new ManualGame(game);
		} else {
			currentGame = new AutomatedGame(game, recorder);
		}

		selectedRow = -1;
		selectedCol = -1;

		// rebuild and display board
		buildBoard();
		updateBoard();

		recorder.stopRecording();
		hasRecording = false;

		if (recordCheckBox.isSelected()) {
			recorder.startRecording("game_record.txt");
			hasRecording = true;
		}

		// if automated mode, start playing automatically
		if (mode == GameMode.AUTOMATED) {
			runAutomatedGame();
		}
	}

	// builds the board buttons again
	private void buildBoard() {
		boardPanel.removeAll();

		int size = game.getBoard().length;
		boardPanel.setLayout(new GridLayout(size, size));
		buttons = new JButton[size][size];

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				JButton button = new JButton();
				buttons[row][col] = button;

				final int r = row;
				final int c = col;

				// add click listener for each button
				button.addActionListener(e -> handleButtonClick(r, c));
				boardPanel.add(button);
			}
		}

		boardPanel.revalidate();
		boardPanel.repaint();
	}

	// runs the automated game until it is over
	private void runAutomatedGame() {
		new Thread(() -> {
			try {
				while (!game.isGameOver()) {

					// automated move
					currentGame.makeMove();

					// update UI
					SwingUtilities.invokeLater(() -> updateBoard());

					// delay so moves are visible
					Thread.sleep(300);
				}

				// show game over message
				SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Automated Game Over"));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	// handles a click on a board position
	private void handleButtonClick(int row, int col) {
		char[][] board = game.getBoard();

		// block clicking in automated mode
		if ((GameMode) modeBox.getSelectedItem() == GameMode.AUTOMATED) {
			JOptionPane.showMessageDialog(this, "Game is running automatically");
			return;
		}

		// first click: select a peg
		if (selectedRow == -1 && selectedCol == -1) {

			// select peg if clicked position has a peg
			if (board[row][col] == 'P') {
				selectedRow = row;
				selectedCol = col;
			}

		} else {

			int fromRow = selectedRow;
			int fromCol = selectedCol;

			// attempt to make move
			boolean moved = game.makeMove(fromRow, fromCol, row, col);

			// reset selection
			selectedRow = -1;
			selectedCol = -1;

			// show message if invalid move
			if (!moved) {
				JOptionPane.showMessageDialog(this, "Invalid move");
			} else {
				// record successful move
				recorder.recordMove(fromRow, fromCol, row, col);
			}

			updateBoard();

			// check if game is over
			if (game.isGameOver()) {
				JOptionPane.showMessageDialog(this, "Game Over");
			}
		}

		updateBoard();
	}

	// updates the GUI board to match the game state
	private void updateBoard() {
		char[][] board = game.getBoard();
		int size = board.length;

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {

				// display peg
				if (board[row][col] == 'P') {
					buttons[row][col].setText("●");
					buttons[row][col].setEnabled(true);
				}
				// display empty
				else if (board[row][col] == 'E') {
					buttons[row][col].setText("");
					buttons[row][col].setEnabled(true);
				}
				// disable invalid positions
				else {
					buttons[row][col].setText("");
					buttons[row][col].setEnabled(false);
				}
			}
		}

		// highlight selected peg
		if (selectedRow != -1 && selectedCol != -1) {
			buttons[selectedRow][selectedCol].setText("X");
		}
	}
}
