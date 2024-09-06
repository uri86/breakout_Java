package main;

import java.awt.Color;

public class Main {
	public static void main(String[] args) {
		Ball ball = new Ball(650, 500, 20, -2, -2, Color.WHITE);
		Paddle paddle = new Paddle(650, 700, 120, 20);
		BlockPanel p = new BlockPanel(ball, paddle, Color.black);
		Screen.createAndShowGUI(p);
	}

}
