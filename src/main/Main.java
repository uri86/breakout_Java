package main;

import java.awt.Color;
import java.util.*;
public class Main {
	public static void main(String[] args) {
		// Define block properties
		int blockWidth = 62;
        int blockHeight = 25;
        int rows = 8;
        int cols = 20;
        int padding = 8;
        int offsetX = 20;
        int offsetY = 20;
        // Create a list to store blocks
        List<Block> blocks = new ArrayList<>();
        // Generate the blocks in rows and columns
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = offsetX + col * (blockWidth + padding);
                int y = offsetY + row * (blockHeight + padding);
                Color color = new Color(100 + row * 22, 160, 90); // Color changes by row
                blocks.add(new Block(x, y, blockWidth, blockHeight, color));
            }
        };
        // Create a ball
        Ball ball = new Ball(650, 500, 20, -2, -2, Color.WHITE);
        // Create paddle
        Paddle paddle = new Paddle(650, 700, 120, 20);
        // Create a panel with the blocks, ball, and Paddle, and set the background color
        BlockPanel p = new BlockPanel(blocks, ball, paddle, Color.black);
        Screen.createAndShowGUI(p);
    }

}
