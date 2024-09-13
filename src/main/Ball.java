package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ball {
	private int diameter;
	private double x, y, xSpeed, ySpeed;
	private Color color;

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
		g.fillOval((int) (x), (int) (y), diameter, diameter);
	}

	public void bounceOffVertical() {
		ySpeed = -ySpeed * 1.01;
	}

	public void bounceOffHorizontal() {
		xSpeed = -xSpeed * 1.01;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) (x), (int) (y), diameter, diameter);
	}

	public double angle() {
		return Math.toDegrees(Math.atan(this.ySpeed / this.xSpeed));
	}

	public void randomAngleChange() {
		double angle = Math.toRadians(this.angle() + (Math.random() * 21) - 10);
		double vector = Physics.getVector(Math.toRadians(this.angle()), this.ySpeed);
		this.xSpeed = Physics.getXVector(vector, angle);
		this.ySpeed = Physics.getYVector(vector, angle);
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
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
}
