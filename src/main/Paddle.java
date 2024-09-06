package main;

import java.awt.*;

public class Paddle {
	int x, y, width, height;
	private int xSpeed;
	private Color color;

	// Create a paddle
	public Paddle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.xSpeed = 0;
		this.width = width;
		this.height = height;
		this.color = Color.WHITE;
	}

	// Method to draw the paddle
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

	// Method to get the bounds of the paddle
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	// Moving functions
	// Move left (change speed)
	public void moveLeft() {
		this.xSpeed = -5;
	}

	// Move right (change speed)
	public void moveRight() {
		this.xSpeed = 5;
	}

	// Actually move
	public void move() {
		this.x += this.xSpeed;
	}

	// Stop
	public void stop() {
		this.xSpeed = 0;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getY() {
		return this.y;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isHit(Ball b) {
		return this.getBounds().intersects(b.getBounds());
	}

}
