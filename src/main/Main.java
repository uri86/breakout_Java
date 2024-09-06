package main;

import java.awt.Color;

public class Main {
	public static void main(String[] args) {
		// Create a ball
		Ball ball = new Ball(650, 500, 20, -2, -2, Color.WHITE);
		// Create paddle
		Paddle paddle = new Paddle(650, 700, 120, 20);
		// Create a panel with the blocks, ball, and Paddle, and set the background
		// color
		BlockPanel p = new BlockPanel(ball, paddle, Color.black);
		Screen.createAndShowGUI(p);
	}

}
