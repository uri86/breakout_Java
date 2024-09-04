package main;

import java.awt.*;
public class Paddle {
	int x, y, width, height;
	int xSpeed;
    Color color;
    
    //Create a paddle
    public Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.xSpeed = 0;
        this.width = width;
        this.height = height;
        this.color = Color.WHITE;
    }
    //Method to draw the paddle
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
}
