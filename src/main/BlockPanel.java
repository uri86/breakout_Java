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
    
    // Paddle movement
    private boolean movingLeft = false;
    private boolean movingRight = false;

    public BlockPanel(List<Block> blocks, Ball ball,Paddle paddle, Color backgroundColor) {
        this.blocks = blocks;
        this.ball = ball;
        this.paddle = paddle;
        this.score = 0;
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
        for (Block block : blocks) {
            block.draw(g); // Draw each block in the list
        }
        ball.draw(g); // Draw the ball
        paddle.draw(g); // Draw the paddle
        
        // Set text properties (color, font, etc.)
        g.setColor(Color.WHITE); // Set the text color
        g.setFont(new Font("Arial", Font.BOLD, 20)); // Set the font (optional)
        
        // Draw the text at a specific position (x, y)
        g.drawString("Score: " + this.score, 20, 760);
        //g.drawString("Lives: 3", 1320, 760);    // Example lives display at (20, 60)
    }
	public void actionPerformed(ActionEvent e) {
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
                if(angle > 40 && angle < 120 ) {
                	ball.randomAngleChange();
                	ball.bounceOffHorizontal(); // Bounce ball on collision and the chance is 0.
                }
                break; // Exit loop after collision to avoid multiple detections
            }
        }
        if(paddleBounds.intersects(ballBounds)) {
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
            timer.stop(); // Stop the timer to pause the game
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

}
