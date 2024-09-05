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
    public void randomizeAngle() {
    	//get the angle of the 
    	double angle = this.angle();
    	//add a randomized number to the angle
    	angle += Math.random()*16;;
    	//convert to radians from degrees
    	angle = Math.toRadians(angle);
    	//get the ratio of the angle
    	double tan = Math.tan(angle);
    	//add a small amount of speed to x axis
    	this.xSpeed -= 0.5;
    	//recalculate the speed of the y axis
    	this.ySpeed = tan*this.xSpeed;
    }
    public void randomAngleChange() {
    	this.xSpeed += Math.random()*4 - 1.5;
    }
}
