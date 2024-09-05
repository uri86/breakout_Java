package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
public class Ball {
	int diameter;
    double x, y, xSpeed, ySpeed;
    Color color;

    public Ball(int x, int y, int diameter, double xSpeed, double ySpeed, Color color) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.color = color;
    }

    public void move() {
        x += xSpeed;
        y += ySpeed;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)(x), (int)(y), diameter, diameter);
    }

    public void bounceOffHorizontal() {
        ySpeed = -ySpeed*1.01;
    }

    public void bounceOffVertical() {
        xSpeed = -xSpeed*1.01;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(x), (int)(y), diameter, diameter);
    }
    public double angle() {
    	// Calculate the angle and convert it to degrees
    	double angle = Math.toDegrees(Math.atan(this.ySpeed/this.ySpeed));
    	return angle;
    }
    public void randomAngleChange() {
    	this.xSpeed += Math.random()*4 - 1.5;
    }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return diameter; // Since the ball is a circle, width is the same as its diameter
    }
    public void resetSpeed() {
    	this.xSpeed = -2;
    	this.ySpeed = -2;
    }
}
