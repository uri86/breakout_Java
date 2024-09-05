package main;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
//import java.io.File;
import java.util.*;

public class BlockPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private List<Block> blocks;
	private Ball ball;
	private Timer timer;
	private Paddle paddle;
	private int score;
	private int lives;

	// Paddle movement
	private boolean movingLeft = false;
	private boolean movingRight = false;

	// Add a gameState variable to manage different states of the game
	private enum GameState {
		START_SCREEN, PLAYING, ELIMINATED
	}
	private GameState gameState = GameState.START_SCREEN;

	public BlockPanel(List<Block> blocks, Ball ball, Paddle paddle, Color backgroundColor) {
		this.blocks = blocks;
		this.ball = ball;
		this.paddle = paddle;
		this.score = 0;
		this.lives = 3;
		this.setPreferredSize(new Dimension(1423, 800));
		this.setBackground(backgroundColor);
		// Setup a timer to repeatedly call actionPerformed
		this.timer = new Timer(10, this); // Adjust delay as needed
		this.timer.start();
		// Setup key bindings for paddle movement
		setupKeyBindings();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (gameState == GameState.START_SCREEN) {
			// Display start screen
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("BREAKOUT GAME", 540, 300);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("Press ENTER to Start", 620, 350);
		} else if (gameState == GameState.PLAYING) {
			// Render game elements
			for (Block block : blocks) {
				block.draw(g); // Draw each block in the list
			}
			ball.draw(g); // Draw the ball
			paddle.draw(g); // Draw the paddle
			// Draw score
			// Set text properties (color, font, etc.)
			g.setColor(Color.WHITE); // Set the text color
			g.setFont(new Font("Arial", Font.BOLD, 20)); // Set the font (optional)
			// Draw the text at a specific position (x, y)
			g.drawString("Score: " + this.score, 20, 760);
			g.drawString("Lives: " + this.lives, 1320, 760);
		} else if (gameState == GameState.ELIMINATED) {
			// Display eliminated screen
			g.setColor(Color.RED);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("GAME OVER", 590, 300);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("Press ENTER to go back to the Start screen", 515, 350);
			Sound.play("./audio/game-over.wav");
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (gameState == GameState.PLAYING) {
			ball.move();
			if (movingLeft) {
				paddle.moveLeft();
			} else if (movingRight) {
				paddle.moveRight();
			} else {
				paddle.stop();
			}
			paddle.move();
			checkCollision();
			checkBounds();
			repaint();
		}
	}

	private void checkCollision() {
		double angle;
		Rectangle ballBounds = ball.getBounds();
		Rectangle paddleBounds = paddle.getBounds();
		for (int i = blocks.size() - 1; i >= 0; i--) {
			Block block = blocks.get(i);
			if (block.getBounds().intersects(ballBounds)) {
				// Get working directory
				// System.out.println(new File(".").getAbsolutePath());
				Sound.play("./audio/breakout-meetingBlock.wav"); // Play hitting block sound
				this.score++;
				blocks.remove(i); // Remove the block if it collides
				ball.bounceOffVertical(); // Bounce ball on collision
				angle = ball.angle();
				if (angle > 40 && angle < 120) {
					ball.randomAngleChange();
					ball.bounceOffHorizontal(); // Bounce ball on collision and the chance is 0.
				}
				break; // Exit loop after collision to avoid multiple detections
			}
		}
		if (paddleBounds.intersects(ballBounds)) {
			ball.bounceOffHorizontal();
			Sound.play("./audio/breakout-meetingPaddle.wav"); // Play hitting paddle sound
		}
	}

	private void checkBounds() {
		Rectangle ballBounds = ball.getBounds();
		if (ballBounds.getMinX() < 0 || ballBounds.getMaxX() > getWidth()) {
			ball.bounceOffVertical(); // Bounce off left or right edge
			Sound.play("./audio/breakout-meetingSideWalls.wav"); // Play hitting side walls sound
		}
		if (ballBounds.getMinY() < 0 || ballBounds.getMaxY() > getHeight()) {
			ball.bounceOffHorizontal(); // Bounce off top or bottom edge
			Sound.play("./audio/breakout-meetingSideWalls.wav"); // Play hitting side walls sound
		}
		if (ballBounds.getMaxY() > 770) {
			this.timer.stop(); // Stop the timer to pause the game
			if (this.lives > 0) {
				this.lives--;
				resetBallAndPaddle();
				this.timer.start();
				this.gameState = GameState.PLAYING;
			} else {
				this.gameState = GameState.ELIMINATED;
			}
		}
		// Prevent the paddle from moving outside the panel
		if (paddle.x < 0) {
			paddle.x = 0;
		}
		if (paddle.x + paddle.width > getWidth()) {
			paddle.x = getWidth() - paddle.width;
		}
	}

	private void setupKeyBindings() {
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();
		// Enter key starts the game
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "startGame");
		actionMap.put("startGame", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameState == GameState.START_SCREEN) {
					resetBallAndPaddle(); // Reset ball and paddle positions
					resetBlocks(); // Reset blocks for a new game
					gameState = GameState.PLAYING;
					timer.start(); // Start the game loop
					repaint();
				}
				if (gameState == GameState.ELIMINATED) {
					gameState = GameState.START_SCREEN;
					lives = 3;
					score = 0;
					repaint(); // Trigger a repaint to show the start screen
				}
			}
		});

		// Left arrow key pressed and released
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "moveLeftPressed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "moveLeftReleased");

		// Right arrow key pressed and released
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "moveRightPressed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "moveRightReleased");
		// Paddle movement logic
		actionMap.put("moveLeftPressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				movingLeft = true;
				movingRight = false;
			}
		});
		actionMap.put("moveLeftReleased", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				movingLeft = false;
			}
		});
		actionMap.put("moveRightPressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				movingRight = true;
				movingLeft = false;
			}
		});
		actionMap.put("moveRightReleased", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				movingRight = false;
			}
		});
	}

	private void resetBallAndPaddle() {
		// Assuming the panel size is 1423x800
		int panelWidth = 1423;
		int panelHeight = 800;
		// Reset the ball's position to the center above the paddle
		ball.setPosition(panelWidth / 2 - ball.getWidth() / 2, panelHeight - 300);	
		// Reset the ball's speed
		ball.resetSpeed();
		// Reset the paddle's position to the center bottom of the screen
		paddle.setPosition(panelWidth / 2 - paddle.getWidth() / 2, panelHeight - 100);
	}

	private void resetBlocks() {
		blocks.clear(); // Clear the current blocks
		// Define block properties
		int blockWidth = 62;
		int blockHeight = 25;
		int rows = 8;
		int cols = 20;
		int padding = 8;
		int offsetX = 20;
		int offsetY = 20;
		// Generate new blocks in rows and columns
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int x = offsetX + col * (blockWidth + padding);
				int y = offsetY + row * (blockHeight + padding);
				Color color = new Color(100 + row * 22, 160, 90); // Color changes by row
				blocks.add(new Block(x, y, blockWidth, blockHeight, color));
			}
		}
		;
	}

}
