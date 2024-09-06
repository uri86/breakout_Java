package main;

import java.awt.*;

public class Block {

	int x, y, width, height;
	Color color;

	// Create a block
	public Block(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	// Method to draw the block
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

	// Method to get the bounds of the block
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public boolean isHit(Ball b) {
		return this.getBounds().intersects(b.getBounds());
	}
}
