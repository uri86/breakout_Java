package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
public class Ball {
	int diameter;
    double x, y, xSpeed, ySpeed;
    Color color;

    public Ball(int x, int y, int diameter, int xSpeed, int ySpeed, Color color) {
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
    	double angle = Math.toDegrees(Math.acos(this.xSpeed/Math.sqrt(Math.pow(this.xSpeed, 2)+Math.pow(this.ySpeed,2))));
    	return angle;
    }
}
