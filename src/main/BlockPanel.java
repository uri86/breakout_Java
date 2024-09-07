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
import java.util.*;

public class BlockPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private List<Block> blocks;
	private List<List<Block>> levels;
	private Ball ball;
	private Paddle paddle;
	private Timer timer;

	private int score = 0;
	private int lives = 3;
	private int currentLevel = 0;

	private boolean movingLeft = false;
	private boolean movingRight = false;

	private enum GameState {
		START_SCREEN, PLAYING, ELIMINATED, WIN
	}

	private GameState gameState;

	public BlockPanel(Ball ball, Paddle paddle, Color backgroundColor) {
		blocks = new ArrayList<>();
		this.ball = ball;
		this.paddle = paddle;
		this.gameState = GameState.START_SCREEN;

		setPreferredSize(new Dimension(1423, 800));
		setBackground(backgroundColor);

		this.timer = new Timer(10, this);
		this.timer.start();

		setupKeyBindings();
		loadLevels();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		switch (gameState) {
		case START_SCREEN -> drawStartScreen(g);
		case PLAYING -> drawGameElements(g);
		case ELIMINATED -> drawGameOverScreen(g);
		case WIN -> drawWinScreen(g);
		}
	}

	private void drawStartScreen(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("BREAKOUT GAME", 540, 300);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Press ENTER to Start", 620, 350);
	}

	private void drawGameElements(Graphics g) {
		for (Block block : blocks) {
			block.draw(g);
		}
		ball.draw(g);
		paddle.draw(g);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Score: " + score, 20, 760);
		g.drawString("Lives: " + lives, 1320, 760);
	}

	private void drawGameOverScreen(Graphics g) {
		g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("GAME OVER", 590, 300);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press ENTER to Restart", 610, 350);
        Sound.play("./audio/game-over.wav");
	}

	private void drawWinScreen(Graphics g) {
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("YOU WIN!", 590, 300);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Press ENTER to Restart", 610, 350);
		Sound.play("./audio/youWin.wav");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameState == GameState.PLAYING) {
			ball.move();
			handlePaddleMovement();
			paddle.move();
			checkCollision();
			checkBounds();
			repaint();
		}
	}

	private void handlePaddleMovement() {
		if (this.movingLeft) {
			paddle.moveLeft();
		} else if (this.movingRight) {
			paddle.moveRight();
		} else {
			paddle.stop();
		}
	}

	private void checkCollision() {
		for (int i = blocks.size() - 1; i >= 0; i--) {
			if (blocks.get(i).isHit(ball)) {
				Sound.play("./audio/breakout-meetingBlock.wav");
				ball.bounceOffVertical();
				if (Math.abs(ball.angle()) < 40 || Math.abs(ball.angle()) > 140) {
					ball.bounceOffHorizontal();
				}
				if(blocks.get(i).isDestroyed()) {
					score++;
					blocks.remove(i);
				}
				break;
			}
		}

		if (paddle.isHit(ball)) {
			ball.bounceOffVertical();
			ball.randomAngleChange();
			if (paddle.hittingPoint(ball)) {
				ball.bounceOffHorizontal();
			}
			Sound.play("./audio/breakout-meetingPaddle.wav");
		}

		if (blocks.isEmpty()) {
			loadNextLevel();
		}
	}

	private void loadNextLevel() {
		currentLevel++;
		if (currentLevel < levels.size()) {
			resetBallAndPaddle();
			loadCurrentLevel();
		} else {
			gameState = GameState.WIN;
		}
	}

	private void checkBounds() {
		Rectangle ballBounds = ball.getBounds();

		if (ballBounds.getMinX() < 0 || ballBounds.getMaxX() > getWidth()) {
			ball.bounceOffHorizontal();
			Sound.play("./audio/breakout-meetingSideWalls.wav");
		}

		if (ballBounds.getMinY() < 0 || ballBounds.getMaxY() > getHeight()) {
			ball.bounceOffVertical();
			Sound.play("./audio/breakout-meetingSideWalls.wav");
		}

		if (ballBounds.getMaxY() > 770) {
			timer.stop();
			handleLifeLoss();
		}

		checkPaddleBounds();
	}

	private void handleLifeLoss() {
		if (lives > 0) {
			lives--;
			resetBallAndPaddle();
			timer.start();
			gameState = GameState.PLAYING;
		} else {
			gameState = GameState.ELIMINATED;
		}
	}

	private void checkPaddleBounds() {
		if (paddle.getX() < 0) {
			paddle.setX(0);
		}
		if (paddle.getX() + paddle.getWidth() > getWidth()) {
			paddle.setX(getWidth() - paddle.getWidth());
		}
	}

	private void resetBallAndPaddle() {
		int panelWidth = 1423;
		int panelHeight = 800;
		ball.setPosition(panelWidth / 2 - ball.getWidth() / 2, panelHeight - 300);
		ball.resetSpeed();
		paddle.setPosition(panelWidth / 2 - paddle.getWidth() / 2, panelHeight - 100);
	}

	private void setupKeyBindings() {
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "startGame");
		actionMap.put("startGame", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameState == GameState.START_SCREEN) {
					startNewGame();
				} else if (gameState == GameState.ELIMINATED) {
					gameState = GameState.START_SCREEN;
					lives = 3;
					score = 0;
					repaint();
				}
			}
		});

		setupMovementKeys(inputMap, actionMap);
	}

	private void setupMovementKeys(InputMap inputMap, ActionMap actionMap) {
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "moveLeftPressed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "moveLeftReleased");

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "moveRightPressed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "moveRightReleased");

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

	private void startNewGame() {
		resetBallAndPaddle();
		resetBlocks();
		gameState = GameState.PLAYING;
		timer.start();
		repaint();
	}

	private void resetBlocks() {
		loadLevels(); // Recreate all levels
		currentLevel = 0; // Reset to the first level
		loadCurrentLevel(); // Load the first level
	}

	private void loadLevels() {
		levels = new ArrayList<>();
		List<Block> level1 = createLevel(7, 20, 1, new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,	Color.CYAN, Color.MAGENTA, new Color(128, 0, 128) });
		List<Block> level2 = createLevel(8, 20, 2, new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,	Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK });
		List<Block> level3 = createLevel(8, 20, 3, new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, new Color(128, 0, 128)});

		levels.add(level1);
		levels.add(level2);
		levels.add(level3);

		loadCurrentLevel();
	}

	private void loadCurrentLevel() {
		blocks.clear();
		blocks.addAll(levels.get(currentLevel));
	}

	private List<Block> createLevel(int rows, int cols, int hits, Color[] color) {
		List<Block> levelBlocks = new ArrayList<>();
		int blockWidth = 62;
		int blockHeight = 25;
		int padding = 8;
		int offsetX = 20;
		int offsetY = 20;

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int x = offsetX + col * (blockWidth + padding);
				int y = offsetY + row * (blockHeight + padding);
				levelBlocks.add(new Block(x, y, blockWidth, blockHeight, hits, color[row]));
			}
		}
		return levelBlocks;
	}
}
